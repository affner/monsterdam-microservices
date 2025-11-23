package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.UserEventDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.UserEvent}.
 */
public interface UserEventService {
    /**
     * Save a userEvent.
     *
     * @param userEventDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserEventDTO> save(UserEventDTO userEventDTO);

    /**
     * Updates a userEvent.
     *
     * @param userEventDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserEventDTO> update(UserEventDTO userEventDTO);

    /**
     * Partially updates a userEvent.
     *
     * @param userEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserEventDTO> partialUpdate(UserEventDTO userEventDTO);

    /**
     * Get all the userEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserEventDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userEvents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userEvents available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserEventDTO> findOne(Long id);

    /**
     * Delete the "id" userEvent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userEvent corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserEventDTO> search(String query, Pageable pageable);
}
