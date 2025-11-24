package com.monsterdam.multimedia.service.mapper;

import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.domain.UserTagRelation;
import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
import com.monsterdam.multimedia.service.dto.UserTagRelationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserTagRelation} and its DTO {@link UserTagRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserTagRelationMapper extends EntityMapper<UserTagRelationDTO, UserTagRelation> {
    @Mapping(target = "contentPackage", source = "contentPackage", qualifiedByName = "contentPackageId")
    UserTagRelationDTO toDto(UserTagRelation s);

    @Named("contentPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContentPackageDTO toDtoContentPackageId(ContentPackage contentPackage);
}
