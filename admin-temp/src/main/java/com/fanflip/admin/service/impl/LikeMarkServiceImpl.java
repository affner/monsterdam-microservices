package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.LikeMarkRepository;
import com.monsterdam.admin.repository.search.LikeMarkSearchRepository;
import com.monsterdam.admin.service.LikeMarkService;
import com.monsterdam.admin.service.dto.LikeMarkDTO;
import com.monsterdam.admin.service.mapper.LikeMarkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.LikeMark}.
 */
@Service
@Transactional
public class LikeMarkServiceImpl implements LikeMarkService {

    private final Logger log = LoggerFactory.getLogger(LikeMarkServiceImpl.class);

    private final LikeMarkRepository likeMarkRepository;

    private final LikeMarkMapper likeMarkMapper;

    private final LikeMarkSearchRepository likeMarkSearchRepository;

    public LikeMarkServiceImpl(
        LikeMarkRepository likeMarkRepository,
        LikeMarkMapper likeMarkMapper,
        LikeMarkSearchRepository likeMarkSearchRepository
    ) {
        this.likeMarkRepository = likeMarkRepository;
        this.likeMarkMapper = likeMarkMapper;
        this.likeMarkSearchRepository = likeMarkSearchRepository;
    }

    @Override
    public Mono<LikeMarkDTO> save(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to save LikeMark : {}", likeMarkDTO);
        return likeMarkRepository
            .save(likeMarkMapper.toEntity(likeMarkDTO))
            .flatMap(likeMarkSearchRepository::save)
            .map(likeMarkMapper::toDto);
    }

    @Override
    public Mono<LikeMarkDTO> update(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to update LikeMark : {}", likeMarkDTO);
        return likeMarkRepository
            .save(likeMarkMapper.toEntity(likeMarkDTO))
            .flatMap(likeMarkSearchRepository::save)
            .map(likeMarkMapper::toDto);
    }

    @Override
    public Mono<LikeMarkDTO> partialUpdate(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to partially update LikeMark : {}", likeMarkDTO);

        return likeMarkRepository
            .findById(likeMarkDTO.getId())
            .map(existingLikeMark -> {
                likeMarkMapper.partialUpdate(existingLikeMark, likeMarkDTO);

                return existingLikeMark;
            })
            .flatMap(likeMarkRepository::save)
            .flatMap(savedLikeMark -> {
                likeMarkSearchRepository.save(savedLikeMark);
                return Mono.just(savedLikeMark);
            })
            .map(likeMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LikeMarkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LikeMarks");
        return likeMarkRepository.findAllBy(pageable).map(likeMarkMapper::toDto);
    }

    public Mono<Long> countAll() {
        return likeMarkRepository.count();
    }

    public Mono<Long> searchCount() {
        return likeMarkSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LikeMarkDTO> findOne(Long id) {
        log.debug("Request to get LikeMark : {}", id);
        return likeMarkRepository.findById(id).map(likeMarkMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete LikeMark : {}", id);
        return likeMarkRepository.deleteById(id).then(likeMarkSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LikeMarkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LikeMarks for query {}", query);
        return likeMarkSearchRepository.search(query, pageable).map(likeMarkMapper::toDto);
    }
}
