package com.fanflip.admin.repository;

import com.fanflip.admin.domain.EmojiType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmojiType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmojiTypeRepository extends ReactiveCrudRepository<EmojiType, Long>, EmojiTypeRepositoryInternal {
    Flux<EmojiType> findAllBy(Pageable pageable);

    @Override
    <S extends EmojiType> Mono<S> save(S entity);

    @Override
    Flux<EmojiType> findAll();

    @Override
    Mono<EmojiType> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmojiTypeRepositoryInternal {
    <S extends EmojiType> Mono<S> save(S entity);

    Flux<EmojiType> findAllBy(Pageable pageable);

    Flux<EmojiType> findAll();

    Mono<EmojiType> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmojiType> findAllBy(Pageable pageable, Criteria criteria);
}
