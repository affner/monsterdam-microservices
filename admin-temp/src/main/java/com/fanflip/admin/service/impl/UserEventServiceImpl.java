package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.UserEventRepository;
import com.fanflip.admin.repository.search.UserEventSearchRepository;
import com.fanflip.admin.service.UserEventService;
import com.fanflip.admin.service.dto.UserEventDTO;
import com.fanflip.admin.service.mapper.UserEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.UserEvent}.
 */
@Service
@Transactional
public class UserEventServiceImpl implements UserEventService {

    private final Logger log = LoggerFactory.getLogger(UserEventServiceImpl.class);

    private final UserEventRepository userEventRepository;

    private final UserEventMapper userEventMapper;

    private final UserEventSearchRepository userEventSearchRepository;

    public UserEventServiceImpl(
        UserEventRepository userEventRepository,
        UserEventMapper userEventMapper,
        UserEventSearchRepository userEventSearchRepository
    ) {
        this.userEventRepository = userEventRepository;
        this.userEventMapper = userEventMapper;
        this.userEventSearchRepository = userEventSearchRepository;
    }

    @Override
    public Mono<UserEventDTO> save(UserEventDTO userEventDTO) {
        log.debug("Request to save UserEvent : {}", userEventDTO);
        return userEventRepository
            .save(userEventMapper.toEntity(userEventDTO))
            .flatMap(userEventSearchRepository::save)
            .map(userEventMapper::toDto);
    }

    @Override
    public Mono<UserEventDTO> update(UserEventDTO userEventDTO) {
        log.debug("Request to update UserEvent : {}", userEventDTO);
        return userEventRepository
            .save(userEventMapper.toEntity(userEventDTO))
            .flatMap(userEventSearchRepository::save)
            .map(userEventMapper::toDto);
    }

    @Override
    public Mono<UserEventDTO> partialUpdate(UserEventDTO userEventDTO) {
        log.debug("Request to partially update UserEvent : {}", userEventDTO);

        return userEventRepository
            .findById(userEventDTO.getId())
            .map(existingUserEvent -> {
                userEventMapper.partialUpdate(existingUserEvent, userEventDTO);

                return existingUserEvent;
            })
            .flatMap(userEventRepository::save)
            .flatMap(savedUserEvent -> {
                userEventSearchRepository.save(savedUserEvent);
                return Mono.just(savedUserEvent);
            })
            .map(userEventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserEvents");
        return userEventRepository.findAllBy(pageable).map(userEventMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userEventRepository.count();
    }

    public Mono<Long> searchCount() {
        return userEventSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserEventDTO> findOne(Long id) {
        log.debug("Request to get UserEvent : {}", id);
        return userEventRepository.findById(id).map(userEventMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserEvent : {}", id);
        return userEventRepository.deleteById(id).then(userEventSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserEvents for query {}", query);
        return userEventSearchRepository.search(query, pageable).map(userEventMapper::toDto);
    }
}
