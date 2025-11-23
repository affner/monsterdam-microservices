package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SpecialTitleDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SpecialTitle}.
 */
public interface SpecialTitleService {
    /**
     * Save a specialTitle.
     *
     * @param specialTitleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SpecialTitleDTO> save(SpecialTitleDTO specialTitleDTO);

    /**
     * Updates a specialTitle.
     *
     * @param specialTitleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SpecialTitleDTO> update(SpecialTitleDTO specialTitleDTO);

    /**
     * Partially updates a specialTitle.
     *
     * @param specialTitleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SpecialTitleDTO> partialUpdate(SpecialTitleDTO specialTitleDTO);

    /**
     * Get all the specialTitles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialTitleDTO> findAll(Pageable pageable);

    /**
     * Returns the number of specialTitles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of specialTitles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" specialTitle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SpecialTitleDTO> findOne(Long id);

    /**
     * Delete the "id" specialTitle.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the specialTitle corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialTitleDTO> search(String query, Pageable pageable);
}
