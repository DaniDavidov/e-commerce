package bg.softuni.ecommerce.model.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;

    private int quantity;

    private String offerName;

    private Long offerId;

    private String brandName;

    private Long brandId;

    private String sellerUsername;

    private Long sellerId;
}
