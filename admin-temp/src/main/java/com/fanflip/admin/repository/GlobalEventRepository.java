package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.GlobalEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the GlobalEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalEventRepository extends ReactiveCrudRepository<GlobalEvent, Long>, GlobalEventRepositoryInternal {
    Flux<GlobalEvent> findAllBy(Pageable pageable);

    @Override
    <S extends GlobalEvent> Mono<S> save(S entity);

    @Override
    Flux<GlobalEvent> findAll();

    @Override
    Mono<GlobalEvent> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface GlobalEventRepositoryInternal {
    <S extends GlobalEvent> Mono<S> save(S entity);

    Flux<GlobalEvent> findAllBy(Pageable pageable);

    Flux<GlobalEvent> findAll();

    Mono<GlobalEvent> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<GlobalEvent> findAllBy(Pageable pageable, Criteria criteria);
}
