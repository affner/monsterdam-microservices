package com.monsterdam.app.repository;

import com.monsterdam.app.domain.UserUIPreferences;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserUIPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserUIPreferencesRepository extends ReactiveCrudRepository<UserUIPreferences, Long>, UserUIPreferencesRepositoryInternal {
    Flux<UserUIPreferences> findAllBy(Pageable pageable);

    @Override
    <S extends UserUIPreferences> Mono<S> save(S entity);

    @Override
    Flux<UserUIPreferences> findAll();

    @Override
    Mono<UserUIPreferences> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserUIPreferencesRepositoryInternal {
    <S extends UserUIPreferences> Mono<S> save(S entity);

    Flux<UserUIPreferences> findAllBy(Pageable pageable);

    Flux<UserUIPreferences> findAll();

    Mono<UserUIPreferences> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserUIPreferences> findAllBy(Pageable pageable, Criteria criteria);
}
