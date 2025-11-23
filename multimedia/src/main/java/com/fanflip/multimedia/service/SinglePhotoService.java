package com.fanflip.multimedia.service;

import com.fanflip.multimedia.service.dto.SinglePhotoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.multimedia.domain.SinglePhoto}.
 */
public interface SinglePhotoService {
    /**
     * Save a singlePhoto.
     *
     * @param singlePhotoDTO the entity to save.
     * @return the persisted entity.
     */
    SinglePhotoDTO save(SinglePhotoDTO singlePhotoDTO);

    /**
     * Updates a singlePhoto.
     *
     * @param singlePhotoDTO the entity to update.
     * @return the persisted entity.
     */
    SinglePhotoDTO update(SinglePhotoDTO singlePhotoDTO);

    /**
     * Partially updates a singlePhoto.
     *
     * @param singlePhotoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SinglePhotoDTO> partialUpdate(SinglePhotoDTO singlePhotoDTO);

    /**
     * Get all the singlePhotos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SinglePhotoDTO> findAll(Pageable pageable);

    /**
     * Get all the singlePhotos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SinglePhotoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" singlePhoto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SinglePhotoDTO> findOne(Long id);

    /**
     * Delete the "id" singlePhoto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
