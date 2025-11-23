package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.SingleDocumentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.SingleDocument}.
 */
public interface SingleDocumentService {
    /**
     * Save a singleDocument.
     *
     * @param singleDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<SingleDocumentDTO> save(SingleDocumentDTO singleDocumentDTO);

    /**
     * Updates a singleDocument.
     *
     * @param singleDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<SingleDocumentDTO> update(SingleDocumentDTO singleDocumentDTO);

    /**
     * Partially updates a singleDocument.
     *
     * @param singleDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<SingleDocumentDTO> partialUpdate(SingleDocumentDTO singleDocumentDTO);

    /**
     * Get all the singleDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleDocumentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of singleDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of singleDocuments available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" singleDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<SingleDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" singleDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the singleDocument corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<SingleDocumentDTO> search(String query, Pageable pageable);
}
