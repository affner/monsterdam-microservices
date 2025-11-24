package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.StateDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.State}.
 */
public interface StateService {
    /**
     * Save a state.
     *
     * @param stateDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<StateDTO> save(StateDTO stateDTO);

    /**
     * Updates a state.
     *
     * @param stateDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<StateDTO> update(StateDTO stateDTO);

    /**
     * Partially updates a state.
     *
     * @param stateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<StateDTO> partialUpdate(StateDTO stateDTO);

    /**
     * Get all the states.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<StateDTO> findAll(Pageable pageable);

    /**
     * Get all the states with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<StateDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of states available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of states available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" state.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<StateDTO> findOne(Long id);

    /**
     * Delete the "id" state.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the state corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<StateDTO> search(String query, Pageable pageable);
}
