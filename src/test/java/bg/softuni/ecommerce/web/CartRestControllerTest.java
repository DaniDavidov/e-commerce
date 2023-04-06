package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.cart.AddItemToCartDto;
import bg.softuni.ecommerce.model.dto.cart.OfferAddedToCartDto;
import bg.softuni.ecommerce.service.CartService;
import bg.softuni.ecommerce.service.OfferService;
import bg.softuni.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class CartRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CartService cartService;


    @WithMockUser(authorities = "ROLE_USER", username = "user@example.com")
    @Test
    void testAddToCartWithRegularUserSuccessful() throws Exception {
        when(cartService.addToCart(any(UserDetails.class), any(AddItemToCartDto.class)))
                .thenReturn(true);
        when(cartService.getAddedOffer(any(AddItemToCartDto.class)))
                .thenReturn(new OfferAddedToCartDto(2, "pants"));

        ObjectMapper objectMapper = new ObjectMapper();
        AddItemToCartDto addProductDto = new AddItemToCartDto(1L, 1);

        mockMvc.perform(post("/api/cart")
                        .content(objectMapper.writeValueAsString(addProductDto))
                        .with(csrf())
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.offerName").value("pants"))
                .andExpect(jsonPath("$.quantity").value(2));

    }
}