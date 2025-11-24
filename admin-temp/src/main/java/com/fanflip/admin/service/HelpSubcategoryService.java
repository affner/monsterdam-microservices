package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.HelpSubcategoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.HelpSubcategory}.
 */
public interface HelpSubcategoryService {
    /**
     * Save a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HelpSubcategoryDTO> save(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Updates a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HelpSubcategoryDTO> update(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Partially updates a helpSubcategory.
     *
     * @param helpSubcategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HelpSubcategoryDTO> partialUpdate(HelpSubcategoryDTO helpSubcategoryDTO);

    /**
     * Get all the helpSubcategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpSubcategoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of helpSubcategories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of helpSubcategories available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" helpSubcategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HelpSubcategoryDTO> findOne(Long id);

    /**
     * Delete the "id" helpSubcategory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the helpSubcategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpSubcategoryDTO> search(String query, Pageable pageable);
}
