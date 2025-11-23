package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.UserReportDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.UserReport}.
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
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserReportDTO> findAll(Pageable pageable);

    /**
     * Get all the userReports with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserReportDTO> findAllWithEagerRelationships(Pageable pageable);

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
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserReportDTO> search(String query, Pageable pageable);
}
