package com.monsterdam.app.service.impl;

import com.monsterdam.app.repository.UserUIPreferencesRepository;
import com.monsterdam.app.repository.search.UserUIPreferencesSearchRepository;
import com.monsterdam.app.service.UserUIPreferencesService;
import com.monsterdam.app.service.dto.UserUIPreferencesDTO;
import com.monsterdam.app.service.mapper.UserUIPreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.app.domain.UserUIPreferences}.
 */
@Service
@Transactional
public class UserUIPreferencesServiceImpl implements UserUIPreferencesService {

    private final Logger log = LoggerFactory.getLogger(UserUIPreferencesServiceImpl.class);

    private final UserUIPreferencesRepository userUIPreferencesRepository;

    private final UserUIPreferencesMapper userUIPreferencesMapper;

    private final UserUIPreferencesSearchRepository userUIPreferencesSearchRepository;

    public UserUIPreferencesServiceImpl(
        UserUIPreferencesRepository userUIPreferencesRepository,
        UserUIPreferencesMapper userUIPreferencesMapper,
        UserUIPreferencesSearchRepository userUIPreferencesSearchRepository
    ) {
        this.userUIPreferencesRepository = userUIPreferencesRepository;
        this.userUIPreferencesMapper = userUIPreferencesMapper;
        this.userUIPreferencesSearchRepository = userUIPreferencesSearchRepository;
    }

    @Override
    public Mono<UserUIPreferencesDTO> save(UserUIPreferencesDTO userUIPreferencesDTO) {
        log.debug("Request to save UserUIPreferences : {}", userUIPreferencesDTO);
        return userUIPreferencesRepository
            .save(userUIPreferencesMapper.toEntity(userUIPreferencesDTO))
            .flatMap(userUIPreferencesSearchRepository::save)
            .map(userUIPreferencesMapper::toDto);
    }

    @Override
    public Mono<UserUIPreferencesDTO> update(UserUIPreferencesDTO userUIPreferencesDTO) {
        log.debug("Request to update UserUIPreferences : {}", userUIPreferencesDTO);
        return userUIPreferencesRepository
            .save(userUIPreferencesMapper.toEntity(userUIPreferencesDTO))
            .flatMap(userUIPreferencesSearchRepository::save)
            .map(userUIPreferencesMapper::toDto);
    }

    @Override
    public Mono<UserUIPreferencesDTO> partialUpdate(UserUIPreferencesDTO userUIPreferencesDTO) {
        log.debug("Request to partially update UserUIPreferences : {}", userUIPreferencesDTO);

        return userUIPreferencesRepository
            .findById(userUIPreferencesDTO.getId())
            .map(existingUserUIPreferences -> {
                userUIPreferencesMapper.partialUpdate(existingUserUIPreferences, userUIPreferencesDTO);

                return existingUserUIPreferences;
            })
            .flatMap(userUIPreferencesRepository::save)
            .flatMap(savedUserUIPreferences -> {
                userUIPreferencesSearchRepository.save(savedUserUIPreferences);
                return Mono.just(savedUserUIPreferences);
            })
            .map(userUIPreferencesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserUIPreferencesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserUIPreferences");
        return userUIPreferencesRepository.findAllBy(pageable).map(userUIPreferencesMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userUIPreferencesRepository.count();
    }

    public Mono<Long> searchCount() {
        return userUIPreferencesSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserUIPreferencesDTO> findOne(Long id) {
        log.debug("Request to get UserUIPreferences : {}", id);
        return userUIPreferencesRepository.findById(id).map(userUIPreferencesMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserUIPreferences : {}", id);
        return userUIPreferencesRepository.deleteById(id).then(userUIPreferencesSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserUIPreferencesDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserUIPreferences for query {}", query);
        return userUIPreferencesSearchRepository.search(query, pageable).map(userUIPreferencesMapper::toDto);
    }
}
