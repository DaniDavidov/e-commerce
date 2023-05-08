package bg.softuni.ecommerce.model.events;

import bg.softuni.ecommerce.model.entity.CartEntity;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Map;

public class BuyProductEvent extends ApplicationEvent {

    private final List<CartEntity> shoppingCart;

    public BuyProductEvent(Object source, List<CartEntity> shoppingCart) {
        super(source);
        this.shoppingCart = shoppingCart;
    }

    public List<CartEntity> getShoppingCart() {
        return shoppingCart;
    }

}
