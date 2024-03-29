package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.cart.AddItemToCartDto;
import bg.softuni.ecommerce.model.dto.cart.CartDto;
import bg.softuni.ecommerce.model.dto.cart.OfferAddedToCartDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.CartEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.error.OfferNotFoundException;
import bg.softuni.ecommerce.repository.CartRepository;
import jakarta.transaction.Transactional;
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

    public CartEntity addToCart(UserDetails userDetails, AddItemToCartDto addItemToCartDto) {
        OfferEntity offer = this.offerService.getOfferById(addItemToCartDto.getOfferId());

        UserEntity user = this.userService.getUserByUsername(userDetails.getUsername());

        CartEntity cartEntity = new CartEntity();
        cartEntity.setBuyer(user);
        cartEntity.setOffer(offer);
        cartEntity.setQuantity(addItemToCartDto.getQuantity());

        this.cartRepository.save(cartEntity);
        return cartEntity;
    }


    public OfferAddedToCartDto getAddedOffer(Long id) {
        return cartRepository.findById(id)
                .map(cartEntity -> new OfferAddedToCartDto(cartEntity.getQuantity(), cartEntity.getOffer().getName()))
                .orElseThrow(() -> new OfferNotFoundException(id));
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
                cartEntity.getOffer().getBrand().getName(),
                cartEntity.getOffer().getBrand().getId(),
                cartEntity.getOffer().getSeller().getUsername(),
                cartEntity.getOffer().getSeller().getId());
    }

    public void deleteOfferFromCart(Long cartId) {
        this.cartRepository.deleteById(cartId);
    }

    public void deleteCart(UserDetails userDetails) {
        UserEntity user = this.userService.getUserByUsername(userDetails.getUsername());
        this.cartRepository.deleteAllByBuyerId(user.getId());
    }

    @Transactional
    public List<CartEntity> getCartByBuyerId(Long buyerId) {
        return this.cartRepository.findAllByBuyerId(buyerId);
    }
}
