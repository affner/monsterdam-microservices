package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.HelpSubcategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.HelpSubcategory}.
 */
public interface HelpSubcategoryService {
    /**
     * Save a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to save.
     * @return the persisted entity.
     */
    HelpSubcategoryDTO save(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Updates a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to update.
     * @return the persisted entity.
     */
    HelpSubcategoryDTO update(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Partially updates a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HelpSubcategoryDTO> partialUpdate(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Get all the helpSubcategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HelpSubcategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" helpSubcategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HelpSubcategoryDTO> findOne(Long id);

    /**
     * Delete the "id" helpSubcategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
