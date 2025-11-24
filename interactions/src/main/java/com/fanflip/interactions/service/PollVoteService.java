package com.monsterdam.interactions.service;

import com.monsterdam.interactions.service.dto.PollVoteDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.interactions.domain.PollVote}.
 */
public interface PollVoteService {
    /**
     * Save a pollVote.
     *
     * @param pollVoteDTO the entity to save.
     * @return the persisted entity.
     */
    PollVoteDTO save(PollVoteDTO pollVoteDTO);

    /**
     * Updates a pollVote.
     *
     * @param pollVoteDTO the entity to update.
     * @return the persisted entity.
     */
    PollVoteDTO update(PollVoteDTO pollVoteDTO);

    /**
     * Partially updates a pollVote.
     *
     * @param pollVoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PollVoteDTO> partialUpdate(PollVoteDTO pollVoteDTO);

    /**
     * Get all the pollVotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollVoteDTO> findAll(Pageable pageable);

    /**
     * Get all the pollVotes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PollVoteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pollVote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PollVoteDTO> findOne(Long id);

    /**
     * Delete the "id" pollVote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
