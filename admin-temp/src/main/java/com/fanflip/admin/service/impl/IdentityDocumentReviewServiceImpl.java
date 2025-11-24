package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.IdentityDocumentReviewRepository;
import com.monsterdam.admin.repository.search.IdentityDocumentReviewSearchRepository;
import com.monsterdam.admin.service.IdentityDocumentReviewService;
import com.monsterdam.admin.service.dto.IdentityDocumentReviewDTO;
import com.monsterdam.admin.service.mapper.IdentityDocumentReviewMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.IdentityDocumentReview}.
 */
@Service
@Transactional
public class IdentityDocumentReviewServiceImpl implements IdentityDocumentReviewService {

    private final Logger log = LoggerFactory.getLogger(IdentityDocumentReviewServiceImpl.class);

    private final IdentityDocumentReviewRepository identityDocumentReviewRepository;

    private final IdentityDocumentReviewMapper identityDocumentReviewMapper;

    private final IdentityDocumentReviewSearchRepository identityDocumentReviewSearchRepository;

    public IdentityDocumentReviewServiceImpl(
        IdentityDocumentReviewRepository identityDocumentReviewRepository,
        IdentityDocumentReviewMapper identityDocumentReviewMapper,
        IdentityDocumentReviewSearchRepository identityDocumentReviewSearchRepository
    ) {
        this.identityDocumentReviewRepository = identityDocumentReviewRepository;
        this.identityDocumentReviewMapper = identityDocumentReviewMapper;
        this.identityDocumentReviewSearchRepository = identityDocumentReviewSearchRepository;
    }

    @Override
    public Mono<IdentityDocumentReviewDTO> save(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        log.debug("Request to save IdentityDocumentReview : {}", identityDocumentReviewDTO);
        return identityDocumentReviewRepository
            .save(identityDocumentReviewMapper.toEntity(identityDocumentReviewDTO))
            .flatMap(identityDocumentReviewSearchRepository::save)
            .map(identityDocumentReviewMapper::toDto);
    }

    @Override
    public Mono<IdentityDocumentReviewDTO> update(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        log.debug("Request to update IdentityDocumentReview : {}", identityDocumentReviewDTO);
        return identityDocumentReviewRepository
            .save(identityDocumentReviewMapper.toEntity(identityDocumentReviewDTO))
            .flatMap(identityDocumentReviewSearchRepository::save)
            .map(identityDocumentReviewMapper::toDto);
    }

    @Override
    public Mono<IdentityDocumentReviewDTO> partialUpdate(IdentityDocumentReviewDTO identityDocumentReviewDTO) {
        log.debug("Request to partially update IdentityDocumentReview : {}", identityDocumentReviewDTO);

        return identityDocumentReviewRepository
            .findById(identityDocumentReviewDTO.getId())
            .map(existingIdentityDocumentReview -> {
                identityDocumentReviewMapper.partialUpdate(existingIdentityDocumentReview, identityDocumentReviewDTO);

                return existingIdentityDocumentReview;
            })
            .flatMap(identityDocumentReviewRepository::save)
            .flatMap(savedIdentityDocumentReview -> {
                identityDocumentReviewSearchRepository.save(savedIdentityDocumentReview);
                return Mono.just(savedIdentityDocumentReview);
            })
            .map(identityDocumentReviewMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IdentityDocumentReviewDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IdentityDocumentReviews");
        return identityDocumentReviewRepository.findAllBy(pageable).map(identityDocumentReviewMapper::toDto);
    }

    public Mono<Long> countAll() {
        return identityDocumentReviewRepository.count();
    }

    public Mono<Long> searchCount() {
        return identityDocumentReviewSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IdentityDocumentReviewDTO> findOne(Long id) {
        log.debug("Request to get IdentityDocumentReview : {}", id);
        return identityDocumentReviewRepository.findById(id).map(identityDocumentReviewMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IdentityDocumentReview : {}", id);
        return identityDocumentReviewRepository.deleteById(id).then(identityDocumentReviewSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IdentityDocumentReviewDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IdentityDocumentReviews for query {}", query);
        return identityDocumentReviewSearchRepository.search(query, pageable).map(identityDocumentReviewMapper::toDto);
    }
}
