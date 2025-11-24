package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.ModerationActionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.ModerationAction}.
 */
public interface ModerationActionService {
    /**
     * Save a moderationAction.
     *
     * @param moderationActionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ModerationActionDTO> save(ModerationActionDTO moderationActionDTO);

    /**
     * Updates a moderationAction.
     *
     * @param moderationActionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ModerationActionDTO> update(ModerationActionDTO moderationActionDTO);

    /**
     * Partially updates a moderationAction.
     *
     * @param moderationActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ModerationActionDTO> partialUpdate(ModerationActionDTO moderationActionDTO);

    /**
     * Get all the moderationActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ModerationActionDTO> findAll(Pageable pageable);

    /**
     * Get all the ModerationActionDTO where AssistanceTicket is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<ModerationActionDTO> findAllWhereAssistanceTicketIsNull();

    /**
     * Returns the number of moderationActions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of moderationActions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" moderationAction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ModerationActionDTO> findOne(Long id);

    /**
     * Delete the "id" moderationAction.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the moderationAction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ModerationActionDTO> search(String query, Pageable pageable);
}
