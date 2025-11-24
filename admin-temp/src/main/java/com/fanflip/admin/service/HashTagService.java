package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.HashTagDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.HashTag}.
 */
public interface HashTagService {
    /**
     * Save a hashTag.
     *
     * @param hashTagDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HashTagDTO> save(HashTagDTO hashTagDTO);

    /**
     * Updates a hashTag.
     *
     * @param hashTagDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HashTagDTO> update(HashTagDTO hashTagDTO);

    /**
     * Partially updates a hashTag.
     *
     * @param hashTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HashTagDTO> partialUpdate(HashTagDTO hashTagDTO);

    /**
     * Get all the hashTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HashTagDTO> findAll(Pageable pageable);

    /**
     * Returns the number of hashTags available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of hashTags available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" hashTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HashTagDTO> findOne(Long id);

    /**
     * Delete the "id" hashTag.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the hashTag corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HashTagDTO> search(String query, Pageable pageable);
}
