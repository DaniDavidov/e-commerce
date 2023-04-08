package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.*;
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

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private OrderEntity testOrder;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        UserEntity owner = this.testDataUtils.createTestUser("owner", "owner@example.com");
        BrandEntity testBrand = this.testDataUtils.createTestBrand();
        PictureEntity testPicture = this.testDataUtils.createTestPicture();
        UserEntity publisher = this.testDataUtils.createTestUser("publisher", "publisher@example.com");

        ItemEntity testItem = this.testDataUtils.createTestItem(testBrand, testPicture);
        OfferEntity testOffer = this.testDataUtils.createTestOffer(publisher, testItem);
        this.testOrder = new OrderEntity(owner, Map.of(testOffer, 3));
        this.testDataUtils.getOrderRepository().save(testOrder);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.getOrderRepository().deleteAll();
        testDataUtils.getOfferRepository().deleteAll();
        testDataUtils.getItemRepository().deleteAll();
        testDataUtils.getUserRepository().deleteAll();
        testDataUtils.getBrandRepository().deleteAll();
        testDataUtils.getPictureRepository().deleteAll();
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testOrdersPageAdminUserShown() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));

        mockMvc.perform(get("/orders/unprocessed"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "ROLE_USER")
    void testGetOrdersRegularUser_Forbidden() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/orders/unprocessed"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testGetOrdersAdminUserSuccess() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));

        mockMvc.perform(get("/orders/unprocessed"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void testOrderDetailsPageWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(get("/orders/{id}/details", testOrder.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testOrderDetailsPageWithAdminUserSuccess() throws Exception {
        mockMvc.perform(get("/orders/{id}/details", testOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("order-details"))
                .andExpect(model().attributeExists("order", "items"));
    }

    @WithMockUser(authorities = "ROLE_USER")
    @Test
    void testConfirmOrderWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/orders/{id}/confirm", testOrder.getId()).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testConfirmOrderWithAdminUserSuccess() throws Exception {
        mockMvc.perform(post("/orders/{id}/confirm", testOrder.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders/unprocessed"));
    }
}