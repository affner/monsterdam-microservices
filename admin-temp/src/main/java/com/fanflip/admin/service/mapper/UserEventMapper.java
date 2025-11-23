package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.UserEvent;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.UserEventDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserEvent} and its DTO {@link UserEventDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserEventMapper extends EntityMapper<UserEventDTO, UserEvent> {
    @Mapping(target = "creator", source = "creator", qualifiedByName = "userProfileId")
    UserEventDTO toDto(UserEvent s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
