package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpSubcategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpSubcategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpSubcategoryRepository extends ReactiveCrudRepository<HelpSubcategory, Long>, HelpSubcategoryRepositoryInternal {
    Flux<HelpSubcategory> findAllBy(Pageable pageable);

    @Query("SELECT * FROM help_subcategory entity WHERE entity.category_id = :id")
    Flux<HelpSubcategory> findByCategory(Long id);

    @Query("SELECT * FROM help_subcategory entity WHERE entity.category_id IS NULL")
    Flux<HelpSubcategory> findAllWhereCategoryIsNull();

    @Override
    <S extends HelpSubcategory> Mono<S> save(S entity);

    @Override
    Flux<HelpSubcategory> findAll();

    @Override
    Mono<HelpSubcategory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HelpSubcategoryRepositoryInternal {
    <S extends HelpSubcategory> Mono<S> save(S entity);

    Flux<HelpSubcategory> findAllBy(Pageable pageable);

    Flux<HelpSubcategory> findAll();

    Mono<HelpSubcategory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpSubcategory> findAllBy(Pageable pageable, Criteria criteria);
}
