package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PurchasedSubscriptionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PurchasedSubscription}.
 */
public interface PurchasedSubscriptionService {
    /**
     * Save a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PurchasedSubscriptionDTO> save(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PurchasedSubscriptionDTO> update(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Partially updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedSubscriptionDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedSubscriptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedSubscriptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of purchasedSubscriptions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of purchasedSubscriptions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PurchasedSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the purchasedSubscription corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedSubscriptionDTO> search(String query, Pageable pageable);
}
