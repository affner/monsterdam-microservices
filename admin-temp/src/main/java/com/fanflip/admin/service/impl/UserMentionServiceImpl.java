package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.UserMentionRepository;
import com.monsterdam.admin.repository.search.UserMentionSearchRepository;
import com.monsterdam.admin.service.UserMentionService;
import com.monsterdam.admin.service.dto.UserMentionDTO;
import com.monsterdam.admin.service.mapper.UserMentionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.UserMention}.
 */
@Service
@Transactional
public class UserMentionServiceImpl implements UserMentionService {

    private final Logger log = LoggerFactory.getLogger(UserMentionServiceImpl.class);

    private final UserMentionRepository userMentionRepository;

    private final UserMentionMapper userMentionMapper;

    private final UserMentionSearchRepository userMentionSearchRepository;

    public UserMentionServiceImpl(
        UserMentionRepository userMentionRepository,
        UserMentionMapper userMentionMapper,
        UserMentionSearchRepository userMentionSearchRepository
    ) {
        this.userMentionRepository = userMentionRepository;
        this.userMentionMapper = userMentionMapper;
        this.userMentionSearchRepository = userMentionSearchRepository;
    }

    @Override
    public Mono<UserMentionDTO> save(UserMentionDTO userMentionDTO) {
        log.debug("Request to save UserMention : {}", userMentionDTO);
        return userMentionRepository
            .save(userMentionMapper.toEntity(userMentionDTO))
            .flatMap(userMentionSearchRepository::save)
            .map(userMentionMapper::toDto);
    }

    @Override
    public Mono<UserMentionDTO> update(UserMentionDTO userMentionDTO) {
        log.debug("Request to update UserMention : {}", userMentionDTO);
        return userMentionRepository
            .save(userMentionMapper.toEntity(userMentionDTO))
            .flatMap(userMentionSearchRepository::save)
            .map(userMentionMapper::toDto);
    }

    @Override
    public Mono<UserMentionDTO> partialUpdate(UserMentionDTO userMentionDTO) {
        log.debug("Request to partially update UserMention : {}", userMentionDTO);

        return userMentionRepository
            .findById(userMentionDTO.getId())
            .map(existingUserMention -> {
                userMentionMapper.partialUpdate(existingUserMention, userMentionDTO);

                return existingUserMention;
            })
            .flatMap(userMentionRepository::save)
            .flatMap(savedUserMention -> {
                userMentionSearchRepository.save(savedUserMention);
                return Mono.just(savedUserMention);
            })
            .map(userMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserMentionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserMentions");
        return userMentionRepository.findAllBy(pageable).map(userMentionMapper::toDto);
    }

    public Flux<UserMentionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userMentionRepository.findAllWithEagerRelationships(pageable).map(userMentionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userMentionRepository.count();
    }

    public Mono<Long> searchCount() {
        return userMentionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserMentionDTO> findOne(Long id) {
        log.debug("Request to get UserMention : {}", id);
        return userMentionRepository.findOneWithEagerRelationships(id).map(userMentionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserMention : {}", id);
        return userMentionRepository.deleteById(id).then(userMentionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserMentionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserMentions for query {}", query);
        return userMentionSearchRepository.search(query, pageable).map(userMentionMapper::toDto);
    }
}
