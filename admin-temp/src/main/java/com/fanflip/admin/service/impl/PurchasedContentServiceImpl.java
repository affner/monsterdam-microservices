package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PurchasedContentRepository;
import com.fanflip.admin.repository.search.PurchasedContentSearchRepository;
import com.fanflip.admin.service.PurchasedContentService;
import com.fanflip.admin.service.dto.PurchasedContentDTO;
import com.fanflip.admin.service.mapper.PurchasedContentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PurchasedContent}.
 */
@Service
@Transactional
public class PurchasedContentServiceImpl implements PurchasedContentService {

    private final Logger log = LoggerFactory.getLogger(PurchasedContentServiceImpl.class);

    private final PurchasedContentRepository purchasedContentRepository;

    private final PurchasedContentMapper purchasedContentMapper;

    private final PurchasedContentSearchRepository purchasedContentSearchRepository;

    public PurchasedContentServiceImpl(
        PurchasedContentRepository purchasedContentRepository,
        PurchasedContentMapper purchasedContentMapper,
        PurchasedContentSearchRepository purchasedContentSearchRepository
    ) {
        this.purchasedContentRepository = purchasedContentRepository;
        this.purchasedContentMapper = purchasedContentMapper;
        this.purchasedContentSearchRepository = purchasedContentSearchRepository;
    }

    @Override
    public Mono<PurchasedContentDTO> save(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to save PurchasedContent : {}", purchasedContentDTO);
        return purchasedContentRepository
            .save(purchasedContentMapper.toEntity(purchasedContentDTO))
            .flatMap(purchasedContentSearchRepository::save)
            .map(purchasedContentMapper::toDto);
    }

    @Override
    public Mono<PurchasedContentDTO> update(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to update PurchasedContent : {}", purchasedContentDTO);
        return purchasedContentRepository
            .save(purchasedContentMapper.toEntity(purchasedContentDTO))
            .flatMap(purchasedContentSearchRepository::save)
            .map(purchasedContentMapper::toDto);
    }

    @Override
    public Mono<PurchasedContentDTO> partialUpdate(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to partially update PurchasedContent : {}", purchasedContentDTO);

        return purchasedContentRepository
            .findById(purchasedContentDTO.getId())
            .map(existingPurchasedContent -> {
                purchasedContentMapper.partialUpdate(existingPurchasedContent, purchasedContentDTO);

                return existingPurchasedContent;
            })
            .flatMap(purchasedContentRepository::save)
            .flatMap(savedPurchasedContent -> {
                purchasedContentSearchRepository.save(savedPurchasedContent);
                return Mono.just(savedPurchasedContent);
            })
            .map(purchasedContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedContents");
        return purchasedContentRepository.findAllBy(pageable).map(purchasedContentMapper::toDto);
    }

    public Flux<PurchasedContentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedContentRepository.findAllWithEagerRelationships(pageable).map(purchasedContentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return purchasedContentRepository.count();
    }

    public Mono<Long> searchCount() {
        return purchasedContentSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PurchasedContentDTO> findOne(Long id) {
        log.debug("Request to get PurchasedContent : {}", id);
        return purchasedContentRepository.findOneWithEagerRelationships(id).map(purchasedContentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PurchasedContent : {}", id);
        return purchasedContentRepository.deleteById(id).then(purchasedContentSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PurchasedContentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurchasedContents for query {}", query);
        return purchasedContentSearchRepository.search(query, pageable).map(purchasedContentMapper::toDto);
    }
}
