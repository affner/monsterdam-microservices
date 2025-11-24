package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.UserAssociation;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.UserAssociationDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAssociation} and its DTO {@link UserAssociationDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAssociationMapper extends EntityMapper<UserAssociationDTO, UserAssociation> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userProfileId")
    UserAssociationDTO toDto(UserAssociation s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
