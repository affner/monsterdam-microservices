package com.fanflip.admin.repository;

import com.fanflip.admin.domain.Budget;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Budget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BudgetRepository extends ReactiveCrudRepository<Budget, Long>, BudgetRepositoryInternal {
    @Override
    <S extends Budget> Mono<S> save(S entity);

    @Override
    Flux<Budget> findAll();

    @Override
    Mono<Budget> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BudgetRepositoryInternal {
    <S extends Budget> Mono<S> save(S entity);

    Flux<Budget> findAllBy(Pageable pageable);

    Flux<Budget> findAll();

    Mono<Budget> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Budget> findAllBy(Pageable pageable, Criteria criteria);
}
