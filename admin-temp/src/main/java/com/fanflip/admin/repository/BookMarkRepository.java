package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.BookMark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the BookMark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookMarkRepository extends ReactiveCrudRepository<BookMark, Long>, BookMarkRepositoryInternal {
    Flux<BookMark> findAllBy(Pageable pageable);

    @Override
    Mono<BookMark> findOneWithEagerRelationships(Long id);

    @Override
    Flux<BookMark> findAllWithEagerRelationships();

    @Override
    Flux<BookMark> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM book_mark entity WHERE entity.post_id = :id")
    Flux<BookMark> findByPost(Long id);

    @Query("SELECT * FROM book_mark entity WHERE entity.post_id IS NULL")
    Flux<BookMark> findAllWherePostIsNull();

    @Query("SELECT * FROM book_mark entity WHERE entity.message_id = :id")
    Flux<BookMark> findByMessage(Long id);

    @Query("SELECT * FROM book_mark entity WHERE entity.message_id IS NULL")
    Flux<BookMark> findAllWhereMessageIsNull();

    @Query("SELECT * FROM book_mark entity WHERE entity.user_id = :id")
    Flux<BookMark> findByUser(Long id);

    @Query("SELECT * FROM book_mark entity WHERE entity.user_id IS NULL")
    Flux<BookMark> findAllWhereUserIsNull();

    @Override
    <S extends BookMark> Mono<S> save(S entity);

    @Override
    Flux<BookMark> findAll();

    @Override
    Mono<BookMark> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BookMarkRepositoryInternal {
    <S extends BookMark> Mono<S> save(S entity);

    Flux<BookMark> findAllBy(Pageable pageable);

    Flux<BookMark> findAll();

    Mono<BookMark> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BookMark> findAllBy(Pageable pageable, Criteria criteria);

    Mono<BookMark> findOneWithEagerRelationships(Long id);

    Flux<BookMark> findAllWithEagerRelationships();

    Flux<BookMark> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
