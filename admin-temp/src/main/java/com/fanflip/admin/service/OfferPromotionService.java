package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.OfferPromotionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.OfferPromotion}.
 */
public interface OfferPromotionService {
    /**
     * Save a offerPromotion.
     *
     * @param offerPromotionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<OfferPromotionDTO> save(OfferPromotionDTO offerPromotionDTO);

    /**
     * Updates a offerPromotion.
     *
     * @param offerPromotionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<OfferPromotionDTO> update(OfferPromotionDTO offerPromotionDTO);

    /**
     * Partially updates a offerPromotion.
     *
     * @param offerPromotionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<OfferPromotionDTO> partialUpdate(OfferPromotionDTO offerPromotionDTO);

    /**
     * Get all the offerPromotions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<OfferPromotionDTO> findAll(Pageable pageable);

    /**
     * Returns the number of offerPromotions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of offerPromotions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" offerPromotion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<OfferPromotionDTO> findOne(Long id);

    /**
     * Delete the "id" offerPromotion.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the offerPromotion corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<OfferPromotionDTO> search(String query, Pageable pageable);
}
