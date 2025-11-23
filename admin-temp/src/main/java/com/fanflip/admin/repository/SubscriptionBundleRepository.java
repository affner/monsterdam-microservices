package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SubscriptionBundle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SubscriptionBundle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionBundleRepository
    extends ReactiveCrudRepository<SubscriptionBundle, Long>, SubscriptionBundleRepositoryInternal {
    Flux<SubscriptionBundle> findAllBy(Pageable pageable);

    @Query("SELECT * FROM subscription_bundle entity WHERE entity.creator_id = :id")
    Flux<SubscriptionBundle> findByCreator(Long id);

    @Query("SELECT * FROM subscription_bundle entity WHERE entity.creator_id IS NULL")
    Flux<SubscriptionBundle> findAllWhereCreatorIsNull();

    @Override
    <S extends SubscriptionBundle> Mono<S> save(S entity);

    @Override
    Flux<SubscriptionBundle> findAll();

    @Override
    Mono<SubscriptionBundle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SubscriptionBundleRepositoryInternal {
    <S extends SubscriptionBundle> Mono<S> save(S entity);

    Flux<SubscriptionBundle> findAllBy(Pageable pageable);

    Flux<SubscriptionBundle> findAll();

    Mono<SubscriptionBundle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SubscriptionBundle> findAllBy(Pageable pageable, Criteria criteria);
}
