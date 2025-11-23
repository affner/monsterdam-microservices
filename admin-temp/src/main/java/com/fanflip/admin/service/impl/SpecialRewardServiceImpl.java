package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SpecialRewardRepository;
import com.fanflip.admin.repository.search.SpecialRewardSearchRepository;
import com.fanflip.admin.service.SpecialRewardService;
import com.fanflip.admin.service.dto.SpecialRewardDTO;
import com.fanflip.admin.service.mapper.SpecialRewardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SpecialReward}.
 */
@Service
@Transactional
public class SpecialRewardServiceImpl implements SpecialRewardService {

    private final Logger log = LoggerFactory.getLogger(SpecialRewardServiceImpl.class);

    private final SpecialRewardRepository specialRewardRepository;

    private final SpecialRewardMapper specialRewardMapper;

    private final SpecialRewardSearchRepository specialRewardSearchRepository;

    public SpecialRewardServiceImpl(
        SpecialRewardRepository specialRewardRepository,
        SpecialRewardMapper specialRewardMapper,
        SpecialRewardSearchRepository specialRewardSearchRepository
    ) {
        this.specialRewardRepository = specialRewardRepository;
        this.specialRewardMapper = specialRewardMapper;
        this.specialRewardSearchRepository = specialRewardSearchRepository;
    }

    @Override
    public Mono<SpecialRewardDTO> save(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to save SpecialReward : {}", specialRewardDTO);
        return specialRewardRepository
            .save(specialRewardMapper.toEntity(specialRewardDTO))
            .flatMap(specialRewardSearchRepository::save)
            .map(specialRewardMapper::toDto);
    }

    @Override
    public Mono<SpecialRewardDTO> update(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to update SpecialReward : {}", specialRewardDTO);
        return specialRewardRepository
            .save(specialRewardMapper.toEntity(specialRewardDTO))
            .flatMap(specialRewardSearchRepository::save)
            .map(specialRewardMapper::toDto);
    }

    @Override
    public Mono<SpecialRewardDTO> partialUpdate(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to partially update SpecialReward : {}", specialRewardDTO);

        return specialRewardRepository
            .findById(specialRewardDTO.getId())
            .map(existingSpecialReward -> {
                specialRewardMapper.partialUpdate(existingSpecialReward, specialRewardDTO);

                return existingSpecialReward;
            })
            .flatMap(specialRewardRepository::save)
            .flatMap(savedSpecialReward -> {
                specialRewardSearchRepository.save(savedSpecialReward);
                return Mono.just(savedSpecialReward);
            })
            .map(specialRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialRewards");
        return specialRewardRepository.findAllBy(pageable).map(specialRewardMapper::toDto);
    }

    public Mono<Long> countAll() {
        return specialRewardRepository.count();
    }

    public Mono<Long> searchCount() {
        return specialRewardSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SpecialRewardDTO> findOne(Long id) {
        log.debug("Request to get SpecialReward : {}", id);
        return specialRewardRepository.findById(id).map(specialRewardMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SpecialReward : {}", id);
        return specialRewardRepository.deleteById(id).then(specialRewardSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialRewardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpecialRewards for query {}", query);
        return specialRewardSearchRepository.search(query, pageable).map(specialRewardMapper::toDto);
    }
}
