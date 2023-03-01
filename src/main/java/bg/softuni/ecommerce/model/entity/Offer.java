package bg.softuni.ecommerce.model.entity;

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
public class Offer extends BaseEntity {

    @ManyToOne
    private Item item;

    @ManyToOne
    private UserEntity seller;

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDate createdAt;

    private LocalDate updatedAt;


}
