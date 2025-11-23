package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SinglePhoto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SinglePhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SinglePhotoRepository extends ReactiveCrudRepository<SinglePhoto, Long>, SinglePhotoRepositoryInternal {
    Flux<SinglePhoto> findAllBy(Pageable pageable);

    @Override
    Mono<SinglePhoto> findOneWithEagerRelationships(Long id);

    @Override
    Flux<SinglePhoto> findAllWithEagerRelationships();

    @Override
    Flux<SinglePhoto> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM single_photo entity WHERE entity.belong_package_id = :id")
    Flux<SinglePhoto> findByBelongPackage(Long id);

    @Query("SELECT * FROM single_photo entity WHERE entity.belong_package_id IS NULL")
    Flux<SinglePhoto> findAllWhereBelongPackageIsNull();

    @Override
    <S extends SinglePhoto> Mono<S> save(S entity);

    @Override
    Flux<SinglePhoto> findAll();

    @Override
    Mono<SinglePhoto> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SinglePhotoRepositoryInternal {
    <S extends SinglePhoto> Mono<S> save(S entity);

    Flux<SinglePhoto> findAllBy(Pageable pageable);

    Flux<SinglePhoto> findAll();

    Mono<SinglePhoto> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SinglePhoto> findAllBy(Pageable pageable, Criteria criteria);

    Mono<SinglePhoto> findOneWithEagerRelationships(Long id);

    Flux<SinglePhoto> findAllWithEagerRelationships();

    Flux<SinglePhoto> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
