package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.AccountingRecordDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.AccountingRecord}.
 */
public interface AccountingRecordService {
    /**
     * Save a accountingRecord.
     *
     * @param accountingRecordDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AccountingRecordDTO> save(AccountingRecordDTO accountingRecordDTO);

    /**
     * Updates a accountingRecord.
     *
     * @param accountingRecordDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AccountingRecordDTO> update(AccountingRecordDTO accountingRecordDTO);

    /**
     * Partially updates a accountingRecord.
     *
     * @param accountingRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AccountingRecordDTO> partialUpdate(AccountingRecordDTO accountingRecordDTO);

    /**
     * Get all the accountingRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AccountingRecordDTO> findAll(Pageable pageable);

    /**
     * Returns the number of accountingRecords available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of accountingRecords available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" accountingRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AccountingRecordDTO> findOne(Long id);

    /**
     * Delete the "id" accountingRecord.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the accountingRecord corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AccountingRecordDTO> search(String query, Pageable pageable);
}
