package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.DocumentReviewObservationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.DocumentReviewObservation}.
 */
public interface DocumentReviewObservationService {
    /**
     * Save a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DocumentReviewObservationDTO> save(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Updates a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DocumentReviewObservationDTO> update(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Partially updates a documentReviewObservation.
     *
     * @param documentReviewObservationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DocumentReviewObservationDTO> partialUpdate(DocumentReviewObservationDTO documentReviewObservationDTO);

    /**
     * Get all the documentReviewObservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DocumentReviewObservationDTO> findAll(Pageable pageable);

    /**
     * Returns the number of documentReviewObservations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of documentReviewObservations available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" documentReviewObservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DocumentReviewObservationDTO> findOne(Long id);

    /**
     * Delete the "id" documentReviewObservation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the documentReviewObservation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DocumentReviewObservationDTO> search(String query, Pageable pageable);
}
