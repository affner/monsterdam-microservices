package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.BudgetRepository;
import com.fanflip.admin.repository.search.BudgetSearchRepository;
import com.fanflip.admin.service.BudgetService;
import com.fanflip.admin.service.dto.BudgetDTO;
import com.fanflip.admin.service.mapper.BudgetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Budget}.
 */
@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private final Logger log = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    private final BudgetSearchRepository budgetSearchRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository, BudgetMapper budgetMapper, BudgetSearchRepository budgetSearchRepository) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
        this.budgetSearchRepository = budgetSearchRepository;
    }

    @Override
    public Mono<BudgetDTO> save(BudgetDTO budgetDTO) {
        log.debug("Request to save Budget : {}", budgetDTO);
        return budgetRepository.save(budgetMapper.toEntity(budgetDTO)).flatMap(budgetSearchRepository::save).map(budgetMapper::toDto);
    }

    @Override
    public Mono<BudgetDTO> update(BudgetDTO budgetDTO) {
        log.debug("Request to update Budget : {}", budgetDTO);
        return budgetRepository.save(budgetMapper.toEntity(budgetDTO)).flatMap(budgetSearchRepository::save).map(budgetMapper::toDto);
    }

    @Override
    public Mono<BudgetDTO> partialUpdate(BudgetDTO budgetDTO) {
        log.debug("Request to partially update Budget : {}", budgetDTO);

        return budgetRepository
            .findById(budgetDTO.getId())
            .map(existingBudget -> {
                budgetMapper.partialUpdate(existingBudget, budgetDTO);

                return existingBudget;
            })
            .flatMap(budgetRepository::save)
            .flatMap(savedBudget -> {
                budgetSearchRepository.save(savedBudget);
                return Mono.just(savedBudget);
            })
            .map(budgetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BudgetDTO> findAll() {
        log.debug("Request to get all Budgets");
        return budgetRepository.findAll().map(budgetMapper::toDto);
    }

    public Mono<Long> countAll() {
        return budgetRepository.count();
    }

    public Mono<Long> searchCount() {
        return budgetSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BudgetDTO> findOne(Long id) {
        log.debug("Request to get Budget : {}", id);
        return budgetRepository.findById(id).map(budgetMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Budget : {}", id);
        return budgetRepository.deleteById(id).then(budgetSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BudgetDTO> search(String query) {
        log.debug("Request to search Budgets for query {}", query);
        try {
            return budgetSearchRepository.search(query).map(budgetMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
