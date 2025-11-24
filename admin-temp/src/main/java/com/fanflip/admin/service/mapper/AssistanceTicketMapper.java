package com.monsterdam.admin.service.mapper;

import com.monsterdam.admin.domain.AdminUserProfile;
import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.domain.ModerationAction;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.service.dto.AdminUserProfileDTO;
import com.monsterdam.admin.service.dto.AssistanceTicketDTO;
import com.monsterdam.admin.service.dto.ModerationActionDTO;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssistanceTicket} and its DTO {@link AssistanceTicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssistanceTicketMapper extends EntityMapper<AssistanceTicketDTO, AssistanceTicket> {
    @Mapping(target = "moderationAction", source = "moderationAction", qualifiedByName = "moderationActionId")
    @Mapping(target = "assignedAdmin", source = "assignedAdmin", qualifiedByName = "adminUserProfileId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userProfileId")
    AssistanceTicketDTO toDto(AssistanceTicket s);

    @Named("moderationActionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModerationActionDTO toDtoModerationActionId(ModerationAction moderationAction);

    @Named("adminUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdminUserProfileDTO toDtoAdminUserProfileId(AdminUserProfile adminUserProfile);

    @Named("userProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserProfileDTO toDtoUserProfileId(UserProfile userProfile);
}
