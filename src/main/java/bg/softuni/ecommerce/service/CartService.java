package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.cart.AddItemToCartDto;
import bg.softuni.ecommerce.model.dto.cart.CartDto;
import bg.softuni.ecommerce.model.dto.cart.OfferAddedToCartDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.CartEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final OfferService offerService;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository, OfferService offerService, UserService userService) {
        this.cartRepository = cartRepository;
        this.offerService = offerService;
        this.userService = userService;
    }

    public boolean addToCart(UserDetails userDetails, AddItemToCartDto addItemToCartDto) {
        OfferEntity offer = this.offerService.getOfferById(addItemToCartDto.getOfferId());

        UserEntity user = this.userService.getUserByUsername(userDetails.getUsername());

        CartEntity cartEntity = new CartEntity();
        cartEntity.setBuyer(user);
        cartEntity.setOffer(offer);

        this.cartRepository.save(cartEntity);
        return true;
    }


    public OfferAddedToCartDto getAddedOffer(AddItemToCartDto cartDto) {
        return cartRepository.findByOfferId(cartDto.getOfferId())
                .map(cartEntity -> new OfferAddedToCartDto(cartEntity.getQuantity(), cartEntity.getOffer().getName()))
                .orElseThrow(() -> new RuntimeException("No such offer"));
    }

    public List<CartDto> getAllOffersInCart(UserDetails userDetails) {
        return this.cartRepository.findAllByBuyerUsername(userDetails.getUsername())
                .stream()
                .map(this::mapToCartDto)
                .collect(Collectors.toList());
    }

    private CartDto mapToCartDto(CartEntity cartEntity) {
        return new CartDto(
                cartEntity.getId(),
                cartEntity.getQuantity(),
                cartEntity.getOffer().getName(),
                cartEntity.getOffer().getId(),
                cartEntity.getOffer().getItem().getBrand().getName(),
                cartEntity.getOffer().getItem().getBrand().getId(),
                cartEntity.getOffer().getSeller().getUsername(),
                cartEntity.getOffer().getSeller().getId());
    }
}
