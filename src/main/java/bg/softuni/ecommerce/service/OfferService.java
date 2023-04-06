package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.ItemEntity;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import bg.softuni.ecommerce.model.entity.PictureEntity;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.error.OfferNotFoundException;
import bg.softuni.ecommerce.repository.OfferRepository;
import bg.softuni.ecommerce.repository.PictureRepository;
import bg.softuni.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ImageCloudService imageCloudService;
    private final PictureRepository pictureRepository;
    private static final OfferRating DEFAULT_OFFER_RATING = OfferRating.ZERO;

    @Autowired
    public OfferService(OfferRepository offerRepository,
                        UserService userService,
                        ItemService itemService,
                        ImageCloudService imageCloudService,
                        PictureRepository pictureRepository) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.itemService = itemService;
        this.imageCloudService = imageCloudService;
        this.pictureRepository = pictureRepository;
    }

    public void createOffer(CreateOfferDto createOfferDto, UserDetails userDetails) {
        UserEntity userEntity = this.userService.getUserByUsername(userDetails.getUsername());
        MultipartFile picture = createOfferDto.getPicture();
        String imageUrl = "";
        if (picture == null) {
            imageUrl = "n/a";
        } else {
            imageUrl = this.imageCloudService.saveImage(createOfferDto.getPicture());
        }

        ItemEntity item = this.itemService.createItem(
                createOfferDto.getClotheType(),
                createOfferDto.getManufactureYear(),
                imageUrl,
                createOfferDto.getBrandId(),
                createOfferDto.getSize());

        OfferEntity offerEntity = mapToOfferEntity(createOfferDto, userEntity, item);
        this.offerRepository.save(offerEntity);
    }

    public Page<OfferDetailsDto> getAllOffers(Pageable pageable) {
        return offerRepository
                .findAll(pageable)
                .map(this::mapToOfferDetails);
    }

    public void deleteOfferById(Long id) {
        this.offerRepository.deleteById(id);
    }

    public OfferEntity getOfferById(Long offerId) {
        OfferEntity offerEntity = this.offerRepository
                .findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        return offerEntity;
    }

    public Page<OfferDetailsDto> getOffersByBrandId(Pageable pageable, Long brandId) {
        return this.offerRepository.findAllByItemBrandId(brandId, pageable)
                .map(this::mapToOfferDetails);

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

    public OfferDetailsDto mapToOfferDetails(OfferEntity offerEntity) {
        return new OfferDetailsDto(
                offerEntity.getId(),
                offerEntity.getName(),
                offerEntity.getItem().getBrand().getId(),
                offerEntity.getItem().getBrand().getName(),
                offerEntity.getItem().getType(),
                offerEntity.getItem().getPicture().getUrl(),
                offerEntity.getItem().getSize(),
                offerEntity.getPrice(),
                offerEntity.getRating(),
                offerEntity.getSeller().getId(),
                offerEntity.getSeller().getUsername(),
                offerEntity.getCreatedAt(),
                offerEntity.getUpdatedAt());
    }

}
