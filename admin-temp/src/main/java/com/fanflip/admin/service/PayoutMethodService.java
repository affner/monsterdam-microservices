package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PayoutMethodDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PayoutMethod}.
 */
public interface PayoutMethodService {
    /**
     * Save a payoutMethod.
     *
     * @param payoutMethodDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PayoutMethodDTO> save(PayoutMethodDTO payoutMethodDTO);

    /**
     * Updates a payoutMethod.
     *
     * @param payoutMethodDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PayoutMethodDTO> update(PayoutMethodDTO payoutMethodDTO);

    /**
     * Partially updates a payoutMethod.
     *
     * @param payoutMethodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PayoutMethodDTO> partialUpdate(PayoutMethodDTO payoutMethodDTO);

    /**
     * Get all the payoutMethods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PayoutMethodDTO> findAll(Pageable pageable);

    /**
     * Returns the number of payoutMethods available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of payoutMethods available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" payoutMethod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PayoutMethodDTO> findOne(Long id);

    /**
     * Delete the "id" payoutMethod.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the payoutMethod corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PayoutMethodDTO> search(String query, Pageable pageable);
}
