package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.UserProfileDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.UserProfile}.
 */
public interface UserProfileService {
    /**
     * Save a userProfile.
     *
     * @param userProfileDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> save(UserProfileDTO userProfileDTO);

    /**
     * Updates a userProfile.
     *
     * @param userProfileDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> update(UserProfileDTO userProfileDTO);

    /**
     * Partially updates a userProfile.
     *
     * @param userProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO);

    /**
     * Get all the userProfiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileDTO> findAll(Pageable pageable);

    /**
     * Get all the userProfiles with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of userProfiles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userProfiles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userProfile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserProfileDTO> findOne(Long id);

    /**
     * Delete the "id" userProfile.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userProfile corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserProfileDTO> search(String query, Pageable pageable);
}
