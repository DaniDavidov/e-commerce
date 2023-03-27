package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.cart.AddItemToCartDto;
import bg.softuni.ecommerce.model.dto.cart.OfferAddedToCartDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.service.CartService;
import bg.softuni.ecommerce.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class CartRestController {

    private final OfferService offerService;
    private final CartService cartService;

    @Autowired
    public CartRestController(OfferService offerService, CartService cartService) {
        this.offerService = offerService;
        this.cartService = cartService;
    }

    @PostMapping(path = "/api/cart", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OfferAddedToCartDto> addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestBody AddItemToCartDto cartDto) {
        if (cartService.addToCart(userDetails, cartDto)) {
            return ResponseEntity.ok(cartService.getAddedOffer(cartDto));
        }

        return ResponseEntity.notFound().build();
    }
}
