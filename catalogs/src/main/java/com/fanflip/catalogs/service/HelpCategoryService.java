package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.HelpCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.HelpCategory}.
 */
public interface HelpCategoryService {
    /**
     * Save a helpCategory.
     *
     * @param helpCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    HelpCategoryDTO save(HelpCategoryDTO helpCategoryDTO);

    /**
     * Updates a helpCategory.
     *
     * @param helpCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    HelpCategoryDTO update(HelpCategoryDTO helpCategoryDTO);

    /**
     * Partially updates a helpCategory.
     *
     * @param helpCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HelpCategoryDTO> partialUpdate(HelpCategoryDTO helpCategoryDTO);

    /**
     * Get all the helpCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HelpCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" helpCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HelpCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" helpCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
