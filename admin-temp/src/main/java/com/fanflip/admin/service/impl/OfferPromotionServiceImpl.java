package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.OfferPromotionRepository;
import com.monsterdam.admin.repository.search.OfferPromotionSearchRepository;
import com.monsterdam.admin.service.OfferPromotionService;
import com.monsterdam.admin.service.dto.OfferPromotionDTO;
import com.monsterdam.admin.service.mapper.OfferPromotionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.OfferPromotion}.
 */
@Service
@Transactional
public class OfferPromotionServiceImpl implements OfferPromotionService {

    private final Logger log = LoggerFactory.getLogger(OfferPromotionServiceImpl.class);

    private final OfferPromotionRepository offerPromotionRepository;

    private final OfferPromotionMapper offerPromotionMapper;

    private final OfferPromotionSearchRepository offerPromotionSearchRepository;

    public OfferPromotionServiceImpl(
        OfferPromotionRepository offerPromotionRepository,
        OfferPromotionMapper offerPromotionMapper,
        OfferPromotionSearchRepository offerPromotionSearchRepository
    ) {
        this.offerPromotionRepository = offerPromotionRepository;
        this.offerPromotionMapper = offerPromotionMapper;
        this.offerPromotionSearchRepository = offerPromotionSearchRepository;
    }

    @Override
    public Mono<OfferPromotionDTO> save(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to save OfferPromotion : {}", offerPromotionDTO);
        return offerPromotionRepository
            .save(offerPromotionMapper.toEntity(offerPromotionDTO))
            .flatMap(offerPromotionSearchRepository::save)
            .map(offerPromotionMapper::toDto);
    }

    @Override
    public Mono<OfferPromotionDTO> update(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to update OfferPromotion : {}", offerPromotionDTO);
        return offerPromotionRepository
            .save(offerPromotionMapper.toEntity(offerPromotionDTO))
            .flatMap(offerPromotionSearchRepository::save)
            .map(offerPromotionMapper::toDto);
    }

    @Override
    public Mono<OfferPromotionDTO> partialUpdate(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to partially update OfferPromotion : {}", offerPromotionDTO);

        return offerPromotionRepository
            .findById(offerPromotionDTO.getId())
            .map(existingOfferPromotion -> {
                offerPromotionMapper.partialUpdate(existingOfferPromotion, offerPromotionDTO);

                return existingOfferPromotion;
            })
            .flatMap(offerPromotionRepository::save)
            .flatMap(savedOfferPromotion -> {
                offerPromotionSearchRepository.save(savedOfferPromotion);
                return Mono.just(savedOfferPromotion);
            })
            .map(offerPromotionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OfferPromotionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OfferPromotions");
        return offerPromotionRepository.findAllBy(pageable).map(offerPromotionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return offerPromotionRepository.count();
    }

    public Mono<Long> searchCount() {
        return offerPromotionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<OfferPromotionDTO> findOne(Long id) {
        log.debug("Request to get OfferPromotion : {}", id);
        return offerPromotionRepository.findById(id).map(offerPromotionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete OfferPromotion : {}", id);
        return offerPromotionRepository.deleteById(id).then(offerPromotionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<OfferPromotionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OfferPromotions for query {}", query);
        return offerPromotionSearchRepository.search(query, pageable).map(offerPromotionMapper::toDto);
    }
}
