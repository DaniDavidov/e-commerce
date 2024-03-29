package bg.softuni.ecommerce.model.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandDto {

    private Long id;

    private String name;

    private Integer numberOfItems;

}
