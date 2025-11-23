package com.fanflip.admin.repository;

import com.fanflip.admin.domain.AdminUserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AdminUserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminUserProfileRepository extends ReactiveCrudRepository<AdminUserProfile, Long>, AdminUserProfileRepositoryInternal {
    @Override
    <S extends AdminUserProfile> Mono<S> save(S entity);

    @Override
    Flux<AdminUserProfile> findAll();

    @Override
    Mono<AdminUserProfile> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AdminUserProfileRepositoryInternal {
    <S extends AdminUserProfile> Mono<S> save(S entity);

    Flux<AdminUserProfile> findAllBy(Pageable pageable);

    Flux<AdminUserProfile> findAll();

    Mono<AdminUserProfile> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AdminUserProfile> findAllBy(Pageable pageable, Criteria criteria);
}
