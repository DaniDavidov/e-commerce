package bg.softuni.ecommerce.model.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferAddedToCartDto {

    private int quantity;

    private String offerName;
}
