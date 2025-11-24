package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.MoneyPayoutDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.MoneyPayout}.
 */
public interface MoneyPayoutService {
    /**
     * Save a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to save.
     * @return the persisted entity.
     */
    MoneyPayoutDTO save(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Updates a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to update.
     * @return the persisted entity.
     */
    MoneyPayoutDTO update(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Partially updates a moneyPayout.
     *
     * @param moneyPayoutDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MoneyPayoutDTO> partialUpdate(MoneyPayoutDTO moneyPayoutDTO);

    /**
     * Get all the moneyPayouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyPayoutDTO> findAll(Pageable pageable);

    /**
     * Get all the moneyPayouts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MoneyPayoutDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" moneyPayout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MoneyPayoutDTO> findOne(Long id);

    /**
     * Delete the "id" moneyPayout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
