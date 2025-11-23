package com.fanflip.admin.repository;

import com.fanflip.admin.domain.UserEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEventRepository extends ReactiveCrudRepository<UserEvent, Long>, UserEventRepositoryInternal {
    Flux<UserEvent> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_event entity WHERE entity.creator_id = :id")
    Flux<UserEvent> findByCreator(Long id);

    @Query("SELECT * FROM user_event entity WHERE entity.creator_id IS NULL")
    Flux<UserEvent> findAllWhereCreatorIsNull();

    @Override
    <S extends UserEvent> Mono<S> save(S entity);

    @Override
    Flux<UserEvent> findAll();

    @Override
    Mono<UserEvent> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserEventRepositoryInternal {
    <S extends UserEvent> Mono<S> save(S entity);

    Flux<UserEvent> findAllBy(Pageable pageable);

    Flux<UserEvent> findAll();

    Mono<UserEvent> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserEvent> findAllBy(Pageable pageable, Criteria criteria);
}
