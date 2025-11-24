package com.monsterdam.multimedia.service;

import com.monsterdam.multimedia.service.dto.SingleLiveStreamDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.multimedia.domain.SingleLiveStream}.
 */
public interface SingleLiveStreamService {
    /**
     * Save a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to save.
     * @return the persisted entity.
     */
    SingleLiveStreamDTO save(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Updates a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to update.
     * @return the persisted entity.
     */
    SingleLiveStreamDTO update(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Partially updates a singleLiveStream.
     *
     * @param singleLiveStreamDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SingleLiveStreamDTO> partialUpdate(SingleLiveStreamDTO singleLiveStreamDTO);

    /**
     * Get all the singleLiveStreams.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SingleLiveStreamDTO> findAll(Pageable pageable);

    /**
     * Get the "id" singleLiveStream.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SingleLiveStreamDTO> findOne(Long id);

    /**
     * Delete the "id" singleLiveStream.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
