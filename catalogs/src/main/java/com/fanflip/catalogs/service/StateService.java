package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.StateDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.State}.
 */
public interface StateService {
    /**
     * Save a state.
     *
     * @param stateDTO the entity to save.
     * @return the persisted entity.
     */
    StateDTO save(StateDTO stateDTO);

    /**
     * Updates a state.
     *
     * @param stateDTO the entity to update.
     * @return the persisted entity.
     */
    StateDTO update(StateDTO stateDTO);

    /**
     * Partially updates a state.
     *
     * @param stateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StateDTO> partialUpdate(StateDTO stateDTO);

    /**
     * Get all the states.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateDTO> findAll(Pageable pageable);

    /**
     * Get all the states with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" state.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StateDTO> findOne(Long id);

    /**
     * Delete the "id" state.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the state corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateDTO> search(String query, Pageable pageable);
}
