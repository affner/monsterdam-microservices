package com.fanflip.admin.repository;

import com.fanflip.admin.domain.IdentityDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IdentityDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentityDocumentRepository extends ReactiveCrudRepository<IdentityDocument, Long>, IdentityDocumentRepositoryInternal {
    @Query("SELECT * FROM identity_document entity WHERE entity.review_id = :id")
    Flux<IdentityDocument> findByReview(Long id);

    @Query("SELECT * FROM identity_document entity WHERE entity.review_id IS NULL")
    Flux<IdentityDocument> findAllWhereReviewIsNull();

    @Override
    <S extends IdentityDocument> Mono<S> save(S entity);

    @Override
    Flux<IdentityDocument> findAll();

    @Override
    Mono<IdentityDocument> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IdentityDocumentRepositoryInternal {
    <S extends IdentityDocument> Mono<S> save(S entity);

    Flux<IdentityDocument> findAllBy(Pageable pageable);

    Flux<IdentityDocument> findAll();

    Mono<IdentityDocument> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IdentityDocument> findAllBy(Pageable pageable, Criteria criteria);
}
