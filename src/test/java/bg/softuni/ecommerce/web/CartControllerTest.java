package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.CartEntity;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.PictureEntity;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private CartEntity testCart;

    @BeforeEach
    void setUp() {
        this.testCart = testDataUtils.createTestCart();
    }

    @AfterEach
    void tearDown() {
        this.testDataUtils.cleanUpDatabase();
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