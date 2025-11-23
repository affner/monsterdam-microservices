package com.fanflip.catalogs.service;

import com.fanflip.catalogs.service.dto.EmojiTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.catalogs.domain.EmojiType}.
 */
public interface EmojiTypeService {
    /**
     * Save a emojiType.
     *
     * @param emojiTypeDTO the entity to save.
     * @return the persisted entity.
     */
    EmojiTypeDTO save(EmojiTypeDTO emojiTypeDTO);

    /**
     * Updates a emojiType.
     *
     * @param emojiTypeDTO the entity to update.
     * @return the persisted entity.
     */
    EmojiTypeDTO update(EmojiTypeDTO emojiTypeDTO);

    /**
     * Partially updates a emojiType.
     *
     * @param emojiTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmojiTypeDTO> partialUpdate(EmojiTypeDTO emojiTypeDTO);

    /**
     * Get all the emojiTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmojiTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" emojiType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmojiTypeDTO> findOne(Long id);

    /**
     * Delete the "id" emojiType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
