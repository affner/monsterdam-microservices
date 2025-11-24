package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.AssetDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.Asset}.
 */
public interface AssetService {
    /**
     * Save a asset.
     *
     * @param assetDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AssetDTO> save(AssetDTO assetDTO);

    /**
     * Updates a asset.
     *
     * @param assetDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AssetDTO> update(AssetDTO assetDTO);

    /**
     * Partially updates a asset.
     *
     * @param assetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AssetDTO> partialUpdate(AssetDTO assetDTO);

    /**
     * Get all the assets.
     *
     * @return the list of entities.
     */
    Flux<AssetDTO> findAll();

    /**
     * Returns the number of assets available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of assets available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" asset.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AssetDTO> findOne(Long id);

    /**
     * Delete the "id" asset.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the asset corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    Flux<AssetDTO> search(String query);
}
