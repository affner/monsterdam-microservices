package com.monsterdam.app.service;

import com.monsterdam.app.service.dto.UserUIPreferencesDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.app.domain.UserUIPreferences}.
 */
public interface UserUIPreferencesService {
    /**
     * Save a userUIPreferences.
     *
     * @param userUIPreferencesDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserUIPreferencesDTO> save(UserUIPreferencesDTO userUIPreferencesDTO);

    /**
     * Updates a userUIPreferences.
     *
     * @param userUIPreferencesDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserUIPreferencesDTO> update(UserUIPreferencesDTO userUIPreferencesDTO);

    /**
     * Partially updates a userUIPreferences.
     *
     * @param userUIPreferencesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserUIPreferencesDTO> partialUpdate(UserUIPreferencesDTO userUIPreferencesDTO);

    /**
     * Get all the userUIPreferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserUIPreferencesDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userUIPreferences available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userUIPreferences available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userUIPreferences.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserUIPreferencesDTO> findOne(Long id);

    /**
     * Delete the "id" userUIPreferences.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userUIPreferences corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserUIPreferencesDTO> search(String query, Pageable pageable);
}
