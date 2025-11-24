package com.monsterdam.multimedia.service;

import com.monsterdam.multimedia.service.dto.VideoStoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.multimedia.domain.VideoStory}.
 */
public interface VideoStoryService {
    /**
     * Save a videoStory.
     *
     * @param videoStoryDTO the entity to save.
     * @return the persisted entity.
     */
    VideoStoryDTO save(VideoStoryDTO videoStoryDTO);

    /**
     * Updates a videoStory.
     *
     * @param videoStoryDTO the entity to update.
     * @return the persisted entity.
     */
    VideoStoryDTO update(VideoStoryDTO videoStoryDTO);

    /**
     * Partially updates a videoStory.
     *
     * @param videoStoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VideoStoryDTO> partialUpdate(VideoStoryDTO videoStoryDTO);

    /**
     * Get all the videoStories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VideoStoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" videoStory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VideoStoryDTO> findOne(Long id);

    /**
     * Delete the "id" videoStory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
