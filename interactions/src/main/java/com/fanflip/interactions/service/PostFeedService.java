package com.fanflip.interactions.service;

import com.fanflip.interactions.service.dto.PostFeedDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.interactions.domain.PostFeed}.
 */
public interface PostFeedService {
    /**
     * Save a postFeed.
     *
     * @param postFeedDTO the entity to save.
     * @return the persisted entity.
     */
    PostFeedDTO save(PostFeedDTO postFeedDTO);

    /**
     * Updates a postFeed.
     *
     * @param postFeedDTO the entity to update.
     * @return the persisted entity.
     */
    PostFeedDTO update(PostFeedDTO postFeedDTO);

    /**
     * Partially updates a postFeed.
     *
     * @param postFeedDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO);

    /**
     * Get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedDTO> findAll(Pageable pageable);

    /**
     * Get all the postFeeds with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostFeedDTO> findOne(Long id);

    /**
     * Delete the "id" postFeed.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the postFeed corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedDTO> search(String query, Pageable pageable);
}
