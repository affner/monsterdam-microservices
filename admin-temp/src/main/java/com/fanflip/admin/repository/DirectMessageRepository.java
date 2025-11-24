package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.DirectMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DirectMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectMessageRepository extends ReactiveCrudRepository<DirectMessage, Long>, DirectMessageRepositoryInternal {
    Flux<DirectMessage> findAllBy(Pageable pageable);

    @Override
    Mono<DirectMessage> findOneWithEagerRelationships(Long id);

    @Override
    Flux<DirectMessage> findAllWithEagerRelationships();

    @Override
    Flux<DirectMessage> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM direct_message entity WHERE entity.content_package_id = :id")
    Flux<DirectMessage> findByContentPackage(Long id);

    @Query("SELECT * FROM direct_message entity WHERE entity.content_package_id IS NULL")
    Flux<DirectMessage> findAllWhereContentPackageIsNull();

    @Query("SELECT * FROM direct_message entity WHERE entity.response_to_id = :id")
    Flux<DirectMessage> findByResponseTo(Long id);

    @Query("SELECT * FROM direct_message entity WHERE entity.response_to_id IS NULL")
    Flux<DirectMessage> findAllWhereResponseToIsNull();

    @Query("SELECT * FROM direct_message entity WHERE entity.replied_story_id = :id")
    Flux<DirectMessage> findByRepliedStory(Long id);

    @Query("SELECT * FROM direct_message entity WHERE entity.replied_story_id IS NULL")
    Flux<DirectMessage> findAllWhereRepliedStoryIsNull();

    @Query("SELECT * FROM direct_message entity WHERE entity.user_id = :id")
    Flux<DirectMessage> findByUser(Long id);

    @Query("SELECT * FROM direct_message entity WHERE entity.user_id IS NULL")
    Flux<DirectMessage> findAllWhereUserIsNull();

    @Query("SELECT * FROM direct_message entity WHERE entity.id not in (select admin_announcement_id from admin_announcement)")
    Flux<DirectMessage> findAllWhereAdminAnnouncementIsNull();

    @Query("SELECT * FROM direct_message entity WHERE entity.id not in (select purchased_tip_id from purchased_tip)")
    Flux<DirectMessage> findAllWherePurchasedTipIsNull();

    @Override
    <S extends DirectMessage> Mono<S> save(S entity);

    @Override
    Flux<DirectMessage> findAll();

    @Override
    Mono<DirectMessage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DirectMessageRepositoryInternal {
    <S extends DirectMessage> Mono<S> save(S entity);

    Flux<DirectMessage> findAllBy(Pageable pageable);

    Flux<DirectMessage> findAll();

    Mono<DirectMessage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DirectMessage> findAllBy(Pageable pageable, Criteria criteria);

    Mono<DirectMessage> findOneWithEagerRelationships(Long id);

    Flux<DirectMessage> findAllWithEagerRelationships();

    Flux<DirectMessage> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
