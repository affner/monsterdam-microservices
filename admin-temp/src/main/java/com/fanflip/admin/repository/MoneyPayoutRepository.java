package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.MoneyPayout;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MoneyPayout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoneyPayoutRepository extends ReactiveCrudRepository<MoneyPayout, Long>, MoneyPayoutRepositoryInternal {
    Flux<MoneyPayout> findAllBy(Pageable pageable);

    @Override
    Mono<MoneyPayout> findOneWithEagerRelationships(Long id);

    @Override
    Flux<MoneyPayout> findAllWithEagerRelationships();

    @Override
    Flux<MoneyPayout> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM money_payout entity WHERE entity.creator_earning_id = :id")
    Flux<MoneyPayout> findByCreatorEarning(Long id);

    @Query("SELECT * FROM money_payout entity WHERE entity.creator_earning_id IS NULL")
    Flux<MoneyPayout> findAllWhereCreatorEarningIsNull();

    @Query("SELECT * FROM money_payout entity WHERE entity.creator_id = :id")
    Flux<MoneyPayout> findByCreator(Long id);

    @Query("SELECT * FROM money_payout entity WHERE entity.creator_id IS NULL")
    Flux<MoneyPayout> findAllWhereCreatorIsNull();

    @Override
    <S extends MoneyPayout> Mono<S> save(S entity);

    @Override
    Flux<MoneyPayout> findAll();

    @Override
    Mono<MoneyPayout> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface MoneyPayoutRepositoryInternal {
    <S extends MoneyPayout> Mono<S> save(S entity);

    Flux<MoneyPayout> findAllBy(Pageable pageable);

    Flux<MoneyPayout> findAll();

    Mono<MoneyPayout> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MoneyPayout> findAllBy(Pageable pageable, Criteria criteria);

    Mono<MoneyPayout> findOneWithEagerRelationships(Long id);

    Flux<MoneyPayout> findAllWithEagerRelationships();

    Flux<MoneyPayout> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
