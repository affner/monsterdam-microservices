package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.BudgetDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.Budget}.
 */
public interface BudgetService {
    /**
     * Save a budget.
     *
     * @param budgetDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BudgetDTO> save(BudgetDTO budgetDTO);

    /**
     * Updates a budget.
     *
     * @param budgetDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BudgetDTO> update(BudgetDTO budgetDTO);

    /**
     * Partially updates a budget.
     *
     * @param budgetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BudgetDTO> partialUpdate(BudgetDTO budgetDTO);

    /**
     * Get all the budgets.
     *
     * @return the list of entities.
     */
    Flux<BudgetDTO> findAll();

    /**
     * Returns the number of budgets available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of budgets available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" budget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BudgetDTO> findOne(Long id);

    /**
     * Delete the "id" budget.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the budget corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<BudgetDTO> search(String query);
}
