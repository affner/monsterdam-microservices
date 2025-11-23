package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.Notification;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.service.dto.NotificationDTO;
import com.fanflip.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "commentedUser", source = "commentedUser", qualifiedByName = "userProfileId")
    @Mapping(target = "messagedUser", source = "messagedUser", qualifiedByName = "userProfileId")
    @Mapping(target = "mentionerUserInPost", source = "mentionerUserInPost", qualifiedByName = "userProfileId")
    @Mapping(target = "mentionerUserInComment", source = "mentionerUserInComment", qualifiedByName = "userProfileId")
    NotificationDTO toDto(Notification s);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
