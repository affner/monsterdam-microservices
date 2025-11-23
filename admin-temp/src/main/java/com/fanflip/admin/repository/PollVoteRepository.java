package com.fanflip.admin.repository;

import com.fanflip.admin.domain.PollVote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PollVote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PollVoteRepository extends ReactiveCrudRepository<PollVote, Long>, PollVoteRepositoryInternal {
    Flux<PollVote> findAllBy(Pageable pageable);

    @Override
    Mono<PollVote> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PollVote> findAllWithEagerRelationships();

    @Override
    Flux<PollVote> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM poll_vote entity WHERE entity.poll_option_id = :id")
    Flux<PollVote> findByPollOption(Long id);

    @Query("SELECT * FROM poll_vote entity WHERE entity.poll_option_id IS NULL")
    Flux<PollVote> findAllWherePollOptionIsNull();

    @Query("SELECT * FROM poll_vote entity WHERE entity.voting_user_id = :id")
    Flux<PollVote> findByVotingUser(Long id);

    @Query("SELECT * FROM poll_vote entity WHERE entity.voting_user_id IS NULL")
    Flux<PollVote> findAllWhereVotingUserIsNull();

    @Override
    <S extends PollVote> Mono<S> save(S entity);

    @Override
    Flux<PollVote> findAll();

    @Override
    Mono<PollVote> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PollVoteRepositoryInternal {
    <S extends PollVote> Mono<S> save(S entity);

    Flux<PollVote> findAllBy(Pageable pageable);

    Flux<PollVote> findAll();

    Mono<PollVote> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PollVote> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PollVote> findOneWithEagerRelationships(Long id);

    Flux<PollVote> findAllWithEagerRelationships();

    Flux<PollVote> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
