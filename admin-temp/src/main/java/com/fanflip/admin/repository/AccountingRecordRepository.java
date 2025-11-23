package com.fanflip.admin.repository;

import com.fanflip.admin.domain.AccountingRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AccountingRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingRecordRepository extends ReactiveCrudRepository<AccountingRecord, Long>, AccountingRecordRepositoryInternal {
    Flux<AccountingRecord> findAllBy(Pageable pageable);

    @Query("SELECT * FROM accounting_record entity WHERE entity.payment_id = :id")
    Flux<AccountingRecord> findByPayment(Long id);

    @Query("SELECT * FROM accounting_record entity WHERE entity.payment_id IS NULL")
    Flux<AccountingRecord> findAllWherePaymentIsNull();

    @Query("SELECT * FROM accounting_record entity WHERE entity.budget_id = :id")
    Flux<AccountingRecord> findByBudget(Long id);

    @Query("SELECT * FROM accounting_record entity WHERE entity.budget_id IS NULL")
    Flux<AccountingRecord> findAllWhereBudgetIsNull();

    @Query("SELECT * FROM accounting_record entity WHERE entity.asset_id = :id")
    Flux<AccountingRecord> findByAsset(Long id);

    @Query("SELECT * FROM accounting_record entity WHERE entity.asset_id IS NULL")
    Flux<AccountingRecord> findAllWhereAssetIsNull();

    @Query("SELECT * FROM accounting_record entity WHERE entity.liability_id = :id")
    Flux<AccountingRecord> findByLiability(Long id);

    @Query("SELECT * FROM accounting_record entity WHERE entity.liability_id IS NULL")
    Flux<AccountingRecord> findAllWhereLiabilityIsNull();

    @Override
    <S extends AccountingRecord> Mono<S> save(S entity);

    @Override
    Flux<AccountingRecord> findAll();

    @Override
    Mono<AccountingRecord> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AccountingRecordRepositoryInternal {
    <S extends AccountingRecord> Mono<S> save(S entity);

    Flux<AccountingRecord> findAllBy(Pageable pageable);

    Flux<AccountingRecord> findAll();

    Mono<AccountingRecord> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AccountingRecord> findAllBy(Pageable pageable, Criteria criteria);
}
