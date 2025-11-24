package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.WalletTransactionDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.WalletTransaction}.
 */
public interface WalletTransactionService {
    /**
     * Save a walletTransaction.
     *
     * @param walletTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<WalletTransactionDTO> save(WalletTransactionDTO walletTransactionDTO);

    /**
     * Updates a walletTransaction.
     *
     * @param walletTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<WalletTransactionDTO> update(WalletTransactionDTO walletTransactionDTO);

    /**
     * Partially updates a walletTransaction.
     *
     * @param walletTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<WalletTransactionDTO> partialUpdate(WalletTransactionDTO walletTransactionDTO);

    /**
     * Get all the walletTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WalletTransactionDTO> findAll(Pageable pageable);

    /**
     * Get all the WalletTransactionDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<WalletTransactionDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the WalletTransactionDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<WalletTransactionDTO> findAllWherePurchasedSubscriptionIsNull();
    /**
     * Get all the WalletTransactionDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<WalletTransactionDTO> findAllWherePurchasedTipIsNull();

    /**
     * Get all the walletTransactions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WalletTransactionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of walletTransactions available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of walletTransactions available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" walletTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<WalletTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" walletTransaction.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the walletTransaction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<WalletTransactionDTO> search(String query, Pageable pageable);
}
