package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PostPollRepository;
import com.fanflip.admin.repository.search.PostPollSearchRepository;
import com.fanflip.admin.service.PostPollService;
import com.fanflip.admin.service.dto.PostPollDTO;
import com.fanflip.admin.service.mapper.PostPollMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PostPoll}.
 */
@Service
@Transactional
public class PostPollServiceImpl implements PostPollService {

    private final Logger log = LoggerFactory.getLogger(PostPollServiceImpl.class);

    private final PostPollRepository postPollRepository;

    private final PostPollMapper postPollMapper;

    private final PostPollSearchRepository postPollSearchRepository;

    public PostPollServiceImpl(
        PostPollRepository postPollRepository,
        PostPollMapper postPollMapper,
        PostPollSearchRepository postPollSearchRepository
    ) {
        this.postPollRepository = postPollRepository;
        this.postPollMapper = postPollMapper;
        this.postPollSearchRepository = postPollSearchRepository;
    }

    @Override
    public Mono<PostPollDTO> save(PostPollDTO postPollDTO) {
        log.debug("Request to save PostPoll : {}", postPollDTO);
        return postPollRepository
            .save(postPollMapper.toEntity(postPollDTO))
            .flatMap(postPollSearchRepository::save)
            .map(postPollMapper::toDto);
    }

    @Override
    public Mono<PostPollDTO> update(PostPollDTO postPollDTO) {
        log.debug("Request to update PostPoll : {}", postPollDTO);
        return postPollRepository
            .save(postPollMapper.toEntity(postPollDTO))
            .flatMap(postPollSearchRepository::save)
            .map(postPollMapper::toDto);
    }

    @Override
    public Mono<PostPollDTO> partialUpdate(PostPollDTO postPollDTO) {
        log.debug("Request to partially update PostPoll : {}", postPollDTO);

        return postPollRepository
            .findById(postPollDTO.getId())
            .map(existingPostPoll -> {
                postPollMapper.partialUpdate(existingPostPoll, postPollDTO);

                return existingPostPoll;
            })
            .flatMap(postPollRepository::save)
            .flatMap(savedPostPoll -> {
                postPollSearchRepository.save(savedPostPoll);
                return Mono.just(savedPostPoll);
            })
            .map(postPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostPollDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostPolls");
        return postPollRepository.findAllBy(pageable).map(postPollMapper::toDto);
    }

    /**
     *  Get all the postPolls where Post is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PostPollDTO> findAllWherePostIsNull() {
        log.debug("Request to get all postPolls where Post is null");
        return postPollRepository.findAllWherePostIsNull().map(postPollMapper::toDto);
    }

    public Mono<Long> countAll() {
        return postPollRepository.count();
    }

    public Mono<Long> searchCount() {
        return postPollSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PostPollDTO> findOne(Long id) {
        log.debug("Request to get PostPoll : {}", id);
        return postPollRepository.findById(id).map(postPollMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PostPoll : {}", id);
        return postPollRepository.deleteById(id).then(postPollSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostPollDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostPolls for query {}", query);
        return postPollSearchRepository.search(query, pageable).map(postPollMapper::toDto);
    }
}
