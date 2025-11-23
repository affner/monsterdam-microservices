package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.UserLiteDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.UserLite}.
 */
public interface UserLiteService {
    /**
     * Save a userLite.
     *
     * @param userLiteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<UserLiteDTO> save(UserLiteDTO userLiteDTO);

    /**
     * Updates a userLite.
     *
     * @param userLiteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<UserLiteDTO> update(UserLiteDTO userLiteDTO);

    /**
     * Partially updates a userLite.
     *
     * @param userLiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<UserLiteDTO> partialUpdate(UserLiteDTO userLiteDTO);

    /**
     * Get all the userLites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserLiteDTO> findAll(Pageable pageable);

    /**
     * Get all the UserLiteDTO where UserProfile is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<UserLiteDTO> findAllWhereUserProfileIsNull();

    /**
     * Returns the number of userLites available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of userLites available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" userLite.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<UserLiteDTO> findOne(Long id);

    /**
     * Delete the "id" userLite.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the userLite corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<UserLiteDTO> search(String query, Pageable pageable);
}
