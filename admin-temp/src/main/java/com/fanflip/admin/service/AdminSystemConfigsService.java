package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.AdminSystemConfigsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.AdminSystemConfigs}.
 */
public interface AdminSystemConfigsService {
    /**
     * Save a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AdminSystemConfigsDTO> save(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Updates a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AdminSystemConfigsDTO> update(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Partially updates a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AdminSystemConfigsDTO> partialUpdate(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Get all the adminSystemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AdminSystemConfigsDTO> findAll(Pageable pageable);

    /**
     * Returns the number of adminSystemConfigs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of adminSystemConfigs available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" adminSystemConfigs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AdminSystemConfigsDTO> findOne(Long id);

    /**
     * Delete the "id" adminSystemConfigs.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the adminSystemConfigs corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AdminSystemConfigsDTO> search(String query, Pageable pageable);
}
