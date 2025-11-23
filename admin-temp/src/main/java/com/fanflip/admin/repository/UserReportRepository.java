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
    Flux<UserReport> findAllBy(Pageable pageable);

    @Override
    Mono<UserReport> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserReport> findAllWithEagerRelationships();

    @Override
    Flux<UserReport> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_report entity WHERE entity.ticket_id = :id")
    Flux<UserReport> findByTicket(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.ticket_id IS NULL")
    Flux<UserReport> findAllWhereTicketIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.reporter_id = :id")
    Flux<UserReport> findByReporter(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.reporter_id IS NULL")
    Flux<UserReport> findAllWhereReporterIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.reported_id = :id")
    Flux<UserReport> findByReported(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.reported_id IS NULL")
    Flux<UserReport> findAllWhereReportedIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.story_id = :id")
    Flux<UserReport> findByStory(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.story_id IS NULL")
    Flux<UserReport> findAllWhereStoryIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.video_id = :id")
    Flux<UserReport> findByVideo(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.video_id IS NULL")
    Flux<UserReport> findAllWhereVideoIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.photo_id = :id")
    Flux<UserReport> findByPhoto(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.photo_id IS NULL")
    Flux<UserReport> findAllWherePhotoIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.audio_id = :id")
    Flux<UserReport> findByAudio(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.audio_id IS NULL")
    Flux<UserReport> findAllWhereAudioIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.live_stream_id = :id")
    Flux<UserReport> findByLiveStream(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.live_stream_id IS NULL")
    Flux<UserReport> findAllWhereLiveStreamIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.message_id = :id")
    Flux<UserReport> findByMessage(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.message_id IS NULL")
    Flux<UserReport> findAllWhereMessageIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.post_id = :id")
    Flux<UserReport> findByPost(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.post_id IS NULL")
    Flux<UserReport> findAllWherePostIsNull();

    @Query("SELECT * FROM user_report entity WHERE entity.post_comment_id = :id")
    Flux<UserReport> findByPostComment(Long id);

    @Query("SELECT * FROM user_report entity WHERE entity.post_comment_id IS NULL")
    Flux<UserReport> findAllWherePostCommentIsNull();

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

    Mono<UserReport> findOneWithEagerRelationships(Long id);

    Flux<UserReport> findAllWithEagerRelationships();

    Flux<UserReport> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
