package com.monsterdam.profile.service.mapper;

import com.monsterdam.profile.domain.StateUserRelation;
import com.monsterdam.profile.domain.UserProfile;
import com.monsterdam.profile.service.dto.StateUserRelationDTO;
import com.monsterdam.profile.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StateUserRelation} and its DTO {@link StateUserRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateUserRelationMapper extends EntityMapper<StateUserRelationDTO, StateUserRelation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    StateUserRelationDTO toDto(StateUserRelation s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
