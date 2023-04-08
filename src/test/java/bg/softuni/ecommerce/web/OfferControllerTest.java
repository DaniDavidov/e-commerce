package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import bg.softuni.ecommerce.repository.BrandRepository;
import bg.softuni.ecommerce.repository.OfferRepository;
import bg.softuni.ecommerce.repository.UserRepository;
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

import java.math.BigDecimal;

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

    @Autowired
    private BrandRepository testBrandRepository;

    @Autowired
    private UserRepository testUserRepository;

    @Autowired
    private OfferRepository testOfferRepository;



    @BeforeEach
    void setUp() {
        BrandEntity testBrand = testDataUtils.createTestBrand();

        UserEntity seller = testDataUtils.createTestUser("seller", "seller@example.com");

        PictureEntity testPicture = testDataUtils.createTestPicture();

        ItemEntity testItem = testDataUtils.createTestItem(testBrand, testPicture);
        this.testOffer = testDataUtils.createTestOffer(seller, testItem);
    }

    @AfterEach
    void tearDown() {
        testDataUtils.getOfferRepository().deleteAll();
        testDataUtils.getItemRepository().deleteAll();
        testDataUtils.getBrandRepository().deleteAll();
        testDataUtils.getUserRepository().deleteAll();
    }

    @Test
    void testAllOffersPageShown() throws Exception {
        mockMvc.perform(get("/offers/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("offers"));
    }



    @WithMockUser(username = "user", password = "12345", authorities = "ROLE_USER")
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

    @WithMockUser(username = "user", authorities = "ROLE_USER")
    @Test
    void testAddOfferPageWithAuthenticatedUserShown() throws Exception {
        mockMvc.perform(get("/offers/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("offer-add"))
                .andExpect(model().attributeExists("createOfferDto"))
                .andExpect(model().attributeExists("brands"));
    }


    @WithUserDetails(value = "seller", userDetailsServiceBeanName = "testUserDataService")
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
                .andExpect(view().name("redirect:/offers/all"));
    }

    @WithMockUser(username = "user", authorities = "ROLE_USER")
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


    @WithMockUser(username = "user", password = "12345", authorities = "ROLE_USER")
    @Test
    void testUpdateOfferPageRegularUserRedirects() throws Exception {
        mockMvc.perform(get("/offers/update/{id}", testOffer.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/offers/1/details"));
    }

    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "testUserDataService")
    @Test
    void testUpdateBookPageAdminShown() throws Exception {
        mockMvc.perform(get("/offers/update/{id}", testOffer.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("offer-add"))
                .andExpect(model().attributeExists("brands"))
                .andExpect(model().attributeExists("createOfferDto"));
    }

    @Test
    @WithMockUser(username = "seller", password = "12345", authorities = "ROLE_USER")
    void testUpdateOfferWithOfferSellerOrAdminSuccess() throws Exception {
        mockMvc.perform(post("/offers/update/{id}", testOffer.getId()).with(csrf())
                        .param("brandId", testOffer.getItem().getBrand().getId().toString())
                        .param("manufactureYear", "2015")
                        .param("name", "cool cloth")
                        .param("price", BigDecimal.valueOf(2000).toString())
                        .param("clotheType", ItemType.CREW_NECK.name())
                        .param("size", SizeEnum.MEDIUM.name())
                        .param("description", "some text"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/offers/%d/details", testOffer.getId())));
    }

    @Test
    @WithMockUser(username = "seller", password = "12345", authorities = "ROLE_USER")
    void testUpdateOfferWithWrongInputsRedirects() throws Exception {
        mockMvc.perform(post("/offers/update/{id}", testOffer.getId()).with(csrf())
                        .param("brandId", "1")
                        .param("manufactureYear", "2015")
                        .param("name", "cool cloth")
                        .param("price", BigDecimal.valueOf(2000).toString())
                        .param("clotheType", ItemType.CREW_NECK.name())
                        .param("size", SizeEnum.MEDIUM.name())
                        .param("description", "some text")
                        .param("picture", "picture"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(String.format("redirect:/offers/update/%d", testOffer.getId())));
    }

    @Test
    @WithMockUser(
            username = "admin",
            password = "12345",
            authorities = "ROLE_ADMIN"
    )
    void testDeleteOfferByAdminWorks() throws Exception {
        mockMvc.perform(post("/offers/delete/{id}", testOffer.getId()).
                        with(csrf())
                ).
                andExpect(status().is3xxRedirection()).
                andExpect(view().name("redirect:/offers/all"));
    }


}