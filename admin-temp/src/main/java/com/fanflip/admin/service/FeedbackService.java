package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.FeedbackDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.Feedback}.
 */
public interface FeedbackService {
    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FeedbackDTO> save(FeedbackDTO feedbackDTO);

    /**
     * Updates a feedback.
     *
     * @param feedbackDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FeedbackDTO> update(FeedbackDTO feedbackDTO);

    /**
     * Partially updates a feedback.
     *
     * @param feedbackDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO);

    /**
     * Get all the feedbacks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackDTO> findAll(Pageable pageable);

    /**
     * Returns the number of feedbacks available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of feedbacks available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" feedback.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FeedbackDTO> findOne(Long id);

    /**
     * Delete the "id" feedback.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the feedback corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FeedbackDTO> search(String query, Pageable pageable);
}
