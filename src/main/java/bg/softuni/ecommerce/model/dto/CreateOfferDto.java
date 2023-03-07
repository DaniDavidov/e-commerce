package bg.softuni.ecommerce.model.dto;

import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferDto {

    @NotNull
    private Long brandId;

    @Positive
    @NotNull
    private Integer manufactureYear;

    @NotEmpty
    private String name;

    @Positive
    private BigDecimal price;

    @NotNull
    private ItemType clotheType;

    @NotNull
    private SizeEnum size;

    private String description;

    @NotEmpty
    private String imageUrl;
}
