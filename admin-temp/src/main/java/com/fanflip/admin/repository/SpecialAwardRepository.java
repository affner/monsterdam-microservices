package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SpecialAward;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SpecialAward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialAwardRepository extends ReactiveCrudRepository<SpecialAward, Long>, SpecialAwardRepositoryInternal {
    Flux<SpecialAward> findAllBy(Pageable pageable);

    @Override
    <S extends SpecialAward> Mono<S> save(S entity);

    @Override
    Flux<SpecialAward> findAll();

    @Override
    Mono<SpecialAward> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SpecialAwardRepositoryInternal {
    <S extends SpecialAward> Mono<S> save(S entity);

    Flux<SpecialAward> findAllBy(Pageable pageable);

    Flux<SpecialAward> findAll();

    Mono<SpecialAward> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SpecialAward> findAllBy(Pageable pageable, Criteria criteria);
}
