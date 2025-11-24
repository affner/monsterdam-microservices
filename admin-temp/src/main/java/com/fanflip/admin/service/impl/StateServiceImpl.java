package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.StateRepository;
import com.monsterdam.admin.repository.search.StateSearchRepository;
import com.monsterdam.admin.service.StateService;
import com.monsterdam.admin.service.dto.StateDTO;
import com.monsterdam.admin.service.mapper.StateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.State}.
 */
@Service
@Transactional
public class StateServiceImpl implements StateService {

    private final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);

    private final StateRepository stateRepository;

    private final StateMapper stateMapper;

    private final StateSearchRepository stateSearchRepository;

    public StateServiceImpl(StateRepository stateRepository, StateMapper stateMapper, StateSearchRepository stateSearchRepository) {
        this.stateRepository = stateRepository;
        this.stateMapper = stateMapper;
        this.stateSearchRepository = stateSearchRepository;
    }

    @Override
    public Mono<StateDTO> save(StateDTO stateDTO) {
        log.debug("Request to save State : {}", stateDTO);
        return stateRepository.save(stateMapper.toEntity(stateDTO)).flatMap(stateSearchRepository::save).map(stateMapper::toDto);
    }

    @Override
    public Mono<StateDTO> update(StateDTO stateDTO) {
        log.debug("Request to update State : {}", stateDTO);
        return stateRepository.save(stateMapper.toEntity(stateDTO)).flatMap(stateSearchRepository::save).map(stateMapper::toDto);
    }

    @Override
    public Mono<StateDTO> partialUpdate(StateDTO stateDTO) {
        log.debug("Request to partially update State : {}", stateDTO);

        return stateRepository
            .findById(stateDTO.getId())
            .map(existingState -> {
                stateMapper.partialUpdate(existingState, stateDTO);

                return existingState;
            })
            .flatMap(stateRepository::save)
            .flatMap(savedState -> {
                stateSearchRepository.save(savedState);
                return Mono.just(savedState);
            })
            .map(stateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<StateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all States");
        return stateRepository.findAllBy(pageable).map(stateMapper::toDto);
    }

    public Flux<StateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return stateRepository.findAllWithEagerRelationships(pageable).map(stateMapper::toDto);
    }

    public Mono<Long> countAll() {
        return stateRepository.count();
    }

    public Mono<Long> searchCount() {
        return stateSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<StateDTO> findOne(Long id) {
        log.debug("Request to get State : {}", id);
        return stateRepository.findOneWithEagerRelationships(id).map(stateMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete State : {}", id);
        return stateRepository.deleteById(id).then(stateSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<StateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of States for query {}", query);
        return stateSearchRepository.search(query, pageable).map(stateMapper::toDto);
    }
}
