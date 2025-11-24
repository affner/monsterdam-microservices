package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.CreatorEarning}.
 */
public interface CreatorEarningService {
    /**
     * Save a creatorEarning.
     *
     * @param creatorEarningDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CreatorEarningDTO> save(CreatorEarningDTO creatorEarningDTO);

    /**
     * Updates a creatorEarning.
     *
     * @param creatorEarningDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CreatorEarningDTO> update(CreatorEarningDTO creatorEarningDTO);

    /**
     * Partially updates a creatorEarning.
     *
     * @param creatorEarningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CreatorEarningDTO> partialUpdate(CreatorEarningDTO creatorEarningDTO);

    /**
     * Get all the creatorEarnings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CreatorEarningDTO> findAll(Pageable pageable);

    /**
     * Get all the CreatorEarningDTO where MoneyPayout is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<CreatorEarningDTO> findAllWhereMoneyPayoutIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<CreatorEarningDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<CreatorEarningDTO> findAllWherePurchasedSubscriptionIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<CreatorEarningDTO> findAllWherePurchasedTipIsNull();

    /**
     * Returns the number of creatorEarnings available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of creatorEarnings available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" creatorEarning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CreatorEarningDTO> findOne(Long id);

    /**
     * Delete the "id" creatorEarning.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the creatorEarning corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CreatorEarningDTO> search(String query, Pageable pageable);
}
