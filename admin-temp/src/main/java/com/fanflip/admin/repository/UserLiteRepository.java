package com.fanflip.admin.repository;

import com.fanflip.admin.domain.UserLite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserLite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserLiteRepository extends ReactiveCrudRepository<UserLite, Long>, UserLiteRepositoryInternal {
    Flux<UserLite> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_lite entity WHERE entity.id not in (select user_profile_id from user_profile)")
    Flux<UserLite> findAllWhereUserProfileIsNull();

    @Override
    <S extends UserLite> Mono<S> save(S entity);

    @Override
    Flux<UserLite> findAll();

    @Override
    Mono<UserLite> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserLiteRepositoryInternal {
    <S extends UserLite> Mono<S> save(S entity);

    Flux<UserLite> findAllBy(Pageable pageable);

    Flux<UserLite> findAll();

    Mono<UserLite> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserLite> findAllBy(Pageable pageable, Criteria criteria);
}
