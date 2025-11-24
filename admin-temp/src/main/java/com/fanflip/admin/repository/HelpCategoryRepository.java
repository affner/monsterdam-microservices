package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpCategoryRepository extends ReactiveCrudRepository<HelpCategory, Long>, HelpCategoryRepositoryInternal {
    Flux<HelpCategory> findAllBy(Pageable pageable);

    @Override
    <S extends HelpCategory> Mono<S> save(S entity);

    @Override
    Flux<HelpCategory> findAll();

    @Override
    Mono<HelpCategory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HelpCategoryRepositoryInternal {
    <S extends HelpCategory> Mono<S> save(S entity);

    Flux<HelpCategory> findAllBy(Pageable pageable);

    Flux<HelpCategory> findAll();

    Mono<HelpCategory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpCategory> findAllBy(Pageable pageable, Criteria criteria);
}
