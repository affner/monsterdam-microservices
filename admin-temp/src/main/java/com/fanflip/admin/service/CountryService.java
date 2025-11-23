package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.CountryDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.Country}.
 */
public interface CountryService {
    /**
     * Save a country.
     *
     * @param countryDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CountryDTO> save(CountryDTO countryDTO);

    /**
     * Updates a country.
     *
     * @param countryDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CountryDTO> update(CountryDTO countryDTO);

    /**
     * Partially updates a country.
     *
     * @param countryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CountryDTO> partialUpdate(CountryDTO countryDTO);

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CountryDTO> findAll(Pageable pageable);

    /**
     * Returns the number of countries available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of countries available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" country.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CountryDTO> findOne(Long id);

    /**
     * Delete the "id" country.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the country corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CountryDTO> search(String query, Pageable pageable);
}
