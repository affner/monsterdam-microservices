package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.OfferPromotion}.
 */
public interface OfferPromotionService {
    /**
     * Save a offerPromotion.
     *
     * @param offerPromotionDTO the entity to save.
     * @return the persisted entity.
     */
    OfferPromotionDTO save(OfferPromotionDTO offerPromotionDTO);

    /**
     * Updates a offerPromotion.
     *
     * @param offerPromotionDTO the entity to update.
     * @return the persisted entity.
     */
    OfferPromotionDTO update(OfferPromotionDTO offerPromotionDTO);

    /**
     * Partially updates a offerPromotion.
     *
     * @param offerPromotionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OfferPromotionDTO> partialUpdate(OfferPromotionDTO offerPromotionDTO);

    /**
     * Get all the offerPromotions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OfferPromotionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" offerPromotion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OfferPromotionDTO> findOne(Long id);

    /**
     * Delete the "id" offerPromotion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
