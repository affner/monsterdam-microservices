package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.EmojiTypeDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.EmojiType}.
 */
public interface EmojiTypeService {
    /**
     * Save a emojiType.
     *
     * @param emojiTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<EmojiTypeDTO> save(EmojiTypeDTO emojiTypeDTO);

    /**
     * Updates a emojiType.
     *
     * @param emojiTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<EmojiTypeDTO> update(EmojiTypeDTO emojiTypeDTO);

    /**
     * Partially updates a emojiType.
     *
     * @param emojiTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<EmojiTypeDTO> partialUpdate(EmojiTypeDTO emojiTypeDTO);

    /**
     * Get all the emojiTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmojiTypeDTO> findAll(Pageable pageable);

    /**
     * Returns the number of emojiTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of emojiTypes available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" emojiType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<EmojiTypeDTO> findOne(Long id);

    /**
     * Delete the "id" emojiType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the emojiType corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<EmojiTypeDTO> search(String query, Pageable pageable);
}
