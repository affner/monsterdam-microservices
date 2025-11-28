package com.monsterdam.catalogs.service.impl;

import com.monsterdam.catalogs.domain.GlobalEvent;
import com.monsterdam.catalogs.repository.GlobalEventRepository;
import com.monsterdam.catalogs.service.GlobalEventService;
import com.monsterdam.catalogs.service.dto.GlobalEventDTO;
import com.monsterdam.catalogs.service.mapper.GlobalEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.catalogs.domain.GlobalEvent}.
 */
@Service
@Transactional
public class GlobalEventServiceImpl implements GlobalEventService {

    private final Logger log = LoggerFactory.getLogger(GlobalEventServiceImpl.class);

    private final GlobalEventRepository globalEventRepository;

    private final GlobalEventMapper globalEventMapper;

    public GlobalEventServiceImpl(GlobalEventRepository globalEventRepository, GlobalEventMapper globalEventMapper) {
        this.globalEventRepository = globalEventRepository;
        this.globalEventMapper = globalEventMapper;
    }

    @Override
    public GlobalEventDTO save(GlobalEventDTO globalEventDTO) {
        log.debug("Request to save GlobalEvent : {}", globalEventDTO);
        GlobalEvent globalEvent = globalEventMapper.toEntity(globalEventDTO);
        globalEvent = globalEventRepository.save(globalEvent);
        return globalEventMapper.toDto(globalEvent);
    }

    @Override
    public GlobalEventDTO update(GlobalEventDTO globalEventDTO) {
        log.debug("Request to update GlobalEvent : {}", globalEventDTO);
        GlobalEvent globalEvent = globalEventMapper.toEntity(globalEventDTO);
        globalEvent = globalEventRepository.save(globalEvent);
        return globalEventMapper.toDto(globalEvent);
    }

    @Override
    public Optional<GlobalEventDTO> partialUpdate(GlobalEventDTO globalEventDTO) {
        log.debug("Request to partially update GlobalEvent : {}", globalEventDTO);

        return globalEventRepository
            .findById(globalEventDTO.getId())
            .map(existingGlobalEvent -> {
                globalEventMapper.partialUpdate(existingGlobalEvent, globalEventDTO);

                return existingGlobalEvent;
            })
            .map(globalEventRepository::save)
            .map(globalEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GlobalEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GlobalEvents");
        return globalEventRepository.findAll(pageable).map(globalEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GlobalEventDTO> findOne(Long id) {
        log.debug("Request to get GlobalEvent : {}", id);
        return globalEventRepository.findById(id).map(globalEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlobalEvent : {}", id);
        globalEventRepository.deleteById(id);
    }
}
