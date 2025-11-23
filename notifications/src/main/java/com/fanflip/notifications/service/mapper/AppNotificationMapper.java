package com.fanflip.notifications.service.mapper;

import com.fanflip.notifications.domain.AppNotification;
import com.fanflip.notifications.service.dto.AppNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppNotification} and its DTO {@link AppNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppNotificationMapper extends EntityMapper<AppNotificationDTO, AppNotification> {}
