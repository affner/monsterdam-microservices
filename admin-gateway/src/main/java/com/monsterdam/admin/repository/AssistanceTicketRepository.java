package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AssistanceTicket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AssistanceTicket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssistanceTicketRepository extends ReactiveCrudRepository<AssistanceTicket, Long>, AssistanceTicketRepositoryInternal {
    @Query("SELECT * FROM assistance_ticket entity WHERE entity.moderation_action_id = :id")
    Flux<AssistanceTicket> findByModerationAction(Long id);

    @Query("SELECT * FROM assistance_ticket entity WHERE entity.moderation_action_id IS NULL")
    Flux<AssistanceTicket> findAllWhereModerationActionIsNull();

    @Query("SELECT * FROM assistance_ticket entity WHERE entity.id not in (select report_id from user_report)")
    Flux<AssistanceTicket> findAllWhereReportIsNull();

    @Query("SELECT * FROM assistance_ticket entity WHERE entity.id not in (select documents_review_id from identity_document_review)")
    Flux<AssistanceTicket> findAllWhereDocumentsReviewIsNull();

    @Query("SELECT * FROM assistance_ticket entity WHERE entity.assigned_admin_id = :id")
    Flux<AssistanceTicket> findByAssignedAdmin(Long id);

    @Query("SELECT * FROM assistance_ticket entity WHERE entity.assigned_admin_id IS NULL")
    Flux<AssistanceTicket> findAllWhereAssignedAdminIsNull();

    @Override
    <S extends AssistanceTicket> Mono<S> save(S entity);

    @Override
    Flux<AssistanceTicket> findAll();

    @Override
    Mono<AssistanceTicket> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AssistanceTicketRepositoryInternal {
    <S extends AssistanceTicket> Mono<S> save(S entity);

    Flux<AssistanceTicket> findAllBy(Pageable pageable);

    Flux<AssistanceTicket> findAll();

    Mono<AssistanceTicket> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AssistanceTicket> findAllBy(Pageable pageable, Criteria criteria);
}
