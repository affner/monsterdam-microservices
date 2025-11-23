package com.fanflip.multimedia.service;

import com.fanflip.multimedia.service.dto.SingleDocumentDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.multimedia.domain.SingleDocument}.
 */
public interface SingleDocumentService {
    /**
     * Save a singleDocument.
     *
     * @param singleDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    SingleDocumentDTO save(SingleDocumentDTO singleDocumentDTO);

    /**
     * Updates a singleDocument.
     *
     * @param singleDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    SingleDocumentDTO update(SingleDocumentDTO singleDocumentDTO);

    /**
     * Partially updates a singleDocument.
     *
     * @param singleDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SingleDocumentDTO> partialUpdate(SingleDocumentDTO singleDocumentDTO);

    /**
     * Get all the singleDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SingleDocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" singleDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SingleDocumentDTO> findOne(Long id);

    /**
     * Delete the "id" singleDocument.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
