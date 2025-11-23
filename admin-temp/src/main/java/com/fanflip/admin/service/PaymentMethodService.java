package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PaymentMethodDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PaymentMethod}.
 */
public interface PaymentMethodService {
    /**
     * Save a paymentMethod.
     *
     * @param paymentMethodDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentMethodDTO> save(PaymentMethodDTO paymentMethodDTO);

    /**
     * Updates a paymentMethod.
     *
     * @param paymentMethodDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentMethodDTO> update(PaymentMethodDTO paymentMethodDTO);

    /**
     * Partially updates a paymentMethod.
     *
     * @param paymentMethodDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentMethodDTO> partialUpdate(PaymentMethodDTO paymentMethodDTO);

    /**
     * Get all the paymentMethods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentMethodDTO> findAll(Pageable pageable);

    /**
     * Returns the number of paymentMethods available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of paymentMethods available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" paymentMethod.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentMethodDTO> findOne(Long id);

    /**
     * Delete the "id" paymentMethod.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the paymentMethod corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentMethodDTO> search(String query, Pageable pageable);
}
