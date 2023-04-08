package bg.softuni.ecommerce.model.mapper;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

//    UpdateOfferDto mapToUpdateOfferDto(OfferEntity offerEntity);

//    @Mapping(source = "item.brand.id", target = "brandId")
    CreateOfferDto mapToCreateOfferDto(OfferEntity offerEntity);
}
