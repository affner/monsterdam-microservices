package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.IdentityDocumentReviewDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.IdentityDocumentReview}.
 */
public interface IdentityDocumentReviewService {
    /**
     * Save a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentReviewDTO> save(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Updates a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentReviewDTO> update(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Partially updates a identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentReviewDTO> partialUpdate(IdentityDocumentReviewDTO identityDocumentReviewDTO);

    /**
     * Get all the identityDocumentReviews.
     *
     * @return the list of entities.
     */
    Flux<IdentityDocumentReviewDTO> findAll();

    /**
     * Returns the number of identityDocumentReviews available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of identityDocumentReviews available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" identityDocumentReview.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IdentityDocumentReviewDTO> findOne(Long id);

    /**
     * Delete the "id" identityDocumentReview.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the identityDocumentReview corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<IdentityDocumentReviewDTO> search(String query);
}
