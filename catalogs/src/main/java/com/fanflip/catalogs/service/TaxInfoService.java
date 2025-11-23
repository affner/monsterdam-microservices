package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.TaxInfoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.TaxInfo}.
 */
public interface TaxInfoService {
    /**
     * Save a taxInfo.
     *
     * @param taxInfoDTO the entity to save.
     * @return the persisted entity.
     */
    TaxInfoDTO save(TaxInfoDTO taxInfoDTO);

    /**
     * Updates a taxInfo.
     *
     * @param taxInfoDTO the entity to update.
     * @return the persisted entity.
     */
    TaxInfoDTO update(TaxInfoDTO taxInfoDTO);

    /**
     * Partially updates a taxInfo.
     *
     * @param taxInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaxInfoDTO> partialUpdate(TaxInfoDTO taxInfoDTO);

    /**
     * Get all the taxInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxInfoDTO> findAll(Pageable pageable);

    /**
     * Get all the taxInfos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaxInfoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" taxInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaxInfoDTO> findOne(Long id);

    /**
     * Delete the "id" taxInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
