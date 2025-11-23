package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.HelpQuestionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.HelpQuestion}.
 */
public interface HelpQuestionService {
    /**
     * Save a helpQuestion.
     *
     * @param helpQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HelpQuestionDTO> save(HelpQuestionDTO helpQuestionDTO);

    /**
     * Updates a helpQuestion.
     *
     * @param helpQuestionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HelpQuestionDTO> update(HelpQuestionDTO helpQuestionDTO);

    /**
     * Partially updates a helpQuestion.
     *
     * @param helpQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HelpQuestionDTO> partialUpdate(HelpQuestionDTO helpQuestionDTO);

    /**
     * Get all the helpQuestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpQuestionDTO> findAll(Pageable pageable);

    /**
     * Get all the helpQuestions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpQuestionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of helpQuestions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of helpQuestions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" helpQuestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HelpQuestionDTO> findOne(Long id);

    /**
     * Delete the "id" helpQuestion.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the helpQuestion corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpQuestionDTO> search(String query, Pageable pageable);
}
