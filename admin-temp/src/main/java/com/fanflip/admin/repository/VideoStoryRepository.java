package com.fanflip.admin.repository;

import com.fanflip.admin.domain.VideoStory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the VideoStory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoStoryRepository extends ReactiveCrudRepository<VideoStory, Long>, VideoStoryRepositoryInternal {
    Flux<VideoStory> findAllBy(Pageable pageable);

    @Query("SELECT * FROM video_story entity WHERE entity.creator_id = :id")
    Flux<VideoStory> findByCreator(Long id);

    @Query("SELECT * FROM video_story entity WHERE entity.creator_id IS NULL")
    Flux<VideoStory> findAllWhereCreatorIsNull();

    @Override
    <S extends VideoStory> Mono<S> save(S entity);

    @Override
    Flux<VideoStory> findAll();

    @Override
    Mono<VideoStory> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface VideoStoryRepositoryInternal {
    <S extends VideoStory> Mono<S> save(S entity);

    Flux<VideoStory> findAllBy(Pageable pageable);

    Flux<VideoStory> findAll();

    Mono<VideoStory> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<VideoStory> findAllBy(Pageable pageable, Criteria criteria);
}
