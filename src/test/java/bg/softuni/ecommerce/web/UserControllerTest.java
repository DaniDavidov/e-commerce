package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.GenderEnum;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.util.TestDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private UserRepository testUserRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        this.testUser = testDataUtils.createTestUser("user", "user@example.com");
    }

    @AfterEach
    void tearUp() {
        if (testDataUtils.getUserRepository().count() > 0) {
            List<UserEntity> all = testDataUtils.getUserRepository().findAll();
            testDataUtils.getUserRepository().deleteAll();
            testDataUtils.getUserRoleRepository().deleteAll();
        }
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testGetAllUsersPageWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testGetAllUsersPageWithAdminUserSuccess() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testGetUserDetailsPageWithRegularUserSuccess() throws Exception {
        mockMvc.perform(get("/users/{id}/details", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("user-details"))
                .andExpect(model().attributeExists("userDetails", "isBlacklisted", "rolesToBeChosen"));
    }

    @Test
    void testGetUserDetailsPageWithAnonymousUserRedirects() throws Exception {
        mockMvc.perform(get("/users/{id}/details", testUser.getId()))
                .andExpect(status().is3xxRedirection());
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testAddToBlacklistWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/blacklist/add/{id}", testUser.getId()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", password = "12345", authorities = "ROLE_ADMIN")
    @Test
    void testAddToBlacklistWithAdminUserRedirects() throws Exception {
        mockMvc.perform(post("/users/blacklist/add/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/users/%d/details", testUser.getId())));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testRemoveFromBlacklistWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/users/blacklist/remove/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testRemoveFromBlacklistWithAdminUserRedirects() throws Exception {
        mockMvc.perform(post("/users/blacklist/remove/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/users/%d/details", testUser.getId())));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testPromoteToModeratorWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/users/toModerator/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testPromoteToModeratorWithAdminUserRedirects() throws Exception {
        mockMvc.perform(post("/users/toModerator/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/users/%d/details", testUser.getId())));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testDemoteToUserWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/users/toUser/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().isForbidden());
    }


    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    @Test
    void testDemoteToUserWithAdminUserRedirects() throws Exception {
        mockMvc.perform(post("/users/toUser/{id}", testUser.getId()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/users/%d/details", testUser.getId())));
    }
}