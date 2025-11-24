package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.PollOptionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.PollOption}.
 */
public interface PollOptionService {
    /**
     * Save a pollOption.
     *
     * @param pollOptionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PollOptionDTO> save(PollOptionDTO pollOptionDTO);

    /**
     * Updates a pollOption.
     *
     * @param pollOptionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PollOptionDTO> update(PollOptionDTO pollOptionDTO);

    /**
     * Partially updates a pollOption.
     *
     * @param pollOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PollOptionDTO> partialUpdate(PollOptionDTO pollOptionDTO);

    /**
     * Get all the pollOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollOptionDTO> findAll(Pageable pageable);

    /**
     * Get all the pollOptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollOptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pollOptions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of pollOptions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" pollOption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PollOptionDTO> findOne(Long id);

    /**
     * Delete the "id" pollOption.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the pollOption corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollOptionDTO> search(String query, Pageable pageable);
}
