package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.PaymentTransactionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.PaymentTransaction}.
 */
public interface PaymentTransactionService {
    /**
     * Save a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PaymentTransactionDTO> save(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Updates a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PaymentTransactionDTO> update(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Partially updates a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PaymentTransactionDTO> partialUpdate(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Get all the paymentTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentTransactionDTO> findAll(Pageable pageable);

    /**
     * Get all the PaymentTransactionDTO where AccountingRecord is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentTransactionDTO> findAllWhereAccountingRecordIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentTransactionDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentTransactionDTO> findAllWherePurchasedSubscriptionIsNull();
    /**
     * Get all the PaymentTransactionDTO where WalletTransaction is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentTransactionDTO> findAllWhereWalletTransactionIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<PaymentTransactionDTO> findAllWherePurchasedTipIsNull();

    /**
     * Get all the paymentTransactions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentTransactionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of paymentTransactions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of paymentTransactions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PaymentTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the paymentTransaction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PaymentTransactionDTO> search(String query, Pageable pageable);
}
