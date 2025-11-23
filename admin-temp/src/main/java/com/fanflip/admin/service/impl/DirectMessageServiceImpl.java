package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.DirectMessageRepository;
import com.fanflip.admin.repository.search.DirectMessageSearchRepository;
import com.fanflip.admin.service.DirectMessageService;
import com.fanflip.admin.service.dto.DirectMessageDTO;
import com.fanflip.admin.service.mapper.DirectMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.DirectMessage}.
 */
@Service
@Transactional
public class DirectMessageServiceImpl implements DirectMessageService {

    private final Logger log = LoggerFactory.getLogger(DirectMessageServiceImpl.class);

    private final DirectMessageRepository directMessageRepository;

    private final DirectMessageMapper directMessageMapper;

    private final DirectMessageSearchRepository directMessageSearchRepository;

    public DirectMessageServiceImpl(
        DirectMessageRepository directMessageRepository,
        DirectMessageMapper directMessageMapper,
        DirectMessageSearchRepository directMessageSearchRepository
    ) {
        this.directMessageRepository = directMessageRepository;
        this.directMessageMapper = directMessageMapper;
        this.directMessageSearchRepository = directMessageSearchRepository;
    }

    @Override
    public Mono<DirectMessageDTO> save(DirectMessageDTO directMessageDTO) {
        log.debug("Request to save DirectMessage : {}", directMessageDTO);
        return directMessageRepository
            .save(directMessageMapper.toEntity(directMessageDTO))
            .flatMap(directMessageSearchRepository::save)
            .map(directMessageMapper::toDto);
    }

    @Override
    public Mono<DirectMessageDTO> update(DirectMessageDTO directMessageDTO) {
        log.debug("Request to update DirectMessage : {}", directMessageDTO);
        return directMessageRepository
            .save(directMessageMapper.toEntity(directMessageDTO))
            .flatMap(directMessageSearchRepository::save)
            .map(directMessageMapper::toDto);
    }

    @Override
    public Mono<DirectMessageDTO> partialUpdate(DirectMessageDTO directMessageDTO) {
        log.debug("Request to partially update DirectMessage : {}", directMessageDTO);

        return directMessageRepository
            .findById(directMessageDTO.getId())
            .map(existingDirectMessage -> {
                directMessageMapper.partialUpdate(existingDirectMessage, directMessageDTO);

                return existingDirectMessage;
            })
            .flatMap(directMessageRepository::save)
            .flatMap(savedDirectMessage -> {
                directMessageSearchRepository.save(savedDirectMessage);
                return Mono.just(savedDirectMessage);
            })
            .map(directMessageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DirectMessageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DirectMessages");
        return directMessageRepository.findAllBy(pageable).map(directMessageMapper::toDto);
    }

    public Flux<DirectMessageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return directMessageRepository.findAllWithEagerRelationships(pageable).map(directMessageMapper::toDto);
    }

    /**
     *  Get all the directMessages where AdminAnnouncement is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DirectMessageDTO> findAllWhereAdminAnnouncementIsNull() {
        log.debug("Request to get all directMessages where AdminAnnouncement is null");
        return directMessageRepository.findAllWhereAdminAnnouncementIsNull().map(directMessageMapper::toDto);
    }

    /**
     *  Get all the directMessages where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DirectMessageDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all directMessages where PurchasedTip is null");
        return directMessageRepository.findAllWherePurchasedTipIsNull().map(directMessageMapper::toDto);
    }

    public Mono<Long> countAll() {
        return directMessageRepository.count();
    }

    public Mono<Long> searchCount() {
        return directMessageSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DirectMessageDTO> findOne(Long id) {
        log.debug("Request to get DirectMessage : {}", id);
        return directMessageRepository.findOneWithEagerRelationships(id).map(directMessageMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DirectMessage : {}", id);
        return directMessageRepository.deleteById(id).then(directMessageSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DirectMessageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DirectMessages for query {}", query);
        return directMessageSearchRepository.search(query, pageable).map(directMessageMapper::toDto);
    }
}
