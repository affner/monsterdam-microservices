package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.GlobalEventRepository;
import com.fanflip.admin.repository.search.GlobalEventSearchRepository;
import com.fanflip.admin.service.GlobalEventService;
import com.fanflip.admin.service.dto.GlobalEventDTO;
import com.fanflip.admin.service.mapper.GlobalEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.GlobalEvent}.
 */
@Service
@Transactional
public class GlobalEventServiceImpl implements GlobalEventService {

    private final Logger log = LoggerFactory.getLogger(GlobalEventServiceImpl.class);

    private final GlobalEventRepository globalEventRepository;

    private final GlobalEventMapper globalEventMapper;

    private final GlobalEventSearchRepository globalEventSearchRepository;

    public GlobalEventServiceImpl(
        GlobalEventRepository globalEventRepository,
        GlobalEventMapper globalEventMapper,
        GlobalEventSearchRepository globalEventSearchRepository
    ) {
        this.globalEventRepository = globalEventRepository;
        this.globalEventMapper = globalEventMapper;
        this.globalEventSearchRepository = globalEventSearchRepository;
    }

    @Override
    public Mono<GlobalEventDTO> save(GlobalEventDTO globalEventDTO) {
        log.debug("Request to save GlobalEvent : {}", globalEventDTO);
        return globalEventRepository
            .save(globalEventMapper.toEntity(globalEventDTO))
            .flatMap(globalEventSearchRepository::save)
            .map(globalEventMapper::toDto);
    }

    @Override
    public Mono<GlobalEventDTO> update(GlobalEventDTO globalEventDTO) {
        log.debug("Request to update GlobalEvent : {}", globalEventDTO);
        return globalEventRepository
            .save(globalEventMapper.toEntity(globalEventDTO))
            .flatMap(globalEventSearchRepository::save)
            .map(globalEventMapper::toDto);
    }

    @Override
    public Mono<GlobalEventDTO> partialUpdate(GlobalEventDTO globalEventDTO) {
        log.debug("Request to partially update GlobalEvent : {}", globalEventDTO);

        return globalEventRepository
            .findById(globalEventDTO.getId())
            .map(existingGlobalEvent -> {
                globalEventMapper.partialUpdate(existingGlobalEvent, globalEventDTO);

                return existingGlobalEvent;
            })
            .flatMap(globalEventRepository::save)
            .flatMap(savedGlobalEvent -> {
                globalEventSearchRepository.save(savedGlobalEvent);
                return Mono.just(savedGlobalEvent);
            })
            .map(globalEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<GlobalEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GlobalEvents");
        return globalEventRepository.findAllBy(pageable).map(globalEventMapper::toDto);
    }

    public Mono<Long> countAll() {
        return globalEventRepository.count();
    }

    public Mono<Long> searchCount() {
        return globalEventSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<GlobalEventDTO> findOne(Long id) {
        log.debug("Request to get GlobalEvent : {}", id);
        return globalEventRepository.findById(id).map(globalEventMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete GlobalEvent : {}", id);
        return globalEventRepository.deleteById(id).then(globalEventSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<GlobalEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of GlobalEvents for query {}", query);
        return globalEventSearchRepository.search(query, pageable).map(globalEventMapper::toDto);
    }
}
