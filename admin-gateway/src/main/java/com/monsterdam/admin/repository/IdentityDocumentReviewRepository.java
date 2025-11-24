package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.IdentityDocumentReview;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IdentityDocumentReview entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentityDocumentReviewRepository
    extends ReactiveCrudRepository<IdentityDocumentReview, Long>, IdentityDocumentReviewRepositoryInternal {
    @Query("SELECT * FROM identity_document_review entity WHERE entity.ticket_id = :id")
    Flux<IdentityDocumentReview> findByTicket(Long id);

    @Query("SELECT * FROM identity_document_review entity WHERE entity.ticket_id IS NULL")
    Flux<IdentityDocumentReview> findAllWhereTicketIsNull();

    @Override
    <S extends IdentityDocumentReview> Mono<S> save(S entity);

    @Override
    Flux<IdentityDocumentReview> findAll();

    @Override
    Mono<IdentityDocumentReview> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IdentityDocumentReviewRepositoryInternal {
    <S extends IdentityDocumentReview> Mono<S> save(S entity);

    Flux<IdentityDocumentReview> findAllBy(Pageable pageable);

    Flux<IdentityDocumentReview> findAll();

    Mono<IdentityDocumentReview> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IdentityDocumentReview> findAllBy(Pageable pageable, Criteria criteria);
}
