package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.PaymentTransaction;
import com.monsterdam.finance.repository.PaymentTransactionRepository;
import com.monsterdam.finance.service.PaymentTransactionService;
import com.monsterdam.finance.service.dto.PaymentTransactionDTO;
import com.monsterdam.finance.service.mapper.PaymentTransactionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.PaymentTransaction}.
 */
@Service
@Transactional
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final Logger log = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final PaymentTransactionMapper paymentTransactionMapper;

    public PaymentTransactionServiceImpl(
        PaymentTransactionRepository paymentTransactionRepository,
        PaymentTransactionMapper paymentTransactionMapper
    ) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
    }

    @Override
    public PaymentTransactionDTO save(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to save PaymentTransaction : {}", paymentTransactionDTO);
        PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionDTO);
        paymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        return paymentTransactionMapper.toDto(paymentTransaction);
    }

    @Override
    public PaymentTransactionDTO update(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to update PaymentTransaction : {}", paymentTransactionDTO);
        PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionDTO);
        paymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        return paymentTransactionMapper.toDto(paymentTransaction);
    }

    @Override
    public Optional<PaymentTransactionDTO> partialUpdate(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to partially update PaymentTransaction : {}", paymentTransactionDTO);

        return paymentTransactionRepository
            .findById(paymentTransactionDTO.getId())
            .map(existingPaymentTransaction -> {
                paymentTransactionMapper.partialUpdate(existingPaymentTransaction, paymentTransactionDTO);

                return existingPaymentTransaction;
            })
            .map(paymentTransactionRepository::save)
            .map(paymentTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTransactions");
        return paymentTransactionRepository.findAll(pageable).map(paymentTransactionMapper::toDto);
    }

    /**
     *  Get all the paymentTransactions where WalletTransaction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTransactionDTO> findAllWhereWalletTransactionIsNull() {
        log.debug("Request to get all paymentTransactions where WalletTransaction is null");
        return StreamSupport
            .stream(paymentTransactionRepository.findAll().spliterator(), false)
            .filter(paymentTransaction -> paymentTransaction.getWalletTransaction() == null)
            .map(paymentTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the paymentTransactions where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTransactionDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedTip is null");
        return StreamSupport
            .stream(paymentTransactionRepository.findAll().spliterator(), false)
            .filter(paymentTransaction -> paymentTransaction.getPurchasedTip() == null)
            .map(paymentTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the paymentTransactions where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTransactionDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedContent is null");
        return StreamSupport
            .stream(paymentTransactionRepository.findAll().spliterator(), false)
            .filter(paymentTransaction -> paymentTransaction.getPurchasedContent() == null)
            .map(paymentTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the paymentTransactions where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentTransactionDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all paymentTransactions where PurchasedSubscription is null");
        return StreamSupport
            .stream(paymentTransactionRepository.findAll().spliterator(), false)
            .filter(paymentTransaction -> paymentTransaction.getPurchasedSubscription() == null)
            .map(paymentTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentTransactionDTO> findOne(Long id) {
        log.debug("Request to get PaymentTransaction : {}", id);
        return paymentTransactionRepository.findById(id).map(paymentTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentTransaction : {}", id);
        paymentTransactionRepository.deleteById(id);
    }
}
