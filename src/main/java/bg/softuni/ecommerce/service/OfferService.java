package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.dto.offer.OfferDetailsDto;
import bg.softuni.ecommerce.model.entity.*;
import bg.softuni.ecommerce.model.entity.enums.OfferRating;
import bg.softuni.ecommerce.model.error.OfferNotFoundException;
import bg.softuni.ecommerce.model.events.BuyProductEvent;
import bg.softuni.ecommerce.repository.OfferRepository;
import bg.softuni.ecommerce.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final UserService userService;
    private final ImageCloudService imageCloudService;
    private final PictureRepository pictureRepository;
    private final BrandService brandService;
    private static final OfferRating DEFAULT_OFFER_RATING = OfferRating.ZERO;

    @Autowired
    public OfferService(OfferRepository offerRepository,
                        UserService userService,
                        ImageCloudService imageCloudService,
                        PictureRepository pictureRepository, BrandService brandService) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.imageCloudService = imageCloudService;
        this.pictureRepository = pictureRepository;
        this.brandService = brandService;
    }

    public void createOffer(CreateOfferDto createOfferDto, UserDetails userDetails) {
        UserEntity userEntity = this.userService.getUserByUsername(userDetails.getUsername());
        PictureEntity picture = createPicture(createOfferDto.getPicture());
        BrandEntity brand = this.brandService.getBrand(createOfferDto.getBrandId());

        OfferEntity offerEntity = mapToOfferEntity(createOfferDto, userEntity, picture, brand);
        this.offerRepository.save(offerEntity);
    }

    public Page<OfferDetailsDto> getAllOffers(Pageable pageable) {
        return offerRepository
                .findAllApprovedOffers(pageable)
                .map(this::mapToOfferDetails);
    }

    public Page<OfferDetailsDto> getAllUnapprovedOffers(Pageable pageable) {
        return this.offerRepository
                .findAllUnapprovedOffers(pageable)
                .map(this::mapToOfferDetails);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
        return this.offerRepository.findAllByBrandId(brandId, pageable)
                .map(this::mapToOfferDetails);

    }


    public void updateOffer(Long offerId, CreateOfferDto createOfferDto) {
        OfferEntity currentOffer = this.getOfferById(offerId);

        PictureEntity picture = createPicture(createOfferDto.getPicture());
        BrandEntity brand = this.brandService.getBrand(createOfferDto.getBrandId());

        OfferEntity updatedOffer = mapToOfferEntity(createOfferDto, currentOffer.getSeller(), picture, brand);
        updatedOffer.setId(currentOffer.getId());
        updatedOffer.setUpdatedAt(LocalDate.now());
        this.offerRepository.save(updatedOffer);
    }

    private PictureEntity createPicture(MultipartFile picture) {
        String imageUrl = "";
        if (picture == null) {
            imageUrl = "n/a";
        } else {
            imageUrl = this.imageCloudService.saveImage(picture);
        }
        PictureEntity pictureEntity = new PictureEntity(imageUrl);
        return this.pictureRepository.save(pictureEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void approveOffer(Long offerId) {
        OfferEntity offer = getOfferById(offerId);
        offer.setApproved(true);
        this.offerRepository.save(offer);
    }

    @EventListener(BuyProductEvent.class)
    public void decreaseQuantity(BuyProductEvent buyProductEvent) {
        List<CartEntity> shoppingCart = buyProductEvent.getShoppingCart();

        for (CartEntity cart : shoppingCart) {
            OfferEntity offer = cart.getOffer();
            int currentProductQuantity = offer.getQuantity();
            int quantityToBeBought = cart.getQuantity();

            offer.setQuantity(currentProductQuantity - quantityToBeBought);
            this.offerRepository.save(offer);
        }
    }

    private OfferEntity mapToOfferEntity(CreateOfferDto createOfferDto, UserEntity user, PictureEntity picture, BrandEntity brand) {
        return new OfferEntity(
                createOfferDto.getClotheType(),
                createOfferDto.getManufactureYear(),
                picture,
                brand,
                createOfferDto.getSize(),
                createOfferDto.getName(),
                DEFAULT_OFFER_RATING,
                user,
                createOfferDto.getPrice(),
                createOfferDto.getQuantity(),
                LocalDate.now(),
                LocalDate.now(),
                createOfferDto.getDescription(),
                false);
    }

    public OfferDetailsDto mapToOfferDetails(OfferEntity offerEntity) {
        return new OfferDetailsDto(
                offerEntity.getId(),
                offerEntity.getName(),
                offerEntity.getBrand().getId(),
                offerEntity.getBrand().getName(),
                offerEntity.getType(),
                offerEntity.getPicture().getUrl(),
                offerEntity.getSize(),
                offerEntity.getPrice(),
                offerEntity.getRating(),
                offerEntity.getSeller().getId(),
                offerEntity.getSeller().getUsername(),
                offerEntity.getCreatedAt(),
                offerEntity.getUpdatedAt(),
                offerEntity.isApproved(),
                offerEntity.getQuantity());
    }

    public CreateOfferDto mapToCreateOfferDto(OfferEntity offerEntity) {
        return new CreateOfferDto(
                offerEntity.getBrand().getId(),
                offerEntity.getManufactureYear(),
                offerEntity.getName(),
                offerEntity.getPrice(),
                offerEntity.getType(),
                offerEntity.getSize(),
                offerEntity.getQuantity(),
                offerEntity.getDescription(),
                null);

    }
}
