package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.ContentPackageDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.ContentPackage}.
 */
public interface ContentPackageService {
    /**
     * Save a contentPackage.
     *
     * @param contentPackageDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ContentPackageDTO> save(ContentPackageDTO contentPackageDTO);

    /**
     * Updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ContentPackageDTO> update(ContentPackageDTO contentPackageDTO);

    /**
     * Partially updates a contentPackage.
     *
     * @param contentPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO);

    /**
     * Get all the contentPackages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ContentPackageDTO> findAll(Pageable pageable);

    /**
     * Get all the ContentPackageDTO where Message is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<ContentPackageDTO> findAllWhereMessageIsNull();
    /**
     * Get all the ContentPackageDTO where Post is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<ContentPackageDTO> findAllWherePostIsNull();

    /**
     * Get all the contentPackages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ContentPackageDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of contentPackages available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of contentPackages available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" contentPackage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ContentPackageDTO> findOne(Long id);

    /**
     * Delete the "id" contentPackage.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the contentPackage corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ContentPackageDTO> search(String query, Pageable pageable);
}
