package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.LikeMark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the LikeMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeMarkRepository extends ReactiveCrudRepository<LikeMark, Long>, LikeMarkRepositoryInternal {
    Flux<LikeMark> findAllBy(Pageable pageable);

    @Override
    <S extends LikeMark> Mono<S> save(S entity);

    @Override
    Flux<LikeMark> findAll();

    @Override
    Mono<LikeMark> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface LikeMarkRepositoryInternal {
    <S extends LikeMark> Mono<S> save(S entity);

    Flux<LikeMark> findAllBy(Pageable pageable);

    Flux<LikeMark> findAll();

    Mono<LikeMark> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<LikeMark> findAllBy(Pageable pageable, Criteria criteria);
}
