package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.cart.AddItemToCartDto;
import bg.softuni.ecommerce.model.dto.cart.CartDto;
import bg.softuni.ecommerce.model.dto.cart.OfferAddedToCartDto;
import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.*;
import bg.softuni.ecommerce.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    private static final Long OFFER_ID = 1L;
    private static final Integer QUANTITY = 2;

    private CartService toTest;

    private CartEntity testCartEntity;

    @Mock
    private CartRepository mockCartRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private OfferRepository mockOfferRepository;

    @Mock
    private PictureRepository mockPictureRepository;


    @Mock
    private BrandRepository mockBrandRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository;


    @Captor
    private ArgumentCaptor<CartEntity> cartEntityArgumentCaptor;

    private OfferService testOfferService;

    private UserService testUserService;

    private ImageCloudService testImageCloudService;

    private UserEntity testUserEntity;

    private OfferEntity testOfferEntity;

    private BrandEntity testBrandEntity;

    private AddItemToCartDto testAddItemToCartDto;

    private UserDetails userDetails;

    private EmailService emailService;

    private BrandService testBrandService;

    @BeforeEach
    void setUp() {
        this.testBrandService = new BrandService(mockBrandRepository);
        this.emailService = new EmailService(new JavaMailSenderImpl(), new TemplateEngine());
        this.testImageCloudService = new ImageCloudService();
        this.testUserService = new UserService(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, emailService);
        this.testOfferService = new OfferService(mockOfferRepository, testUserService, testImageCloudService, mockPictureRepository, testBrandService);
        this.toTest = new CartService(mockCartRepository, testOfferService, testUserService);
        this.testUserEntity = new UserEntity(
                "test",
                "test@example.com",
                "12345",
                "Test",
                "Testov",
                "089089",
                GenderEnum.MALE,
                Set.of(new UserRoleEntity(UserRoleEnum.USER)));
        this.testBrandEntity = new BrandEntity(
                "testBrand",
                "slogan",
                LocalDate.of(2000, 1, 12));
        this.testOfferEntity = new OfferEntity(
                ItemType.TROUSERS,
                2000,
                new PictureEntity("n/a"),
                testBrandEntity,
                SizeEnum.LARGE,
                "pants",
                OfferRating.ZERO,
                testUserEntity,
                BigDecimal.valueOf(2000),
                2,
                LocalDate.now(),
                LocalDate.now(),
                "some text",
                true);
        this.testAddItemToCartDto = new AddItemToCartDto(OFFER_ID, QUANTITY);
        this.testCartEntity = new CartEntity(testOfferEntity, QUANTITY, testUserEntity);
        this.userDetails = new User(
                testUserEntity.getUsername(),
                testUserEntity.getPassword(),
                testUserEntity.getUserRoles()
                        .stream()
                        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getName().name()))
                        .collect(Collectors.toList()));
    }

    @Test
    void testAddToCart() {

        AddItemToCartDto testAddItemToCartDto = new AddItemToCartDto(OFFER_ID, QUANTITY);
        when(mockOfferRepository.findById(OFFER_ID)).thenReturn(Optional.of(testOfferEntity));
        when(mockUserRepository.findByUsername(testUserEntity.getUsername())).thenReturn(Optional.of(testUserEntity));


        toTest.addToCart(userDetails, testAddItemToCartDto);
        Mockito.verify(mockCartRepository).save(cartEntityArgumentCaptor.capture());
        CartEntity savedCartEntity = cartEntityArgumentCaptor.getValue();

        Assertions.assertEquals(testOfferEntity.getName(), savedCartEntity.getOffer().getName());
        Assertions.assertEquals(testOfferEntity.getPrice(), savedCartEntity.getOffer().getPrice());
        Assertions.assertEquals(testOfferEntity.getType().name(), savedCartEntity.getOffer().getType().name());
        Assertions.assertEquals(testBrandEntity.getName(), savedCartEntity.getOffer().getBrand().getName());
    }

    @Test
    void testGetAddedOffer() {
        when(mockCartRepository.findById(testCartEntity.getId())).thenReturn(Optional.of(testCartEntity));
        OfferAddedToCartDto addedOffer = toTest.getAddedOffer(testCartEntity.getId());

        Assertions.assertEquals(this.testOfferEntity.getName(), addedOffer.getOfferName());
        Assertions.assertEquals(QUANTITY, addedOffer.getQuantity());

    }

    @Test
    void testGetAllOffersInCart() {
        when(mockCartRepository.findAllByBuyerUsername(userDetails.getUsername())).thenReturn(List.of(testCartEntity));

        List<CartDto> allOffersInCart = toTest.getAllOffersInCart(this.userDetails);

        CartDto testCartDto = allOffersInCart.stream().findFirst().orElseThrow(() -> new AssertionFailedError("No such cart"));

        Assertions.assertEquals(QUANTITY, testCartDto.getQuantity());
        Assertions.assertEquals(testOfferEntity.getName(), testCartDto.getOfferName());
        Assertions.assertEquals(testBrandEntity.getName(), testCartDto.getBrandName());
        Assertions.assertEquals(testUserEntity.getUsername(), testCartDto.getSellerUsername());
    }
}