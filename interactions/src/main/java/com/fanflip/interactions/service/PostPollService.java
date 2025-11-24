package com.monsterdam.interactions.service;

import com.monsterdam.interactions.service.dto.PostPollDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.interactions.domain.PostPoll}.
 */
public interface PostPollService {
    /**
     * Save a postPoll.
     *
     * @param postPollDTO the entity to save.
     * @return the persisted entity.
     */
    PostPollDTO save(PostPollDTO postPollDTO);

    /**
     * Updates a postPoll.
     *
     * @param postPollDTO the entity to update.
     * @return the persisted entity.
     */
    PostPollDTO update(PostPollDTO postPollDTO);

    /**
     * Partially updates a postPoll.
     *
     * @param postPollDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PostPollDTO> partialUpdate(PostPollDTO postPollDTO);

    /**
     * Get all the postPolls.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostPollDTO> findAll(Pageable pageable);

    /**
     * Get all the PostPollDTO where Post is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PostPollDTO> findAllWherePostIsNull();

    /**
     * Get the "id" postPoll.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostPollDTO> findOne(Long id);

    /**
     * Delete the "id" postPoll.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
