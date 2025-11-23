package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SingleDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SingleDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SingleDocumentRepository extends ReactiveCrudRepository<SingleDocument, Long>, SingleDocumentRepositoryInternal {
    Flux<SingleDocument> findAllBy(Pageable pageable);

    @Query("SELECT * FROM single_document entity WHERE entity.user_id = :id")
    Flux<SingleDocument> findByUser(Long id);

    @Query("SELECT * FROM single_document entity WHERE entity.user_id IS NULL")
    Flux<SingleDocument> findAllWhereUserIsNull();

    @Override
    <S extends SingleDocument> Mono<S> save(S entity);

    @Override
    Flux<SingleDocument> findAll();

    @Override
    Mono<SingleDocument> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SingleDocumentRepositoryInternal {
    <S extends SingleDocument> Mono<S> save(S entity);

    Flux<SingleDocument> findAllBy(Pageable pageable);

    Flux<SingleDocument> findAll();

    Mono<SingleDocument> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SingleDocument> findAllBy(Pageable pageable, Criteria criteria);
}
