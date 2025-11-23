package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PostComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PostComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostCommentRepository extends ReactiveCrudRepository<PostComment, Long>, PostCommentRepositoryInternal {
    Flux<PostComment> findAllBy(Pageable pageable);

    @Override
    Mono<PostComment> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PostComment> findAllWithEagerRelationships();

    @Override
    Flux<PostComment> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM post_comment entity WHERE entity.post_id = :id")
    Flux<PostComment> findByPost(Long id);

    @Query("SELECT * FROM post_comment entity WHERE entity.post_id IS NULL")
    Flux<PostComment> findAllWherePostIsNull();

    @Query("SELECT * FROM post_comment entity WHERE entity.response_to_id = :id")
    Flux<PostComment> findByResponseTo(Long id);

    @Query("SELECT * FROM post_comment entity WHERE entity.response_to_id IS NULL")
    Flux<PostComment> findAllWhereResponseToIsNull();

    @Query("SELECT * FROM post_comment entity WHERE entity.commenter_id = :id")
    Flux<PostComment> findByCommenter(Long id);

    @Query("SELECT * FROM post_comment entity WHERE entity.commenter_id IS NULL")
    Flux<PostComment> findAllWhereCommenterIsNull();

    @Override
    <S extends PostComment> Mono<S> save(S entity);

    @Override
    Flux<PostComment> findAll();

    @Override
    Mono<PostComment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PostCommentRepositoryInternal {
    <S extends PostComment> Mono<S> save(S entity);

    Flux<PostComment> findAllBy(Pageable pageable);

    Flux<PostComment> findAll();

    Mono<PostComment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PostComment> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PostComment> findOneWithEagerRelationships(Long id);

    Flux<PostComment> findAllWithEagerRelationships();

    Flux<PostComment> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
