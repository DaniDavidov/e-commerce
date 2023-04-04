package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.BrandEntity;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.PictureEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.enums.ItemType;
import bg.softuni.ecommerce.model.entity.enums.SizeEnum;
import bg.softuni.ecommerce.repository.BrandRepository;
import bg.softuni.ecommerce.repository.ItemRepository;
import bg.softuni.ecommerce.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final PictureRepository pictureRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, BrandRepository brandRepository, PictureRepository pictureRepository) {
        this.itemRepository = itemRepository;
        this.brandRepository = brandRepository;
        this.pictureRepository = pictureRepository;
    }

    public ItemEntity createItem(ItemType type, Integer manufactureYear, String imageUrl, Long brandId, SizeEnum size) {
        BrandEntity brandEntity = this.brandRepository.findById(brandId).orElse(null);
        Objects.requireNonNull(brandEntity);
        PictureEntity picture = new PictureEntity(imageUrl);
        this.pictureRepository.save(picture);

        ItemEntity itemEntity = new ItemEntity(type, manufactureYear, picture, brandEntity, size);
        this.itemRepository.save(itemEntity);
        return itemEntity;
    }
}
