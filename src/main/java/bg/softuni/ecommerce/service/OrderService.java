package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.OrderDto;
import bg.softuni.ecommerce.model.entity.CartEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.OrderEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class OrderService {

    private final CartService cartService;
    private final UserService userService;
    private final OrderRepository orderRepository;

    public OrderService(CartService cartService, UserService userService, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.userService = userService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createOrder(UserDetails userDetails) {
        UserEntity buyer = this.userService.getUserByUsername(userDetails.getUsername());

        List<CartEntity> cart = this.cartService.getCartByBuyerId(buyer.getId());
        Map<OfferEntity, Integer> offers = new LinkedHashMap<>();

        for (CartEntity entry : cart) {
            OfferEntity offer = entry.getOffer();
            int quantity = entry.getQuantity();
            offers.put(offer, quantity);
        }

        OrderEntity orderEntity = new OrderEntity(buyer, offers);

        this.orderRepository.save(orderEntity);
    }

    public List<OrderDto> getAllUnprocessedOrders() {
        return this.orderRepository.findAllUnprocessed()
                .stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getAllProcessedOrders() {
        return this.orderRepository.findAllProcessed()
                .stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderEntity getOrderById(Long orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("No such order");
        }

        return orderOpt.get();
    }

    @Transactional
    public Map<OfferEntity, Integer> getOrderItems(Long id) {
        return getOrderById(id).getItems();
    }

    public OrderDto mapToOrderDto(OrderEntity orderEntity) {
        return new OrderDto(
                orderEntity.getId(),
                orderEntity.getOwner().getUsername(),
                orderEntity.isProcessed());
    }

    public void confirmOrderById(Long orderId) {
        OrderEntity order = getOrderById(orderId);
        order.setProcessed(true);
        orderRepository.save(order);
    }

    public void clearAllConfirmedOrders() {
        this.orderRepository.deleteAllProcessed();
    }
}
