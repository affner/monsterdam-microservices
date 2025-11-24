package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.Currency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Currency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long>, CurrencyRepositoryInternal {
    Flux<Currency> findAllBy(Pageable pageable);

    @Override
    <S extends Currency> Mono<S> save(S entity);

    @Override
    Flux<Currency> findAll();

    @Override
    Mono<Currency> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CurrencyRepositoryInternal {
    <S extends Currency> Mono<S> save(S entity);

    Flux<Currency> findAllBy(Pageable pageable);

    Flux<Currency> findAll();

    Mono<Currency> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Currency> findAllBy(Pageable pageable, Criteria criteria);
}
