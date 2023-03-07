package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import bg.softuni.ecommerce.repository.BrandRepository;
import bg.softuni.ecommerce.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class ItemService {

    private ItemRepository itemRepository;
    private BrandRepository brandRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, BrandRepository brandRepository) {
        this.itemRepository = itemRepository;
        this.brandRepository = brandRepository;
    }

    public ItemEntity createItem(ItemType type, Integer manufactureYear, Long brandId, SizeEnum size) {
        BrandEntity brandEntity = this.brandRepository.findById(brandId).orElse(null);
        Objects.requireNonNull(brandEntity);

        ItemEntity itemEntity = new ItemEntity(type, manufactureYear, brandEntity, size);
        this.itemRepository.save(itemEntity);
        return itemEntity;
    }
}
