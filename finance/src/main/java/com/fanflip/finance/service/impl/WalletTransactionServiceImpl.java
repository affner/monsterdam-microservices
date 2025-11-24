package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.WalletTransaction;
import com.monsterdam.finance.repository.WalletTransactionRepository;
import com.monsterdam.finance.service.WalletTransactionService;
import com.monsterdam.finance.service.dto.WalletTransactionDTO;
import com.monsterdam.finance.service.mapper.WalletTransactionMapper;
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
 * Service Implementation for managing {@link com.monsterdam.finance.domain.WalletTransaction}.
 */
@Service
@Transactional
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final Logger log = LoggerFactory.getLogger(WalletTransactionServiceImpl.class);

    private final WalletTransactionRepository walletTransactionRepository;

    private final WalletTransactionMapper walletTransactionMapper;

    public WalletTransactionServiceImpl(
        WalletTransactionRepository walletTransactionRepository,
        WalletTransactionMapper walletTransactionMapper
    ) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
    }

    @Override
    public WalletTransactionDTO save(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to save WalletTransaction : {}", walletTransactionDTO);
        WalletTransaction walletTransaction = walletTransactionMapper.toEntity(walletTransactionDTO);
        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    public WalletTransactionDTO update(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to update WalletTransaction : {}", walletTransactionDTO);
        WalletTransaction walletTransaction = walletTransactionMapper.toEntity(walletTransactionDTO);
        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return walletTransactionMapper.toDto(walletTransaction);
    }

    @Override
    public Optional<WalletTransactionDTO> partialUpdate(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to partially update WalletTransaction : {}", walletTransactionDTO);

        return walletTransactionRepository
            .findById(walletTransactionDTO.getId())
            .map(existingWalletTransaction -> {
                walletTransactionMapper.partialUpdate(existingWalletTransaction, walletTransactionDTO);

                return existingWalletTransaction;
            })
            .map(walletTransactionRepository::save)
            .map(walletTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WalletTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WalletTransactions");
        return walletTransactionRepository.findAll(pageable).map(walletTransactionMapper::toDto);
    }

    public Page<WalletTransactionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return walletTransactionRepository.findAllWithEagerRelationships(pageable).map(walletTransactionMapper::toDto);
    }

    /**
     *  Get all the walletTransactions where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WalletTransactionDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedTip is null");
        return StreamSupport
            .stream(walletTransactionRepository.findAll().spliterator(), false)
            .filter(walletTransaction -> walletTransaction.getPurchasedTip() == null)
            .map(walletTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the walletTransactions where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WalletTransactionDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedContent is null");
        return StreamSupport
            .stream(walletTransactionRepository.findAll().spliterator(), false)
            .filter(walletTransaction -> walletTransaction.getPurchasedContent() == null)
            .map(walletTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the walletTransactions where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WalletTransactionDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedSubscription is null");
        return StreamSupport
            .stream(walletTransactionRepository.findAll().spliterator(), false)
            .filter(walletTransaction -> walletTransaction.getPurchasedSubscription() == null)
            .map(walletTransactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WalletTransactionDTO> findOne(Long id) {
        log.debug("Request to get WalletTransaction : {}", id);
        return walletTransactionRepository.findOneWithEagerRelationships(id).map(walletTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WalletTransaction : {}", id);
        walletTransactionRepository.deleteById(id);
    }
}
