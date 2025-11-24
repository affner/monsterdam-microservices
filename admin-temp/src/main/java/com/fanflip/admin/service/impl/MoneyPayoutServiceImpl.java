package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.MoneyPayoutRepository;
import com.monsterdam.admin.repository.search.MoneyPayoutSearchRepository;
import com.monsterdam.admin.service.MoneyPayoutService;
import com.monsterdam.admin.service.dto.MoneyPayoutDTO;
import com.monsterdam.admin.service.mapper.MoneyPayoutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.MoneyPayout}.
 */
@Service
@Transactional
public class MoneyPayoutServiceImpl implements MoneyPayoutService {

    private final Logger log = LoggerFactory.getLogger(MoneyPayoutServiceImpl.class);

    private final MoneyPayoutRepository moneyPayoutRepository;

    private final MoneyPayoutMapper moneyPayoutMapper;

    private final MoneyPayoutSearchRepository moneyPayoutSearchRepository;

    public MoneyPayoutServiceImpl(
        MoneyPayoutRepository moneyPayoutRepository,
        MoneyPayoutMapper moneyPayoutMapper,
        MoneyPayoutSearchRepository moneyPayoutSearchRepository
    ) {
        this.moneyPayoutRepository = moneyPayoutRepository;
        this.moneyPayoutMapper = moneyPayoutMapper;
        this.moneyPayoutSearchRepository = moneyPayoutSearchRepository;
    }

    @Override
    public Mono<MoneyPayoutDTO> save(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to save MoneyPayout : {}", moneyPayoutDTO);
        return moneyPayoutRepository
            .save(moneyPayoutMapper.toEntity(moneyPayoutDTO))
            .flatMap(moneyPayoutSearchRepository::save)
            .map(moneyPayoutMapper::toDto);
    }

    @Override
    public Mono<MoneyPayoutDTO> update(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to update MoneyPayout : {}", moneyPayoutDTO);
        return moneyPayoutRepository
            .save(moneyPayoutMapper.toEntity(moneyPayoutDTO))
            .flatMap(moneyPayoutSearchRepository::save)
            .map(moneyPayoutMapper::toDto);
    }

    @Override
    public Mono<MoneyPayoutDTO> partialUpdate(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to partially update MoneyPayout : {}", moneyPayoutDTO);

        return moneyPayoutRepository
            .findById(moneyPayoutDTO.getId())
            .map(existingMoneyPayout -> {
                moneyPayoutMapper.partialUpdate(existingMoneyPayout, moneyPayoutDTO);

                return existingMoneyPayout;
            })
            .flatMap(moneyPayoutRepository::save)
            .flatMap(savedMoneyPayout -> {
                moneyPayoutSearchRepository.save(savedMoneyPayout);
                return Mono.just(savedMoneyPayout);
            })
            .map(moneyPayoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MoneyPayoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoneyPayouts");
        return moneyPayoutRepository.findAllBy(pageable).map(moneyPayoutMapper::toDto);
    }

    public Flux<MoneyPayoutDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moneyPayoutRepository.findAllWithEagerRelationships(pageable).map(moneyPayoutMapper::toDto);
    }

    public Mono<Long> countAll() {
        return moneyPayoutRepository.count();
    }

    public Mono<Long> searchCount() {
        return moneyPayoutSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<MoneyPayoutDTO> findOne(Long id) {
        log.debug("Request to get MoneyPayout : {}", id);
        return moneyPayoutRepository.findOneWithEagerRelationships(id).map(moneyPayoutMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete MoneyPayout : {}", id);
        return moneyPayoutRepository.deleteById(id).then(moneyPayoutSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MoneyPayoutDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of MoneyPayouts for query {}", query);
        return moneyPayoutSearchRepository.search(query, pageable).map(moneyPayoutMapper::toDto);
    }
}
