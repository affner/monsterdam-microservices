package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.AdminAnnouncementDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.AdminAnnouncement}.
 */
public interface AdminAnnouncementService {
    /**
     * Save a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AdminAnnouncementDTO> save(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Updates a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AdminAnnouncementDTO> update(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Partially updates a adminAnnouncement.
     *
     * @param adminAnnouncementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AdminAnnouncementDTO> partialUpdate(AdminAnnouncementDTO adminAnnouncementDTO);

    /**
     * Get all the adminAnnouncements.
     *
     * @return the list of entities.
     */
    Flux<AdminAnnouncementDTO> findAll();

    /**
     * Returns the number of adminAnnouncements available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of adminAnnouncements available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" adminAnnouncement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AdminAnnouncementDTO> findOne(Long id);

    /**
     * Delete the "id" adminAnnouncement.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the adminAnnouncement corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<AdminAnnouncementDTO> search(String query);
}
