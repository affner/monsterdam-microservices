package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.ChatRoomRepository;
import com.fanflip.admin.repository.search.ChatRoomSearchRepository;
import com.fanflip.admin.service.ChatRoomService;
import com.fanflip.admin.service.dto.ChatRoomDTO;
import com.fanflip.admin.service.mapper.ChatRoomMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.ChatRoom}.
 */
@Service
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final Logger log = LoggerFactory.getLogger(ChatRoomServiceImpl.class);

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;

    private final ChatRoomSearchRepository chatRoomSearchRepository;

    public ChatRoomServiceImpl(
        ChatRoomRepository chatRoomRepository,
        ChatRoomMapper chatRoomMapper,
        ChatRoomSearchRepository chatRoomSearchRepository
    ) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMapper = chatRoomMapper;
        this.chatRoomSearchRepository = chatRoomSearchRepository;
    }

    @Override
    public Mono<ChatRoomDTO> save(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to save ChatRoom : {}", chatRoomDTO);
        return chatRoomRepository
            .save(chatRoomMapper.toEntity(chatRoomDTO))
            .flatMap(chatRoomSearchRepository::save)
            .map(chatRoomMapper::toDto);
    }

    @Override
    public Mono<ChatRoomDTO> update(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to update ChatRoom : {}", chatRoomDTO);
        return chatRoomRepository
            .save(chatRoomMapper.toEntity(chatRoomDTO))
            .flatMap(chatRoomSearchRepository::save)
            .map(chatRoomMapper::toDto);
    }

    @Override
    public Mono<ChatRoomDTO> partialUpdate(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to partially update ChatRoom : {}", chatRoomDTO);

        return chatRoomRepository
            .findById(chatRoomDTO.getId())
            .map(existingChatRoom -> {
                chatRoomMapper.partialUpdate(existingChatRoom, chatRoomDTO);

                return existingChatRoom;
            })
            .flatMap(chatRoomRepository::save)
            .flatMap(savedChatRoom -> {
                chatRoomSearchRepository.save(savedChatRoom);
                return Mono.just(savedChatRoom);
            })
            .map(chatRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ChatRoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatRooms");
        return chatRoomRepository.findAllBy(pageable).map(chatRoomMapper::toDto);
    }

    public Flux<ChatRoomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return chatRoomRepository.findAllWithEagerRelationships(pageable).map(chatRoomMapper::toDto);
    }

    public Mono<Long> countAll() {
        return chatRoomRepository.count();
    }

    public Mono<Long> searchCount() {
        return chatRoomSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ChatRoomDTO> findOne(Long id) {
        log.debug("Request to get ChatRoom : {}", id);
        return chatRoomRepository.findOneWithEagerRelationships(id).map(chatRoomMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ChatRoom : {}", id);
        return chatRoomRepository.deleteById(id).then(chatRoomSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ChatRoomDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChatRooms for query {}", query);
        return chatRoomSearchRepository.search(query, pageable).map(chatRoomMapper::toDto);
    }
}
