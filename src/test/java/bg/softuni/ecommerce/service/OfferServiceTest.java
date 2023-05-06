package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
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
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    private OfferService toTest;

    private BrandService testBrandService;

    @Mock
    private OfferRepository mockOfferRepository;

    @Mock
    private PictureRepository mockPictureRepository;

    @Mock
    private BrandRepository mockBrandRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Captor
    private ArgumentCaptor<OfferEntity> offerEntityArgumentCaptor;

    private ImageCloudService testImageCloudService;

    private UserService testUserService;

    private UserEntity testUserEntity;

    private EmailService emailService;


    @BeforeEach
    void setUp() {
        this.testBrandService = new BrandService(mockBrandRepository);
        this.emailService = new EmailService(new JavaMailSenderImpl(), new TemplateEngine());
        this.testUserService = new UserService(mockUserRepository, mockUserRoleRepository, mockPasswordEncoder, emailService);
        this.testImageCloudService = new ImageCloudService();
        this.toTest = new OfferService(mockOfferRepository, testUserService, testImageCloudService, mockPictureRepository, testBrandService);
        this.testUserEntity = new UserEntity(
                "test",
                "test@example.com",
                "12345",
                "Test",
                "Testov",
                "089089",
                GenderEnum.MALE,
                Set.of(new UserRoleEntity(UserRoleEnum.USER)));

    }

    @Test
    void testCreateOfferSuccess() {
        CreateOfferDto testCreateOfferDto = new CreateOfferDto();
        testCreateOfferDto.setBrandId(1L);
        testCreateOfferDto.setManufactureYear(2000);
        testCreateOfferDto.setName("pants");
        testCreateOfferDto.setPrice(BigDecimal.valueOf(200));
        testCreateOfferDto.setClotheType(ItemType.TROUSERS);
        testCreateOfferDto.setSize(SizeEnum.LARGE);
        testCreateOfferDto.setDescription("some description");

        BrandEntity testBrandEntity = new BrandEntity();
        testBrandEntity.setId(testCreateOfferDto.getBrandId());
        testBrandEntity.setName("Gucci");
        testBrandEntity.setSlogan("some slogan");
        testBrandEntity.setEstimatedAt(LocalDate.of(1978, 1, 12));

        UserDetails userDetails = new User(
                testUserEntity.getUsername(),
                testUserEntity.getPassword(),
                testUserEntity.getUserRoles()
                        .stream()
                        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getName().name()))
                        .collect(Collectors.toList()));

        when(mockUserRepository.findByUsername(userDetails.getUsername()))
                .thenReturn(Optional.of(testUserEntity));
        when(mockBrandRepository.findById(testCreateOfferDto.getBrandId()))
                .thenReturn(Optional.of(testBrandEntity));

        toTest.createOffer(testCreateOfferDto, userDetails);
        Mockito.verify(mockOfferRepository).save(offerEntityArgumentCaptor.capture());
        OfferEntity savedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(testCreateOfferDto.getBrandId(), savedOfferEntity.getBrand().getId());
        Assertions.assertEquals(testCreateOfferDto.getManufactureYear(), savedOfferEntity.getManufactureYear());
        Assertions.assertEquals(testCreateOfferDto.getName(), savedOfferEntity.getName());
        Assertions.assertEquals(testCreateOfferDto.getPrice(), savedOfferEntity.getPrice());
        Assertions.assertEquals(testCreateOfferDto.getClotheType(), savedOfferEntity.getType());
        Assertions.assertEquals(testCreateOfferDto.getSize(), savedOfferEntity.getSize());
    }

}