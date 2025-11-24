package com.monsterdam.multimedia.service;

import com.monsterdam.multimedia.service.dto.UserTagRelationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.multimedia.domain.UserTagRelation}.
 */
public interface UserTagRelationService {
    /**
     * Save a userTagRelation.
     *
     * @param userTagRelationDTO the entity to save.
     * @return the persisted entity.
     */
    UserTagRelationDTO save(UserTagRelationDTO userTagRelationDTO);

    /**
     * Updates a userTagRelation.
     *
     * @param userTagRelationDTO the entity to update.
     * @return the persisted entity.
     */
    UserTagRelationDTO update(UserTagRelationDTO userTagRelationDTO);

    /**
     * Partially updates a userTagRelation.
     *
     * @param userTagRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserTagRelationDTO> partialUpdate(UserTagRelationDTO userTagRelationDTO);

    /**
     * Get all the userTagRelations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserTagRelationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userTagRelation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserTagRelationDTO> findOne(Long id);

    /**
     * Delete the "id" userTagRelation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
