package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.SpecialAwardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.SpecialAward}.
 */
public interface SpecialAwardService {
    /**
     * Save a specialAward.
     *
     * @param specialAwardDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialAwardDTO save(SpecialAwardDTO specialAwardDTO);

    /**
     * Updates a specialAward.
     *
     * @param specialAwardDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialAwardDTO update(SpecialAwardDTO specialAwardDTO);

    /**
     * Partially updates a specialAward.
     *
     * @param specialAwardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialAwardDTO> partialUpdate(SpecialAwardDTO specialAwardDTO);

    /**
     * Get all the specialAwards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialAwardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" specialAward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialAwardDTO> findOne(Long id);

    /**
     * Delete the "id" specialAward.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
