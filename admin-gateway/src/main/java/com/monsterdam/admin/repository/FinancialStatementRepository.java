package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.FinancialStatement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FinancialStatement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialStatementRepository
    extends ReactiveCrudRepository<FinancialStatement, Long>, FinancialStatementRepositoryInternal {
    @Override
    Mono<FinancialStatement> findOneWithEagerRelationships(Long id);

    @Override
    Flux<FinancialStatement> findAllWithEagerRelationships();

    @Override
    Flux<FinancialStatement> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM financial_statement entity JOIN rel_financial_statement__accounting_records joinTable ON entity.id = joinTable.accounting_records_id WHERE joinTable.accounting_records_id = :id"
    )
    Flux<FinancialStatement> findByAccountingRecords(Long id);

    @Override
    <S extends FinancialStatement> Mono<S> save(S entity);

    @Override
    Flux<FinancialStatement> findAll();

    @Override
    Mono<FinancialStatement> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FinancialStatementRepositoryInternal {
    <S extends FinancialStatement> Mono<S> save(S entity);

    Flux<FinancialStatement> findAllBy(Pageable pageable);

    Flux<FinancialStatement> findAll();

    Mono<FinancialStatement> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FinancialStatement> findAllBy(Pageable pageable, Criteria criteria);

    Mono<FinancialStatement> findOneWithEagerRelationships(Long id);

    Flux<FinancialStatement> findAllWithEagerRelationships();

    Flux<FinancialStatement> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
