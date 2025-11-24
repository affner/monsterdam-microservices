package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.TaxDeclaration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the TaxDeclaration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaxDeclarationRepository extends ReactiveCrudRepository<TaxDeclaration, Long>, TaxDeclarationRepositoryInternal {
    Flux<TaxDeclaration> findAllBy(Pageable pageable);

    @Override
    Mono<TaxDeclaration> findOneWithEagerRelationships(Long id);

    @Override
    Flux<TaxDeclaration> findAllWithEagerRelationships();

    @Override
    Flux<TaxDeclaration> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM tax_declaration entity JOIN rel_tax_declaration__accounting_records joinTable ON entity.id = joinTable.accounting_records_id WHERE joinTable.accounting_records_id = :id"
    )
    Flux<TaxDeclaration> findByAccountingRecords(Long id);

    @Override
    <S extends TaxDeclaration> Mono<S> save(S entity);

    @Override
    Flux<TaxDeclaration> findAll();

    @Override
    Mono<TaxDeclaration> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface TaxDeclarationRepositoryInternal {
    <S extends TaxDeclaration> Mono<S> save(S entity);

    Flux<TaxDeclaration> findAllBy(Pageable pageable);

    Flux<TaxDeclaration> findAll();

    Mono<TaxDeclaration> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<TaxDeclaration> findAllBy(Pageable pageable, Criteria criteria);

    Mono<TaxDeclaration> findOneWithEagerRelationships(Long id);

    Flux<TaxDeclaration> findAllWithEagerRelationships();

    Flux<TaxDeclaration> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
