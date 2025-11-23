package com.fanflip.finance.service;

import com.fanflip.finance.service.dto.WalletTransactionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.fanflip.finance.domain.WalletTransaction}.
 */
public interface WalletTransactionService {
    /**
     * Save a walletTransaction.
     *
     * @param walletTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    WalletTransactionDTO save(WalletTransactionDTO walletTransactionDTO);

    /**
     * Updates a walletTransaction.
     *
     * @param walletTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    WalletTransactionDTO update(WalletTransactionDTO walletTransactionDTO);

    /**
     * Partially updates a walletTransaction.
     *
     * @param walletTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WalletTransactionDTO> partialUpdate(WalletTransactionDTO walletTransactionDTO);

    /**
     * Get all the walletTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WalletTransactionDTO> findAll(Pageable pageable);

    /**
     * Get all the WalletTransactionDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WalletTransactionDTO> findAllWherePurchasedTipIsNull();
    /**
     * Get all the WalletTransactionDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WalletTransactionDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the WalletTransactionDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<WalletTransactionDTO> findAllWherePurchasedSubscriptionIsNull();

    /**
     * Get all the walletTransactions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WalletTransactionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" walletTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WalletTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" walletTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
