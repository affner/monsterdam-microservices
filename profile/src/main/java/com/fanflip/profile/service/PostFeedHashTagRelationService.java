package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.PostFeedHashTagRelationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.PostFeedHashTagRelation}.
 */
public interface PostFeedHashTagRelationService {
    /**
     * Save a postFeedHashTagRelation.
     *
     * @param postFeedHashTagRelationDTO the entity to save.
     * @return the persisted entity.
     */
    PostFeedHashTagRelationDTO save(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO);

    /**
     * Updates a postFeedHashTagRelation.
     *
     * @param postFeedHashTagRelationDTO the entity to update.
     * @return the persisted entity.
     */
    PostFeedHashTagRelationDTO update(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO);

    /**
     * Partially updates a postFeedHashTagRelation.
     *
     * @param postFeedHashTagRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostFeedHashTagRelationDTO> partialUpdate(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO);

    /**
     * Get all the postFeedHashTagRelations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostFeedHashTagRelationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postFeedHashTagRelation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostFeedHashTagRelationDTO> findOne(Long id);

    /**
     * Delete the "id" postFeedHashTagRelation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
