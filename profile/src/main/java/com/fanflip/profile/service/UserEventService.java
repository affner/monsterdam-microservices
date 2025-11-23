package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.UserEventDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.UserEvent}.
 */
public interface UserEventService {
    /**
     * Save a userEvent.
     *
     * @param userEventDTO the entity to save.
     * @return the persisted entity.
     */
    UserEventDTO save(UserEventDTO userEventDTO);

    /**
     * Updates a userEvent.
     *
     * @param userEventDTO the entity to update.
     * @return the persisted entity.
     */
    UserEventDTO update(UserEventDTO userEventDTO);

    /**
     * Partially updates a userEvent.
     *
     * @param userEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserEventDTO> partialUpdate(UserEventDTO userEventDTO);

    /**
     * Get all the userEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserEventDTO> findOne(Long id);

    /**
     * Delete the "id" userEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
