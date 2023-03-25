package bg.softuni.ecommerce.model.validation;

import bg.softuni.ecommerce.repository.BrandRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueBrandNameValidator implements ConstraintValidator<UniqueBrandName, String> {
    private final BrandRepository brandRepository;

    @Autowired
    public UniqueBrandNameValidator(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.brandRepository.findByName(value).isEmpty();
    }
}
