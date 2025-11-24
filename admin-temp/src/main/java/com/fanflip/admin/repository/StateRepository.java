package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the State entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateRepository extends ReactiveCrudRepository<State, Long>, StateRepositoryInternal {
    Flux<State> findAllBy(Pageable pageable);

    @Override
    Mono<State> findOneWithEagerRelationships(Long id);

    @Override
    Flux<State> findAllWithEagerRelationships();

    @Override
    Flux<State> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM state entity WHERE entity.country_id = :id")
    Flux<State> findByCountry(Long id);

    @Query("SELECT * FROM state entity WHERE entity.country_id IS NULL")
    Flux<State> findAllWhereCountryIsNull();

    @Override
    <S extends State> Mono<S> save(S entity);

    @Override
    Flux<State> findAll();

    @Override
    Mono<State> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface StateRepositoryInternal {
    <S extends State> Mono<S> save(S entity);

    Flux<State> findAllBy(Pageable pageable);

    Flux<State> findAll();

    Mono<State> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<State> findAllBy(Pageable pageable, Criteria criteria);

    Mono<State> findOneWithEagerRelationships(Long id);

    Flux<State> findAllWithEagerRelationships();

    Flux<State> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
