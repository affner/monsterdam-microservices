package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.GlobalEventDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.GlobalEvent}.
 */
public interface GlobalEventService {
    /**
     * Save a globalEvent.
     *
     * @param globalEventDTO the entity to save.
     * @return the persisted entity.
     */
    GlobalEventDTO save(GlobalEventDTO globalEventDTO);

    /**
     * Updates a globalEvent.
     *
     * @param globalEventDTO the entity to update.
     * @return the persisted entity.
     */
    GlobalEventDTO update(GlobalEventDTO globalEventDTO);

    /**
     * Partially updates a globalEvent.
     *
     * @param globalEventDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GlobalEventDTO> partialUpdate(GlobalEventDTO globalEventDTO);

    /**
     * Get all the globalEvents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GlobalEventDTO> findAll(Pageable pageable);

    /**
     * Get the "id" globalEvent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GlobalEventDTO> findOne(Long id);

    /**
     * Delete the "id" globalEvent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
