package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PostPollDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PostPoll}.
 */
public interface PostPollService {
    /**
     * Save a postPoll.
     *
     * @param postPollDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PostPollDTO> save(PostPollDTO postPollDTO);

    /**
     * Updates a postPoll.
     *
     * @param postPollDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PostPollDTO> update(PostPollDTO postPollDTO);

    /**
     * Partially updates a postPoll.
     *
     * @param postPollDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PostPollDTO> partialUpdate(PostPollDTO postPollDTO);

    /**
     * Get all the postPolls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostPollDTO> findAll(Pageable pageable);

    /**
     * Get all the PostPollDTO where Post is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PostPollDTO> findAllWherePostIsNull();

    /**
     * Returns the number of postPolls available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of postPolls available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" postPoll.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PostPollDTO> findOne(Long id);

    /**
     * Delete the "id" postPoll.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the postPoll corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PostPollDTO> search(String query, Pageable pageable);
}
