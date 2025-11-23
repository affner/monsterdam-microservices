package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.ChatRoomDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.ChatRoom}.
 */
public interface ChatRoomService {
    /**
     * Save a chatRoom.
     *
     * @param chatRoomDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ChatRoomDTO> save(ChatRoomDTO chatRoomDTO);

    /**
     * Updates a chatRoom.
     *
     * @param chatRoomDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ChatRoomDTO> update(ChatRoomDTO chatRoomDTO);

    /**
     * Partially updates a chatRoom.
     *
     * @param chatRoomDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ChatRoomDTO> partialUpdate(ChatRoomDTO chatRoomDTO);

    /**
     * Get all the chatRooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ChatRoomDTO> findAll(Pageable pageable);

    /**
     * Get all the chatRooms with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ChatRoomDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of chatRooms available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of chatRooms available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" chatRoom.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ChatRoomDTO> findOne(Long id);

    /**
     * Delete the "id" chatRoom.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the chatRoom corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<ChatRoomDTO> search(String query, Pageable pageable);
}
