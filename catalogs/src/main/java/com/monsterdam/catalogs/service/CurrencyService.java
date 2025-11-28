package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.CurrencyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.Currency}.
 */
public interface CurrencyService {
    /**
     * Save a currency.
     *
     * @param currencyDTO the entity to save.
     * @return the persisted entity.
     */
    CurrencyDTO save(CurrencyDTO currencyDTO);

    /**
     * Updates a currency.
     *
     * @param currencyDTO the entity to update.
     * @return the persisted entity.
     */
    CurrencyDTO update(CurrencyDTO currencyDTO);

    /**
     * Partially updates a currency.
     *
     * @param currencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CurrencyDTO> partialUpdate(CurrencyDTO currencyDTO);

    /**
     * Get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CurrencyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" currency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CurrencyDTO> findOne(Long id);

    /**
     * Delete the "id" currency.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
