package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.UserReportDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.UserReport}.
 */
public interface UserReportService {
    /**
     * Save a userReport.
     *
     * @param userReportDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserReportDTO> save(UserReportDTO userReportDTO);

    /**
     * Updates a userReport.
     *
     * @param userReportDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserReportDTO> update(UserReportDTO userReportDTO);

    /**
     * Partially updates a userReport.
     *
     * @param userReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserReportDTO> partialUpdate(UserReportDTO userReportDTO);

    /**
     * Get all the userReports.
     *
     * @return the list of entities.
     */
    Flux<UserReportDTO> findAll();

    /**
     * Returns the number of userReports available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userReports available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserReportDTO> findOne(Long id);

    /**
     * Delete the "id" userReport.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userReport corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<UserReportDTO> search(String query);
}
