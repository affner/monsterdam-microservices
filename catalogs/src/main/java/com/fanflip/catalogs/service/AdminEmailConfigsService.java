package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.AdminEmailConfigsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.AdminEmailConfigs}.
 */
public interface AdminEmailConfigsService {
    /**
     * Save a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to save.
     * @return the persisted entity.
     */
    AdminEmailConfigsDTO save(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Updates a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to update.
     * @return the persisted entity.
     */
    AdminEmailConfigsDTO update(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Partially updates a adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdminEmailConfigsDTO> partialUpdate(AdminEmailConfigsDTO adminEmailConfigsDTO);

    /**
     * Get all the adminEmailConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdminEmailConfigsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adminEmailConfigs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdminEmailConfigsDTO> findOne(Long id);

    /**
     * Delete the "id" adminEmailConfigs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
