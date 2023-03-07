package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.OfferRepository;
import bg.softuni.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository, ItemService itemService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }

    public void createOffer(CreateOfferDto createOfferDto, UserDetails userDetails) {
        UserEntity userEntity = this.userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        Objects.requireNonNull(userDetails);
        ItemEntity item = this.itemService.createItem(
                createOfferDto.getClotheType(),
                createOfferDto.getManufactureYear(),
                createOfferDto.getBrandId(),
                createOfferDto.getSize());

        OfferEntity offerEntity = mapToOfferEntity(createOfferDto, userEntity, item);
        this.offerRepository.save(offerEntity);
    }

    private OfferEntity mapToOfferEntity(CreateOfferDto createOfferDto, UserEntity user, ItemEntity item) {
        return new OfferEntity(
                item,
                createOfferDto.getName(),
                user,
                createOfferDto.getPrice(),
                LocalDate.now(),
                LocalDate.now());
    }
}
