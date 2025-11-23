package com.fanflip.admin.repository;

import com.fanflip.admin.domain.Feedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Feedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackRepository extends ReactiveCrudRepository<Feedback, Long>, FeedbackRepositoryInternal {
    Flux<Feedback> findAllBy(Pageable pageable);

    @Query("SELECT * FROM feedback entity WHERE entity.creator_id = :id")
    Flux<Feedback> findByCreator(Long id);

    @Query("SELECT * FROM feedback entity WHERE entity.creator_id IS NULL")
    Flux<Feedback> findAllWhereCreatorIsNull();

    @Override
    <S extends Feedback> Mono<S> save(S entity);

    @Override
    Flux<Feedback> findAll();

    @Override
    Mono<Feedback> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeedbackRepositoryInternal {
    <S extends Feedback> Mono<S> save(S entity);

    Flux<Feedback> findAllBy(Pageable pageable);

    Flux<Feedback> findAll();

    Mono<Feedback> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Feedback> findAllBy(Pageable pageable, Criteria criteria);
}
