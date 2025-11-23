package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SubscriptionBundleDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SubscriptionBundle}.
 */
public interface SubscriptionBundleService {
    /**
     * Save a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SubscriptionBundleDTO> save(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Updates a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SubscriptionBundleDTO> update(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Partially updates a subscriptionBundle.
     *
     * @param subscriptionBundleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SubscriptionBundleDTO> partialUpdate(SubscriptionBundleDTO subscriptionBundleDTO);

    /**
     * Get all the subscriptionBundles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SubscriptionBundleDTO> findAll(Pageable pageable);

    /**
     * Returns the number of subscriptionBundles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of subscriptionBundles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" subscriptionBundle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SubscriptionBundleDTO> findOne(Long id);

    /**
     * Delete the "id" subscriptionBundle.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the subscriptionBundle corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SubscriptionBundleDTO> search(String query, Pageable pageable);
}
