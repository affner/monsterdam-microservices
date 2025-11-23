package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PostCommentRepository;
import com.fanflip.admin.repository.search.PostCommentSearchRepository;
import com.fanflip.admin.service.PostCommentService;
import com.fanflip.admin.service.dto.PostCommentDTO;
import com.fanflip.admin.service.mapper.PostCommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PostComment}.
 */
@Service
@Transactional
public class PostCommentServiceImpl implements PostCommentService {

    private final Logger log = LoggerFactory.getLogger(PostCommentServiceImpl.class);

    private final PostCommentRepository postCommentRepository;

    private final PostCommentMapper postCommentMapper;

    private final PostCommentSearchRepository postCommentSearchRepository;

    public PostCommentServiceImpl(
        PostCommentRepository postCommentRepository,
        PostCommentMapper postCommentMapper,
        PostCommentSearchRepository postCommentSearchRepository
    ) {
        this.postCommentRepository = postCommentRepository;
        this.postCommentMapper = postCommentMapper;
        this.postCommentSearchRepository = postCommentSearchRepository;
    }

    @Override
    public Mono<PostCommentDTO> save(PostCommentDTO postCommentDTO) {
        log.debug("Request to save PostComment : {}", postCommentDTO);
        return postCommentRepository
            .save(postCommentMapper.toEntity(postCommentDTO))
            .flatMap(postCommentSearchRepository::save)
            .map(postCommentMapper::toDto);
    }

    @Override
    public Mono<PostCommentDTO> update(PostCommentDTO postCommentDTO) {
        log.debug("Request to update PostComment : {}", postCommentDTO);
        return postCommentRepository
            .save(postCommentMapper.toEntity(postCommentDTO))
            .flatMap(postCommentSearchRepository::save)
            .map(postCommentMapper::toDto);
    }

    @Override
    public Mono<PostCommentDTO> partialUpdate(PostCommentDTO postCommentDTO) {
        log.debug("Request to partially update PostComment : {}", postCommentDTO);

        return postCommentRepository
            .findById(postCommentDTO.getId())
            .map(existingPostComment -> {
                postCommentMapper.partialUpdate(existingPostComment, postCommentDTO);

                return existingPostComment;
            })
            .flatMap(postCommentRepository::save)
            .flatMap(savedPostComment -> {
                postCommentSearchRepository.save(savedPostComment);
                return Mono.just(savedPostComment);
            })
            .map(postCommentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostCommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostComments");
        return postCommentRepository.findAllBy(pageable).map(postCommentMapper::toDto);
    }

    public Flux<PostCommentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return postCommentRepository.findAllWithEagerRelationships(pageable).map(postCommentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return postCommentRepository.count();
    }

    public Mono<Long> searchCount() {
        return postCommentSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PostCommentDTO> findOne(Long id) {
        log.debug("Request to get PostComment : {}", id);
        return postCommentRepository.findOneWithEagerRelationships(id).map(postCommentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PostComment : {}", id);
        return postCommentRepository.deleteById(id).then(postCommentSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PostCommentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostComments for query {}", query);
        return postCommentSearchRepository.search(query, pageable).map(postCommentMapper::toDto);
    }
}
