package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.ModerationAction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ModerationAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModerationActionRepository extends ReactiveCrudRepository<ModerationAction, Long>, ModerationActionRepositoryInternal {
    @Query("SELECT * FROM moderation_action entity WHERE entity.id not in (select assistance_ticket_id from assistance_ticket)")
    Flux<ModerationAction> findAllWhereAssistanceTicketIsNull();

    @Override
    <S extends ModerationAction> Mono<S> save(S entity);

    @Override
    Flux<ModerationAction> findAll();

    @Override
    Mono<ModerationAction> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ModerationActionRepositoryInternal {
    <S extends ModerationAction> Mono<S> save(S entity);

    Flux<ModerationAction> findAllBy(Pageable pageable);

    Flux<ModerationAction> findAll();

    Mono<ModerationAction> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ModerationAction> findAllBy(Pageable pageable, Criteria criteria);
}
