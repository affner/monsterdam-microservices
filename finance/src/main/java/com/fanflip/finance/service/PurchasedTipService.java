package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.PurchasedTipDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.PurchasedTip}.
 */
public interface PurchasedTipService {
    /**
     * Save a purchasedTip.
     *
     * @param purchasedTipDTO the entity to save.
     * @return the persisted entity.
     */
    PurchasedTipDTO save(PurchasedTipDTO purchasedTipDTO);

    /**
     * Updates a purchasedTip.
     *
     * @param purchasedTipDTO the entity to update.
     * @return the persisted entity.
     */
    PurchasedTipDTO update(PurchasedTipDTO purchasedTipDTO);

    /**
     * Partially updates a purchasedTip.
     *
     * @param purchasedTipDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchasedTipDTO> partialUpdate(PurchasedTipDTO purchasedTipDTO);

    /**
     * Get all the purchasedTips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedTipDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedTips with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedTipDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" purchasedTip.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchasedTipDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedTip.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
