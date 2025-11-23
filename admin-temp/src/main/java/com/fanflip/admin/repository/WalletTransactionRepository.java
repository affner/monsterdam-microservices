package com.fanflip.admin.repository;

import com.fanflip.admin.domain.WalletTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the WalletTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletTransactionRepository extends ReactiveCrudRepository<WalletTransaction, Long>, WalletTransactionRepositoryInternal {
    Flux<WalletTransaction> findAllBy(Pageable pageable);

    @Override
    Mono<WalletTransaction> findOneWithEagerRelationships(Long id);

    @Override
    Flux<WalletTransaction> findAllWithEagerRelationships();

    @Override
    Flux<WalletTransaction> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.payment_id = :id")
    Flux<WalletTransaction> findByPayment(Long id);

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.payment_id IS NULL")
    Flux<WalletTransaction> findAllWherePaymentIsNull();

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.viewer_id = :id")
    Flux<WalletTransaction> findByViewer(Long id);

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.viewer_id IS NULL")
    Flux<WalletTransaction> findAllWhereViewerIsNull();

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.id not in (select purchased_content_id from purchased_content)")
    Flux<WalletTransaction> findAllWherePurchasedContentIsNull();

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.id not in (select purchased_subscription_id from purchased_subscription)")
    Flux<WalletTransaction> findAllWherePurchasedSubscriptionIsNull();

    @Query("SELECT * FROM wallet_transaction entity WHERE entity.id not in (select purchased_tip_id from purchased_tip)")
    Flux<WalletTransaction> findAllWherePurchasedTipIsNull();

    @Override
    <S extends WalletTransaction> Mono<S> save(S entity);

    @Override
    Flux<WalletTransaction> findAll();

    @Override
    Mono<WalletTransaction> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface WalletTransactionRepositoryInternal {
    <S extends WalletTransaction> Mono<S> save(S entity);

    Flux<WalletTransaction> findAllBy(Pageable pageable);

    Flux<WalletTransaction> findAll();

    Mono<WalletTransaction> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<WalletTransaction> findAllBy(Pageable pageable, Criteria criteria);

    Mono<WalletTransaction> findOneWithEagerRelationships(Long id);

    Flux<WalletTransaction> findAllWithEagerRelationships();

    Flux<WalletTransaction> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
