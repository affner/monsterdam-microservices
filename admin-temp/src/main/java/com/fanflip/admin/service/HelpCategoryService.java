package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.HelpCategoryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.HelpCategory}.
 */
public interface HelpCategoryService {
    /**
     * Save a helpCategory.
     *
     * @param helpCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HelpCategoryDTO> save(HelpCategoryDTO helpCategoryDTO);

    /**
     * Updates a helpCategory.
     *
     * @param helpCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HelpCategoryDTO> update(HelpCategoryDTO helpCategoryDTO);

    /**
     * Partially updates a helpCategory.
     *
     * @param helpCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HelpCategoryDTO> partialUpdate(HelpCategoryDTO helpCategoryDTO);

    /**
     * Get all the helpCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpCategoryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of helpCategories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of helpCategories available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" helpCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HelpCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" helpCategory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the helpCategory corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpCategoryDTO> search(String query, Pageable pageable);
}
