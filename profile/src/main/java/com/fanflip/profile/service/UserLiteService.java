package com.fanflip.profile.service;

import com.fanflip.profile.service.dto.UserLiteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.profile.domain.UserLite}.
 */
public interface UserLiteService {
    /**
     * Save a userLite.
     *
     * @param userLiteDTO the entity to save.
     * @return the persisted entity.
     */
    UserLiteDTO save(UserLiteDTO userLiteDTO);

    /**
     * Updates a userLite.
     *
     * @param userLiteDTO the entity to update.
     * @return the persisted entity.
     */
    UserLiteDTO update(UserLiteDTO userLiteDTO);

    /**
     * Partially updates a userLite.
     *
     * @param userLiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserLiteDTO> partialUpdate(UserLiteDTO userLiteDTO);

    /**
     * Get all the userLites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserLiteDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userLite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserLiteDTO> findOne(Long id);

    /**
     * Delete the "id" userLite.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
