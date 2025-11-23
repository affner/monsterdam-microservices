package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SpecialRewardDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SpecialReward}.
 */
public interface SpecialRewardService {
    /**
     * Save a specialReward.
     *
     * @param specialRewardDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SpecialRewardDTO> save(SpecialRewardDTO specialRewardDTO);

    /**
     * Updates a specialReward.
     *
     * @param specialRewardDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SpecialRewardDTO> update(SpecialRewardDTO specialRewardDTO);

    /**
     * Partially updates a specialReward.
     *
     * @param specialRewardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SpecialRewardDTO> partialUpdate(SpecialRewardDTO specialRewardDTO);

    /**
     * Get all the specialRewards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialRewardDTO> findAll(Pageable pageable);

    /**
     * Returns the number of specialRewards available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of specialRewards available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" specialReward.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SpecialRewardDTO> findOne(Long id);

    /**
     * Delete the "id" specialReward.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the specialReward corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SpecialRewardDTO> search(String query, Pageable pageable);
}
