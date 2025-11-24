package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.SingleLiveStream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SingleLiveStream entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleLiveStreamRepository extends ReactiveCrudRepository<SingleLiveStream, Long>, SingleLiveStreamRepositoryInternal {
    Flux<SingleLiveStream> findAllBy(Pageable pageable);

    @Override
    <S extends SingleLiveStream> Mono<S> save(S entity);

    @Override
    Flux<SingleLiveStream> findAll();

    @Override
    Mono<SingleLiveStream> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SingleLiveStreamRepositoryInternal {
    <S extends SingleLiveStream> Mono<S> save(S entity);

    Flux<SingleLiveStream> findAllBy(Pageable pageable);

    Flux<SingleLiveStream> findAll();

    Mono<SingleLiveStream> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SingleLiveStream> findAllBy(Pageable pageable, Criteria criteria);
}
