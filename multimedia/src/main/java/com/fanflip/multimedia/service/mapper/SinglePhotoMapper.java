package com.fanflip.multimedia.service.mapper;

import com.fanflip.multimedia.domain.ContentPackage;
import com.fanflip.multimedia.domain.SinglePhoto;
import com.fanflip.multimedia.service.dto.ContentPackageDTO;
import com.fanflip.multimedia.service.dto.SinglePhotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SinglePhoto} and its DTO {@link SinglePhotoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@DecoratedWith(SinglePhotoMapperDecorator.class)
public interface SinglePhotoMapper extends EntityMapper<SinglePhotoDTO, SinglePhoto> {
    @Mapping(target = "belongPackage", source = "belongPackage", qualifiedByName = "contentPackageAmount")
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "content", ignore = true)
    SinglePhotoDTO toDto(SinglePhoto s);

    @Named("contentPackageAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    ContentPackageDTO toDtoContentPackageAmount(ContentPackage contentPackage);
}
