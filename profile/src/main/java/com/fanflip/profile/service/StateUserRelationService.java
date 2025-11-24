package com.monsterdam.profile.service;

import com.monsterdam.profile.service.dto.StateUserRelationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.profile.domain.StateUserRelation}.
 */
public interface StateUserRelationService {
    /**
     * Save a stateUserRelation.
     *
     * @param stateUserRelationDTO the entity to save.
     * @return the persisted entity.
     */
    StateUserRelationDTO save(StateUserRelationDTO stateUserRelationDTO);

    /**
     * Updates a stateUserRelation.
     *
     * @param stateUserRelationDTO the entity to update.
     * @return the persisted entity.
     */
    StateUserRelationDTO update(StateUserRelationDTO stateUserRelationDTO);

    /**
     * Partially updates a stateUserRelation.
     *
     * @param stateUserRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StateUserRelationDTO> partialUpdate(StateUserRelationDTO stateUserRelationDTO);

    /**
     * Get all the stateUserRelations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateUserRelationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" stateUserRelation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StateUserRelationDTO> findOne(Long id);

    /**
     * Delete the "id" stateUserRelation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
