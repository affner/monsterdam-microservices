package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.LikeMarkDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.LikeMark}.
 */
public interface LikeMarkService {
    /**
     * Save a likeMark.
     *
     * @param likeMarkDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<LikeMarkDTO> save(LikeMarkDTO likeMarkDTO);

    /**
     * Updates a likeMark.
     *
     * @param likeMarkDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<LikeMarkDTO> update(LikeMarkDTO likeMarkDTO);

    /**
     * Partially updates a likeMark.
     *
     * @param likeMarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<LikeMarkDTO> partialUpdate(LikeMarkDTO likeMarkDTO);

    /**
     * Get all the likeMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LikeMarkDTO> findAll(Pageable pageable);

    /**
     * Returns the number of likeMarks available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of likeMarks available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" likeMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<LikeMarkDTO> findOne(Long id);

    /**
     * Delete the "id" likeMark.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the likeMark corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<LikeMarkDTO> search(String query, Pageable pageable);
}
