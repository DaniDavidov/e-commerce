package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.OrderEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
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
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private OrderEntity testOrder;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.testOrder = testDataUtils.createTestOrder();
    }

//    @AfterEach
//    void tearDown() {
//        testDataUtils.cleanUpDatabase();
//    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testPOrdersPageAdminUserShown() throws Exception {
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
    @WithMockUser(authorities = "ROLE_USER")
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
        mockMvc.perform(post("/orders/{id}/confirm", testOrder.getId()))
                .andExpect(status().isForbidden());
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testConfirmOrderWithAdminUserSuccess() throws Exception {
        mockMvc.perform(post("/orders/{id}/confirm", testOrder.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/orders"));
    }
}