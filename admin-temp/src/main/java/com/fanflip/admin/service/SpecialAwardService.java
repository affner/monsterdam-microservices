package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.SpecialAwardDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.SpecialAward}.
 */
public interface SpecialAwardService {
    /**
     * Save a specialAward.
     *
     * @param specialAwardDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SpecialAwardDTO> save(SpecialAwardDTO specialAwardDTO);

    /**
     * Updates a specialAward.
     *
     * @param specialAwardDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SpecialAwardDTO> update(SpecialAwardDTO specialAwardDTO);

    /**
     * Partially updates a specialAward.
     *
     * @param specialAwardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SpecialAwardDTO> partialUpdate(SpecialAwardDTO specialAwardDTO);

    /**
     * Get all the specialAwards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialAwardDTO> findAll(Pageable pageable);

    /**
     * Returns the number of specialAwards available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of specialAwards available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" specialAward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SpecialAwardDTO> findOne(Long id);

    /**
     * Delete the "id" specialAward.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the specialAward corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialAwardDTO> search(String query, Pageable pageable);
}
