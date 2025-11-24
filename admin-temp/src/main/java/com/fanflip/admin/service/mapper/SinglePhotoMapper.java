package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.ContentPackage;
import com.monsterdam.admin.domain.SinglePhoto;
import com.monsterdam.admin.service.dto.ContentPackageDTO;
import com.monsterdam.admin.service.dto.SinglePhotoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SinglePhoto} and its DTO {@link SinglePhotoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SinglePhotoMapper extends EntityMapper<SinglePhotoDTO, SinglePhoto> {
    @Mapping(target = "belongPackage", source = "belongPackage", qualifiedByName = "contentPackageAmount")
    SinglePhotoDTO toDto(SinglePhoto s);

    @Named("contentPackageAmount")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "amount", source = "amount")
    ContentPackageDTO toDtoContentPackageAmount(ContentPackage contentPackage);
}
