package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PurchasedSubscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchasedSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedSubscriptionRepository
    extends ReactiveCrudRepository<PurchasedSubscription, Long>, PurchasedSubscriptionRepositoryInternal {
    Flux<PurchasedSubscription> findAllBy(Pageable pageable);

    @Override
    Mono<PurchasedSubscription> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PurchasedSubscription> findAllWithEagerRelationships();

    @Override
    Flux<PurchasedSubscription> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.payment_id = :id")
    Flux<PurchasedSubscription> findByPayment(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.payment_id IS NULL")
    Flux<PurchasedSubscription> findAllWherePaymentIsNull();

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.wallet_transaction_id = :id")
    Flux<PurchasedSubscription> findByWalletTransaction(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.wallet_transaction_id IS NULL")
    Flux<PurchasedSubscription> findAllWhereWalletTransactionIsNull();

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.creator_earning_id = :id")
    Flux<PurchasedSubscription> findByCreatorEarning(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.creator_earning_id IS NULL")
    Flux<PurchasedSubscription> findAllWhereCreatorEarningIsNull();

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.subscription_bundle_id = :id")
    Flux<PurchasedSubscription> findBySubscriptionBundle(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.subscription_bundle_id IS NULL")
    Flux<PurchasedSubscription> findAllWhereSubscriptionBundleIsNull();

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.applied_promotion_id = :id")
    Flux<PurchasedSubscription> findByAppliedPromotion(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.applied_promotion_id IS NULL")
    Flux<PurchasedSubscription> findAllWhereAppliedPromotionIsNull();

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.viewer_id = :id")
    Flux<PurchasedSubscription> findByViewer(Long id);

    @Query("SELECT * FROM purchased_subscription entity WHERE entity.viewer_id IS NULL")
    Flux<PurchasedSubscription> findAllWhereViewerIsNull();

    @Override
    <S extends PurchasedSubscription> Mono<S> save(S entity);

    @Override
    Flux<PurchasedSubscription> findAll();

    @Override
    Mono<PurchasedSubscription> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PurchasedSubscriptionRepositoryInternal {
    <S extends PurchasedSubscription> Mono<S> save(S entity);

    Flux<PurchasedSubscription> findAllBy(Pageable pageable);

    Flux<PurchasedSubscription> findAll();

    Mono<PurchasedSubscription> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchasedSubscription> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PurchasedSubscription> findOneWithEagerRelationships(Long id);

    Flux<PurchasedSubscription> findAllWithEagerRelationships();

    Flux<PurchasedSubscription> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
