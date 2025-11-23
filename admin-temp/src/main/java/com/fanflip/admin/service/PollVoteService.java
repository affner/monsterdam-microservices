package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PollVoteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PollVote}.
 */
public interface PollVoteService {
    /**
     * Save a pollVote.
     *
     * @param pollVoteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PollVoteDTO> save(PollVoteDTO pollVoteDTO);

    /**
     * Updates a pollVote.
     *
     * @param pollVoteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PollVoteDTO> update(PollVoteDTO pollVoteDTO);

    /**
     * Partially updates a pollVote.
     *
     * @param pollVoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PollVoteDTO> partialUpdate(PollVoteDTO pollVoteDTO);

    /**
     * Get all the pollVotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollVoteDTO> findAll(Pageable pageable);

    /**
     * Get all the pollVotes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollVoteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pollVotes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of pollVotes available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" pollVote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PollVoteDTO> findOne(Long id);

    /**
     * Delete the "id" pollVote.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the pollVote corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PollVoteDTO> search(String query, Pageable pageable);
}
