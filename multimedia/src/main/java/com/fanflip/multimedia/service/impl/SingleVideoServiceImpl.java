package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.SingleVideo;
import com.monsterdam.multimedia.repository.SingleVideoRepository;
import com.monsterdam.multimedia.service.SingleVideoService;
import com.monsterdam.multimedia.service.dto.SingleVideoDTO;
import com.monsterdam.multimedia.service.mapper.SingleVideoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.SingleVideo}.
 */
@Service
@Transactional
public class SingleVideoServiceImpl implements SingleVideoService {

    private final Logger log = LoggerFactory.getLogger(SingleVideoServiceImpl.class);

    private final SingleVideoRepository singleVideoRepository;

    private final SingleVideoMapper singleVideoMapper;

    public SingleVideoServiceImpl(SingleVideoRepository singleVideoRepository, SingleVideoMapper singleVideoMapper) {
        this.singleVideoRepository = singleVideoRepository;
        this.singleVideoMapper = singleVideoMapper;
    }

    @Override
    public SingleVideoDTO save(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to save SingleVideo : {}", singleVideoDTO);
        SingleVideo singleVideo = singleVideoMapper.toEntity(singleVideoDTO);
        singleVideo = singleVideoRepository.save(singleVideo);
        return singleVideoMapper.toDto(singleVideo);
    }

    @Override
    public SingleVideoDTO update(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to update SingleVideo : {}", singleVideoDTO);
        SingleVideo singleVideo = singleVideoMapper.toEntity(singleVideoDTO);
        singleVideo = singleVideoRepository.save(singleVideo);
        return singleVideoMapper.toDto(singleVideo);
    }

    @Override
    public Optional<SingleVideoDTO> partialUpdate(SingleVideoDTO singleVideoDTO) {
        log.debug("Request to partially update SingleVideo : {}", singleVideoDTO);

        return singleVideoRepository
            .findById(singleVideoDTO.getId())
            .map(existingSingleVideo -> {
                singleVideoMapper.partialUpdate(existingSingleVideo, singleVideoDTO);

                return existingSingleVideo;
            })
            .map(singleVideoRepository::save)
            .map(singleVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleVideoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleVideos");
        return singleVideoRepository.findAll(pageable).map(singleVideoMapper::toDto);
    }

    public Page<SingleVideoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return singleVideoRepository.findAllWithEagerRelationships(pageable).map(singleVideoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleVideoDTO> findOne(Long id) {
        log.debug("Request to get SingleVideo : {}", id);
        return singleVideoRepository.findOneWithEagerRelationships(id).map(singleVideoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SingleVideo : {}", id);
        singleVideoRepository.deleteById(id);
    }
}
