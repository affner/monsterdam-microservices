package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.PurchasedTipRepository;
import com.monsterdam.admin.repository.search.PurchasedTipSearchRepository;
import com.monsterdam.admin.service.PurchasedTipService;
import com.monsterdam.admin.service.dto.PurchasedTipDTO;
import com.monsterdam.admin.service.mapper.PurchasedTipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.PurchasedTip}.
 */
@Service
@Transactional
public class PurchasedTipServiceImpl implements PurchasedTipService {

    private final Logger log = LoggerFactory.getLogger(PurchasedTipServiceImpl.class);

    private final PurchasedTipRepository purchasedTipRepository;

    private final PurchasedTipMapper purchasedTipMapper;

    private final PurchasedTipSearchRepository purchasedTipSearchRepository;

    public PurchasedTipServiceImpl(
        PurchasedTipRepository purchasedTipRepository,
        PurchasedTipMapper purchasedTipMapper,
        PurchasedTipSearchRepository purchasedTipSearchRepository
    ) {
        this.purchasedTipRepository = purchasedTipRepository;
        this.purchasedTipMapper = purchasedTipMapper;
        this.purchasedTipSearchRepository = purchasedTipSearchRepository;
    }

    @Override
    public Mono<PurchasedTipDTO> save(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to save PurchasedTip : {}", purchasedTipDTO);
        return purchasedTipRepository
            .save(purchasedTipMapper.toEntity(purchasedTipDTO))
            .flatMap(purchasedTipSearchRepository::save)
            .map(purchasedTipMapper::toDto);
    }

    @Override
    public Mono<PurchasedTipDTO> update(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to update PurchasedTip : {}", purchasedTipDTO);
        return purchasedTipRepository
            .save(purchasedTipMapper.toEntity(purchasedTipDTO))
            .flatMap(purchasedTipSearchRepository::save)
            .map(purchasedTipMapper::toDto);
    }

    @Override
    public Mono<PurchasedTipDTO> partialUpdate(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to partially update PurchasedTip : {}", purchasedTipDTO);

        return purchasedTipRepository
            .findById(purchasedTipDTO.getId())
            .map(existingPurchasedTip -> {
                purchasedTipMapper.partialUpdate(existingPurchasedTip, purchasedTipDTO);

                return existingPurchasedTip;
            })
            .flatMap(purchasedTipRepository::save)
            .flatMap(savedPurchasedTip -> {
                purchasedTipSearchRepository.save(savedPurchasedTip);
                return Mono.just(savedPurchasedTip);
            })
            .map(purchasedTipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedTipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedTips");
        return purchasedTipRepository.findAllBy(pageable).map(purchasedTipMapper::toDto);
    }

    public Flux<PurchasedTipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedTipRepository.findAllWithEagerRelationships(pageable).map(purchasedTipMapper::toDto);
    }

    public Mono<Long> countAll() {
        return purchasedTipRepository.count();
    }

    public Mono<Long> searchCount() {
        return purchasedTipSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PurchasedTipDTO> findOne(Long id) {
        log.debug("Request to get PurchasedTip : {}", id);
        return purchasedTipRepository.findOneWithEagerRelationships(id).map(purchasedTipMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PurchasedTip : {}", id);
        return purchasedTipRepository.deleteById(id).then(purchasedTipSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedTipDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchasedTips for query {}", query);
        return purchasedTipSearchRepository.search(query, pageable).map(purchasedTipMapper::toDto);
    }
}
