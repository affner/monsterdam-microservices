package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.ModerationActionRepository;
import com.monsterdam.admin.repository.search.ModerationActionSearchRepository;
import com.monsterdam.admin.service.ModerationActionService;
import com.monsterdam.admin.service.dto.ModerationActionDTO;
import com.monsterdam.admin.service.mapper.ModerationActionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.ModerationAction}.
 */
@Service
@Transactional
public class ModerationActionServiceImpl implements ModerationActionService {

    private final Logger log = LoggerFactory.getLogger(ModerationActionServiceImpl.class);

    private final ModerationActionRepository moderationActionRepository;

    private final ModerationActionMapper moderationActionMapper;

    private final ModerationActionSearchRepository moderationActionSearchRepository;

    public ModerationActionServiceImpl(
        ModerationActionRepository moderationActionRepository,
        ModerationActionMapper moderationActionMapper,
        ModerationActionSearchRepository moderationActionSearchRepository
    ) {
        this.moderationActionRepository = moderationActionRepository;
        this.moderationActionMapper = moderationActionMapper;
        this.moderationActionSearchRepository = moderationActionSearchRepository;
    }

    @Override
    public Mono<ModerationActionDTO> save(ModerationActionDTO moderationActionDTO) {
        log.debug("Request to save ModerationAction : {}", moderationActionDTO);
        return moderationActionRepository
            .save(moderationActionMapper.toEntity(moderationActionDTO))
            .flatMap(moderationActionSearchRepository::save)
            .map(moderationActionMapper::toDto);
    }

    @Override
    public Mono<ModerationActionDTO> update(ModerationActionDTO moderationActionDTO) {
        log.debug("Request to update ModerationAction : {}", moderationActionDTO);
        return moderationActionRepository
            .save(moderationActionMapper.toEntity(moderationActionDTO))
            .flatMap(moderationActionSearchRepository::save)
            .map(moderationActionMapper::toDto);
    }

    @Override
    public Mono<ModerationActionDTO> partialUpdate(ModerationActionDTO moderationActionDTO) {
        log.debug("Request to partially update ModerationAction : {}", moderationActionDTO);

        return moderationActionRepository
            .findById(moderationActionDTO.getId())
            .map(existingModerationAction -> {
                moderationActionMapper.partialUpdate(existingModerationAction, moderationActionDTO);

                return existingModerationAction;
            })
            .flatMap(moderationActionRepository::save)
            .flatMap(savedModerationAction -> {
                moderationActionSearchRepository.save(savedModerationAction);
                return Mono.just(savedModerationAction);
            })
            .map(moderationActionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ModerationActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ModerationActions");
        return moderationActionRepository.findAllBy(pageable).map(moderationActionMapper::toDto);
    }

    /**
     *  Get all the moderationActions where AssistanceTicket is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModerationActionDTO> findAllWhereAssistanceTicketIsNull() {
        log.debug("Request to get all moderationActions where AssistanceTicket is null");
        return moderationActionRepository.findAllWhereAssistanceTicketIsNull().map(moderationActionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return moderationActionRepository.count();
    }

    public Mono<Long> searchCount() {
        return moderationActionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ModerationActionDTO> findOne(Long id) {
        log.debug("Request to get ModerationAction : {}", id);
        return moderationActionRepository.findById(id).map(moderationActionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ModerationAction : {}", id);
        return moderationActionRepository.deleteById(id).then(moderationActionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ModerationActionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ModerationActions for query {}", query);
        return moderationActionSearchRepository.search(query, pageable).map(moderationActionMapper::toDto);
    }
}
