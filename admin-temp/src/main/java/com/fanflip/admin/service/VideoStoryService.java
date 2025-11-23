package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.VideoStoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.VideoStory}.
 */
public interface VideoStoryService {
    /**
     * Save a videoStory.
     *
     * @param videoStoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<VideoStoryDTO> save(VideoStoryDTO videoStoryDTO);

    /**
     * Updates a videoStory.
     *
     * @param videoStoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<VideoStoryDTO> update(VideoStoryDTO videoStoryDTO);

    /**
     * Partially updates a videoStory.
     *
     * @param videoStoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<VideoStoryDTO> partialUpdate(VideoStoryDTO videoStoryDTO);

    /**
     * Get all the videoStories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VideoStoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of videoStories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of videoStories available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" videoStory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<VideoStoryDTO> findOne(Long id);

    /**
     * Delete the "id" videoStory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the videoStory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<VideoStoryDTO> search(String query, Pageable pageable);
}
