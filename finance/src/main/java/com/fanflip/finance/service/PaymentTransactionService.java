package com.monsterdam.finance.service;

import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.finance.domain.PaymentTransaction}.
 */
public interface PaymentTransactionService {
    /**
     * Save a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentTransactionDTO save(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Updates a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentTransactionDTO update(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Partially updates a paymentTransaction.
     *
     * @param paymentTransactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentTransactionDTO> partialUpdate(PaymentTransactionDTO paymentTransactionDTO);

    /**
     * Get all the paymentTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PaymentTransactionDTO> findAll(Pageable pageable);

    /**
     * Get all the PaymentTransactionDTO where WalletTransaction is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaymentTransactionDTO> findAllWhereWalletTransactionIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaymentTransactionDTO> findAllWherePurchasedTipIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedContent is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaymentTransactionDTO> findAllWherePurchasedContentIsNull();
    /**
     * Get all the PaymentTransactionDTO where PurchasedSubscription is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PaymentTransactionDTO> findAllWherePurchasedSubscriptionIsNull();

    /**
     * Get the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" paymentTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
