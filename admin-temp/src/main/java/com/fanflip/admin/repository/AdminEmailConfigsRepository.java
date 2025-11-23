package com.fanflip.admin.repository;

import com.fanflip.admin.domain.AdminEmailConfigs;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the AdminEmailConfigs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminEmailConfigsRepository extends ReactiveCrudRepository<AdminEmailConfigs, Long>, AdminEmailConfigsRepositoryInternal {
    Flux<AdminEmailConfigs> findAllBy(Pageable pageable);

    @Override
    <S extends AdminEmailConfigs> Mono<S> save(S entity);

    @Override
    Flux<AdminEmailConfigs> findAll();

    @Override
    Mono<AdminEmailConfigs> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface AdminEmailConfigsRepositoryInternal {
    <S extends AdminEmailConfigs> Mono<S> save(S entity);

    Flux<AdminEmailConfigs> findAllBy(Pageable pageable);

    Flux<AdminEmailConfigs> findAll();

    Mono<AdminEmailConfigs> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<AdminEmailConfigs> findAllBy(Pageable pageable, Criteria criteria);
}
