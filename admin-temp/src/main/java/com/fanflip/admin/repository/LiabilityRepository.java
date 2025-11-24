package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.Liability;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Liability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LiabilityRepository extends ReactiveCrudRepository<Liability, Long>, LiabilityRepositoryInternal {
    Flux<Liability> findAllBy(Pageable pageable);

    @Override
    <S extends Liability> Mono<S> save(S entity);

    @Override
    Flux<Liability> findAll();

    @Override
    Mono<Liability> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LiabilityRepositoryInternal {
    <S extends Liability> Mono<S> save(S entity);

    Flux<Liability> findAllBy(Pageable pageable);

    Flux<Liability> findAll();

    Mono<Liability> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Liability> findAllBy(Pageable pageable, Criteria criteria);
}
