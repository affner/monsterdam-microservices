package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PurchasedSubscriptionRepository;
import com.fanflip.admin.repository.search.PurchasedSubscriptionSearchRepository;
import com.fanflip.admin.service.PurchasedSubscriptionService;
import com.fanflip.admin.service.dto.PurchasedSubscriptionDTO;
import com.fanflip.admin.service.mapper.PurchasedSubscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PurchasedSubscription}.
 */
@Service
@Transactional
public class PurchasedSubscriptionServiceImpl implements PurchasedSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(PurchasedSubscriptionServiceImpl.class);

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    private final PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    private final PurchasedSubscriptionSearchRepository purchasedSubscriptionSearchRepository;

    public PurchasedSubscriptionServiceImpl(
        PurchasedSubscriptionRepository purchasedSubscriptionRepository,
        PurchasedSubscriptionMapper purchasedSubscriptionMapper,
        PurchasedSubscriptionSearchRepository purchasedSubscriptionSearchRepository
    ) {
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
        this.purchasedSubscriptionMapper = purchasedSubscriptionMapper;
        this.purchasedSubscriptionSearchRepository = purchasedSubscriptionSearchRepository;
    }

    @Override
    public Mono<PurchasedSubscriptionDTO> save(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        return purchasedSubscriptionRepository
            .save(purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO))
            .flatMap(purchasedSubscriptionSearchRepository::save)
            .map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    public Mono<PurchasedSubscriptionDTO> update(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to update PurchasedSubscription : {}", purchasedSubscriptionDTO);
        return purchasedSubscriptionRepository
            .save(purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO))
            .flatMap(purchasedSubscriptionSearchRepository::save)
            .map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    public Mono<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to partially update PurchasedSubscription : {}", purchasedSubscriptionDTO);

        return purchasedSubscriptionRepository
            .findById(purchasedSubscriptionDTO.getId())
            .map(existingPurchasedSubscription -> {
                purchasedSubscriptionMapper.partialUpdate(existingPurchasedSubscription, purchasedSubscriptionDTO);

                return existingPurchasedSubscription;
            })
            .flatMap(purchasedSubscriptionRepository::save)
            .flatMap(savedPurchasedSubscription -> {
                purchasedSubscriptionSearchRepository.save(savedPurchasedSubscription);
                return Mono.just(savedPurchasedSubscription);
            })
            .map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedSubscriptions");
        return purchasedSubscriptionRepository.findAllBy(pageable).map(purchasedSubscriptionMapper::toDto);
    }

    public Flux<PurchasedSubscriptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedSubscriptionRepository.findAllWithEagerRelationships(pageable).map(purchasedSubscriptionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return purchasedSubscriptionRepository.count();
    }

    public Mono<Long> searchCount() {
        return purchasedSubscriptionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PurchasedSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get PurchasedSubscription : {}", id);
        return purchasedSubscriptionRepository.findOneWithEagerRelationships(id).map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PurchasedSubscription : {}", id);
        return purchasedSubscriptionRepository.deleteById(id).then(purchasedSubscriptionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedSubscriptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchasedSubscriptions for query {}", query);
        return purchasedSubscriptionSearchRepository.search(query, pageable).map(purchasedSubscriptionMapper::toDto);
    }
}
