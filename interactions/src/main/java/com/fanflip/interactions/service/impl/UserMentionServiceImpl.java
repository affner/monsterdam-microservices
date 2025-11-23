package com.fanflip.interactions.service.impl;

import com.fanflip.interactions.domain.UserMention;
import com.fanflip.interactions.repository.UserMentionRepository;
import com.fanflip.interactions.service.UserMentionService;
import com.fanflip.interactions.service.dto.UserMentionDTO;
import com.fanflip.interactions.service.mapper.UserMentionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.interactions.domain.UserMention}.
 */
@Service
@Transactional
public class UserMentionServiceImpl implements UserMentionService {

    private final Logger log = LoggerFactory.getLogger(UserMentionServiceImpl.class);

    private final UserMentionRepository userMentionRepository;

    private final UserMentionMapper userMentionMapper;

    public UserMentionServiceImpl(UserMentionRepository userMentionRepository, UserMentionMapper userMentionMapper) {
        this.userMentionRepository = userMentionRepository;
        this.userMentionMapper = userMentionMapper;
    }

    @Override
    public UserMentionDTO save(UserMentionDTO userMentionDTO) {
        log.debug("Request to save UserMention : {}", userMentionDTO);
        UserMention userMention = userMentionMapper.toEntity(userMentionDTO);
        userMention = userMentionRepository.save(userMention);
        return userMentionMapper.toDto(userMention);
    }

    @Override
    public UserMentionDTO update(UserMentionDTO userMentionDTO) {
        log.debug("Request to update UserMention : {}", userMentionDTO);
        UserMention userMention = userMentionMapper.toEntity(userMentionDTO);
        userMention = userMentionRepository.save(userMention);
        return userMentionMapper.toDto(userMention);
    }

    @Override
    public Optional<UserMentionDTO> partialUpdate(UserMentionDTO userMentionDTO) {
        log.debug("Request to partially update UserMention : {}", userMentionDTO);

        return userMentionRepository
            .findById(userMentionDTO.getId())
            .map(existingUserMention -> {
                userMentionMapper.partialUpdate(existingUserMention, userMentionDTO);

                return existingUserMention;
            })
            .map(userMentionRepository::save)
            .map(userMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserMentionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserMentions");
        return userMentionRepository.findAll(pageable).map(userMentionMapper::toDto);
    }

    public Page<UserMentionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userMentionRepository.findAllWithEagerRelationships(pageable).map(userMentionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserMentionDTO> findOne(Long id) {
        log.debug("Request to get UserMention : {}", id);
        return userMentionRepository.findOneWithEagerRelationships(id).map(userMentionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserMention : {}", id);
        userMentionRepository.deleteById(id);
    }
}
