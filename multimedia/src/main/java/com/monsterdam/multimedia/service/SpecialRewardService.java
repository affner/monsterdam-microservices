package com.monsterdam.multimedia.service;

import com.monsterdam.multimedia.service.dto.SpecialRewardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.multimedia.domain.SpecialReward}.
 */
public interface SpecialRewardService {
    /**
     * Save a specialReward.
     *
     * @param specialRewardDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialRewardDTO save(SpecialRewardDTO specialRewardDTO);

    /**
     * Updates a specialReward.
     *
     * @param specialRewardDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialRewardDTO update(SpecialRewardDTO specialRewardDTO);

    /**
     * Partially updates a specialReward.
     *
     * @param specialRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialRewardDTO> partialUpdate(SpecialRewardDTO specialRewardDTO);

    /**
     * Get all the specialRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialRewardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" specialReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialRewardDTO> findOne(Long id);

    /**
     * Delete the "id" specialReward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
