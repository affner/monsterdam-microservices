package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.UserLiteRepository;
import com.fanflip.admin.repository.search.UserLiteSearchRepository;
import com.fanflip.admin.service.UserLiteService;
import com.fanflip.admin.service.dto.UserLiteDTO;
import com.fanflip.admin.service.mapper.UserLiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.UserLite}.
 */
@Service
@Transactional
public class UserLiteServiceImpl implements UserLiteService {

    private final Logger log = LoggerFactory.getLogger(UserLiteServiceImpl.class);

    private final UserLiteRepository userLiteRepository;

    private final UserLiteMapper userLiteMapper;

    private final UserLiteSearchRepository userLiteSearchRepository;

    public UserLiteServiceImpl(
        UserLiteRepository userLiteRepository,
        UserLiteMapper userLiteMapper,
        UserLiteSearchRepository userLiteSearchRepository
    ) {
        this.userLiteRepository = userLiteRepository;
        this.userLiteMapper = userLiteMapper;
        this.userLiteSearchRepository = userLiteSearchRepository;
    }

    @Override
    public Mono<UserLiteDTO> save(UserLiteDTO userLiteDTO) {
        log.debug("Request to save UserLite : {}", userLiteDTO);
        return userLiteRepository
            .save(userLiteMapper.toEntity(userLiteDTO))
            .flatMap(userLiteSearchRepository::save)
            .map(userLiteMapper::toDto);
    }

    @Override
    public Mono<UserLiteDTO> update(UserLiteDTO userLiteDTO) {
        log.debug("Request to update UserLite : {}", userLiteDTO);
        return userLiteRepository
            .save(userLiteMapper.toEntity(userLiteDTO))
            .flatMap(userLiteSearchRepository::save)
            .map(userLiteMapper::toDto);
    }

    @Override
    public Mono<UserLiteDTO> partialUpdate(UserLiteDTO userLiteDTO) {
        log.debug("Request to partially update UserLite : {}", userLiteDTO);

        return userLiteRepository
            .findById(userLiteDTO.getId())
            .map(existingUserLite -> {
                userLiteMapper.partialUpdate(existingUserLite, userLiteDTO);

                return existingUserLite;
            })
            .flatMap(userLiteRepository::save)
            .flatMap(savedUserLite -> {
                userLiteSearchRepository.save(savedUserLite);
                return Mono.just(savedUserLite);
            })
            .map(userLiteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserLiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserLites");
        return userLiteRepository.findAllBy(pageable).map(userLiteMapper::toDto);
    }

    /**
     *  Get all the userLites where UserProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<UserLiteDTO> findAllWhereUserProfileIsNull() {
        log.debug("Request to get all userLites where UserProfile is null");
        return userLiteRepository.findAllWhereUserProfileIsNull().map(userLiteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userLiteRepository.count();
    }

    public Mono<Long> searchCount() {
        return userLiteSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserLiteDTO> findOne(Long id) {
        log.debug("Request to get UserLite : {}", id);
        return userLiteRepository.findById(id).map(userLiteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserLite : {}", id);
        return userLiteRepository.deleteById(id).then(userLiteSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserLiteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserLites for query {}", query);
        return userLiteSearchRepository.search(query, pageable).map(userLiteMapper::toDto);
    }
}
