package bg.softuni.ecommerce.util;

import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.*;
import bg.softuni.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TestDataUtils {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BrandRepository brandRepository;
    private final OfferRepository offerRepository;
    private final OrderRepository orderRepository;
    private final PictureRepository pictureRepository;
    private final BlacklistRepository blacklistRepository;
    private final CartRepository cartRepository;

    @Autowired
    public TestDataUtils(UserRepository userRepository,
                         UserRoleRepository userRoleRepository,
                         BrandRepository brandRepository,
                         OfferRepository offerRepository,
                         OrderRepository orderRepository,
                         PictureRepository pictureRepository,
                         BlacklistRepository blacklistRepository,
                         CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.brandRepository = brandRepository;
        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.pictureRepository = pictureRepository;
        this.blacklistRepository = blacklistRepository;
        this.cartRepository = cartRepository;
    }

    public void initRoles() {
        if (userRoleRepository.count() == 0) {
            List<UserRoleEntity> userRoles = Arrays.stream(UserRoleEnum.values())
                    .map(UserRoleEntity::new)
                    .toList();
            this.userRoleRepository.saveAll(userRoles);
        }
    }

    public UserEntity createTestAdmin(String username, String email) {

        initRoles();

        var admin = new UserEntity(username, email, "12345", "Admin", "Adminov", "089089089", GenderEnum.MALE,
                new HashSet<>(userRoleRepository.findAll()));

        return userRepository.save(admin);
    }

    public UserEntity createTestUser(String username, String email) {

        initRoles();

        var user = new UserEntity(username, email, "12345", "User", "Userov", "089089089", GenderEnum.MALE,
                userRoleRepository.
                        findAll().stream().
                        filter(r -> r.getName() == UserRoleEnum.USER).
                        collect(Collectors.toSet()));

        return this.userRepository.save(user);
    }

    public BrandEntity createTestBrand() {
        BrandEntity brandEntity =  new BrandEntity("Valentino", "amazing clothes", LocalDate.of(2000, 1, 12));
        return this.brandRepository.save(brandEntity);
    }

    public CartEntity createTestCart() {
        CartEntity cart = new CartEntity(createTestOffer(createTestUser("publisher", "publisher@examle.com"), createTestBrand(), createTestPicture()), 2, createTestUser("buyer", "buyer@example.com"));
        return this.cartRepository.save(cart);
    }

    public OfferEntity createTestOffer(UserEntity publisher, BrandEntity brand, PictureEntity picture) {
        OfferEntity offer = new OfferEntity(ItemType.TROUSERS, 2000, picture, brand, SizeEnum.LARGE, "a test offer", OfferRating.ZERO, publisher, BigDecimal.valueOf(2000), LocalDate.now(), LocalDate.now(), "text");
        return this.offerRepository.save(offer);
    }

    public Long getLastAddedOfferId() {
        return offerRepository.findAll().stream()
                .max(Comparator.comparingLong(OfferEntity::getId)).get().getId();
    }

    public void cleanUpDatabase() {
        orderRepository.deleteAll();
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
        offerRepository.deleteAll();
        brandRepository.deleteAll();
        blacklistRepository.deleteAll();
    }

    public PictureEntity createTestPicture() {
        PictureEntity picture = new PictureEntity("");

        return this.pictureRepository.save(picture);
    }

    public OrderEntity createTestOrder() {
        OrderEntity order = new OrderEntity(createTestUser("user1", "user1@xample.com"), createTestItems());
        return this.orderRepository.save(order);
    }

    public Map<OfferEntity, Integer> createTestItems() {
        HashMap<OfferEntity, Integer> items = new HashMap<>();
        BrandEntity testBrand = createTestBrand();
        PictureEntity testPicture = createTestPicture();

        UserEntity publisher = createTestUser("publisher", "publisher@example.com");
        items.put(createTestOffer(publisher, testBrand, testPicture), 3);
        return items;
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public UserRoleRepository getUserRoleRepository() {
        return userRoleRepository;
    }

    public BrandRepository getBrandRepository() {
        return brandRepository;
    }


    public OfferRepository getOfferRepository() {
        return offerRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public PictureRepository getPictureRepository() {
        return pictureRepository;
    }

    public CartRepository getCartRepository() {
        return cartRepository;
    }
}
