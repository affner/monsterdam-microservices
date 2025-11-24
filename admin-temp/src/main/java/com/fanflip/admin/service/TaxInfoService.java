package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.TaxInfoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.TaxInfo}.
 */
public interface TaxInfoService {
    /**
     * Save a taxInfo.
     *
     * @param taxInfoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TaxInfoDTO> save(TaxInfoDTO taxInfoDTO);

    /**
     * Updates a taxInfo.
     *
     * @param taxInfoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TaxInfoDTO> update(TaxInfoDTO taxInfoDTO);

    /**
     * Partially updates a taxInfo.
     *
     * @param taxInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TaxInfoDTO> partialUpdate(TaxInfoDTO taxInfoDTO);

    /**
     * Get all the taxInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TaxInfoDTO> findAll(Pageable pageable);

    /**
     * Get all the taxInfos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TaxInfoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of taxInfos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of taxInfos available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" taxInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TaxInfoDTO> findOne(Long id);

    /**
     * Delete the "id" taxInfo.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the taxInfo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TaxInfoDTO> search(String query, Pageable pageable);
}
