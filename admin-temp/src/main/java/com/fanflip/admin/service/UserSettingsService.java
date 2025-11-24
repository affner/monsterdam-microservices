package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.UserSettingsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.UserSettings}.
 */
public interface UserSettingsService {
    /**
     * Save a userSettings.
     *
     * @param userSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserSettingsDTO> save(UserSettingsDTO userSettingsDTO);

    /**
     * Updates a userSettings.
     *
     * @param userSettingsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserSettingsDTO> update(UserSettingsDTO userSettingsDTO);

    /**
     * Partially updates a userSettings.
     *
     * @param userSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO);

    /**
     * Get all the userSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserSettingsDTO> findAll(Pageable pageable);

    /**
     * Get all the UserSettingsDTO where UserProfile is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<UserSettingsDTO> findAllWhereUserProfileIsNull();

    /**
     * Returns the number of userSettings available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userSettings available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userSettings.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserSettingsDTO> findOne(Long id);

    /**
     * Delete the "id" userSettings.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userSettings corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserSettingsDTO> search(String query, Pageable pageable);
}
