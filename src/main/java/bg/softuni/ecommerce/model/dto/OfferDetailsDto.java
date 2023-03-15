package bg.softuni.ecommerce.model.dto;

import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OfferDetailsDto {

    private String name;

    private String brandName;

    private ItemType clotheType;

    private String imageUrl;

    private SizeEnum size;

    private BigDecimal price;

    private OfferRating offerRating;

    private String sellerUsername;

    private String sellerEmail;

    private String sellerPhoneNumber;


}
