package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.CreatorEarningDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.CreatorEarning}.
 */
public interface CreatorEarningService {
    /**
     * Save a creatorEarning.
     *
     * @param creatorEarningDTO the entity to save.
     * @return the persisted entity.
     */
    CreatorEarningDTO save(CreatorEarningDTO creatorEarningDTO);

    /**
     * Updates a creatorEarning.
     *
     * @param creatorEarningDTO the entity to update.
     * @return the persisted entity.
     */
    CreatorEarningDTO update(CreatorEarningDTO creatorEarningDTO);

    /**
     * Partially updates a creatorEarning.
     *
     * @param creatorEarningDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CreatorEarningDTO> partialUpdate(CreatorEarningDTO creatorEarningDTO);

    /**
     * Get all the creatorEarnings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreatorEarningDTO> findAll(Pageable pageable);

    /**
     * Get all the CreatorEarningDTO where MoneyPayout is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CreatorEarningDTO> findAllWhereMoneyPayoutIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CreatorEarningDTO> findAllWherePurchasedTipIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CreatorEarningDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the CreatorEarningDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CreatorEarningDTO> findAllWherePurchasedSubscriptionIsNull();

    /**
     * Get the "id" creatorEarning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CreatorEarningDTO> findOne(Long id);

    /**
     * Delete the "id" creatorEarning.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
