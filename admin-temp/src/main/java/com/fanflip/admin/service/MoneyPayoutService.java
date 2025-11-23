package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.MoneyPayoutDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.MoneyPayout}.
 */
public interface MoneyPayoutService {
    /**
     * Save a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<MoneyPayoutDTO> save(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Updates a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<MoneyPayoutDTO> update(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Partially updates a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<MoneyPayoutDTO> partialUpdate(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Get all the moneyPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MoneyPayoutDTO> findAll(Pageable pageable);

    /**
     * Get all the moneyPayouts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MoneyPayoutDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of moneyPayouts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of moneyPayouts available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" moneyPayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<MoneyPayoutDTO> findOne(Long id);

    /**
     * Delete the "id" moneyPayout.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the moneyPayout corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<MoneyPayoutDTO> search(String query, Pageable pageable);
}
