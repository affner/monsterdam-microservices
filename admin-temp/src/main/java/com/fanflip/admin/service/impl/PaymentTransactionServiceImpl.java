package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PaymentTransactionRepository;
import com.fanflip.admin.repository.search.PaymentTransactionSearchRepository;
import com.fanflip.admin.service.PaymentTransactionService;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
import com.fanflip.admin.service.mapper.PaymentTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PaymentTransaction}.
 */
@Service
@Transactional
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final Logger log = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final PaymentTransactionMapper paymentTransactionMapper;

    private final PaymentTransactionSearchRepository paymentTransactionSearchRepository;

    public PaymentTransactionServiceImpl(
        PaymentTransactionRepository paymentTransactionRepository,
        PaymentTransactionMapper paymentTransactionMapper,
        PaymentTransactionSearchRepository paymentTransactionSearchRepository
    ) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
        this.paymentTransactionSearchRepository = paymentTransactionSearchRepository;
    }

    @Override
    public Mono<PaymentTransactionDTO> save(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to save PaymentTransaction : {}", paymentTransactionDTO);
        return paymentTransactionRepository
            .save(paymentTransactionMapper.toEntity(paymentTransactionDTO))
            .flatMap(paymentTransactionSearchRepository::save)
            .map(paymentTransactionMapper::toDto);
    }

    @Override
    public Mono<PaymentTransactionDTO> update(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to update PaymentTransaction : {}", paymentTransactionDTO);
        return paymentTransactionRepository
            .save(paymentTransactionMapper.toEntity(paymentTransactionDTO))
            .flatMap(paymentTransactionSearchRepository::save)
            .map(paymentTransactionMapper::toDto);
    }

    @Override
    public Mono<PaymentTransactionDTO> partialUpdate(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to partially update PaymentTransaction : {}", paymentTransactionDTO);

        return paymentTransactionRepository
            .findById(paymentTransactionDTO.getId())
            .map(existingPaymentTransaction -> {
                paymentTransactionMapper.partialUpdate(existingPaymentTransaction, paymentTransactionDTO);

                return existingPaymentTransaction;
            })
            .flatMap(paymentTransactionRepository::save)
            .flatMap(savedPaymentTransaction -> {
                paymentTransactionSearchRepository.save(savedPaymentTransaction);
                return Mono.just(savedPaymentTransaction);
            })
            .map(paymentTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTransactions");
        return paymentTransactionRepository.findAllBy(pageable).map(paymentTransactionMapper::toDto);
    }

    public Flux<PaymentTransactionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return paymentTransactionRepository.findAllWithEagerRelationships(pageable).map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where AccountingRecord is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAllWhereAccountingRecordIsNull() {
        log.debug("Request to get all paymentTransactions where AccountingRecord is null");
        return paymentTransactionRepository.findAllWhereAccountingRecordIsNull().map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedContent is null");
        return paymentTransactionRepository.findAllWherePurchasedContentIsNull().map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedSubscription is null");
        return paymentTransactionRepository.findAllWherePurchasedSubscriptionIsNull().map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where WalletTransaction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAllWhereWalletTransactionIsNull() {
        log.debug("Request to get all paymentTransactions where WalletTransaction is null");
        return paymentTransactionRepository.findAllWhereWalletTransactionIsNull().map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedTip is null");
        return paymentTransactionRepository.findAllWherePurchasedTipIsNull().map(paymentTransactionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentTransactionRepository.count();
    }

    public Mono<Long> searchCount() {
        return paymentTransactionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentTransactionDTO> findOne(Long id) {
        log.debug("Request to get PaymentTransaction : {}", id);
        return paymentTransactionRepository.findOneWithEagerRelationships(id).map(paymentTransactionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PaymentTransaction : {}", id);
        return paymentTransactionRepository.deleteById(id).then(paymentTransactionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentTransactions for query {}", query);
        return paymentTransactionSearchRepository.search(query, pageable).map(paymentTransactionMapper::toDto);
    }
}
