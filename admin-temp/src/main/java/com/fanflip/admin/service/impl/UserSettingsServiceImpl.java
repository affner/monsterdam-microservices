package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.UserSettingsRepository;
import com.monsterdam.admin.repository.search.UserSettingsSearchRepository;
import com.monsterdam.admin.service.UserSettingsService;
import com.monsterdam.admin.service.dto.UserSettingsDTO;
import com.monsterdam.admin.service.mapper.UserSettingsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.UserSettings}.
 */
@Service
@Transactional
public class UserSettingsServiceImpl implements UserSettingsService {

    private final Logger log = LoggerFactory.getLogger(UserSettingsServiceImpl.class);

    private final UserSettingsRepository userSettingsRepository;

    private final UserSettingsMapper userSettingsMapper;

    private final UserSettingsSearchRepository userSettingsSearchRepository;

    public UserSettingsServiceImpl(
        UserSettingsRepository userSettingsRepository,
        UserSettingsMapper userSettingsMapper,
        UserSettingsSearchRepository userSettingsSearchRepository
    ) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
        this.userSettingsSearchRepository = userSettingsSearchRepository;
    }

    @Override
    public Mono<UserSettingsDTO> save(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to save UserSettings : {}", userSettingsDTO);
        return userSettingsRepository
            .save(userSettingsMapper.toEntity(userSettingsDTO))
            .flatMap(userSettingsSearchRepository::save)
            .map(userSettingsMapper::toDto);
    }

    @Override
    public Mono<UserSettingsDTO> update(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to update UserSettings : {}", userSettingsDTO);
        return userSettingsRepository
            .save(userSettingsMapper.toEntity(userSettingsDTO))
            .flatMap(userSettingsSearchRepository::save)
            .map(userSettingsMapper::toDto);
    }

    @Override
    public Mono<UserSettingsDTO> partialUpdate(UserSettingsDTO userSettingsDTO) {
        log.debug("Request to partially update UserSettings : {}", userSettingsDTO);

        return userSettingsRepository
            .findById(userSettingsDTO.getId())
            .map(existingUserSettings -> {
                userSettingsMapper.partialUpdate(existingUserSettings, userSettingsDTO);

                return existingUserSettings;
            })
            .flatMap(userSettingsRepository::save)
            .flatMap(savedUserSettings -> {
                userSettingsSearchRepository.save(savedUserSettings);
                return Mono.just(savedUserSettings);
            })
            .map(userSettingsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserSettings");
        return userSettingsRepository.findAllBy(pageable).map(userSettingsMapper::toDto);
    }

    /**
     *  Get all the userSettings where UserProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserSettingsDTO> findAllWhereUserProfileIsNull() {
        log.debug("Request to get all userSettings where UserProfile is null");
        return userSettingsRepository.findAllWhereUserProfileIsNull().map(userSettingsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userSettingsRepository.count();
    }

    public Mono<Long> searchCount() {
        return userSettingsSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserSettingsDTO> findOne(Long id) {
        log.debug("Request to get UserSettings : {}", id);
        return userSettingsRepository.findById(id).map(userSettingsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserSettings : {}", id);
        return userSettingsRepository.deleteById(id).then(userSettingsSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserSettingsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserSettings for query {}", query);
        return userSettingsSearchRepository.search(query, pageable).map(userSettingsMapper::toDto);
    }
}
