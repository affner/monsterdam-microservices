package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.SocialNetwork;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SocialNetwork entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialNetworkRepository extends ReactiveCrudRepository<SocialNetwork, Long>, SocialNetworkRepositoryInternal {
    Flux<SocialNetwork> findAllBy(Pageable pageable);

    @Override
    <S extends SocialNetwork> Mono<S> save(S entity);

    @Override
    Flux<SocialNetwork> findAll();

    @Override
    Mono<SocialNetwork> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SocialNetworkRepositoryInternal {
    <S extends SocialNetwork> Mono<S> save(S entity);

    Flux<SocialNetwork> findAllBy(Pageable pageable);

    Flux<SocialNetwork> findAll();

    Mono<SocialNetwork> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SocialNetwork> findAllBy(Pageable pageable, Criteria criteria);
}
