package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SinglePhotoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SinglePhoto}.
 */
public interface SinglePhotoService {
    /**
     * Save a singlePhoto.
     *
     * @param singlePhotoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SinglePhotoDTO> save(SinglePhotoDTO singlePhotoDTO);

    /**
     * Updates a singlePhoto.
     *
     * @param singlePhotoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SinglePhotoDTO> update(SinglePhotoDTO singlePhotoDTO);

    /**
     * Partially updates a singlePhoto.
     *
     * @param singlePhotoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SinglePhotoDTO> partialUpdate(SinglePhotoDTO singlePhotoDTO);

    /**
     * Get all the singlePhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SinglePhotoDTO> findAll(Pageable pageable);

    /**
     * Get all the singlePhotos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SinglePhotoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of singlePhotos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of singlePhotos available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" singlePhoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SinglePhotoDTO> findOne(Long id);

    /**
     * Delete the "id" singlePhoto.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the singlePhoto corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SinglePhotoDTO> search(String query, Pageable pageable);
}
