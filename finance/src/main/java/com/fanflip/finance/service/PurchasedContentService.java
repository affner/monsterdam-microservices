package com.fanflip.finance.service;

import com.fanflip.finance.service.dto.PurchasedContentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.finance.domain.PurchasedContent}.
 */
public interface PurchasedContentService {
    /**
     * Save a purchasedContent.
     *
     * @param purchasedContentDTO the entity to save.
     * @return the persisted entity.
     */
    PurchasedContentDTO save(PurchasedContentDTO purchasedContentDTO);

    /**
     * Updates a purchasedContent.
     *
     * @param purchasedContentDTO the entity to update.
     * @return the persisted entity.
     */
    PurchasedContentDTO update(PurchasedContentDTO purchasedContentDTO);

    /**
     * Partially updates a purchasedContent.
     *
     * @param purchasedContentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PurchasedContentDTO> partialUpdate(PurchasedContentDTO purchasedContentDTO);

    /**
     * Get all the purchasedContents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedContentDTO> findAll(Pageable pageable);

    /**
     * Get all the purchasedContents with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PurchasedContentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" purchasedContent.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PurchasedContentDTO> findOne(Long id);

    /**
     * Delete the "id" purchasedContent.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
