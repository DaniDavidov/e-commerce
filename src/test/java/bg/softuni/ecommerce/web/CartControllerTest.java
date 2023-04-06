package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.repository.CartRepository;
import bg.softuni.ecommerce.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    private static final int ITEM_QUANTITY = 2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private CartEntity testCart;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        UserEntity publisher = this.testDataUtils.createTestUser("publisher", "publisher@example.com");
        BrandEntity testBrand = this.testDataUtils.createTestBrand();
        PictureEntity testPicture = this.testDataUtils.createTestPicture();

        ItemEntity testItem = this.testDataUtils.createTestItem(testBrand, testPicture);
        OfferEntity testOffer = this.testDataUtils.createTestOffer(publisher, testItem);

        UserEntity owner = this.testDataUtils.createTestUser("owner", "owner@example.com");
        this.testCart = new CartEntity(testOffer, ITEM_QUANTITY, owner);
        this.cartRepository.save(testCart);

    }

    @AfterEach
    void tearDown() {
        this.testDataUtils.getCartRepository().deleteAll();
        this.testDataUtils.getOfferRepository().deleteAll();
        this.testDataUtils.getItemRepository().deleteAll();
        this.testDataUtils.getUserRepository().deleteAll();
        this.testDataUtils.getBrandRepository().deleteAll();
        this.testDataUtils.getPictureRepository().deleteAll();
    }

    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testGetCartWithRegularUserSuccess() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"));
    }

    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testDeleteOfferFromCartSuccess() throws Exception {
        mockMvc.perform(post("/cart/{id}/remove", testCart.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

    }

    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testDeleteAllOffersFromCartSuccess() throws Exception {
        mockMvc.perform(post("/cart/removeAll", testCart.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));
    }

    @WithUserDetails(value = "buyer", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testConfirmCartSuccess() throws Exception {
        mockMvc.perform(post("/cart/confirm").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));
    }
}