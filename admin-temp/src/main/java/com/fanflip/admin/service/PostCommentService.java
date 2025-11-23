package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PostCommentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PostComment}.
 */
public interface PostCommentService {
    /**
     * Save a postComment.
     *
     * @param postCommentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PostCommentDTO> save(PostCommentDTO postCommentDTO);

    /**
     * Updates a postComment.
     *
     * @param postCommentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PostCommentDTO> update(PostCommentDTO postCommentDTO);

    /**
     * Partially updates a postComment.
     *
     * @param postCommentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PostCommentDTO> partialUpdate(PostCommentDTO postCommentDTO);

    /**
     * Get all the postComments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostCommentDTO> findAll(Pageable pageable);

    /**
     * Get all the postComments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostCommentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of postComments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of postComments available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" postComment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PostCommentDTO> findOne(Long id);

    /**
     * Delete the "id" postComment.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the postComment corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostCommentDTO> search(String query, Pageable pageable);
}
