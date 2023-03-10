package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.BrandDto;
import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {
    private BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDto> allBrands() {
        return this.brandRepository.findAll()
                .stream()
                .map(this::mapToMapDto)
                .collect(Collectors.toList());
    }

    private BrandDto mapToMapDto(BrandEntity brandEntity) {
        return new BrandDto(brandEntity.getId(), brandEntity.getImageUrl(), brandEntity.getName());
    }
}
