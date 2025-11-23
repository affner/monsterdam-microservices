package com.fanflip.interactions.service;

import com.fanflip.interactions.service.dto.LikeMarkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.interactions.domain.LikeMark}.
 */
public interface LikeMarkService {
    /**
     * Save a likeMark.
     *
     * @param likeMarkDTO the entity to save.
     * @return the persisted entity.
     */
    LikeMarkDTO save(LikeMarkDTO likeMarkDTO);

    /**
     * Updates a likeMark.
     *
     * @param likeMarkDTO the entity to update.
     * @return the persisted entity.
     */
    LikeMarkDTO update(LikeMarkDTO likeMarkDTO);

    /**
     * Partially updates a likeMark.
     *
     * @param likeMarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LikeMarkDTO> partialUpdate(LikeMarkDTO likeMarkDTO);

    /**
     * Get all the likeMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LikeMarkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" likeMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LikeMarkDTO> findOne(Long id);

    /**
     * Delete the "id" likeMark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
