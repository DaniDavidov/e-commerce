package bg.softuni.ecommerce.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_entities")
public class CartEntity extends BaseEntity {

    @OneToOne
    private OfferEntity offer;

    private int quantity;

    @ManyToOne
    private UserEntity buyer;

}
