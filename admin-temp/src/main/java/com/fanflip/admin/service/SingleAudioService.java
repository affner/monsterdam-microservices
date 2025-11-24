package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.SingleAudioDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.SingleAudio}.
 */
public interface SingleAudioService {
    /**
     * Save a singleAudio.
     *
     * @param singleAudioDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SingleAudioDTO> save(SingleAudioDTO singleAudioDTO);

    /**
     * Updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SingleAudioDTO> update(SingleAudioDTO singleAudioDTO);

    /**
     * Partially updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO);

    /**
     * Get all the singleAudios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleAudioDTO> findAll(Pageable pageable);

    /**
     * Get all the SingleAudioDTO where ContentPackage is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<SingleAudioDTO> findAllWhereContentPackageIsNull();

    /**
     * Returns the number of singleAudios available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of singleAudios available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" singleAudio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SingleAudioDTO> findOne(Long id);

    /**
     * Delete the "id" singleAudio.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the singleAudio corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleAudioDTO> search(String query, Pageable pageable);
}
