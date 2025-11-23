package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SingleVideoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SingleVideo}.
 */
public interface SingleVideoService {
    /**
     * Save a singleVideo.
     *
     * @param singleVideoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SingleVideoDTO> save(SingleVideoDTO singleVideoDTO);

    /**
     * Updates a singleVideo.
     *
     * @param singleVideoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SingleVideoDTO> update(SingleVideoDTO singleVideoDTO);

    /**
     * Partially updates a singleVideo.
     *
     * @param singleVideoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SingleVideoDTO> partialUpdate(SingleVideoDTO singleVideoDTO);

    /**
     * Get all the singleVideos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleVideoDTO> findAll(Pageable pageable);

    /**
     * Get all the singleVideos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleVideoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of singleVideos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of singleVideos available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" singleVideo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SingleVideoDTO> findOne(Long id);

    /**
     * Delete the "id" singleVideo.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the singleVideo corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleVideoDTO> search(String query, Pageable pageable);
}
