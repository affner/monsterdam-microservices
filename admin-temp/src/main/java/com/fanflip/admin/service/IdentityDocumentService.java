package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.IdentityDocumentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.IdentityDocument}.
 */
public interface IdentityDocumentService {
    /**
     * Save a identityDocument.
     *
     * @param identityDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentDTO> save(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Updates a identityDocument.
     *
     * @param identityDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentDTO> update(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Partially updates a identityDocument.
     *
     * @param identityDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<IdentityDocumentDTO> partialUpdate(IdentityDocumentDTO identityDocumentDTO);

    /**
     * Get all the identityDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<IdentityDocumentDTO> findAll(Pageable pageable);

    /**
     * Returns the number of identityDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of identityDocuments available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" identityDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<IdentityDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" identityDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the identityDocument corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<IdentityDocumentDTO> search(String query, Pageable pageable);
}
