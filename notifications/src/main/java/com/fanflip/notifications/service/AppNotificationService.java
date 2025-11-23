package com.fanflip.notifications.service;

import com.fanflip.notifications.service.dto.AppNotificationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.notifications.domain.AppNotification}.
 */
public interface AppNotificationService {
    /**
     * Save a appNotification.
     *
     * @param appNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    AppNotificationDTO save(AppNotificationDTO appNotificationDTO);

    /**
     * Updates a appNotification.
     *
     * @param appNotificationDTO the entity to update.
     * @return the persisted entity.
     */
    AppNotificationDTO update(AppNotificationDTO appNotificationDTO);

    /**
     * Partially updates a appNotification.
     *
     * @param appNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppNotificationDTO> partialUpdate(AppNotificationDTO appNotificationDTO);

    /**
     * Get all the appNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AppNotificationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" appNotification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppNotificationDTO> findOne(Long id);

    /**
     * Delete the "id" appNotification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
