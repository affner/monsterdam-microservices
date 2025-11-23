package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SpecialAwardRepository;
import com.fanflip.admin.repository.search.SpecialAwardSearchRepository;
import com.fanflip.admin.service.SpecialAwardService;
import com.fanflip.admin.service.dto.SpecialAwardDTO;
import com.fanflip.admin.service.mapper.SpecialAwardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SpecialAward}.
 */
@Service
@Transactional
public class SpecialAwardServiceImpl implements SpecialAwardService {

    private final Logger log = LoggerFactory.getLogger(SpecialAwardServiceImpl.class);

    private final SpecialAwardRepository specialAwardRepository;

    private final SpecialAwardMapper specialAwardMapper;

    private final SpecialAwardSearchRepository specialAwardSearchRepository;

    public SpecialAwardServiceImpl(
        SpecialAwardRepository specialAwardRepository,
        SpecialAwardMapper specialAwardMapper,
        SpecialAwardSearchRepository specialAwardSearchRepository
    ) {
        this.specialAwardRepository = specialAwardRepository;
        this.specialAwardMapper = specialAwardMapper;
        this.specialAwardSearchRepository = specialAwardSearchRepository;
    }

    @Override
    public Mono<SpecialAwardDTO> save(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to save SpecialAward : {}", specialAwardDTO);
        return specialAwardRepository
            .save(specialAwardMapper.toEntity(specialAwardDTO))
            .flatMap(specialAwardSearchRepository::save)
            .map(specialAwardMapper::toDto);
    }

    @Override
    public Mono<SpecialAwardDTO> update(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to update SpecialAward : {}", specialAwardDTO);
        return specialAwardRepository
            .save(specialAwardMapper.toEntity(specialAwardDTO))
            .flatMap(specialAwardSearchRepository::save)
            .map(specialAwardMapper::toDto);
    }

    @Override
    public Mono<SpecialAwardDTO> partialUpdate(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to partially update SpecialAward : {}", specialAwardDTO);

        return specialAwardRepository
            .findById(specialAwardDTO.getId())
            .map(existingSpecialAward -> {
                specialAwardMapper.partialUpdate(existingSpecialAward, specialAwardDTO);

                return existingSpecialAward;
            })
            .flatMap(specialAwardRepository::save)
            .flatMap(savedSpecialAward -> {
                specialAwardSearchRepository.save(savedSpecialAward);
                return Mono.just(savedSpecialAward);
            })
            .map(specialAwardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialAwardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialAwards");
        return specialAwardRepository.findAllBy(pageable).map(specialAwardMapper::toDto);
    }

    public Mono<Long> countAll() {
        return specialAwardRepository.count();
    }

    public Mono<Long> searchCount() {
        return specialAwardSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SpecialAwardDTO> findOne(Long id) {
        log.debug("Request to get SpecialAward : {}", id);
        return specialAwardRepository.findById(id).map(specialAwardMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SpecialAward : {}", id);
        return specialAwardRepository.deleteById(id).then(specialAwardSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialAwardDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpecialAwards for query {}", query);
        return specialAwardSearchRepository.search(query, pageable).map(specialAwardMapper::toDto);
    }
}
