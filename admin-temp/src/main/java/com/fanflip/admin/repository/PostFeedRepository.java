package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PostFeed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PostFeed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostFeedRepository extends ReactiveCrudRepository<PostFeed, Long>, PostFeedRepositoryInternal {
    Flux<PostFeed> findAllBy(Pageable pageable);

    @Override
    Mono<PostFeed> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PostFeed> findAllWithEagerRelationships();

    @Override
    Flux<PostFeed> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM post_feed entity WHERE entity.poll_id = :id")
    Flux<PostFeed> findByPoll(Long id);

    @Query("SELECT * FROM post_feed entity WHERE entity.poll_id IS NULL")
    Flux<PostFeed> findAllWherePollIsNull();

    @Query("SELECT * FROM post_feed entity WHERE entity.content_package_id = :id")
    Flux<PostFeed> findByContentPackage(Long id);

    @Query("SELECT * FROM post_feed entity WHERE entity.content_package_id IS NULL")
    Flux<PostFeed> findAllWhereContentPackageIsNull();

    @Query(
        "SELECT entity.* FROM post_feed entity JOIN rel_post_feed__hash_tags joinTable ON entity.id = joinTable.hash_tags_id WHERE joinTable.hash_tags_id = :id"
    )
    Flux<PostFeed> findByHashTags(Long id);

    @Query("SELECT * FROM post_feed entity WHERE entity.creator_id = :id")
    Flux<PostFeed> findByCreator(Long id);

    @Query("SELECT * FROM post_feed entity WHERE entity.creator_id IS NULL")
    Flux<PostFeed> findAllWhereCreatorIsNull();

    @Override
    <S extends PostFeed> Mono<S> save(S entity);

    @Override
    Flux<PostFeed> findAll();

    @Override
    Mono<PostFeed> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PostFeedRepositoryInternal {
    <S extends PostFeed> Mono<S> save(S entity);

    Flux<PostFeed> findAllBy(Pageable pageable);

    Flux<PostFeed> findAll();

    Mono<PostFeed> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PostFeed> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PostFeed> findOneWithEagerRelationships(Long id);

    Flux<PostFeed> findAllWithEagerRelationships();

    Flux<PostFeed> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
