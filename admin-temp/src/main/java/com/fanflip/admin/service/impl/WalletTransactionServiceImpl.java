package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.WalletTransactionRepository;
import com.fanflip.admin.repository.search.WalletTransactionSearchRepository;
import com.fanflip.admin.service.WalletTransactionService;
import com.fanflip.admin.service.dto.WalletTransactionDTO;
import com.fanflip.admin.service.mapper.WalletTransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.WalletTransaction}.
 */
@Service
@Transactional
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final Logger log = LoggerFactory.getLogger(WalletTransactionServiceImpl.class);

    private final WalletTransactionRepository walletTransactionRepository;

    private final WalletTransactionMapper walletTransactionMapper;

    private final WalletTransactionSearchRepository walletTransactionSearchRepository;

    public WalletTransactionServiceImpl(
        WalletTransactionRepository walletTransactionRepository,
        WalletTransactionMapper walletTransactionMapper,
        WalletTransactionSearchRepository walletTransactionSearchRepository
    ) {
        this.walletTransactionRepository = walletTransactionRepository;
        this.walletTransactionMapper = walletTransactionMapper;
        this.walletTransactionSearchRepository = walletTransactionSearchRepository;
    }

    @Override
    public Mono<WalletTransactionDTO> save(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to save WalletTransaction : {}", walletTransactionDTO);
        return walletTransactionRepository
            .save(walletTransactionMapper.toEntity(walletTransactionDTO))
            .flatMap(walletTransactionSearchRepository::save)
            .map(walletTransactionMapper::toDto);
    }

    @Override
    public Mono<WalletTransactionDTO> update(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to update WalletTransaction : {}", walletTransactionDTO);
        return walletTransactionRepository
            .save(walletTransactionMapper.toEntity(walletTransactionDTO))
            .flatMap(walletTransactionSearchRepository::save)
            .map(walletTransactionMapper::toDto);
    }

    @Override
    public Mono<WalletTransactionDTO> partialUpdate(WalletTransactionDTO walletTransactionDTO) {
        log.debug("Request to partially update WalletTransaction : {}", walletTransactionDTO);

        return walletTransactionRepository
            .findById(walletTransactionDTO.getId())
            .map(existingWalletTransaction -> {
                walletTransactionMapper.partialUpdate(existingWalletTransaction, walletTransactionDTO);

                return existingWalletTransaction;
            })
            .flatMap(walletTransactionRepository::save)
            .flatMap(savedWalletTransaction -> {
                walletTransactionSearchRepository.save(savedWalletTransaction);
                return Mono.just(savedWalletTransaction);
            })
            .map(walletTransactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<WalletTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WalletTransactions");
        return walletTransactionRepository.findAllBy(pageable).map(walletTransactionMapper::toDto);
    }

    public Flux<WalletTransactionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return walletTransactionRepository.findAllWithEagerRelationships(pageable).map(walletTransactionMapper::toDto);
    }

    /**
     *  Get all the walletTransactions where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WalletTransactionDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedContent is null");
        return walletTransactionRepository.findAllWherePurchasedContentIsNull().map(walletTransactionMapper::toDto);
    }

    /**
     *  Get all the walletTransactions where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WalletTransactionDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedSubscription is null");
        return walletTransactionRepository.findAllWherePurchasedSubscriptionIsNull().map(walletTransactionMapper::toDto);
    }

    /**
     *  Get all the walletTransactions where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<WalletTransactionDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all walletTransactions where PurchasedTip is null");
        return walletTransactionRepository.findAllWherePurchasedTipIsNull().map(walletTransactionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return walletTransactionRepository.count();
    }

    public Mono<Long> searchCount() {
        return walletTransactionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<WalletTransactionDTO> findOne(Long id) {
        log.debug("Request to get WalletTransaction : {}", id);
        return walletTransactionRepository.findOneWithEagerRelationships(id).map(walletTransactionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete WalletTransaction : {}", id);
        return walletTransactionRepository.deleteById(id).then(walletTransactionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<WalletTransactionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WalletTransactions for query {}", query);
        return walletTransactionSearchRepository.search(query, pageable).map(walletTransactionMapper::toDto);
    }
}
