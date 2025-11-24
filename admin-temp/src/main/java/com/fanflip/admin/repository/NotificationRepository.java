package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends ReactiveCrudRepository<Notification, Long>, NotificationRepositoryInternal {
    Flux<Notification> findAllBy(Pageable pageable);

    @Query("SELECT * FROM notification entity WHERE entity.commented_user_id = :id")
    Flux<Notification> findByCommentedUser(Long id);

    @Query("SELECT * FROM notification entity WHERE entity.commented_user_id IS NULL")
    Flux<Notification> findAllWhereCommentedUserIsNull();

    @Query("SELECT * FROM notification entity WHERE entity.messaged_user_id = :id")
    Flux<Notification> findByMessagedUser(Long id);

    @Query("SELECT * FROM notification entity WHERE entity.messaged_user_id IS NULL")
    Flux<Notification> findAllWhereMessagedUserIsNull();

    @Query("SELECT * FROM notification entity WHERE entity.mentioner_user_in_post_id = :id")
    Flux<Notification> findByMentionerUserInPost(Long id);

    @Query("SELECT * FROM notification entity WHERE entity.mentioner_user_in_post_id IS NULL")
    Flux<Notification> findAllWhereMentionerUserInPostIsNull();

    @Query("SELECT * FROM notification entity WHERE entity.mentioner_user_in_comment_id = :id")
    Flux<Notification> findByMentionerUserInComment(Long id);

    @Query("SELECT * FROM notification entity WHERE entity.mentioner_user_in_comment_id IS NULL")
    Flux<Notification> findAllWhereMentionerUserInCommentIsNull();

    @Override
    <S extends Notification> Mono<S> save(S entity);

    @Override
    Flux<Notification> findAll();

    @Override
    Mono<Notification> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface NotificationRepositoryInternal {
    <S extends Notification> Mono<S> save(S entity);

    Flux<Notification> findAllBy(Pageable pageable);

    Flux<Notification> findAll();

    Mono<Notification> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Notification> findAllBy(Pageable pageable, Criteria criteria);
}
