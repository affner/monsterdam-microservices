package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.SingleVideo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SingleVideo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleVideoRepository extends ReactiveCrudRepository<SingleVideo, Long>, SingleVideoRepositoryInternal {
    Flux<SingleVideo> findAllBy(Pageable pageable);

    @Override
    Mono<SingleVideo> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SingleVideo> findAllWithEagerRelationships();

    @Override
    Flux<SingleVideo> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM single_video entity WHERE entity.belong_package_id = :id")
    Flux<SingleVideo> findByBelongPackage(Long id);

    @Query("SELECT * FROM single_video entity WHERE entity.belong_package_id IS NULL")
    Flux<SingleVideo> findAllWhereBelongPackageIsNull();

    @Override
    <S extends SingleVideo> Mono<S> save(S entity);

    @Override
    Flux<SingleVideo> findAll();

    @Override
    Mono<SingleVideo> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SingleVideoRepositoryInternal {
    <S extends SingleVideo> Mono<S> save(S entity);

    Flux<SingleVideo> findAllBy(Pageable pageable);

    Flux<SingleVideo> findAll();

    Mono<SingleVideo> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SingleVideo> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SingleVideo> findOneWithEagerRelationships(Long id);

    Flux<SingleVideo> findAllWithEagerRelationships();

    Flux<SingleVideo> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
