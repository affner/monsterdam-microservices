package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.SocialNetworkDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.SocialNetwork}.
 */
public interface SocialNetworkService {
    /**
     * Save a socialNetwork.
     *
     * @param socialNetworkDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SocialNetworkDTO> save(SocialNetworkDTO socialNetworkDTO);

    /**
     * Updates a socialNetwork.
     *
     * @param socialNetworkDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SocialNetworkDTO> update(SocialNetworkDTO socialNetworkDTO);

    /**
     * Partially updates a socialNetwork.
     *
     * @param socialNetworkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SocialNetworkDTO> partialUpdate(SocialNetworkDTO socialNetworkDTO);

    /**
     * Get all the socialNetworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SocialNetworkDTO> findAll(Pageable pageable);

    /**
     * Returns the number of socialNetworks available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of socialNetworks available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" socialNetwork.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SocialNetworkDTO> findOne(Long id);

    /**
     * Delete the "id" socialNetwork.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the socialNetwork corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SocialNetworkDTO> search(String query, Pageable pageable);
}
