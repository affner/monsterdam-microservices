package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.UserAssociationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.UserAssociation}.
 */
public interface UserAssociationService {
    /**
     * Save a userAssociation.
     *
     * @param userAssociationDTO the entity to save.
     * @return the persisted entity.
     */
    UserAssociationDTO save(UserAssociationDTO userAssociationDTO);

    /**
     * Updates a userAssociation.
     *
     * @param userAssociationDTO the entity to update.
     * @return the persisted entity.
     */
    UserAssociationDTO update(UserAssociationDTO userAssociationDTO);

    /**
     * Partially updates a userAssociation.
     *
     * @param userAssociationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAssociationDTO> partialUpdate(UserAssociationDTO userAssociationDTO);

    /**
     * Get all the userAssociations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAssociationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userAssociation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAssociationDTO> findOne(Long id);

    /**
     * Delete the "id" userAssociation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
