package com.monsterdam.profile.service;

import com.monsterdam.profile.service.dto.HashTagDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.profile.domain.HashTag}.
 */
public interface HashTagService {
    /**
     * Save a hashTag.
     *
     * @param hashTagDTO the entity to save.
     * @return the persisted entity.
     */
    HashTagDTO save(HashTagDTO hashTagDTO);

    /**
     * Updates a hashTag.
     *
     * @param hashTagDTO the entity to update.
     * @return the persisted entity.
     */
    HashTagDTO update(HashTagDTO hashTagDTO);

    /**
     * Partially updates a hashTag.
     *
     * @param hashTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HashTagDTO> partialUpdate(HashTagDTO hashTagDTO);

    /**
     * Get all the hashTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HashTagDTO> findAll(Pageable pageable);

    /**
     * Get the "id" hashTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HashTagDTO> findOne(Long id);

    /**
     * Delete the "id" hashTag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

}
