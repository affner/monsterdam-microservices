package com.monsterdam.notifications.service.mapper;

import com.monsterdam.notifications.domain.AppNotification;
import com.monsterdam.notifications.service.dto.AppNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppNotification} and its DTO {@link AppNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppNotificationMapper extends EntityMapper<AppNotificationDTO, AppNotification> {}
