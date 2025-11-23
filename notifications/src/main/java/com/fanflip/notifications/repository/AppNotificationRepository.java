package com.fanflip.notifications.repository;

import com.fanflip.notifications.domain.AppNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppNotificationRepository extends JpaRepository<AppNotification, Long> {}
