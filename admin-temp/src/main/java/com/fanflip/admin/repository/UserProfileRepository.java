package com.monsterdam.admin.repository;

import com.monsterdam.admin.domain.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends ReactiveCrudRepository<UserProfile, Long>, UserProfileRepositoryInternal {
    Flux<UserProfile> findAllBy(Pageable pageable);

    @Override
    Mono<UserProfile> findOneWithEagerRelationships(Long id);

    @Override
    Flux<UserProfile> findAllWithEagerRelationships();

    @Override
    Flux<UserProfile> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM user_profile entity WHERE entity.user_lite_id = :id")
    Flux<UserProfile> findByUserLite(Long id);

    @Query("SELECT * FROM user_profile entity WHERE entity.user_lite_id IS NULL")
    Flux<UserProfile> findAllWhereUserLiteIsNull();

    @Query("SELECT * FROM user_profile entity WHERE entity.settings_id = :id")
    Flux<UserProfile> findBySettings(Long id);

    @Query("SELECT * FROM user_profile entity WHERE entity.settings_id IS NULL")
    Flux<UserProfile> findAllWhereSettingsIsNull();

    @Query("SELECT * FROM user_profile entity WHERE entity.country_of_birth_id = :id")
    Flux<UserProfile> findByCountryOfBirth(Long id);

    @Query("SELECT * FROM user_profile entity WHERE entity.country_of_birth_id IS NULL")
    Flux<UserProfile> findAllWhereCountryOfBirthIsNull();

    @Query("SELECT * FROM user_profile entity WHERE entity.state_of_residence_id = :id")
    Flux<UserProfile> findByStateOfResidence(Long id);

    @Query("SELECT * FROM user_profile entity WHERE entity.state_of_residence_id IS NULL")
    Flux<UserProfile> findAllWhereStateOfResidenceIsNull();

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__followed joinTable ON entity.id = joinTable.followed_id WHERE joinTable.followed_id = :id"
    )
    Flux<UserProfile> findByFollowed(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__blocked_list joinTable ON entity.id = joinTable.blocked_list_id WHERE joinTable.blocked_list_id = :id"
    )
    Flux<UserProfile> findByBlockedList(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__loya_lists joinTable ON entity.id = joinTable.loya_lists_id WHERE joinTable.loya_lists_id = :id"
    )
    Flux<UserProfile> findByLoyaLists(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__subscribed joinTable ON entity.id = joinTable.subscribed_id WHERE joinTable.subscribed_id = :id"
    )
    Flux<UserProfile> findBySubscribed(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__joined_events joinTable ON entity.id = joinTable.joined_events_id WHERE joinTable.joined_events_id = :id"
    )
    Flux<UserProfile> findByJoinedEvents(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__blocked_ubications joinTable ON entity.id = joinTable.blocked_ubications_id WHERE joinTable.blocked_ubications_id = :id"
    )
    Flux<UserProfile> findByBlockedUbications(Long id);

    @Query(
        "SELECT entity.* FROM user_profile entity JOIN rel_user_profile__hash_tags joinTable ON entity.id = joinTable.hash_tags_id WHERE joinTable.hash_tags_id = :id"
    )
    Flux<UserProfile> findByHashTags(Long id);

    @Override
    <S extends UserProfile> Mono<S> save(S entity);

    @Override
    Flux<UserProfile> findAll();

    @Override
    Mono<UserProfile> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface UserProfileRepositoryInternal {
    <S extends UserProfile> Mono<S> save(S entity);

    Flux<UserProfile> findAllBy(Pageable pageable);

    Flux<UserProfile> findAll();

    Mono<UserProfile> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<UserProfile> findAllBy(Pageable pageable, Criteria criteria);

    Mono<UserProfile> findOneWithEagerRelationships(Long id);

    Flux<UserProfile> findAllWithEagerRelationships();

    Flux<UserProfile> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
