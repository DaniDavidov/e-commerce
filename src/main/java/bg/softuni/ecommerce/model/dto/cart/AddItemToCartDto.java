package bg.softuni.ecommerce.model.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddItemToCartDto {

    private Long offerId;

    private int quantity;

}
