package com.fanflip.admin.repository;

import com.fanflip.admin.domain.SpecialTitle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SpecialTitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialTitleRepository extends ReactiveCrudRepository<SpecialTitle, Long>, SpecialTitleRepositoryInternal {
    Flux<SpecialTitle> findAllBy(Pageable pageable);

    @Override
    <S extends SpecialTitle> Mono<S> save(S entity);

    @Override
    Flux<SpecialTitle> findAll();

    @Override
    Mono<SpecialTitle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SpecialTitleRepositoryInternal {
    <S extends SpecialTitle> Mono<S> save(S entity);

    Flux<SpecialTitle> findAllBy(Pageable pageable);

    Flux<SpecialTitle> findAll();

    Mono<SpecialTitle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SpecialTitle> findAllBy(Pageable pageable, Criteria criteria);
}
