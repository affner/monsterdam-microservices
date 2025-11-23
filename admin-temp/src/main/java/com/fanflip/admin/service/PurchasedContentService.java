package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.PurchasedContentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.PurchasedContent}.
 */
public interface PurchasedContentService {
    /**
     * Save a purchasedContent.
     *
     * @param purchasedContentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PurchasedContentDTO> save(PurchasedContentDTO purchasedContentDTO);

    /**
     * Updates a purchasedContent.
     *
     * @param purchasedContentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PurchasedContentDTO> update(PurchasedContentDTO purchasedContentDTO);

    /**
     * Partially updates a purchasedContent.
     *
     * @param purchasedContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PurchasedContentDTO> partialUpdate(PurchasedContentDTO purchasedContentDTO);

    /**
     * Get all the purchasedContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedContentDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedContents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedContentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of purchasedContents available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of purchasedContents available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" purchasedContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PurchasedContentDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedContent.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the purchasedContent corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PurchasedContentDTO> search(String query, Pageable pageable);
}
