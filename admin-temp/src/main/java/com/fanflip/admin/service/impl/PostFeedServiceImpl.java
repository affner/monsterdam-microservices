package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.PostFeedRepository;
import com.monsterdam.admin.repository.search.PostFeedSearchRepository;
import com.monsterdam.admin.service.PostFeedService;
import com.monsterdam.admin.service.dto.PostFeedDTO;
import com.monsterdam.admin.service.mapper.PostFeedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.PostFeed}.
 */
@Service
@Transactional
public class PostFeedServiceImpl implements PostFeedService {

    private final Logger log = LoggerFactory.getLogger(PostFeedServiceImpl.class);

    private final PostFeedRepository postFeedRepository;

    private final PostFeedMapper postFeedMapper;

    private final PostFeedSearchRepository postFeedSearchRepository;

    public PostFeedServiceImpl(
        PostFeedRepository postFeedRepository,
        PostFeedMapper postFeedMapper,
        PostFeedSearchRepository postFeedSearchRepository
    ) {
        this.postFeedRepository = postFeedRepository;
        this.postFeedMapper = postFeedMapper;
        this.postFeedSearchRepository = postFeedSearchRepository;
    }

    @Override
    public Mono<PostFeedDTO> save(PostFeedDTO postFeedDTO) {
        log.debug("Request to save PostFeed : {}", postFeedDTO);
        return postFeedRepository
            .save(postFeedMapper.toEntity(postFeedDTO))
            .flatMap(postFeedSearchRepository::save)
            .map(postFeedMapper::toDto);
    }

    @Override
    public Mono<PostFeedDTO> update(PostFeedDTO postFeedDTO) {
        log.debug("Request to update PostFeed : {}", postFeedDTO);
        return postFeedRepository
            .save(postFeedMapper.toEntity(postFeedDTO))
            .flatMap(postFeedSearchRepository::save)
            .map(postFeedMapper::toDto);
    }

    @Override
    public Mono<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO) {
        log.debug("Request to partially update PostFeed : {}", postFeedDTO);

        return postFeedRepository
            .findById(postFeedDTO.getId())
            .map(existingPostFeed -> {
                postFeedMapper.partialUpdate(existingPostFeed, postFeedDTO);

                return existingPostFeed;
            })
            .flatMap(postFeedRepository::save)
            .flatMap(savedPostFeed -> {
                postFeedSearchRepository.save(savedPostFeed);
                return Mono.just(savedPostFeed);
            })
            .map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostFeedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFeeds");
        return postFeedRepository.findAllBy(pageable).map(postFeedMapper::toDto);
    }

    public Flux<PostFeedDTO> findAllWithEagerRelationships(Pageable pageable) {
        return postFeedRepository.findAllWithEagerRelationships(pageable).map(postFeedMapper::toDto);
    }

    public Mono<Long> countAll() {
        return postFeedRepository.count();
    }

    public Mono<Long> searchCount() {
        return postFeedSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PostFeedDTO> findOne(Long id) {
        log.debug("Request to get PostFeed : {}", id);
        return postFeedRepository.findOneWithEagerRelationships(id).map(postFeedMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PostFeed : {}", id);
        return postFeedRepository.deleteById(id).then(postFeedSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostFeedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostFeeds for query {}", query);
        return postFeedSearchRepository.search(query, pageable).map(postFeedMapper::toDto);
    }
}
