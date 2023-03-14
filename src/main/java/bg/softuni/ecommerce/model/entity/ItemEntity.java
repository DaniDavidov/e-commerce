package bg.softuni.ecommerce.model.entity;

import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType type;

    @Column(name = "manufacture_year", nullable = false)
    private Integer manufactureYear;

    @ManyToOne
    private PictureEntity picture;

    @ManyToOne
    private BrandEntity brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SizeEnum size;


}
