package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.AccountingRecordRepository;
import com.monsterdam.admin.repository.search.AccountingRecordSearchRepository;
import com.monsterdam.admin.service.AccountingRecordService;
import com.monsterdam.admin.service.dto.AccountingRecordDTO;
import com.monsterdam.admin.service.mapper.AccountingRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.AccountingRecord}.
 */
@Service
@Transactional
public class AccountingRecordServiceImpl implements AccountingRecordService {

    private final Logger log = LoggerFactory.getLogger(AccountingRecordServiceImpl.class);

    private final AccountingRecordRepository accountingRecordRepository;

    private final AccountingRecordMapper accountingRecordMapper;

    private final AccountingRecordSearchRepository accountingRecordSearchRepository;

    public AccountingRecordServiceImpl(
        AccountingRecordRepository accountingRecordRepository,
        AccountingRecordMapper accountingRecordMapper,
        AccountingRecordSearchRepository accountingRecordSearchRepository
    ) {
        this.accountingRecordRepository = accountingRecordRepository;
        this.accountingRecordMapper = accountingRecordMapper;
        this.accountingRecordSearchRepository = accountingRecordSearchRepository;
    }

    @Override
    public Mono<AccountingRecordDTO> save(AccountingRecordDTO accountingRecordDTO) {
        log.debug("Request to save AccountingRecord : {}", accountingRecordDTO);
        return accountingRecordRepository
            .save(accountingRecordMapper.toEntity(accountingRecordDTO))
            .flatMap(accountingRecordSearchRepository::save)
            .map(accountingRecordMapper::toDto);
    }

    @Override
    public Mono<AccountingRecordDTO> update(AccountingRecordDTO accountingRecordDTO) {
        log.debug("Request to update AccountingRecord : {}", accountingRecordDTO);
        return accountingRecordRepository
            .save(accountingRecordMapper.toEntity(accountingRecordDTO))
            .flatMap(accountingRecordSearchRepository::save)
            .map(accountingRecordMapper::toDto);
    }

    @Override
    public Mono<AccountingRecordDTO> partialUpdate(AccountingRecordDTO accountingRecordDTO) {
        log.debug("Request to partially update AccountingRecord : {}", accountingRecordDTO);

        return accountingRecordRepository
            .findById(accountingRecordDTO.getId())
            .map(existingAccountingRecord -> {
                accountingRecordMapper.partialUpdate(existingAccountingRecord, accountingRecordDTO);

                return existingAccountingRecord;
            })
            .flatMap(accountingRecordRepository::save)
            .flatMap(savedAccountingRecord -> {
                accountingRecordSearchRepository.save(savedAccountingRecord);
                return Mono.just(savedAccountingRecord);
            })
            .map(accountingRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AccountingRecordDTO> findAll() {
        log.debug("Request to get all AccountingRecords");
        return accountingRecordRepository.findAll().map(accountingRecordMapper::toDto);
    }

    public Mono<Long> countAll() {
        return accountingRecordRepository.count();
    }

    public Mono<Long> searchCount() {
        return accountingRecordSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AccountingRecordDTO> findOne(Long id) {
        log.debug("Request to get AccountingRecord : {}", id);
        return accountingRecordRepository.findById(id).map(accountingRecordMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AccountingRecord : {}", id);
        return accountingRecordRepository.deleteById(id).then(accountingRecordSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AccountingRecordDTO> search(String query) {
        log.debug("Request to search AccountingRecords for query {}", query);
        try {
            return accountingRecordSearchRepository.search(query).map(accountingRecordMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
