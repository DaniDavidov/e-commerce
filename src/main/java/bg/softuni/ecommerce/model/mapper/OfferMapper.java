package bg.softuni.ecommerce.model.mapper;

import bg.softuni.ecommerce.model.dto.offer.CreateOfferDto;
import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

//    UpdateOfferDto mapToUpdateOfferDto(OfferEntity offerEntity);

    @Mapping(source = "item.brand.id", target = "brandId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "item.manufactureYear", target = "manufactureYear")
    @Mapping(source = "item.type", target = "clotheType")
    @Mapping(source = "item.size", target = "size")
    @Mapping(source = "description", target = "description")
    CreateOfferDto mapToCreateOfferDto(OfferEntity offerEntity);
}
