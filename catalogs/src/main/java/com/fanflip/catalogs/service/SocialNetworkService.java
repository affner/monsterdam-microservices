package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.SocialNetworkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.SocialNetwork}.
 */
public interface SocialNetworkService {
    /**
     * Save a socialNetwork.
     *
     * @param socialNetworkDTO the entity to save.
     * @return the persisted entity.
     */
    SocialNetworkDTO save(SocialNetworkDTO socialNetworkDTO);

    /**
     * Updates a socialNetwork.
     *
     * @param socialNetworkDTO the entity to update.
     * @return the persisted entity.
     */
    SocialNetworkDTO update(SocialNetworkDTO socialNetworkDTO);

    /**
     * Partially updates a socialNetwork.
     *
     * @param socialNetworkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SocialNetworkDTO> partialUpdate(SocialNetworkDTO socialNetworkDTO);

    /**
     * Get all the socialNetworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SocialNetworkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" socialNetwork.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SocialNetworkDTO> findOne(Long id);

    /**
     * Delete the "id" socialNetwork.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
