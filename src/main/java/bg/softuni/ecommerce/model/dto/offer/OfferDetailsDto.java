package bg.softuni.ecommerce.model.dto.offer;

import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OfferDetailsDto {

    private Long id;

    private String name;

    private Long brandId;

    private String brandName;

    private ItemType clotheType;

    private String imageUrl;

    private SizeEnum size;

    private BigDecimal price;

    private OfferRating offerRating;

    private Long sellerId;

    private String sellerUsername;

    private LocalDate createdAt;

    private LocalDate lastModified;


}
