package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PurchasedTip;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PurchasedTip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedTipRepository extends ReactiveCrudRepository<PurchasedTip, Long>, PurchasedTipRepositoryInternal {
    Flux<PurchasedTip> findAllBy(Pageable pageable);

    @Override
    Mono<PurchasedTip> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PurchasedTip> findAllWithEagerRelationships();

    @Override
    Flux<PurchasedTip> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM purchased_tip entity WHERE entity.payment_id = :id")
    Flux<PurchasedTip> findByPayment(Long id);

    @Query("SELECT * FROM purchased_tip entity WHERE entity.payment_id IS NULL")
    Flux<PurchasedTip> findAllWherePaymentIsNull();

    @Query("SELECT * FROM purchased_tip entity WHERE entity.wallet_transaction_id = :id")
    Flux<PurchasedTip> findByWalletTransaction(Long id);

    @Query("SELECT * FROM purchased_tip entity WHERE entity.wallet_transaction_id IS NULL")
    Flux<PurchasedTip> findAllWhereWalletTransactionIsNull();

    @Query("SELECT * FROM purchased_tip entity WHERE entity.creator_earning_id = :id")
    Flux<PurchasedTip> findByCreatorEarning(Long id);

    @Query("SELECT * FROM purchased_tip entity WHERE entity.creator_earning_id IS NULL")
    Flux<PurchasedTip> findAllWhereCreatorEarningIsNull();

    @Query("SELECT * FROM purchased_tip entity WHERE entity.message_id = :id")
    Flux<PurchasedTip> findByMessage(Long id);

    @Query("SELECT * FROM purchased_tip entity WHERE entity.message_id IS NULL")
    Flux<PurchasedTip> findAllWhereMessageIsNull();

    @Override
    <S extends PurchasedTip> Mono<S> save(S entity);

    @Override
    Flux<PurchasedTip> findAll();

    @Override
    Mono<PurchasedTip> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PurchasedTipRepositoryInternal {
    <S extends PurchasedTip> Mono<S> save(S entity);

    Flux<PurchasedTip> findAllBy(Pageable pageable);

    Flux<PurchasedTip> findAll();

    Mono<PurchasedTip> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PurchasedTip> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PurchasedTip> findOneWithEagerRelationships(Long id);

    Flux<PurchasedTip> findAllWithEagerRelationships();

    Flux<PurchasedTip> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
