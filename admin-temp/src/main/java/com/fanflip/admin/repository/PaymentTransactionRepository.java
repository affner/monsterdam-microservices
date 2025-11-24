package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PaymentTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PaymentTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransactionRepository
    extends ReactiveCrudRepository<PaymentTransaction, Long>, PaymentTransactionRepositoryInternal {
    Flux<PaymentTransaction> findAllBy(Pageable pageable);

    @Override
    Mono<PaymentTransaction> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PaymentTransaction> findAllWithEagerRelationships();

    @Override
    Flux<PaymentTransaction> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM payment_transaction entity WHERE entity.payment_method_id = :id")
    Flux<PaymentTransaction> findByPaymentMethod(Long id);

    @Query("SELECT * FROM payment_transaction entity WHERE entity.payment_method_id IS NULL")
    Flux<PaymentTransaction> findAllWherePaymentMethodIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.payment_provider_id = :id")
    Flux<PaymentTransaction> findByPaymentProvider(Long id);

    @Query("SELECT * FROM payment_transaction entity WHERE entity.payment_provider_id IS NULL")
    Flux<PaymentTransaction> findAllWherePaymentProviderIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.viewer_id = :id")
    Flux<PaymentTransaction> findByViewer(Long id);

    @Query("SELECT * FROM payment_transaction entity WHERE entity.viewer_id IS NULL")
    Flux<PaymentTransaction> findAllWhereViewerIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.id not in (select accounting_record_id from accounting_record)")
    Flux<PaymentTransaction> findAllWhereAccountingRecordIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.id not in (select purchased_content_id from purchased_content)")
    Flux<PaymentTransaction> findAllWherePurchasedContentIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.id not in (select purchased_subscription_id from purchased_subscription)")
    Flux<PaymentTransaction> findAllWherePurchasedSubscriptionIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.id not in (select wallet_transaction_id from wallet_transaction)")
    Flux<PaymentTransaction> findAllWhereWalletTransactionIsNull();

    @Query("SELECT * FROM payment_transaction entity WHERE entity.id not in (select purchased_tip_id from purchased_tip)")
    Flux<PaymentTransaction> findAllWherePurchasedTipIsNull();

    @Override
    <S extends PaymentTransaction> Mono<S> save(S entity);

    @Override
    Flux<PaymentTransaction> findAll();

    @Override
    Mono<PaymentTransaction> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PaymentTransactionRepositoryInternal {
    <S extends PaymentTransaction> Mono<S> save(S entity);

    Flux<PaymentTransaction> findAllBy(Pageable pageable);

    Flux<PaymentTransaction> findAll();

    Mono<PaymentTransaction> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PaymentTransaction> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PaymentTransaction> findOneWithEagerRelationships(Long id);

    Flux<PaymentTransaction> findAllWithEagerRelationships();

    Flux<PaymentTransaction> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
