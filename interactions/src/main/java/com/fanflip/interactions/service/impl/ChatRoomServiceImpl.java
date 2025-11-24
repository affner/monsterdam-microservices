package com.monsterdam.interactions.service.impl;

import com.monsterdam.interactions.domain.ChatRoom;
import com.monsterdam.interactions.repository.ChatRoomRepository;
import com.monsterdam.interactions.service.ChatRoomService;
import com.monsterdam.interactions.service.dto.ChatRoomDTO;
import com.monsterdam.interactions.service.mapper.ChatRoomMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.interactions.domain.ChatRoom}.
 */
@Service
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final Logger log = LoggerFactory.getLogger(ChatRoomServiceImpl.class);

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomServiceImpl(ChatRoomRepository chatRoomRepository, ChatRoomMapper chatRoomMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMapper = chatRoomMapper;
    }

    @Override
    public ChatRoomDTO save(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to save ChatRoom : {}", chatRoomDTO);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomDTO);
        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDto(chatRoom);
    }

    @Override
    public ChatRoomDTO update(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to update ChatRoom : {}", chatRoomDTO);
        ChatRoom chatRoom = chatRoomMapper.toEntity(chatRoomDTO);
        chatRoom = chatRoomRepository.save(chatRoom);
        return chatRoomMapper.toDto(chatRoom);
    }

    @Override
    public Optional<ChatRoomDTO> partialUpdate(ChatRoomDTO chatRoomDTO) {
        log.debug("Request to partially update ChatRoom : {}", chatRoomDTO);

        return chatRoomRepository
            .findById(chatRoomDTO.getId())
            .map(existingChatRoom -> {
                chatRoomMapper.partialUpdate(existingChatRoom, chatRoomDTO);

                return existingChatRoom;
            })
            .map(chatRoomRepository::save)
            .map(chatRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatRoomDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatRooms");
        return chatRoomRepository.findAll(pageable).map(chatRoomMapper::toDto);
    }

    public Page<ChatRoomDTO> findAllWithEagerRelationships(Pageable pageable) {
        return chatRoomRepository.findAllWithEagerRelationships(pageable).map(chatRoomMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChatRoomDTO> findOne(Long id) {
        log.debug("Request to get ChatRoom : {}", id);
        return chatRoomRepository.findOneWithEagerRelationships(id).map(chatRoomMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChatRoom : {}", id);
        chatRoomRepository.deleteById(id);
    }
}
