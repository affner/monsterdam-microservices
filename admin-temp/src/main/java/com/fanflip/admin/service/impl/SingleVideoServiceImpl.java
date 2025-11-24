package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.SingleVideoRepository;
import com.monsterdam.admin.repository.search.SingleVideoSearchRepository;
import com.monsterdam.admin.service.SingleVideoService;
import com.monsterdam.admin.service.dto.SingleVideoDTO;
import com.monsterdam.admin.service.mapper.SingleVideoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.SingleVideo}.
 */
@Service
@Transactional
public class SingleVideoServiceImpl implements SingleVideoService {

    private final Logger log = LoggerFactory.getLogger(SingleVideoServiceImpl.class);

    private final SingleVideoRepository singleVideoRepository;

    private final SingleVideoMapper singleVideoMapper;

    private final SingleVideoSearchRepository singleVideoSearchRepository;

    public SingleVideoServiceImpl(
        SingleVideoRepository singleVideoRepository,
        SingleVideoMapper singleVideoMapper,
        SingleVideoSearchRepository singleVideoSearchRepository
    ) {
        this.singleVideoRepository = singleVideoRepository;
        this.singleVideoMapper = singleVideoMapper;
        this.singleVideoSearchRepository = singleVideoSearchRepository;
    }

    @Override
    public Mono<SingleVideoDTO> save(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to save SingleVideo : {}", singleVideoDTO);
        return singleVideoRepository
            .save(singleVideoMapper.toEntity(singleVideoDTO))
            .flatMap(singleVideoSearchRepository::save)
            .map(singleVideoMapper::toDto);
    }

    @Override
    public Mono<SingleVideoDTO> update(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to update SingleVideo : {}", singleVideoDTO);
        return singleVideoRepository
            .save(singleVideoMapper.toEntity(singleVideoDTO))
            .flatMap(singleVideoSearchRepository::save)
            .map(singleVideoMapper::toDto);
    }

    @Override
    public Mono<SingleVideoDTO> partialUpdate(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to partially update SingleVideo : {}", singleVideoDTO);

        return singleVideoRepository
            .findById(singleVideoDTO.getId())
            .map(existingSingleVideo -> {
                singleVideoMapper.partialUpdate(existingSingleVideo, singleVideoDTO);

                return existingSingleVideo;
            })
            .flatMap(singleVideoRepository::save)
            .flatMap(savedSingleVideo -> {
                singleVideoSearchRepository.save(savedSingleVideo);
                return Mono.just(savedSingleVideo);
            })
            .map(singleVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleVideoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleVideos");
        return singleVideoRepository.findAllBy(pageable).map(singleVideoMapper::toDto);
    }

    public Flux<SingleVideoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return singleVideoRepository.findAllWithEagerRelationships(pageable).map(singleVideoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singleVideoRepository.count();
    }

    public Mono<Long> searchCount() {
        return singleVideoSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SingleVideoDTO> findOne(Long id) {
        log.debug("Request to get SingleVideo : {}", id);
        return singleVideoRepository.findOneWithEagerRelationships(id).map(singleVideoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SingleVideo : {}", id);
        return singleVideoRepository.deleteById(id).then(singleVideoSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleVideoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SingleVideos for query {}", query);
        return singleVideoSearchRepository.search(query, pageable).map(singleVideoMapper::toDto);
    }
}
