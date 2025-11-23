package com.fanflip.admin.repository;

import com.fanflip.admin.domain.UserAssociation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserAssociation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAssociationRepository extends ReactiveCrudRepository<UserAssociation, Long>, UserAssociationRepositoryInternal {
    Flux<UserAssociation> findAllBy(Pageable pageable);

    @Query("SELECT * FROM user_association entity WHERE entity.owner_id = :id")
    Flux<UserAssociation> findByOwner(Long id);

    @Query("SELECT * FROM user_association entity WHERE entity.owner_id IS NULL")
    Flux<UserAssociation> findAllWhereOwnerIsNull();

    @Override
    <S extends UserAssociation> Mono<S> save(S entity);

    @Override
    Flux<UserAssociation> findAll();

    @Override
    Mono<UserAssociation> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserAssociationRepositoryInternal {
    <S extends UserAssociation> Mono<S> save(S entity);

    Flux<UserAssociation> findAllBy(Pageable pageable);

    Flux<UserAssociation> findAll();

    Mono<UserAssociation> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserAssociation> findAllBy(Pageable pageable, Criteria criteria);
}
