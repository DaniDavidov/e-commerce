package bg.softuni.ecommerce.model.mapper;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

//    UpdateOfferDto mapToUpdateOfferDto(OfferEntity offerEntity);

//    @Mapping(source = "brand.id", target = "brandId")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "price", target = "price")
//    @Mapping(source = "manufactureYear", target = "manufactureYear")
//    @Mapping(source = "type", target = "clotheType")
//    @Mapping(source = "size", target = "size")
//    @Mapping(source = "description", target = "description")
//    CreateOfferDto mapToCreateOfferDto(OfferEntity offerEntity);
}
