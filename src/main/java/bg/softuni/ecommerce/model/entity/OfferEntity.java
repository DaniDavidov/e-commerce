package bg.softuni.ecommerce.model.entity;

import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import jakarta.persistence.*;
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
@Entity
@Table(name = "offers")
public class OfferEntity extends BaseEntity {

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


    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private OfferRating rating;

    @ManyToOne
    private UserEntity seller;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    private String description;


}
