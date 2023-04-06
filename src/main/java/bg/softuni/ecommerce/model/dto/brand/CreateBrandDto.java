package bg.softuni.ecommerce.model.dto.brand;

import bg.softuni.ecommerce.model.validation.UniqueBrandName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandDto {

    @NotNull
    @Size(min = 2)
    @UniqueBrandName
    private String name;

    @Size(min = 5)
    private String slogan;

    @PastOrPresent
    private LocalDate estimatedAt;
}
