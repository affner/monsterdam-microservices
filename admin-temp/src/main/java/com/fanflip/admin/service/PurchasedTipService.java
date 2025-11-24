package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.PurchasedTipDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.PurchasedTip}.
 */
public interface PurchasedTipService {
    /**
     * Save a purchasedTip.
     *
     * @param purchasedTipDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PurchasedTipDTO> save(PurchasedTipDTO purchasedTipDTO);

    /**
     * Updates a purchasedTip.
     *
     * @param purchasedTipDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PurchasedTipDTO> update(PurchasedTipDTO purchasedTipDTO);

    /**
     * Partially updates a purchasedTip.
     *
     * @param purchasedTipDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PurchasedTipDTO> partialUpdate(PurchasedTipDTO purchasedTipDTO);

    /**
     * Get all the purchasedTips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedTipDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedTips with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedTipDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of purchasedTips available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of purchasedTips available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" purchasedTip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PurchasedTipDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedTip.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the purchasedTip corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedTipDTO> search(String query, Pageable pageable);
}
