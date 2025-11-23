package com.fanflip.interactions.service;

import com.fanflip.interactions.service.dto.PollOptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.interactions.domain.PollOption}.
 */
public interface PollOptionService {
    /**
     * Save a pollOption.
     *
     * @param pollOptionDTO the entity to save.
     * @return the persisted entity.
     */
    PollOptionDTO save(PollOptionDTO pollOptionDTO);

    /**
     * Updates a pollOption.
     *
     * @param pollOptionDTO the entity to update.
     * @return the persisted entity.
     */
    PollOptionDTO update(PollOptionDTO pollOptionDTO);

    /**
     * Partially updates a pollOption.
     *
     * @param pollOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PollOptionDTO> partialUpdate(PollOptionDTO pollOptionDTO);

    /**
     * Get all the pollOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollOptionDTO> findAll(Pageable pageable);

    /**
     * Get all the pollOptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollOptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pollOption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PollOptionDTO> findOne(Long id);

    /**
     * Delete the "id" pollOption.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
