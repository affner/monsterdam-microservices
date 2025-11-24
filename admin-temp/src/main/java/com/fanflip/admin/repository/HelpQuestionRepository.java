package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.HelpQuestion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the HelpQuestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HelpQuestionRepository extends ReactiveCrudRepository<HelpQuestion, Long>, HelpQuestionRepositoryInternal {
    Flux<HelpQuestion> findAllBy(Pageable pageable);

    @Override
    Mono<HelpQuestion> findOneWithEagerRelationships(Long id);

    @Override
    Flux<HelpQuestion> findAllWithEagerRelationships();

    @Override
    Flux<HelpQuestion> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM help_question entity JOIN rel_help_question__question joinTable ON entity.id = joinTable.question_id WHERE joinTable.question_id = :id"
    )
    Flux<HelpQuestion> findByQuestion(Long id);

    @Query("SELECT * FROM help_question entity WHERE entity.subcategory_id = :id")
    Flux<HelpQuestion> findBySubcategory(Long id);

    @Query("SELECT * FROM help_question entity WHERE entity.subcategory_id IS NULL")
    Flux<HelpQuestion> findAllWhereSubcategoryIsNull();

    @Override
    <S extends HelpQuestion> Mono<S> save(S entity);

    @Override
    Flux<HelpQuestion> findAll();

    @Override
    Mono<HelpQuestion> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface HelpQuestionRepositoryInternal {
    <S extends HelpQuestion> Mono<S> save(S entity);

    Flux<HelpQuestion> findAllBy(Pageable pageable);

    Flux<HelpQuestion> findAll();

    Mono<HelpQuestion> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<HelpQuestion> findAllBy(Pageable pageable, Criteria criteria);

    Mono<HelpQuestion> findOneWithEagerRelationships(Long id);

    Flux<HelpQuestion> findAllWithEagerRelationships();

    Flux<HelpQuestion> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
