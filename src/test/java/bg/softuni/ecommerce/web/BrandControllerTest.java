package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.util.TestDataUtils;
import bg.softuni.ecommerce.util.TestUserDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private BrandEntity testBrand;



    @BeforeEach
    void setUp() {
        this.testBrand = testDataUtils.createTestBrand();
    }

    @AfterEach
    void tearUp() {
        this.testDataUtils.cleanUpDatabase();
    }

    @Test
    void testAllBrandsPageShown() throws Exception {
        mockMvc.perform(get("/brands/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("brands"))
                .andExpect(model().attributeExists("brands"));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testGetAddBrandPageWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(get("/brands/add"))
                .andExpect(status().isForbidden());
    }


    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testPostAddBrandPageWithRegularUserIsForbidden() throws Exception {
        mockMvc.perform(post("/brands/add"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", password = "12345", authorities = "ROLE_ADMIN")
    @Test
    void testPostAddBrandPageWithAdminUserRedirects() throws Exception {
        mockMvc.perform(post("/brands/add")
                .param("name", "adibas")
                .param("slogan", "some slogan")
                        .param("estimatedAt", String.valueOf(LocalDate.of(2022, 1, 12)))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/brands/all"));

    }


}