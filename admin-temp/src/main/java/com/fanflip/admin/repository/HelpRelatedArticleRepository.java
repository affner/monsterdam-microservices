package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpRelatedArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpRelatedArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpRelatedArticleRepository
    extends ReactiveCrudRepository<HelpRelatedArticle, Long>, HelpRelatedArticleRepositoryInternal {
    Flux<HelpRelatedArticle> findAllBy(Pageable pageable);

    @Override
    <S extends HelpRelatedArticle> Mono<S> save(S entity);

    @Override
    Flux<HelpRelatedArticle> findAll();

    @Override
    Mono<HelpRelatedArticle> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HelpRelatedArticleRepositoryInternal {
    <S extends HelpRelatedArticle> Mono<S> save(S entity);

    Flux<HelpRelatedArticle> findAllBy(Pageable pageable);

    Flux<HelpRelatedArticle> findAll();

    Mono<HelpRelatedArticle> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpRelatedArticle> findAllBy(Pageable pageable, Criteria criteria);
}
