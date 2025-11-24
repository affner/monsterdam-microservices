package com.monsterdam.catalogs.service.impl;

import com.monsterdam.catalogs.domain.State;
import com.monsterdam.catalogs.repository.StateRepository;
import com.monsterdam.catalogs.repository.search.StateSearchRepository;
import com.monsterdam.catalogs.service.StateService;
import com.monsterdam.catalogs.service.dto.StateDTO;
import com.monsterdam.catalogs.service.mapper.StateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.catalogs.domain.State}.
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
    public StateDTO save(StateDTO stateDTO) {
        log.debug("Request to save State : {}", stateDTO);
        State state = stateMapper.toEntity(stateDTO);
        state = stateRepository.save(state);
        StateDTO result = stateMapper.toDto(state);
        stateSearchRepository.index(state);
        return result;
    }

    @Override
    public StateDTO update(StateDTO stateDTO) {
        log.debug("Request to update State : {}", stateDTO);
        State state = stateMapper.toEntity(stateDTO);
        state = stateRepository.save(state);
        StateDTO result = stateMapper.toDto(state);
        stateSearchRepository.index(state);
        return result;
    }

    @Override
    public Optional<StateDTO> partialUpdate(StateDTO stateDTO) {
        log.debug("Request to partially update State : {}", stateDTO);

        return stateRepository
            .findById(stateDTO.getId())
            .map(existingState -> {
                stateMapper.partialUpdate(existingState, stateDTO);

                return existingState;
            })
            .map(stateRepository::save)
            .map(savedState -> {
                stateSearchRepository.index(savedState);
                return savedState;
            })
            .map(stateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all States");
        return stateRepository.findAll(pageable).map(stateMapper::toDto);
    }

    public Page<StateDTO> findAllWithEagerRelationships(Pageable pageable) {
        return stateRepository.findAllWithEagerRelationships(pageable).map(stateMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StateDTO> findOne(Long id) {
        log.debug("Request to get State : {}", id);
        return stateRepository.findOneWithEagerRelationships(id).map(stateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete State : {}", id);
        stateRepository.deleteById(id);
        stateSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of States for query {}", query);
        return stateSearchRepository.search(query, pageable).map(stateMapper::toDto);
    }
}
