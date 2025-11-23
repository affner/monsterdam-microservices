package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.CurrencyDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.Currency}.
 */
public interface CurrencyService {
    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CurrencyDTO> save(CurrencyDTO currencyDTO);

    /**
     * Updates a currency.
     *
     * @param currencyDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CurrencyDTO> update(CurrencyDTO currencyDTO);

    /**
     * Partially updates a currency.
     *
     * @param currencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CurrencyDTO> partialUpdate(CurrencyDTO currencyDTO);

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CurrencyDTO> findAll(Pageable pageable);

    /**
     * Returns the number of currencies available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of currencies available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" currency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CurrencyDTO> findOne(Long id);

    /**
     * Delete the "id" currency.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the currency corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CurrencyDTO> search(String query, Pageable pageable);
}
