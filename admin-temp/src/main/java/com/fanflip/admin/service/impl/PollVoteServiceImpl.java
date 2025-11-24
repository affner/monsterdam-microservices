package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.PollVoteRepository;
import com.monsterdam.admin.repository.search.PollVoteSearchRepository;
import com.monsterdam.admin.service.PollVoteService;
import com.monsterdam.admin.service.dto.PollVoteDTO;
import com.monsterdam.admin.service.mapper.PollVoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.PollVote}.
 */
@Service
@Transactional
public class PollVoteServiceImpl implements PollVoteService {

    private final Logger log = LoggerFactory.getLogger(PollVoteServiceImpl.class);

    private final PollVoteRepository pollVoteRepository;

    private final PollVoteMapper pollVoteMapper;

    private final PollVoteSearchRepository pollVoteSearchRepository;

    public PollVoteServiceImpl(
        PollVoteRepository pollVoteRepository,
        PollVoteMapper pollVoteMapper,
        PollVoteSearchRepository pollVoteSearchRepository
    ) {
        this.pollVoteRepository = pollVoteRepository;
        this.pollVoteMapper = pollVoteMapper;
        this.pollVoteSearchRepository = pollVoteSearchRepository;
    }

    @Override
    public Mono<PollVoteDTO> save(PollVoteDTO pollVoteDTO) {
        log.debug("Request to save PollVote : {}", pollVoteDTO);
        return pollVoteRepository
            .save(pollVoteMapper.toEntity(pollVoteDTO))
            .flatMap(pollVoteSearchRepository::save)
            .map(pollVoteMapper::toDto);
    }

    @Override
    public Mono<PollVoteDTO> update(PollVoteDTO pollVoteDTO) {
        log.debug("Request to update PollVote : {}", pollVoteDTO);
        return pollVoteRepository
            .save(pollVoteMapper.toEntity(pollVoteDTO))
            .flatMap(pollVoteSearchRepository::save)
            .map(pollVoteMapper::toDto);
    }

    @Override
    public Mono<PollVoteDTO> partialUpdate(PollVoteDTO pollVoteDTO) {
        log.debug("Request to partially update PollVote : {}", pollVoteDTO);

        return pollVoteRepository
            .findById(pollVoteDTO.getId())
            .map(existingPollVote -> {
                pollVoteMapper.partialUpdate(existingPollVote, pollVoteDTO);

                return existingPollVote;
            })
            .flatMap(pollVoteRepository::save)
            .flatMap(savedPollVote -> {
                pollVoteSearchRepository.save(savedPollVote);
                return Mono.just(savedPollVote);
            })
            .map(pollVoteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PollVoteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PollVotes");
        return pollVoteRepository.findAllBy(pageable).map(pollVoteMapper::toDto);
    }

    public Flux<PollVoteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pollVoteRepository.findAllWithEagerRelationships(pageable).map(pollVoteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pollVoteRepository.count();
    }

    public Mono<Long> searchCount() {
        return pollVoteSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PollVoteDTO> findOne(Long id) {
        log.debug("Request to get PollVote : {}", id);
        return pollVoteRepository.findOneWithEagerRelationships(id).map(pollVoteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PollVote : {}", id);
        return pollVoteRepository.deleteById(id).then(pollVoteSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PollVoteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PollVotes for query {}", query);
        return pollVoteSearchRepository.search(query, pageable).map(pollVoteMapper::toDto);
    }
}
