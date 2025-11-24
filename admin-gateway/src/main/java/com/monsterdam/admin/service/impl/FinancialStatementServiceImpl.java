package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.FinancialStatementRepository;
import com.monsterdam.admin.repository.search.FinancialStatementSearchRepository;
import com.monsterdam.admin.service.FinancialStatementService;
import com.monsterdam.admin.service.dto.FinancialStatementDTO;
import com.monsterdam.admin.service.mapper.FinancialStatementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.FinancialStatement}.
 */
@Service
@Transactional
public class FinancialStatementServiceImpl implements FinancialStatementService {

    private final Logger log = LoggerFactory.getLogger(FinancialStatementServiceImpl.class);

    private final FinancialStatementRepository financialStatementRepository;

    private final FinancialStatementMapper financialStatementMapper;

    private final FinancialStatementSearchRepository financialStatementSearchRepository;

    public FinancialStatementServiceImpl(
        FinancialStatementRepository financialStatementRepository,
        FinancialStatementMapper financialStatementMapper,
        FinancialStatementSearchRepository financialStatementSearchRepository
    ) {
        this.financialStatementRepository = financialStatementRepository;
        this.financialStatementMapper = financialStatementMapper;
        this.financialStatementSearchRepository = financialStatementSearchRepository;
    }

    @Override
    public Mono<FinancialStatementDTO> save(FinancialStatementDTO financialStatementDTO) {
        log.debug("Request to save FinancialStatement : {}", financialStatementDTO);
        return financialStatementRepository
            .save(financialStatementMapper.toEntity(financialStatementDTO))
            .flatMap(financialStatementSearchRepository::save)
            .map(financialStatementMapper::toDto);
    }

    @Override
    public Mono<FinancialStatementDTO> update(FinancialStatementDTO financialStatementDTO) {
        log.debug("Request to update FinancialStatement : {}", financialStatementDTO);
        return financialStatementRepository
            .save(financialStatementMapper.toEntity(financialStatementDTO))
            .flatMap(financialStatementSearchRepository::save)
            .map(financialStatementMapper::toDto);
    }

    @Override
    public Mono<FinancialStatementDTO> partialUpdate(FinancialStatementDTO financialStatementDTO) {
        log.debug("Request to partially update FinancialStatement : {}", financialStatementDTO);

        return financialStatementRepository
            .findById(financialStatementDTO.getId())
            .map(existingFinancialStatement -> {
                financialStatementMapper.partialUpdate(existingFinancialStatement, financialStatementDTO);

                return existingFinancialStatement;
            })
            .flatMap(financialStatementRepository::save)
            .flatMap(savedFinancialStatement -> {
                financialStatementSearchRepository.save(savedFinancialStatement);
                return Mono.just(savedFinancialStatement);
            })
            .map(financialStatementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FinancialStatementDTO> findAll() {
        log.debug("Request to get all FinancialStatements");
        return financialStatementRepository.findAll().map(financialStatementMapper::toDto);
    }

    public Flux<FinancialStatementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return financialStatementRepository.findAllWithEagerRelationships(pageable).map(financialStatementMapper::toDto);
    }

    public Mono<Long> countAll() {
        return financialStatementRepository.count();
    }

    public Mono<Long> searchCount() {
        return financialStatementSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FinancialStatementDTO> findOne(Long id) {
        log.debug("Request to get FinancialStatement : {}", id);
        return financialStatementRepository.findOneWithEagerRelationships(id).map(financialStatementMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FinancialStatement : {}", id);
        return financialStatementRepository.deleteById(id).then(financialStatementSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FinancialStatementDTO> search(String query) {
        log.debug("Request to search FinancialStatements for query {}", query);
        try {
            return financialStatementSearchRepository.search(query).map(financialStatementMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
