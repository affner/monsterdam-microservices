package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.DocumentReviewObservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DocumentReviewObservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentReviewObservationRepository
    extends ReactiveCrudRepository<DocumentReviewObservation, Long>, DocumentReviewObservationRepositoryInternal {
    @Query("SELECT * FROM document_review_observation entity WHERE entity.review_id = :id")
    Flux<DocumentReviewObservation> findByReview(Long id);

    @Query("SELECT * FROM document_review_observation entity WHERE entity.review_id IS NULL")
    Flux<DocumentReviewObservation> findAllWhereReviewIsNull();

    @Override
    <S extends DocumentReviewObservation> Mono<S> save(S entity);

    @Override
    Flux<DocumentReviewObservation> findAll();

    @Override
    Mono<DocumentReviewObservation> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DocumentReviewObservationRepositoryInternal {
    <S extends DocumentReviewObservation> Mono<S> save(S entity);

    Flux<DocumentReviewObservation> findAllBy(Pageable pageable);

    Flux<DocumentReviewObservation> findAll();

    Mono<DocumentReviewObservation> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DocumentReviewObservation> findAllBy(Pageable pageable, Criteria criteria);
}
