package bg.softuni.ecommerce.model.mapper;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferMapper {

//    UpdateOfferDto mapToUpdateOfferDto(OfferEntity offerEntity);

    CreateOfferDto mapToCreateOfferDto(OfferEntity offerEntity);
}
