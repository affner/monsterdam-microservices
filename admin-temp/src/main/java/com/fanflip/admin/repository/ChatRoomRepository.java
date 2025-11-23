package com.fanflip.admin.repository;

import com.fanflip.admin.domain.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ChatRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long>, ChatRoomRepositoryInternal {
    Flux<ChatRoom> findAllBy(Pageable pageable);

    @Override
    Mono<ChatRoom> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ChatRoom> findAllWithEagerRelationships();

    @Override
    Flux<ChatRoom> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM chat_room entity JOIN rel_chat_room__sent_messages joinTable ON entity.id = joinTable.sent_messages_id WHERE joinTable.sent_messages_id = :id"
    )
    Flux<ChatRoom> findBySentMessages(Long id);

    @Query("SELECT * FROM chat_room entity WHERE entity.user_id = :id")
    Flux<ChatRoom> findByUser(Long id);

    @Query("SELECT * FROM chat_room entity WHERE entity.user_id IS NULL")
    Flux<ChatRoom> findAllWhereUserIsNull();

    @Override
    <S extends ChatRoom> Mono<S> save(S entity);

    @Override
    Flux<ChatRoom> findAll();

    @Override
    Mono<ChatRoom> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ChatRoomRepositoryInternal {
    <S extends ChatRoom> Mono<S> save(S entity);

    Flux<ChatRoom> findAllBy(Pageable pageable);

    Flux<ChatRoom> findAll();

    Mono<ChatRoom> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ChatRoom> findAllBy(Pageable pageable, Criteria criteria);

    Mono<ChatRoom> findOneWithEagerRelationships(Long id);

    Flux<ChatRoom> findAllWithEagerRelationships();

    Flux<ChatRoom> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
