package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.TaxDeclarationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.TaxDeclaration}.
 */
public interface TaxDeclarationService {
    /**
     * Save a taxDeclaration.
     *
     * @param taxDeclarationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<TaxDeclarationDTO> save(TaxDeclarationDTO taxDeclarationDTO);

    /**
     * Updates a taxDeclaration.
     *
     * @param taxDeclarationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<TaxDeclarationDTO> update(TaxDeclarationDTO taxDeclarationDTO);

    /**
     * Partially updates a taxDeclaration.
     *
     * @param taxDeclarationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<TaxDeclarationDTO> partialUpdate(TaxDeclarationDTO taxDeclarationDTO);

    /**
     * Get all the taxDeclarations.
     *
     * @return the list of entities.
     */
    Flux<TaxDeclarationDTO> findAll();

    /**
     * Get all the taxDeclarations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<TaxDeclarationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of taxDeclarations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of taxDeclarations available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" taxDeclaration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<TaxDeclarationDTO> findOne(Long id);

    /**
     * Delete the "id" taxDeclaration.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the taxDeclaration corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<TaxDeclarationDTO> search(String query);
}
