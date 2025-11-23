package com.fanflip.admin.repository;

import com.fanflip.admin.domain.CreatorEarning;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CreatorEarning entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreatorEarningRepository extends ReactiveCrudRepository<CreatorEarning, Long>, CreatorEarningRepositoryInternal {
    Flux<CreatorEarning> findAllBy(Pageable pageable);

    @Query("SELECT * FROM creator_earning entity WHERE entity.creator_id = :id")
    Flux<CreatorEarning> findByCreator(Long id);

    @Query("SELECT * FROM creator_earning entity WHERE entity.creator_id IS NULL")
    Flux<CreatorEarning> findAllWhereCreatorIsNull();

    @Query("SELECT * FROM creator_earning entity WHERE entity.id not in (select money_payout_id from money_payout)")
    Flux<CreatorEarning> findAllWhereMoneyPayoutIsNull();

    @Query("SELECT * FROM creator_earning entity WHERE entity.id not in (select purchased_content_id from purchased_content)")
    Flux<CreatorEarning> findAllWherePurchasedContentIsNull();

    @Query("SELECT * FROM creator_earning entity WHERE entity.id not in (select purchased_subscription_id from purchased_subscription)")
    Flux<CreatorEarning> findAllWherePurchasedSubscriptionIsNull();

    @Query("SELECT * FROM creator_earning entity WHERE entity.id not in (select purchased_tip_id from purchased_tip)")
    Flux<CreatorEarning> findAllWherePurchasedTipIsNull();

    @Override
    <S extends CreatorEarning> Mono<S> save(S entity);

    @Override
    Flux<CreatorEarning> findAll();

    @Override
    Mono<CreatorEarning> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CreatorEarningRepositoryInternal {
    <S extends CreatorEarning> Mono<S> save(S entity);

    Flux<CreatorEarning> findAllBy(Pageable pageable);

    Flux<CreatorEarning> findAll();

    Mono<CreatorEarning> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CreatorEarning> findAllBy(Pageable pageable, Criteria criteria);
}
