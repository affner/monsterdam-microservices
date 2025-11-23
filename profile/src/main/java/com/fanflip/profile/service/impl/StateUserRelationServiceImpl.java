package com.fanflip.profile.service.impl;

import com.fanflip.profile.domain.StateUserRelation;
import com.fanflip.profile.repository.StateUserRelationRepository;
import com.fanflip.profile.service.StateUserRelationService;
import com.fanflip.profile.service.dto.StateUserRelationDTO;
import com.fanflip.profile.service.mapper.StateUserRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.profile.domain.StateUserRelation}.
 */
@Service
@Transactional
public class StateUserRelationServiceImpl implements StateUserRelationService {

    private final Logger log = LoggerFactory.getLogger(StateUserRelationServiceImpl.class);

    private final StateUserRelationRepository stateUserRelationRepository;

    private final StateUserRelationMapper stateUserRelationMapper;

    public StateUserRelationServiceImpl(
        StateUserRelationRepository stateUserRelationRepository,
        StateUserRelationMapper stateUserRelationMapper
    ) {
        this.stateUserRelationRepository = stateUserRelationRepository;
        this.stateUserRelationMapper = stateUserRelationMapper;
    }

    @Override
    public StateUserRelationDTO save(StateUserRelationDTO stateUserRelationDTO) {
        log.debug("Request to save StateUserRelation : {}", stateUserRelationDTO);
        StateUserRelation stateUserRelation = stateUserRelationMapper.toEntity(stateUserRelationDTO);
        stateUserRelation = stateUserRelationRepository.save(stateUserRelation);
        return stateUserRelationMapper.toDto(stateUserRelation);
    }

    @Override
    public StateUserRelationDTO update(StateUserRelationDTO stateUserRelationDTO) {
        log.debug("Request to update StateUserRelation : {}", stateUserRelationDTO);
        StateUserRelation stateUserRelation = stateUserRelationMapper.toEntity(stateUserRelationDTO);
        stateUserRelation = stateUserRelationRepository.save(stateUserRelation);
        return stateUserRelationMapper.toDto(stateUserRelation);
    }

    @Override
    public Optional<StateUserRelationDTO> partialUpdate(StateUserRelationDTO stateUserRelationDTO) {
        log.debug("Request to partially update StateUserRelation : {}", stateUserRelationDTO);

        return stateUserRelationRepository
            .findById(stateUserRelationDTO.getId())
            .map(existingStateUserRelation -> {
                stateUserRelationMapper.partialUpdate(existingStateUserRelation, stateUserRelationDTO);

                return existingStateUserRelation;
            })
            .map(stateUserRelationRepository::save)
            .map(stateUserRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StateUserRelationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StateUserRelations");
        return stateUserRelationRepository.findAll(pageable).map(stateUserRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StateUserRelationDTO> findOne(Long id) {
        log.debug("Request to get StateUserRelation : {}", id);
        return stateUserRelationRepository.findById(id).map(stateUserRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete StateUserRelation : {}", id);
        stateUserRelationRepository.deleteById(id);
    }
}
