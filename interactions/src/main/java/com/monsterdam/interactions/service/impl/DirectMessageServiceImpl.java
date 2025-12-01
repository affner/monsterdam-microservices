package com.monsterdam.interactions.service.impl;

import com.monsterdam.interactions.domain.DirectMessage;
import com.monsterdam.interactions.repository.DirectMessageRepository;
import com.monsterdam.interactions.service.DirectMessageService;
import com.monsterdam.interactions.service.dto.DirectMessageDTO;
import com.monsterdam.interactions.service.mapper.DirectMessageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.interactions.domain.DirectMessage}.
 */
@Service
@Transactional
public class DirectMessageServiceImpl implements DirectMessageService {

    private final Logger log = LoggerFactory.getLogger(DirectMessageServiceImpl.class);

    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    public DirectMessageServiceImpl(DirectMessageRepository directMessageRepository, DirectMessageMapper directMessageMapper) {
        this.directMessageRepository = directMessageRepository;
        this.directMessageMapper = directMessageMapper;
    }

    @Override
    public DirectMessageDTO save(DirectMessageDTO directMessageDTO) {
        log.debug("Request to save DirectMessage : {}", directMessageDTO);
        DirectMessage directMessage = directMessageMapper.toEntity(directMessageDTO);
        directMessage = directMessageRepository.save(directMessage);
        return directMessageMapper.toDto(directMessage);
    }

    @Override
    public DirectMessageDTO update(DirectMessageDTO directMessageDTO) {
        log.debug("Request to update DirectMessage : {}", directMessageDTO);
        DirectMessage directMessage = directMessageMapper.toEntity(directMessageDTO);
        directMessage = directMessageRepository.save(directMessage);
        return directMessageMapper.toDto(directMessage);
    }

    @Override
    public Optional<DirectMessageDTO> partialUpdate(DirectMessageDTO directMessageDTO) {
        log.debug("Request to partially update DirectMessage : {}", directMessageDTO);

        return directMessageRepository
            .findById(directMessageDTO.getId())
            .map(existingDirectMessage -> {
                directMessageMapper.partialUpdate(existingDirectMessage, directMessageDTO);

                return existingDirectMessage;
            })
            .map(directMessageRepository::save)
            .map(directMessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DirectMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DirectMessages");
        return directMessageRepository.findAll(pageable).map(directMessageMapper::toDto);
    }

    public Page<DirectMessageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return directMessageRepository.findAllWithEagerRelationships(pageable).map(directMessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DirectMessageDTO> findOne(Long id) {
        log.debug("Request to get DirectMessage : {}", id);
        return directMessageRepository.findOneWithEagerRelationships(id).map(directMessageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DirectMessage : {}", id);
        directMessageRepository.deleteById(id);
    }
}
