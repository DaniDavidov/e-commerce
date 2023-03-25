package bg.softuni.ecommerce.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "brand", targetEntity = ItemEntity.class)
    private List<ItemEntity> items;

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
