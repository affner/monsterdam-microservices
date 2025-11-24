package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.FinancialStatementDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.FinancialStatement}.
 */
public interface FinancialStatementService {
    /**
     * Save a financialStatement.
     *
     * @param financialStatementDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<FinancialStatementDTO> save(FinancialStatementDTO financialStatementDTO);

    /**
     * Updates a financialStatement.
     *
     * @param financialStatementDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<FinancialStatementDTO> update(FinancialStatementDTO financialStatementDTO);

    /**
     * Partially updates a financialStatement.
     *
     * @param financialStatementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<FinancialStatementDTO> partialUpdate(FinancialStatementDTO financialStatementDTO);

    /**
     * Get all the financialStatements.
     *
     * @return the list of entities.
     */
    Flux<FinancialStatementDTO> findAll();

    /**
     * Get all the financialStatements with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<FinancialStatementDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of financialStatements available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of financialStatements available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" financialStatement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<FinancialStatementDTO> findOne(Long id);

    /**
     * Delete the "id" financialStatement.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the financialStatement corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<FinancialStatementDTO> search(String query);
}
