package com.fanflip.interactions.service.impl;

import com.fanflip.interactions.domain.LikeMark;
import com.fanflip.interactions.repository.LikeMarkRepository;
import com.fanflip.interactions.service.LikeMarkService;
import com.fanflip.interactions.service.dto.LikeMarkDTO;
import com.fanflip.interactions.service.mapper.LikeMarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.interactions.domain.LikeMark}.
 */
@Service
@Transactional
public class LikeMarkServiceImpl implements LikeMarkService {

    private final Logger log = LoggerFactory.getLogger(LikeMarkServiceImpl.class);

    private final LikeMarkRepository likeMarkRepository;

    private final LikeMarkMapper likeMarkMapper;

    public LikeMarkServiceImpl(LikeMarkRepository likeMarkRepository, LikeMarkMapper likeMarkMapper) {
        this.likeMarkRepository = likeMarkRepository;
        this.likeMarkMapper = likeMarkMapper;
    }

    @Override
    public LikeMarkDTO save(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to save LikeMark : {}", likeMarkDTO);
        LikeMark likeMark = likeMarkMapper.toEntity(likeMarkDTO);
        likeMark = likeMarkRepository.save(likeMark);
        return likeMarkMapper.toDto(likeMark);
    }

    @Override
    public LikeMarkDTO update(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to update LikeMark : {}", likeMarkDTO);
        LikeMark likeMark = likeMarkMapper.toEntity(likeMarkDTO);
        likeMark = likeMarkRepository.save(likeMark);
        return likeMarkMapper.toDto(likeMark);
    }

    @Override
    public Optional<LikeMarkDTO> partialUpdate(LikeMarkDTO likeMarkDTO) {
        log.debug("Request to partially update LikeMark : {}", likeMarkDTO);

        return likeMarkRepository
            .findById(likeMarkDTO.getId())
            .map(existingLikeMark -> {
                likeMarkMapper.partialUpdate(existingLikeMark, likeMarkDTO);

                return existingLikeMark;
            })
            .map(likeMarkRepository::save)
            .map(likeMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeMarkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LikeMarks");
        return likeMarkRepository.findAll(pageable).map(likeMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LikeMarkDTO> findOne(Long id) {
        log.debug("Request to get LikeMark : {}", id);
        return likeMarkRepository.findById(id).map(likeMarkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LikeMark : {}", id);
        likeMarkRepository.deleteById(id);
    }
}
