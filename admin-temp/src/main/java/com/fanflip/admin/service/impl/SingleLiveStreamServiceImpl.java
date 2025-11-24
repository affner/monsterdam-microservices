package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.SingleLiveStreamRepository;
import com.monsterdam.admin.repository.search.SingleLiveStreamSearchRepository;
import com.monsterdam.admin.service.SingleLiveStreamService;
import com.monsterdam.admin.service.dto.SingleLiveStreamDTO;
import com.monsterdam.admin.service.mapper.SingleLiveStreamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.SingleLiveStream}.
 */
@Service
@Transactional
public class SingleLiveStreamServiceImpl implements SingleLiveStreamService {

    private final Logger log = LoggerFactory.getLogger(SingleLiveStreamServiceImpl.class);

    private final SingleLiveStreamRepository singleLiveStreamRepository;

    private final SingleLiveStreamMapper singleLiveStreamMapper;

    private final SingleLiveStreamSearchRepository singleLiveStreamSearchRepository;

    public SingleLiveStreamServiceImpl(
        SingleLiveStreamRepository singleLiveStreamRepository,
        SingleLiveStreamMapper singleLiveStreamMapper,
        SingleLiveStreamSearchRepository singleLiveStreamSearchRepository
    ) {
        this.singleLiveStreamRepository = singleLiveStreamRepository;
        this.singleLiveStreamMapper = singleLiveStreamMapper;
        this.singleLiveStreamSearchRepository = singleLiveStreamSearchRepository;
    }

    @Override
    public Mono<SingleLiveStreamDTO> save(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to save SingleLiveStream : {}", singleLiveStreamDTO);
        return singleLiveStreamRepository
            .save(singleLiveStreamMapper.toEntity(singleLiveStreamDTO))
            .flatMap(singleLiveStreamSearchRepository::save)
            .map(singleLiveStreamMapper::toDto);
    }

    @Override
    public Mono<SingleLiveStreamDTO> update(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to update SingleLiveStream : {}", singleLiveStreamDTO);
        return singleLiveStreamRepository
            .save(singleLiveStreamMapper.toEntity(singleLiveStreamDTO))
            .flatMap(singleLiveStreamSearchRepository::save)
            .map(singleLiveStreamMapper::toDto);
    }

    @Override
    public Mono<SingleLiveStreamDTO> partialUpdate(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to partially update SingleLiveStream : {}", singleLiveStreamDTO);

        return singleLiveStreamRepository
            .findById(singleLiveStreamDTO.getId())
            .map(existingSingleLiveStream -> {
                singleLiveStreamMapper.partialUpdate(existingSingleLiveStream, singleLiveStreamDTO);

                return existingSingleLiveStream;
            })
            .flatMap(singleLiveStreamRepository::save)
            .flatMap(savedSingleLiveStream -> {
                singleLiveStreamSearchRepository.save(savedSingleLiveStream);
                return Mono.just(savedSingleLiveStream);
            })
            .map(singleLiveStreamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleLiveStreamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleLiveStreams");
        return singleLiveStreamRepository.findAllBy(pageable).map(singleLiveStreamMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singleLiveStreamRepository.count();
    }

    public Mono<Long> searchCount() {
        return singleLiveStreamSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SingleLiveStreamDTO> findOne(Long id) {
        log.debug("Request to get SingleLiveStream : {}", id);
        return singleLiveStreamRepository.findById(id).map(singleLiveStreamMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SingleLiveStream : {}", id);
        return singleLiveStreamRepository.deleteById(id).then(singleLiveStreamSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleLiveStreamDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SingleLiveStreams for query {}", query);
        return singleLiveStreamSearchRepository.search(query, pageable).map(singleLiveStreamMapper::toDto);
    }
}
