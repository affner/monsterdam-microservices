package com.fanflip.interactions.service.impl;

import com.fanflip.interactions.domain.PostFeed;
import com.fanflip.interactions.repository.PostFeedRepository;
import com.fanflip.interactions.repository.search.PostFeedSearchRepository;
import com.fanflip.interactions.service.PostFeedService;
import com.fanflip.interactions.service.dto.PostFeedDTO;
import com.fanflip.interactions.service.mapper.PostFeedMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.interactions.domain.PostFeed}.
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
    public PostFeedDTO save(PostFeedDTO postFeedDTO) {
        log.debug("Request to save PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        PostFeedDTO result = postFeedMapper.toDto(postFeed);
        postFeedSearchRepository.index(postFeed);
        return result;
    }

    @Override
    public PostFeedDTO update(PostFeedDTO postFeedDTO) {
        log.debug("Request to update PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        PostFeedDTO result = postFeedMapper.toDto(postFeed);
        postFeedSearchRepository.index(postFeed);
        return result;
    }

    @Override
    public Optional<PostFeedDTO> partialUpdate(PostFeedDTO postFeedDTO) {
        log.debug("Request to partially update PostFeed : {}", postFeedDTO);

        return postFeedRepository
            .findById(postFeedDTO.getId())
            .map(existingPostFeed -> {
                postFeedMapper.partialUpdate(existingPostFeed, postFeedDTO);

                return existingPostFeed;
            })
            .map(postFeedRepository::save)
            .map(savedPostFeed -> {
                postFeedSearchRepository.index(savedPostFeed);
                return savedPostFeed;
            })
            .map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFeeds");
        return postFeedRepository.findAll(pageable).map(postFeedMapper::toDto);
    }

    public Page<PostFeedDTO> findAllWithEagerRelationships(Pageable pageable) {
        return postFeedRepository.findAllWithEagerRelationships(pageable).map(postFeedMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostFeedDTO> findOne(Long id) {
        log.debug("Request to get PostFeed : {}", id);
        return postFeedRepository.findOneWithEagerRelationships(id).map(postFeedMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostFeed : {}", id);
        postFeedRepository.deleteById(id);
        postFeedSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostFeeds for query {}", query);
        return postFeedSearchRepository.search(query, pageable).map(postFeedMapper::toDto);
    }
}
