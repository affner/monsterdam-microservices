package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PostFeedDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PostFeed}.
 */
public interface PostFeedService {
    /**
     * Save a postFeed.
     *
     * @param postFeedDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PostFeedDTO> save(PostFeedDTO postFeedDTO);

    /**
     * Updates a postFeed.
     *
     * @param postFeedDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PostFeedDTO> update(PostFeedDTO postFeedDTO);

    /**
     * Partially updates a postFeed.
     *
     * @param postFeedDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO);

    /**
     * Get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostFeedDTO> findAll(Pageable pageable);

    /**
     * Get all the postFeeds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostFeedDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of postFeeds available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of postFeeds available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PostFeedDTO> findOne(Long id);

    /**
     * Delete the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the postFeed corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostFeedDTO> search(String query, Pageable pageable);
}
