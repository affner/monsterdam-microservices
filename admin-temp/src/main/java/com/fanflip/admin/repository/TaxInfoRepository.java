package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.TaxInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TaxInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxInfoRepository extends ReactiveCrudRepository<TaxInfo, Long>, TaxInfoRepositoryInternal {
    Flux<TaxInfo> findAllBy(Pageable pageable);

    @Override
    Mono<TaxInfo> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TaxInfo> findAllWithEagerRelationships();

    @Override
    Flux<TaxInfo> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM tax_info entity WHERE entity.country_id = :id")
    Flux<TaxInfo> findByCountry(Long id);

    @Query("SELECT * FROM tax_info entity WHERE entity.country_id IS NULL")
    Flux<TaxInfo> findAllWhereCountryIsNull();

    @Override
    <S extends TaxInfo> Mono<S> save(S entity);

    @Override
    Flux<TaxInfo> findAll();

    @Override
    Mono<TaxInfo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TaxInfoRepositoryInternal {
    <S extends TaxInfo> Mono<S> save(S entity);

    Flux<TaxInfo> findAllBy(Pageable pageable);

    Flux<TaxInfo> findAll();

    Mono<TaxInfo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TaxInfo> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TaxInfo> findOneWithEagerRelationships(Long id);

    Flux<TaxInfo> findAllWithEagerRelationships();

    Flux<TaxInfo> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
