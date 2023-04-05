package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
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
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataUtils testDataUtils;

    private OfferEntity testOffer;


    @BeforeEach
    void setUp() {
        testOffer = testDataUtils.createTestOffer(testDataUtils.createTestUser("userr", "userr@example.com"),
                testDataUtils.createTestItem(testDataUtils.createTestBrand(), testDataUtils.createTestPicture()));
    }

//    @AfterEach
//    void tearDown() {
//        testDataUtils.cleanUpDatabase();
//    }

    @Test
    void testAllOffersPageShown() throws Exception {
        mockMvc.perform(get("/offers/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers"));
    }


    @Test
    void testOfferDetailsPageShown() throws Exception {
        mockMvc.perform(get("/offers/{offerId}/details", testOffer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("offer-details"))
                .andExpect(model().attributeExists("offerDetails"));
    }

    @Test
    void testAddOfferPageWithNotAuthenticatedUserRedirects() throws Exception {
        mockMvc.perform(get("/offers/add"))
                .andExpect(status().is3xxRedirection());
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testAddOfferPageWithAuthenticatedUserShown() throws Exception {
        mockMvc.perform(get("/offers/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("offer-add"))
                .andExpect(model().attributeExists("createOfferDto"))
                .andExpect(model().attributeExists("brands"));
    }


    @WithUserDetails(value = "user", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testAddOfferPageWithAuthenticatedUserSuccess() throws Exception {
        mockMvc.perform(post("/offers/add")
                .param("brandId", testOffer.getItem().getBrand().getId().toString())
                .param("manufactureYear", testOffer.getItem().getManufactureYear().toString())
                .param("name", "pants")
                .param("price", "200")
                .param("clotheType", ItemType.TROUSERS.name())
                .param("size", SizeEnum.MEDIUM.name())
                .param("description", "wefhiuhwiu")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/offers/%d/details", testDataUtils.getLastAddedOfferId())));
    }

    @WithUserDetails(value = "user", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testAddOfferPageWithInvalidInputRedirects() throws Exception {
        mockMvc.perform(post("/offers/add")
                        .param("brandId", testOffer.getItem().getBrand().getId().toString())
                        .param("manufactureYear", testOffer.getItem().getManufactureYear().toString())
                        .param("name", "pants")
                        .param("price", "-8")
                        .param("clotheType", ItemType.TROUSERS.name())
                        .param("size", SizeEnum.MEDIUM.name())
                        .param("description", "wefhiuhwiu")
                        .param("picture", "nothing")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/offers/add"));
    }


//    @WithUserDetails(value = "user", userDetailsServiceBeanName = "testUserDataService")
//    @Test
//    void testUpdateBookPageRegularUserRedirects() throws Exception {
//        mockMvc.perform(get("/offers/{id}/update", testOffer.getId()))
//                .andExpect()
//    }
//
//    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDataService")
//    @Test
//    void testUpdateBookPageAdminShown() throws Exception {
//        mockMvc.perform(get("/offers/update/{id}", testBook.getId()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("book-add-or-update"))
//                .andExpect(model().attributeExists("bookModel"))
//                .andExpect(model().attributeExists("authors"))
//                .andExpect(model().attributeExists("genres"));
//    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = "ROLE_ADMIN"
    )
    void testDeleteOfferByAdminWorks() throws Exception {
        mockMvc.perform(delete("/offers/delete/{id}", testOffer.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/offers/all"));
    }


}