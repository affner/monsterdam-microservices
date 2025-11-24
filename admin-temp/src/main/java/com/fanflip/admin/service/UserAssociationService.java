package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.UserAssociationDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.UserAssociation}.
 */
public interface UserAssociationService {
    /**
     * Save a userAssociation.
     *
     * @param userAssociationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserAssociationDTO> save(UserAssociationDTO userAssociationDTO);

    /**
     * Updates a userAssociation.
     *
     * @param userAssociationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserAssociationDTO> update(UserAssociationDTO userAssociationDTO);

    /**
     * Partially updates a userAssociation.
     *
     * @param userAssociationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserAssociationDTO> partialUpdate(UserAssociationDTO userAssociationDTO);

    /**
     * Get all the userAssociations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserAssociationDTO> findAll(Pageable pageable);

    /**
     * Returns the number of userAssociations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userAssociations available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userAssociation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserAssociationDTO> findOne(Long id);

    /**
     * Delete the "id" userAssociation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userAssociation corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserAssociationDTO> search(String query, Pageable pageable);
}
