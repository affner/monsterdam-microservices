package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AdminUserProfile;
import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.ModerationAction;
import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.service.dto.ModerationActionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssistanceTicket} and its DTO {@link AssistanceTicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssistanceTicketMapper extends EntityMapper<AssistanceTicketDTO, AssistanceTicket> {
    @Mapping(target = "moderationAction", source = "moderationAction", qualifiedByName = "moderationActionId")
    @Mapping(target = "assignedAdmin", source = "assignedAdmin", qualifiedByName = "adminUserProfileId")
    AssistanceTicketDTO toDto(AssistanceTicket s);

    @Named("moderationActionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ModerationActionDTO toDtoModerationActionId(ModerationAction moderationAction);

    @Named("adminUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdminUserProfileDTO toDtoAdminUserProfileId(AdminUserProfile adminUserProfile);
}
