package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.UserTagRelation;
import com.monsterdam.multimedia.repository.UserTagRelationRepository;
import com.monsterdam.multimedia.service.UserTagRelationService;
import com.monsterdam.multimedia.service.dto.UserTagRelationDTO;
import com.monsterdam.multimedia.service.mapper.UserTagRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.UserTagRelation}.
 */
@Service
@Transactional
public class UserTagRelationServiceImpl implements UserTagRelationService {

    private final Logger log = LoggerFactory.getLogger(UserTagRelationServiceImpl.class);

    private final UserTagRelationRepository userTagRelationRepository;

    private final UserTagRelationMapper userTagRelationMapper;

    public UserTagRelationServiceImpl(UserTagRelationRepository userTagRelationRepository, UserTagRelationMapper userTagRelationMapper) {
        this.userTagRelationRepository = userTagRelationRepository;
        this.userTagRelationMapper = userTagRelationMapper;
    }

    @Override
    public UserTagRelationDTO save(UserTagRelationDTO userTagRelationDTO) {
        log.debug("Request to save UserTagRelation : {}", userTagRelationDTO);
        UserTagRelation userTagRelation = userTagRelationMapper.toEntity(userTagRelationDTO);
        userTagRelation = userTagRelationRepository.save(userTagRelation);
        return userTagRelationMapper.toDto(userTagRelation);
    }

    @Override
    public UserTagRelationDTO update(UserTagRelationDTO userTagRelationDTO) {
        log.debug("Request to update UserTagRelation : {}", userTagRelationDTO);
        UserTagRelation userTagRelation = userTagRelationMapper.toEntity(userTagRelationDTO);
        userTagRelation = userTagRelationRepository.save(userTagRelation);
        return userTagRelationMapper.toDto(userTagRelation);
    }

    @Override
    public Optional<UserTagRelationDTO> partialUpdate(UserTagRelationDTO userTagRelationDTO) {
        log.debug("Request to partially update UserTagRelation : {}", userTagRelationDTO);

        return userTagRelationRepository
            .findById(userTagRelationDTO.getId())
            .map(existingUserTagRelation -> {
                userTagRelationMapper.partialUpdate(existingUserTagRelation, userTagRelationDTO);

                return existingUserTagRelation;
            })
            .map(userTagRelationRepository::save)
            .map(userTagRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserTagRelationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserTagRelations");
        return userTagRelationRepository.findAll(pageable).map(userTagRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserTagRelationDTO> findOne(Long id) {
        log.debug("Request to get UserTagRelation : {}", id);
        return userTagRelationRepository.findById(id).map(userTagRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserTagRelation : {}", id);
        userTagRelationRepository.deleteById(id);
    }
}
