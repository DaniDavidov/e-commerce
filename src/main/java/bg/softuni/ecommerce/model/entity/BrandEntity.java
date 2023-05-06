package bg.softuni.ecommerce.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "brands")
public class BrandEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "brand", targetEntity = OfferEntity.class, fetch = FetchType.EAGER)
    private List<OfferEntity> offers;

    private String slogan;

    @Column(nullable = false, name = "estimated_at")
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd")
    private LocalDate estimatedAt;

    public BrandEntity(String name, String slogan, LocalDate estimatedAt) {
        this.name = name;
        this.slogan = slogan;
        this.estimatedAt = estimatedAt;
    }
}
