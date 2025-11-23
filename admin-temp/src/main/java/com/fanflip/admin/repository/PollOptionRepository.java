package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PollOption;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PollOption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollOptionRepository extends ReactiveCrudRepository<PollOption, Long>, PollOptionRepositoryInternal {
    Flux<PollOption> findAllBy(Pageable pageable);

    @Override
    Mono<PollOption> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PollOption> findAllWithEagerRelationships();

    @Override
    Flux<PollOption> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM poll_option entity WHERE entity.poll_id = :id")
    Flux<PollOption> findByPoll(Long id);

    @Query("SELECT * FROM poll_option entity WHERE entity.poll_id IS NULL")
    Flux<PollOption> findAllWherePollIsNull();

    @Override
    <S extends PollOption> Mono<S> save(S entity);

    @Override
    Flux<PollOption> findAll();

    @Override
    Mono<PollOption> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PollOptionRepositoryInternal {
    <S extends PollOption> Mono<S> save(S entity);

    Flux<PollOption> findAllBy(Pageable pageable);

    Flux<PollOption> findAll();

    Mono<PollOption> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PollOption> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PollOption> findOneWithEagerRelationships(Long id);

    Flux<PollOption> findAllWithEagerRelationships();

    Flux<PollOption> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
