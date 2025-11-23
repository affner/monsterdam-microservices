package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.AdminUserProfile}.
 */
public interface AdminUserProfileService {
    /**
     * Save a adminUserProfile.
     *
     * @param adminUserProfileDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AdminUserProfileDTO> save(AdminUserProfileDTO adminUserProfileDTO);

    /**
     * Updates a adminUserProfile.
     *
     * @param adminUserProfileDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AdminUserProfileDTO> update(AdminUserProfileDTO adminUserProfileDTO);

    /**
     * Partially updates a adminUserProfile.
     *
     * @param adminUserProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AdminUserProfileDTO> partialUpdate(AdminUserProfileDTO adminUserProfileDTO);

    /**
     * Get all the adminUserProfiles.
     *
     * @return the list of entities.
     */
    Flux<AdminUserProfileDTO> findAll();

    /**
     * Returns the number of adminUserProfiles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of adminUserProfiles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" adminUserProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AdminUserProfileDTO> findOne(Long id);

    /**
     * Delete the "id" adminUserProfile.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the adminUserProfile corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<AdminUserProfileDTO> search(String query);
}
