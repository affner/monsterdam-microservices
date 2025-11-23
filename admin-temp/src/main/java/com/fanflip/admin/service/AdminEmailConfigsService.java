package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.AdminEmailConfigsDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.AdminEmailConfigs}.
 */
public interface AdminEmailConfigsService {
    /**
     * Save a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AdminEmailConfigsDTO> save(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Updates a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AdminEmailConfigsDTO> update(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Partially updates a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AdminEmailConfigsDTO> partialUpdate(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Get all the adminEmailConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AdminEmailConfigsDTO> findAll(Pageable pageable);

    /**
     * Returns the number of adminEmailConfigs available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of adminEmailConfigs available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" adminEmailConfigs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AdminEmailConfigsDTO> findOne(Long id);

    /**
     * Delete the "id" adminEmailConfigs.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the adminEmailConfigs corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AdminEmailConfigsDTO> search(String query, Pageable pageable);
}
