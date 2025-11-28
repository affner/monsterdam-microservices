package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.PostFeedHashTagRelation;
import com.monsterdam.profile.repository.PostFeedHashTagRelationRepository;
import com.monsterdam.profile.service.PostFeedHashTagRelationService;
import com.monsterdam.profile.service.dto.PostFeedHashTagRelationDTO;
import com.monsterdam.profile.service.mapper.PostFeedHashTagRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.profile.domain.PostFeedHashTagRelation}.
 */
@Service
@Transactional
public class PostFeedHashTagRelationServiceImpl implements PostFeedHashTagRelationService {

    private final Logger log = LoggerFactory.getLogger(PostFeedHashTagRelationServiceImpl.class);

    private final PostFeedHashTagRelationRepository postFeedHashTagRelationRepository;

    private final PostFeedHashTagRelationMapper postFeedHashTagRelationMapper;

    public PostFeedHashTagRelationServiceImpl(
        PostFeedHashTagRelationRepository postFeedHashTagRelationRepository,
        PostFeedHashTagRelationMapper postFeedHashTagRelationMapper
    ) {
        this.postFeedHashTagRelationRepository = postFeedHashTagRelationRepository;
        this.postFeedHashTagRelationMapper = postFeedHashTagRelationMapper;
    }

    @Override
    public PostFeedHashTagRelationDTO save(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO) {
        log.debug("Request to save PostFeedHashTagRelation : {}", postFeedHashTagRelationDTO);
        PostFeedHashTagRelation postFeedHashTagRelation = postFeedHashTagRelationMapper.toEntity(postFeedHashTagRelationDTO);
        postFeedHashTagRelation = postFeedHashTagRelationRepository.save(postFeedHashTagRelation);
        return postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);
    }

    @Override
    public PostFeedHashTagRelationDTO update(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO) {
        log.debug("Request to update PostFeedHashTagRelation : {}", postFeedHashTagRelationDTO);
        PostFeedHashTagRelation postFeedHashTagRelation = postFeedHashTagRelationMapper.toEntity(postFeedHashTagRelationDTO);
        postFeedHashTagRelation = postFeedHashTagRelationRepository.save(postFeedHashTagRelation);
        return postFeedHashTagRelationMapper.toDto(postFeedHashTagRelation);
    }

    @Override
    public Optional<PostFeedHashTagRelationDTO> partialUpdate(PostFeedHashTagRelationDTO postFeedHashTagRelationDTO) {
        log.debug("Request to partially update PostFeedHashTagRelation : {}", postFeedHashTagRelationDTO);

        return postFeedHashTagRelationRepository
            .findById(postFeedHashTagRelationDTO.getId())
            .map(existingPostFeedHashTagRelation -> {
                postFeedHashTagRelationMapper.partialUpdate(existingPostFeedHashTagRelation, postFeedHashTagRelationDTO);

                return existingPostFeedHashTagRelation;
            })
            .map(postFeedHashTagRelationRepository::save)
            .map(postFeedHashTagRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostFeedHashTagRelationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFeedHashTagRelations");
        return postFeedHashTagRelationRepository.findAll(pageable).map(postFeedHashTagRelationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostFeedHashTagRelationDTO> findOne(Long id) {
        log.debug("Request to get PostFeedHashTagRelation : {}", id);
        return postFeedHashTagRelationRepository.findById(id).map(postFeedHashTagRelationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostFeedHashTagRelation : {}", id);
        postFeedHashTagRelationRepository.deleteById(id);
    }
}
