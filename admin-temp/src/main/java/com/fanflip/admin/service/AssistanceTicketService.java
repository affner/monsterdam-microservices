package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.AssistanceTicketDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.AssistanceTicket}.
 */
public interface AssistanceTicketService {
    /**
     * Save a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AssistanceTicketDTO> save(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Updates a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AssistanceTicketDTO> update(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Partially updates a assistanceTicket.
     *
     * @param assistanceTicketDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AssistanceTicketDTO> partialUpdate(AssistanceTicketDTO assistanceTicketDTO);

    /**
     * Get all the assistanceTickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AssistanceTicketDTO> findAll(Pageable pageable);

    /**
     * Get all the AssistanceTicketDTO where Report is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<AssistanceTicketDTO> findAllWhereReportIsNull();
    /**
     * Get all the AssistanceTicketDTO where DocumentsReview is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<AssistanceTicketDTO> findAllWhereDocumentsReviewIsNull();

    /**
     * Returns the number of assistanceTickets available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of assistanceTickets available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" assistanceTicket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AssistanceTicketDTO> findOne(Long id);

    /**
     * Delete the "id" assistanceTicket.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the assistanceTicket corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AssistanceTicketDTO> search(String query, Pageable pageable);
}
