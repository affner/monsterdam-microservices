package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.LiabilityDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.Liability}.
 */
public interface LiabilityService {
    /**
     * Save a liability.
     *
     * @param liabilityDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LiabilityDTO> save(LiabilityDTO liabilityDTO);

    /**
     * Updates a liability.
     *
     * @param liabilityDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LiabilityDTO> update(LiabilityDTO liabilityDTO);

    /**
     * Partially updates a liability.
     *
     * @param liabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LiabilityDTO> partialUpdate(LiabilityDTO liabilityDTO);

    /**
     * Get all the liabilities.
     *
     * @return the list of entities.
     */
    Flux<LiabilityDTO> findAll();

    /**
     * Returns the number of liabilities available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of liabilities available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" liability.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LiabilityDTO> findOne(Long id);

    /**
     * Delete the "id" liability.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the liability corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<LiabilityDTO> search(String query);
}
