package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.AdminSystemConfigsDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.AdminSystemConfigs}.
 */
public interface AdminSystemConfigsService {
    /**
     * Save a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to save.
     * @return the persisted entity.
     */
    AdminSystemConfigsDTO save(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Updates a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to update.
     * @return the persisted entity.
     */
    AdminSystemConfigsDTO update(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Partially updates a adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdminSystemConfigsDTO> partialUpdate(AdminSystemConfigsDTO adminSystemConfigsDTO);

    /**
     * Get all the adminSystemConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AdminSystemConfigsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" adminSystemConfigs.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdminSystemConfigsDTO> findOne(Long id);

    /**
     * Delete the "id" adminSystemConfigs.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
