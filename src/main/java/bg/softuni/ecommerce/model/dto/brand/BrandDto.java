package bg.softuni.ecommerce.model.dto.brand;

import bg.softuni.ecommerce.model.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BrandDto {

    private Long id;

    private String name;

    private Integer numberOfItems;

}
