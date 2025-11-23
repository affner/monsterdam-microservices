package com.fanflip.multimedia.service;

import com.fanflip.multimedia.service.dto.SingleAudioDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.multimedia.domain.SingleAudio}.
 */
public interface SingleAudioService {
    /**
     * Save a singleAudio.
     *
     * @param singleAudioDTO the entity to save.
     * @return the persisted entity.
     */
    SingleAudioDTO save(SingleAudioDTO singleAudioDTO);

    /**
     * Updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update.
     * @return the persisted entity.
     */
    SingleAudioDTO update(SingleAudioDTO singleAudioDTO);

    /**
     * Partially updates a singleAudio.
     *
     * @param singleAudioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO);

    /**
     * Get all the singleAudios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SingleAudioDTO> findAll(Pageable pageable);

    /**
     * Get all the SingleAudioDTO where ContentPackage is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<SingleAudioDTO> findAllWhereContentPackageIsNull();

    /**
     * Get the "id" singleAudio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SingleAudioDTO> findOne(Long id);

    /**
     * Delete the "id" singleAudio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
