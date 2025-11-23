package com.fanflip.admin.service.mapper;

import com.fanflip.admin.domain.AdminAnnouncement;
import com.fanflip.admin.domain.AdminUserProfile;
import com.fanflip.admin.domain.DirectMessage;
import com.fanflip.admin.service.dto.AdminAnnouncementDTO;
import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import com.fanflip.admin.service.dto.DirectMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdminAnnouncement} and its DTO {@link AdminAnnouncementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdminAnnouncementMapper extends EntityMapper<AdminAnnouncementDTO, AdminAnnouncement> {
    @Mapping(target = "announcerMessage", source = "announcerMessage", qualifiedByName = "directMessageId")
    @Mapping(target = "admin", source = "admin", qualifiedByName = "adminUserProfileId")
    AdminAnnouncementDTO toDto(AdminAnnouncement s);

    @Named("directMessageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectMessageDTO toDtoDirectMessageId(DirectMessage directMessage);

    @Named("adminUserProfileId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdminUserProfileDTO toDtoAdminUserProfileId(AdminUserProfile adminUserProfile);
}
