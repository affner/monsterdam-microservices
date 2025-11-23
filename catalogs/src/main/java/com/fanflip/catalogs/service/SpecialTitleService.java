package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.SpecialTitleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.SpecialTitle}.
 */
public interface SpecialTitleService {
    /**
     * Save a specialTitle.
     *
     * @param specialTitleDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialTitleDTO save(SpecialTitleDTO specialTitleDTO);

    /**
     * Updates a specialTitle.
     *
     * @param specialTitleDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialTitleDTO update(SpecialTitleDTO specialTitleDTO);

    /**
     * Partially updates a specialTitle.
     *
     * @param specialTitleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialTitleDTO> partialUpdate(SpecialTitleDTO specialTitleDTO);

    /**
     * Get all the specialTitles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialTitleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" specialTitle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialTitleDTO> findOne(Long id);

    /**
     * Delete the "id" specialTitle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
