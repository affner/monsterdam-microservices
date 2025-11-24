package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.UserMention;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserMention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMentionRepository extends ReactiveCrudRepository<UserMention, Long>, UserMentionRepositoryInternal {
    Flux<UserMention> findAllBy(Pageable pageable);

    @Override
    Mono<UserMention> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserMention> findAllWithEagerRelationships();

    @Override
    Flux<UserMention> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_mention entity WHERE entity.origin_post_id = :id")
    Flux<UserMention> findByOriginPost(Long id);

    @Query("SELECT * FROM user_mention entity WHERE entity.origin_post_id IS NULL")
    Flux<UserMention> findAllWhereOriginPostIsNull();

    @Query("SELECT * FROM user_mention entity WHERE entity.origin_post_comment_id = :id")
    Flux<UserMention> findByOriginPostComment(Long id);

    @Query("SELECT * FROM user_mention entity WHERE entity.origin_post_comment_id IS NULL")
    Flux<UserMention> findAllWhereOriginPostCommentIsNull();

    @Query("SELECT * FROM user_mention entity WHERE entity.mentioned_user_id = :id")
    Flux<UserMention> findByMentionedUser(Long id);

    @Query("SELECT * FROM user_mention entity WHERE entity.mentioned_user_id IS NULL")
    Flux<UserMention> findAllWhereMentionedUserIsNull();

    @Override
    <S extends UserMention> Mono<S> save(S entity);

    @Override
    Flux<UserMention> findAll();

    @Override
    Mono<UserMention> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserMentionRepositoryInternal {
    <S extends UserMention> Mono<S> save(S entity);

    Flux<UserMention> findAllBy(Pageable pageable);

    Flux<UserMention> findAll();

    Mono<UserMention> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserMention> findAllBy(Pageable pageable, Criteria criteria);

    Mono<UserMention> findOneWithEagerRelationships(Long id);

    Flux<UserMention> findAllWithEagerRelationships();

    Flux<UserMention> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
