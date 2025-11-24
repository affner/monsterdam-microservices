package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.SubscriptionBundleRepository;
import com.monsterdam.admin.repository.search.SubscriptionBundleSearchRepository;
import com.monsterdam.admin.service.SubscriptionBundleService;
import com.monsterdam.admin.service.dto.SubscriptionBundleDTO;
import com.monsterdam.admin.service.mapper.SubscriptionBundleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.SubscriptionBundle}.
 */
@Service
@Transactional
public class SubscriptionBundleServiceImpl implements SubscriptionBundleService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleServiceImpl.class);

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    private final SubscriptionBundleMapper subscriptionBundleMapper;

    private final SubscriptionBundleSearchRepository subscriptionBundleSearchRepository;

    public SubscriptionBundleServiceImpl(
        SubscriptionBundleRepository subscriptionBundleRepository,
        SubscriptionBundleMapper subscriptionBundleMapper,
        SubscriptionBundleSearchRepository subscriptionBundleSearchRepository
    ) {
        this.subscriptionBundleRepository = subscriptionBundleRepository;
        this.subscriptionBundleMapper = subscriptionBundleMapper;
        this.subscriptionBundleSearchRepository = subscriptionBundleSearchRepository;
    }

    @Override
    public Mono<SubscriptionBundleDTO> save(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        return subscriptionBundleRepository
            .save(subscriptionBundleMapper.toEntity(subscriptionBundleDTO))
            .flatMap(subscriptionBundleSearchRepository::save)
            .map(subscriptionBundleMapper::toDto);
    }

    @Override
    public Mono<SubscriptionBundleDTO> update(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to update SubscriptionBundle : {}", subscriptionBundleDTO);
        return subscriptionBundleRepository
            .save(subscriptionBundleMapper.toEntity(subscriptionBundleDTO))
            .flatMap(subscriptionBundleSearchRepository::save)
            .map(subscriptionBundleMapper::toDto);
    }

    @Override
    public Mono<SubscriptionBundleDTO> partialUpdate(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to partially update SubscriptionBundle : {}", subscriptionBundleDTO);

        return subscriptionBundleRepository
            .findById(subscriptionBundleDTO.getId())
            .map(existingSubscriptionBundle -> {
                subscriptionBundleMapper.partialUpdate(existingSubscriptionBundle, subscriptionBundleDTO);

                return existingSubscriptionBundle;
            })
            .flatMap(subscriptionBundleRepository::save)
            .flatMap(savedSubscriptionBundle -> {
                subscriptionBundleSearchRepository.save(savedSubscriptionBundle);
                return Mono.just(savedSubscriptionBundle);
            })
            .map(subscriptionBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SubscriptionBundleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionBundles");
        return subscriptionBundleRepository.findAllBy(pageable).map(subscriptionBundleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return subscriptionBundleRepository.count();
    }

    public Mono<Long> searchCount() {
        return subscriptionBundleSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SubscriptionBundleDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionBundle : {}", id);
        return subscriptionBundleRepository.findById(id).map(subscriptionBundleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SubscriptionBundle : {}", id);
        return subscriptionBundleRepository.deleteById(id).then(subscriptionBundleSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SubscriptionBundleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SubscriptionBundles for query {}", query);
        return subscriptionBundleSearchRepository.search(query, pageable).map(subscriptionBundleMapper::toDto);
    }
}
