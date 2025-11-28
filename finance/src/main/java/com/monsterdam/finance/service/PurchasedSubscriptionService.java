package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.PurchasedSubscriptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.PurchasedSubscription}.
 */
public interface PurchasedSubscriptionService {
    /**
     * Save a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    PurchasedSubscriptionDTO save(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    PurchasedSubscriptionDTO update(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Partially updates a purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO);

    /**
     * Get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedSubscriptionDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedSubscriptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedSubscriptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchasedSubscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedSubscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
