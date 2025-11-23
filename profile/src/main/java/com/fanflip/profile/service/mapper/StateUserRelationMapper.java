package com.fanflip.profile.service.mapper;

import com.fanflip.profile.domain.StateUserRelation;
import com.fanflip.profile.domain.UserProfile;
import com.fanflip.profile.service.dto.StateUserRelationDTO;
import com.fanflip.profile.service.dto.UserProfileDTO;
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
