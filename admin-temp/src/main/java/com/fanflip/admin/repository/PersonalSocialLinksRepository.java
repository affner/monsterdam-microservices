package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.PersonalSocialLinks;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PersonalSocialLinks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalSocialLinksRepository
    extends ReactiveCrudRepository<PersonalSocialLinks, Long>, PersonalSocialLinksRepositoryInternal {
    Flux<PersonalSocialLinks> findAllBy(Pageable pageable);

    @Override
    Mono<PersonalSocialLinks> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PersonalSocialLinks> findAllWithEagerRelationships();

    @Override
    Flux<PersonalSocialLinks> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM personal_social_links entity WHERE entity.social_network_id = :id")
    Flux<PersonalSocialLinks> findBySocialNetwork(Long id);

    @Query("SELECT * FROM personal_social_links entity WHERE entity.social_network_id IS NULL")
    Flux<PersonalSocialLinks> findAllWhereSocialNetworkIsNull();

    @Query("SELECT * FROM personal_social_links entity WHERE entity.user_id = :id")
    Flux<PersonalSocialLinks> findByUser(Long id);

    @Query("SELECT * FROM personal_social_links entity WHERE entity.user_id IS NULL")
    Flux<PersonalSocialLinks> findAllWhereUserIsNull();

    @Override
    <S extends PersonalSocialLinks> Mono<S> save(S entity);

    @Override
    Flux<PersonalSocialLinks> findAll();

    @Override
    Mono<PersonalSocialLinks> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PersonalSocialLinksRepositoryInternal {
    <S extends PersonalSocialLinks> Mono<S> save(S entity);

    Flux<PersonalSocialLinks> findAllBy(Pageable pageable);

    Flux<PersonalSocialLinks> findAll();

    Mono<PersonalSocialLinks> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PersonalSocialLinks> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PersonalSocialLinks> findOneWithEagerRelationships(Long id);

    Flux<PersonalSocialLinks> findAllWithEagerRelationships();

    Flux<PersonalSocialLinks> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
