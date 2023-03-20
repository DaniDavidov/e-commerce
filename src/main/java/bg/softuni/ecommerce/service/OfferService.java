package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.mapper.OfferMapper;
import bg.softuni.ecommerce.repository.OfferRepository;
import bg.softuni.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private static final OfferRating DEFAULT_OFFER_RATING = OfferRating.ZERO;

    @Autowired
    public OfferService(OfferRepository offerRepository,
                        UserRepository userRepository,
                        ItemService itemService) {
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
                createOfferDto.getImageUrl(),
                createOfferDto.getBrandId(),
                createOfferDto.getSize());

        OfferEntity offerEntity = mapToOfferEntity(createOfferDto, userEntity, item);
        this.offerRepository.save(offerEntity);
    }

    public Page<OfferDetailsDto> getAllOffers(Pageable pageable) {
        return offerRepository
                .findAll(pageable)
                .map(this::mapToOfferDetailsDto);
    }

    private OfferEntity mapToOfferEntity(CreateOfferDto createOfferDto, UserEntity user, ItemEntity item) {
        return new OfferEntity(
                item,
                createOfferDto.getName(),
                DEFAULT_OFFER_RATING,
                user,
                createOfferDto.getPrice(),
                LocalDate.now(),
                LocalDate.now());
    }

    private OfferDetailsDto mapToOfferDetailsDto(OfferEntity offerEntity) {
        return new OfferDetailsDto(
                offerEntity.getId(),
                offerEntity.getName(),
                offerEntity.getItem().getBrand().getName(),
                offerEntity.getItem().getType(),
                offerEntity.getItem().getPicture().getUrl(),
                offerEntity.getItem().getSize(),
                offerEntity.getPrice(),
                offerEntity.getRating(),
                offerEntity.getSeller().getUsername(),
                offerEntity.getSeller().getEmail(),
                offerEntity.getSeller().getPhoneNumber());
    }
}
