package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SingleLiveStreamDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SingleLiveStream}.
 */
public interface SingleLiveStreamService {
    /**
     * Save a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SingleLiveStreamDTO> save(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Updates a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SingleLiveStreamDTO> update(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Partially updates a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SingleLiveStreamDTO> partialUpdate(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Get all the singleLiveStreams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleLiveStreamDTO> findAll(Pageable pageable);

    /**
     * Returns the number of singleLiveStreams available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of singleLiveStreams available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" singleLiveStream.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SingleLiveStreamDTO> findOne(Long id);

    /**
     * Delete the "id" singleLiveStream.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the singleLiveStream corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleLiveStreamDTO> search(String query, Pageable pageable);
}
