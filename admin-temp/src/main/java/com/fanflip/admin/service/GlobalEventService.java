package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.GlobalEventDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.GlobalEvent}.
 */
public interface GlobalEventService {
    /**
     * Save a globalEvent.
     *
     * @param globalEventDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<GlobalEventDTO> save(GlobalEventDTO globalEventDTO);

    /**
     * Updates a globalEvent.
     *
     * @param globalEventDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<GlobalEventDTO> update(GlobalEventDTO globalEventDTO);

    /**
     * Partially updates a globalEvent.
     *
     * @param globalEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<GlobalEventDTO> partialUpdate(GlobalEventDTO globalEventDTO);

    /**
     * Get all the globalEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<GlobalEventDTO> findAll(Pageable pageable);

    /**
     * Returns the number of globalEvents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of globalEvents available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" globalEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<GlobalEventDTO> findOne(Long id);

    /**
     * Delete the "id" globalEvent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the globalEvent corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<GlobalEventDTO> search(String query, Pageable pageable);
}
