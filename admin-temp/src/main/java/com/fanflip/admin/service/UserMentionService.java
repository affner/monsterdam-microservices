package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.UserMentionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.UserMention}.
 */
public interface UserMentionService {
    /**
     * Save a userMention.
     *
     * @param userMentionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserMentionDTO> save(UserMentionDTO userMentionDTO);

    /**
     * Updates a userMention.
     *
     * @param userMentionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserMentionDTO> update(UserMentionDTO userMentionDTO);

    /**
     * Partially updates a userMention.
     *
     * @param userMentionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserMentionDTO> partialUpdate(UserMentionDTO userMentionDTO);

    /**
     * Get all the userMentions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserMentionDTO> findAll(Pageable pageable);

    /**
     * Get all the userMentions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserMentionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of userMentions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userMentions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userMention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserMentionDTO> findOne(Long id);

    /**
     * Delete the "id" userMention.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userMention corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserMentionDTO> search(String query, Pageable pageable);
}
