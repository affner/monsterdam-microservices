package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PayoutMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PayoutMethod entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayoutMethodRepository extends ReactiveCrudRepository<PayoutMethod, Long>, PayoutMethodRepositoryInternal {
    Flux<PayoutMethod> findAllBy(Pageable pageable);

    @Override
    <S extends PayoutMethod> Mono<S> save(S entity);

    @Override
    Flux<PayoutMethod> findAll();

    @Override
    Mono<PayoutMethod> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PayoutMethodRepositoryInternal {
    <S extends PayoutMethod> Mono<S> save(S entity);

    Flux<PayoutMethod> findAllBy(Pageable pageable);

    Flux<PayoutMethod> findAll();

    Mono<PayoutMethod> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PayoutMethod> findAllBy(Pageable pageable, Criteria criteria);
}
