package bg.softuni.ecommerce.web;

import bg.softuni.ecommerce.model.dto.cart.CartDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.service.CartService;
import bg.softuni.ecommerce.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OfferService offerService;

    @Autowired
    public CartController(CartService cartService, OfferService offerService) {
        this.cartService = cartService;
        this.offerService = offerService;
    }

    @GetMapping
    public String getCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<CartDto> cart = cartService.getAllOffersInCart(userDetails);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/{id}/remove")
    public String deleteOfferFromCart(@PathVariable("id") Long cartId,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        this.cartService.deleteOfferFromCart(cartId);
        return "redirect:/cart";
    }

    @PostMapping("/removeAll")
    public String deleteAllOffersFromCart(@AuthenticationPrincipal UserDetails userDetails) {
        this.cartService.deleteCart(userDetails);
        return "redirect:/cart";
    }

}
