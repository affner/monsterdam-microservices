package com.fanflip.admin.repository;

import com.fanflip.admin.domain.UserReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserReportRepository extends ReactiveCrudRepository<UserReport, Long>, UserReportRepositoryInternal {
    @Query("SELECT * FROM user_report entity WHERE entity.ticket_id = :id")
    Flux<UserReport> findByTicket(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.ticket_id IS NULL")
    Flux<UserReport> findAllWhereTicketIsNull();

    @Override
    <S extends UserReport> Mono<S> save(S entity);

    @Override
    Flux<UserReport> findAll();

    @Override
    Mono<UserReport> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserReportRepositoryInternal {
    <S extends UserReport> Mono<S> save(S entity);

    Flux<UserReport> findAllBy(Pageable pageable);

    Flux<UserReport> findAll();

    Mono<UserReport> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserReport> findAllBy(Pageable pageable, Criteria criteria);
}
