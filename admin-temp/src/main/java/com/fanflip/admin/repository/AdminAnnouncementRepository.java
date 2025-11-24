package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.AdminAnnouncement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AdminAnnouncement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminAnnouncementRepository extends ReactiveCrudRepository<AdminAnnouncement, Long>, AdminAnnouncementRepositoryInternal {
    Flux<AdminAnnouncement> findAllBy(Pageable pageable);

    @Query("SELECT * FROM admin_announcement entity WHERE entity.announcer_message_id = :id")
    Flux<AdminAnnouncement> findByAnnouncerMessage(Long id);

    @Query("SELECT * FROM admin_announcement entity WHERE entity.announcer_message_id IS NULL")
    Flux<AdminAnnouncement> findAllWhereAnnouncerMessageIsNull();

    @Query("SELECT * FROM admin_announcement entity WHERE entity.admin_id = :id")
    Flux<AdminAnnouncement> findByAdmin(Long id);

    @Query("SELECT * FROM admin_announcement entity WHERE entity.admin_id IS NULL")
    Flux<AdminAnnouncement> findAllWhereAdminIsNull();

    @Override
    <S extends AdminAnnouncement> Mono<S> save(S entity);

    @Override
    Flux<AdminAnnouncement> findAll();

    @Override
    Mono<AdminAnnouncement> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AdminAnnouncementRepositoryInternal {
    <S extends AdminAnnouncement> Mono<S> save(S entity);

    Flux<AdminAnnouncement> findAllBy(Pageable pageable);

    Flux<AdminAnnouncement> findAll();

    Mono<AdminAnnouncement> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AdminAnnouncement> findAllBy(Pageable pageable, Criteria criteria);
}
