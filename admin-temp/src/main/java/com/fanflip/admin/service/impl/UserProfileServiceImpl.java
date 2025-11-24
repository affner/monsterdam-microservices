package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.UserProfileRepository;
import com.monsterdam.admin.repository.search.UserProfileSearchRepository;
import com.monsterdam.admin.service.UserProfileService;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.mapper.UserProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.UserProfile}.
 */
@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileMapper userProfileMapper;

    private final UserProfileSearchRepository userProfileSearchRepository;

    public UserProfileServiceImpl(
        UserProfileRepository userProfileRepository,
        UserProfileMapper userProfileMapper,
        UserProfileSearchRepository userProfileSearchRepository
    ) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
        this.userProfileSearchRepository = userProfileSearchRepository;
    }

    @Override
    public Mono<UserProfileDTO> save(UserProfileDTO userProfileDTO) {
        log.debug("Request to save UserProfile : {}", userProfileDTO);
        return userProfileRepository
            .save(userProfileMapper.toEntity(userProfileDTO))
            .flatMap(userProfileSearchRepository::save)
            .map(userProfileMapper::toDto);
    }

    @Override
    public Mono<UserProfileDTO> update(UserProfileDTO userProfileDTO) {
        log.debug("Request to update UserProfile : {}", userProfileDTO);
        return userProfileRepository
            .save(userProfileMapper.toEntity(userProfileDTO))
            .flatMap(userProfileSearchRepository::save)
            .map(userProfileMapper::toDto);
    }

    @Override
    public Mono<UserProfileDTO> partialUpdate(UserProfileDTO userProfileDTO) {
        log.debug("Request to partially update UserProfile : {}", userProfileDTO);

        return userProfileRepository
            .findById(userProfileDTO.getId())
            .map(existingUserProfile -> {
                userProfileMapper.partialUpdate(existingUserProfile, userProfileDTO);

                return existingUserProfile;
            })
            .flatMap(userProfileRepository::save)
            .flatMap(savedUserProfile -> {
                userProfileSearchRepository.save(savedUserProfile);
                return Mono.just(savedUserProfile);
            })
            .map(userProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserProfiles");
        return userProfileRepository.findAllBy(pageable).map(userProfileMapper::toDto);
    }

    public Flux<UserProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userProfileRepository.findAllWithEagerRelationships(pageable).map(userProfileMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userProfileRepository.count();
    }

    public Mono<Long> searchCount() {
        return userProfileSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserProfileDTO> findOne(Long id) {
        log.debug("Request to get UserProfile : {}", id);
        return userProfileRepository.findOneWithEagerRelationships(id).map(userProfileMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserProfile : {}", id);
        return userProfileRepository.deleteById(id).then(userProfileSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserProfileDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserProfiles for query {}", query);
        return userProfileSearchRepository.search(query, pageable).map(userProfileMapper::toDto);
    }
}
