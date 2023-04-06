package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.brand.CreateBrandDto;
import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.BrandRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    private BrandService toTest;

    @Mock
    private BrandRepository mockBrandRepository;

    @Captor
    private ArgumentCaptor<BrandEntity> brandEntityArgumentCaptor;

    private BrandEntity testBrandEntity;
    @BeforeEach
    void setUp() {
        this.toTest = new BrandService(mockBrandRepository);
        this.testBrandEntity = new BrandEntity(
                "Test brand",
                "some slogan",
                LocalDate.of(2000, 1, 12));
    }

    @Test
    void testCreateBrandSuccess() {
        CreateBrandDto testCreateBrandDto = new CreateBrandDto(
                testBrandEntity.getName(),
                testBrandEntity.getSlogan(),
                testBrandEntity.getEstimatedAt());

        toTest.createBrand(testCreateBrandDto);

        Mockito.verify(mockBrandRepository).save(brandEntityArgumentCaptor.capture());

        BrandEntity savedBrandEntity = brandEntityArgumentCaptor.getValue();

        Assertions.assertEquals(testCreateBrandDto.getName(), savedBrandEntity.getName());
        Assertions.assertEquals(testCreateBrandDto.getSlogan(), savedBrandEntity.getSlogan());
        Assertions.assertEquals(testCreateBrandDto.getEstimatedAt(), savedBrandEntity.getEstimatedAt());
    }
}