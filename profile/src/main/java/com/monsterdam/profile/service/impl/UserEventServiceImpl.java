package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.UserEvent;
import com.monsterdam.profile.repository.UserEventRepository;
import com.monsterdam.profile.service.UserEventService;
import com.monsterdam.profile.service.dto.UserEventDTO;
import com.monsterdam.profile.service.mapper.UserEventMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.profile.domain.UserEvent}.
 */
@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {

    private final Logger log = LoggerFactory.getLogger(UserEventServiceImpl.class);

    private final UserEventRepository userEventRepository;

    private final UserEventMapper userEventMapper;

    public UserEventServiceImpl(UserEventRepository userEventRepository, UserEventMapper userEventMapper) {
        this.userEventRepository = userEventRepository;
        this.userEventMapper = userEventMapper;
    }

    @Override
    public UserEventDTO save(UserEventDTO userEventDTO) {
        log.debug("Request to save UserEvent : {}", userEventDTO);
        UserEvent userEvent = userEventMapper.toEntity(userEventDTO);
        userEvent = userEventRepository.save(userEvent);
        return userEventMapper.toDto(userEvent);
    }

    @Override
    public UserEventDTO update(UserEventDTO userEventDTO) {
        log.debug("Request to update UserEvent : {}", userEventDTO);
        UserEvent userEvent = userEventMapper.toEntity(userEventDTO);
        userEvent = userEventRepository.save(userEvent);
        return userEventMapper.toDto(userEvent);
    }

    @Override
    public Optional<UserEventDTO> partialUpdate(UserEventDTO userEventDTO) {
        log.debug("Request to partially update UserEvent : {}", userEventDTO);

        return userEventRepository
            .findById(userEventDTO.getId())
            .map(existingUserEvent -> {
                userEventMapper.partialUpdate(existingUserEvent, userEventDTO);

                return existingUserEvent;
            })
            .map(userEventRepository::save)
            .map(userEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserEvents");
        return userEventRepository.findAll(pageable).map(userEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserEventDTO> findOne(Long id) {
        log.debug("Request to get UserEvent : {}", id);
        return userEventRepository.findById(id).map(userEventMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserEvent : {}", id);
        userEventRepository.deleteById(id);
    }
}
