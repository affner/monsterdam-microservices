package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.CreatorEarningRepository;
import com.fanflip.admin.repository.search.CreatorEarningSearchRepository;
import com.fanflip.admin.service.CreatorEarningService;
import com.fanflip.admin.service.dto.CreatorEarningDTO;
import com.fanflip.admin.service.mapper.CreatorEarningMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.CreatorEarning}.
 */
@Service
@Transactional
public class CreatorEarningServiceImpl implements CreatorEarningService {

    private final Logger log = LoggerFactory.getLogger(CreatorEarningServiceImpl.class);

    private final CreatorEarningRepository creatorEarningRepository;

    private final CreatorEarningMapper creatorEarningMapper;

    private final CreatorEarningSearchRepository creatorEarningSearchRepository;

    public CreatorEarningServiceImpl(
        CreatorEarningRepository creatorEarningRepository,
        CreatorEarningMapper creatorEarningMapper,
        CreatorEarningSearchRepository creatorEarningSearchRepository
    ) {
        this.creatorEarningRepository = creatorEarningRepository;
        this.creatorEarningMapper = creatorEarningMapper;
        this.creatorEarningSearchRepository = creatorEarningSearchRepository;
    }

    @Override
    public Mono<CreatorEarningDTO> save(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to save CreatorEarning : {}", creatorEarningDTO);
        return creatorEarningRepository
            .save(creatorEarningMapper.toEntity(creatorEarningDTO))
            .flatMap(creatorEarningSearchRepository::save)
            .map(creatorEarningMapper::toDto);
    }

    @Override
    public Mono<CreatorEarningDTO> update(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to update CreatorEarning : {}", creatorEarningDTO);
        return creatorEarningRepository
            .save(creatorEarningMapper.toEntity(creatorEarningDTO))
            .flatMap(creatorEarningSearchRepository::save)
            .map(creatorEarningMapper::toDto);
    }

    @Override
    public Mono<CreatorEarningDTO> partialUpdate(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to partially update CreatorEarning : {}", creatorEarningDTO);

        return creatorEarningRepository
            .findById(creatorEarningDTO.getId())
            .map(existingCreatorEarning -> {
                creatorEarningMapper.partialUpdate(existingCreatorEarning, creatorEarningDTO);

                return existingCreatorEarning;
            })
            .flatMap(creatorEarningRepository::save)
            .flatMap(savedCreatorEarning -> {
                creatorEarningSearchRepository.save(savedCreatorEarning);
                return Mono.just(savedCreatorEarning);
            })
            .map(creatorEarningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreatorEarnings");
        return creatorEarningRepository.findAllBy(pageable).map(creatorEarningMapper::toDto);
    }

    /**
     *  Get all the creatorEarnings where MoneyPayout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> findAllWhereMoneyPayoutIsNull() {
        log.debug("Request to get all creatorEarnings where MoneyPayout is null");
        return creatorEarningRepository.findAllWhereMoneyPayoutIsNull().map(creatorEarningMapper::toDto);
    }

    /**
     *  Get all the creatorEarnings where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedContent is null");
        return creatorEarningRepository.findAllWherePurchasedContentIsNull().map(creatorEarningMapper::toDto);
    }

    /**
     *  Get all the creatorEarnings where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedSubscription is null");
        return creatorEarningRepository.findAllWherePurchasedSubscriptionIsNull().map(creatorEarningMapper::toDto);
    }

    /**
     *  Get all the creatorEarnings where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedTip is null");
        return creatorEarningRepository.findAllWherePurchasedTipIsNull().map(creatorEarningMapper::toDto);
    }

    public Mono<Long> countAll() {
        return creatorEarningRepository.count();
    }

    public Mono<Long> searchCount() {
        return creatorEarningSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CreatorEarningDTO> findOne(Long id) {
        log.debug("Request to get CreatorEarning : {}", id);
        return creatorEarningRepository.findById(id).map(creatorEarningMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CreatorEarning : {}", id);
        return creatorEarningRepository.deleteById(id).then(creatorEarningSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CreatorEarningDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CreatorEarnings for query {}", query);
        return creatorEarningSearchRepository.search(query, pageable).map(creatorEarningMapper::toDto);
    }
}
