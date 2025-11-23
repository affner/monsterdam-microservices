package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PollOptionRepository;
import com.fanflip.admin.repository.search.PollOptionSearchRepository;
import com.fanflip.admin.service.PollOptionService;
import com.fanflip.admin.service.dto.PollOptionDTO;
import com.fanflip.admin.service.mapper.PollOptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PollOption}.
 */
@Service
@Transactional
public class PollOptionServiceImpl implements PollOptionService {

    private final Logger log = LoggerFactory.getLogger(PollOptionServiceImpl.class);

    private final PollOptionRepository pollOptionRepository;

    private final PollOptionMapper pollOptionMapper;

    private final PollOptionSearchRepository pollOptionSearchRepository;

    public PollOptionServiceImpl(
        PollOptionRepository pollOptionRepository,
        PollOptionMapper pollOptionMapper,
        PollOptionSearchRepository pollOptionSearchRepository
    ) {
        this.pollOptionRepository = pollOptionRepository;
        this.pollOptionMapper = pollOptionMapper;
        this.pollOptionSearchRepository = pollOptionSearchRepository;
    }

    @Override
    public Mono<PollOptionDTO> save(PollOptionDTO pollOptionDTO) {
        log.debug("Request to save PollOption : {}", pollOptionDTO);
        return pollOptionRepository
            .save(pollOptionMapper.toEntity(pollOptionDTO))
            .flatMap(pollOptionSearchRepository::save)
            .map(pollOptionMapper::toDto);
    }

    @Override
    public Mono<PollOptionDTO> update(PollOptionDTO pollOptionDTO) {
        log.debug("Request to update PollOption : {}", pollOptionDTO);
        return pollOptionRepository
            .save(pollOptionMapper.toEntity(pollOptionDTO))
            .flatMap(pollOptionSearchRepository::save)
            .map(pollOptionMapper::toDto);
    }

    @Override
    public Mono<PollOptionDTO> partialUpdate(PollOptionDTO pollOptionDTO) {
        log.debug("Request to partially update PollOption : {}", pollOptionDTO);

        return pollOptionRepository
            .findById(pollOptionDTO.getId())
            .map(existingPollOption -> {
                pollOptionMapper.partialUpdate(existingPollOption, pollOptionDTO);

                return existingPollOption;
            })
            .flatMap(pollOptionRepository::save)
            .flatMap(savedPollOption -> {
                pollOptionSearchRepository.save(savedPollOption);
                return Mono.just(savedPollOption);
            })
            .map(pollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PollOptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PollOptions");
        return pollOptionRepository.findAllBy(pageable).map(pollOptionMapper::toDto);
    }

    public Flux<PollOptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pollOptionRepository.findAllWithEagerRelationships(pageable).map(pollOptionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pollOptionRepository.count();
    }

    public Mono<Long> searchCount() {
        return pollOptionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PollOptionDTO> findOne(Long id) {
        log.debug("Request to get PollOption : {}", id);
        return pollOptionRepository.findOneWithEagerRelationships(id).map(pollOptionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PollOption : {}", id);
        return pollOptionRepository.deleteById(id).then(pollOptionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PollOptionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PollOptions for query {}", query);
        return pollOptionSearchRepository.search(query, pageable).map(pollOptionMapper::toDto);
    }
}
