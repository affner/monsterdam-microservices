package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.BookMarkDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.BookMark}.
 */
public interface BookMarkService {
    /**
     * Save a bookMark.
     *
     * @param bookMarkDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<BookMarkDTO> save(BookMarkDTO bookMarkDTO);

    /**
     * Updates a bookMark.
     *
     * @param bookMarkDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<BookMarkDTO> update(BookMarkDTO bookMarkDTO);

    /**
     * Partially updates a bookMark.
     *
     * @param bookMarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<BookMarkDTO> partialUpdate(BookMarkDTO bookMarkDTO);

    /**
     * Get all the bookMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BookMarkDTO> findAll(Pageable pageable);

    /**
     * Get all the bookMarks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BookMarkDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of bookMarks available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of bookMarks available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" bookMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<BookMarkDTO> findOne(Long id);

    /**
     * Delete the "id" bookMark.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the bookMark corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<BookMarkDTO> search(String query, Pageable pageable);
}
