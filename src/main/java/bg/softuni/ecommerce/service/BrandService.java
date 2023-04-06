package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.brand.BrandDto;
import bg.softuni.ecommerce.model.dto.brand.CreateBrandDto;
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
                .map(this::mapToBrandDto)
                .collect(Collectors.toList());
    }

    private BrandDto mapToBrandDto(BrandEntity brandEntity) {
        return new BrandDto(
                brandEntity.getId(),
                brandEntity.getName(),
                brandEntity.getItems().size());
    }

    public void createBrand(CreateBrandDto createBrandDto) {
        BrandEntity brandEntity = mapToBrandEntity(createBrandDto);

        this.brandRepository.save(brandEntity);
    }

    private BrandEntity mapToBrandEntity(CreateBrandDto createBrandDto) {
        return new BrandEntity(
                createBrandDto.getName(),
                createBrandDto.getSlogan(),
                createBrandDto.getEstimatedAt());
    }
}
