package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.SingleAudio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SingleAudio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleAudioRepository extends ReactiveCrudRepository<SingleAudio, Long>, SingleAudioRepositoryInternal {
    Flux<SingleAudio> findAllBy(Pageable pageable);

    @Query("SELECT * FROM single_audio entity WHERE entity.id not in (select content_package_id from content_package)")
    Flux<SingleAudio> findAllWhereContentPackageIsNull();

    @Override
    <S extends SingleAudio> Mono<S> save(S entity);

    @Override
    Flux<SingleAudio> findAll();

    @Override
    Mono<SingleAudio> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SingleAudioRepositoryInternal {
    <S extends SingleAudio> Mono<S> save(S entity);

    Flux<SingleAudio> findAllBy(Pageable pageable);

    Flux<SingleAudio> findAll();

    Mono<SingleAudio> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SingleAudio> findAllBy(Pageable pageable, Criteria criteria);
}
