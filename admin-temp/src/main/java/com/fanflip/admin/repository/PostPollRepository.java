package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PostPoll;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PostPoll entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostPollRepository extends ReactiveCrudRepository<PostPoll, Long>, PostPollRepositoryInternal {
    Flux<PostPoll> findAllBy(Pageable pageable);

    @Query("SELECT * FROM post_poll entity WHERE entity.id not in (select post_id from post_feed)")
    Flux<PostPoll> findAllWherePostIsNull();

    @Override
    <S extends PostPoll> Mono<S> save(S entity);

    @Override
    Flux<PostPoll> findAll();

    @Override
    Mono<PostPoll> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PostPollRepositoryInternal {
    <S extends PostPoll> Mono<S> save(S entity);

    Flux<PostPoll> findAllBy(Pageable pageable);

    Flux<PostPoll> findAll();

    Mono<PostPoll> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PostPoll> findAllBy(Pageable pageable, Criteria criteria);
}
