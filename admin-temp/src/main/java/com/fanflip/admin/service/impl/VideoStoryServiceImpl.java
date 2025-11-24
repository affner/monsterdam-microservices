package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.VideoStoryRepository;
import com.monsterdam.admin.repository.search.VideoStorySearchRepository;
import com.monsterdam.admin.service.VideoStoryService;
import com.monsterdam.admin.service.dto.VideoStoryDTO;
import com.monsterdam.admin.service.mapper.VideoStoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.VideoStory}.
 */
@Service
@Transactional
public class VideoStoryServiceImpl implements VideoStoryService {

    private final Logger log = LoggerFactory.getLogger(VideoStoryServiceImpl.class);

    private final VideoStoryRepository videoStoryRepository;

    private final VideoStoryMapper videoStoryMapper;

    private final VideoStorySearchRepository videoStorySearchRepository;

    public VideoStoryServiceImpl(
        VideoStoryRepository videoStoryRepository,
        VideoStoryMapper videoStoryMapper,
        VideoStorySearchRepository videoStorySearchRepository
    ) {
        this.videoStoryRepository = videoStoryRepository;
        this.videoStoryMapper = videoStoryMapper;
        this.videoStorySearchRepository = videoStorySearchRepository;
    }

    @Override
    public Mono<VideoStoryDTO> save(VideoStoryDTO videoStoryDTO) {
        log.debug("Request to save VideoStory : {}", videoStoryDTO);
        return videoStoryRepository
            .save(videoStoryMapper.toEntity(videoStoryDTO))
            .flatMap(videoStorySearchRepository::save)
            .map(videoStoryMapper::toDto);
    }

    @Override
    public Mono<VideoStoryDTO> update(VideoStoryDTO videoStoryDTO) {
        log.debug("Request to update VideoStory : {}", videoStoryDTO);
        return videoStoryRepository
            .save(videoStoryMapper.toEntity(videoStoryDTO))
            .flatMap(videoStorySearchRepository::save)
            .map(videoStoryMapper::toDto);
    }

    @Override
    public Mono<VideoStoryDTO> partialUpdate(VideoStoryDTO videoStoryDTO) {
        log.debug("Request to partially update VideoStory : {}", videoStoryDTO);

        return videoStoryRepository
            .findById(videoStoryDTO.getId())
            .map(existingVideoStory -> {
                videoStoryMapper.partialUpdate(existingVideoStory, videoStoryDTO);

                return existingVideoStory;
            })
            .flatMap(videoStoryRepository::save)
            .flatMap(savedVideoStory -> {
                videoStorySearchRepository.save(savedVideoStory);
                return Mono.just(savedVideoStory);
            })
            .map(videoStoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VideoStoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VideoStories");
        return videoStoryRepository.findAllBy(pageable).map(videoStoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return videoStoryRepository.count();
    }

    public Mono<Long> searchCount() {
        return videoStorySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<VideoStoryDTO> findOne(Long id) {
        log.debug("Request to get VideoStory : {}", id);
        return videoStoryRepository.findById(id).map(videoStoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete VideoStory : {}", id);
        return videoStoryRepository.deleteById(id).then(videoStorySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VideoStoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of VideoStories for query {}", query);
        return videoStorySearchRepository.search(query, pageable).map(videoStoryMapper::toDto);
    }
}
