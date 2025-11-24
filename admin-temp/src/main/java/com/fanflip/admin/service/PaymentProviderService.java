package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.PaymentProviderDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.PaymentProvider}.
 */
public interface PaymentProviderService {
    /**
     * Save a paymentProvider.
     *
     * @param paymentProviderDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentProviderDTO> save(PaymentProviderDTO paymentProviderDTO);

    /**
     * Updates a paymentProvider.
     *
     * @param paymentProviderDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentProviderDTO> update(PaymentProviderDTO paymentProviderDTO);

    /**
     * Partially updates a paymentProvider.
     *
     * @param paymentProviderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentProviderDTO> partialUpdate(PaymentProviderDTO paymentProviderDTO);

    /**
     * Get all the paymentProviders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentProviderDTO> findAll(Pageable pageable);

    /**
     * Returns the number of paymentProviders available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of paymentProviders available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" paymentProvider.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentProviderDTO> findOne(Long id);

    /**
     * Delete the "id" paymentProvider.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the paymentProvider corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentProviderDTO> search(String query, Pageable pageable);
}
