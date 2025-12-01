package com.monsterdam.interactions.service.impl;

import com.monsterdam.interactions.domain.PostFeed;
import com.monsterdam.interactions.repository.PostFeedRepository;
import com.monsterdam.interactions.service.PostFeedService;
import com.monsterdam.interactions.service.dto.PostFeedDTO;
import com.monsterdam.interactions.service.mapper.PostFeedMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.interactions.domain.PostFeed}.
 */
@Service
@Transactional
public class PostFeedServiceImpl implements PostFeedService {

    private final Logger log = LoggerFactory.getLogger(PostFeedServiceImpl.class);

    private final PostFeedRepository postFeedRepository;

    private final PostFeedMapper postFeedMapper;

    public PostFeedServiceImpl(PostFeedRepository postFeedRepository, PostFeedMapper postFeedMapper) {
        this.postFeedRepository = postFeedRepository;
        this.postFeedMapper = postFeedMapper;
    }

    @Override
    public PostFeedDTO save(PostFeedDTO postFeedDTO) {
        log.debug("Request to save PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        return postFeedMapper.toDto(postFeed);
    }

    @Override
    public PostFeedDTO update(PostFeedDTO postFeedDTO) {
        log.debug("Request to update PostFeed : {}", postFeedDTO);
        PostFeed postFeed = postFeedMapper.toEntity(postFeedDTO);
        postFeed = postFeedRepository.save(postFeed);
        return postFeedMapper.toDto(postFeed);
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
    }
}
