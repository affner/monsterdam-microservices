package com.monsterdam.interactions.service;

import com.monsterdam.interactions.service.dto.UserMentionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.interactions.domain.UserMention}.
 */
public interface UserMentionService {
    /**
     * Save a userMention.
     *
     * @param userMentionDTO the entity to save.
     * @return the persisted entity.
     */
    UserMentionDTO save(UserMentionDTO userMentionDTO);

    /**
     * Updates a userMention.
     *
     * @param userMentionDTO the entity to update.
     * @return the persisted entity.
     */
    UserMentionDTO update(UserMentionDTO userMentionDTO);

    /**
     * Partially updates a userMention.
     *
     * @param userMentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserMentionDTO> partialUpdate(UserMentionDTO userMentionDTO);

    /**
     * Get all the userMentions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserMentionDTO> findAll(Pageable pageable);

    /**
     * Get all the userMentions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserMentionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userMention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserMentionDTO> findOne(Long id);

    /**
     * Delete the "id" userMention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
