package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.ContentPackage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ContentPackage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentPackageRepository extends ReactiveCrudRepository<ContentPackage, Long>, ContentPackageRepositoryInternal {
    Flux<ContentPackage> findAllBy(Pageable pageable);

    @Override
    Mono<ContentPackage> findOneWithEagerRelationships(Long id);

    @Override
    Flux<ContentPackage> findAllWithEagerRelationships();

    @Override
    Flux<ContentPackage> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM content_package entity WHERE entity.audio_id = :id")
    Flux<ContentPackage> findByAudio(Long id);

    @Query("SELECT * FROM content_package entity WHERE entity.audio_id IS NULL")
    Flux<ContentPackage> findAllWhereAudioIsNull();

    @Query(
        "SELECT entity.* FROM content_package entity JOIN rel_content_package__users_tagged joinTable ON entity.id = joinTable.users_tagged_id WHERE joinTable.users_tagged_id = :id"
    )
    Flux<ContentPackage> findByUsersTagged(Long id);

    @Query("SELECT * FROM content_package entity WHERE entity.id not in (select message_id from direct_message)")
    Flux<ContentPackage> findAllWhereMessageIsNull();

    @Query("SELECT * FROM content_package entity WHERE entity.id not in (select post_id from post_feed)")
    Flux<ContentPackage> findAllWherePostIsNull();

    @Override
    <S extends ContentPackage> Mono<S> save(S entity);

    @Override
    Flux<ContentPackage> findAll();

    @Override
    Mono<ContentPackage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ContentPackageRepositoryInternal {
    <S extends ContentPackage> Mono<S> save(S entity);

    Flux<ContentPackage> findAllBy(Pageable pageable);

    Flux<ContentPackage> findAll();

    Mono<ContentPackage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ContentPackage> findAllBy(Pageable pageable, Criteria criteria);

    Mono<ContentPackage> findOneWithEagerRelationships(Long id);

    Flux<ContentPackage> findAllWithEagerRelationships();

    Flux<ContentPackage> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
