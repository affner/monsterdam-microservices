package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.LiabilityRepository;
import com.fanflip.admin.repository.search.LiabilitySearchRepository;
import com.fanflip.admin.service.LiabilityService;
import com.fanflip.admin.service.dto.LiabilityDTO;
import com.fanflip.admin.service.mapper.LiabilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Liability}.
 */
@Service
@Transactional
public class LiabilityServiceImpl implements LiabilityService {

    private final Logger log = LoggerFactory.getLogger(LiabilityServiceImpl.class);

    private final LiabilityRepository liabilityRepository;

    private final LiabilityMapper liabilityMapper;

    private final LiabilitySearchRepository liabilitySearchRepository;

    public LiabilityServiceImpl(
        LiabilityRepository liabilityRepository,
        LiabilityMapper liabilityMapper,
        LiabilitySearchRepository liabilitySearchRepository
    ) {
        this.liabilityRepository = liabilityRepository;
        this.liabilityMapper = liabilityMapper;
        this.liabilitySearchRepository = liabilitySearchRepository;
    }

    @Override
    public Mono<LiabilityDTO> save(LiabilityDTO liabilityDTO) {
        log.debug("Request to save Liability : {}", liabilityDTO);
        return liabilityRepository
            .save(liabilityMapper.toEntity(liabilityDTO))
            .flatMap(liabilitySearchRepository::save)
            .map(liabilityMapper::toDto);
    }

    @Override
    public Mono<LiabilityDTO> update(LiabilityDTO liabilityDTO) {
        log.debug("Request to update Liability : {}", liabilityDTO);
        return liabilityRepository
            .save(liabilityMapper.toEntity(liabilityDTO))
            .flatMap(liabilitySearchRepository::save)
            .map(liabilityMapper::toDto);
    }

    @Override
    public Mono<LiabilityDTO> partialUpdate(LiabilityDTO liabilityDTO) {
        log.debug("Request to partially update Liability : {}", liabilityDTO);

        return liabilityRepository
            .findById(liabilityDTO.getId())
            .map(existingLiability -> {
                liabilityMapper.partialUpdate(existingLiability, liabilityDTO);

                return existingLiability;
            })
            .flatMap(liabilityRepository::save)
            .flatMap(savedLiability -> {
                liabilitySearchRepository.save(savedLiability);
                return Mono.just(savedLiability);
            })
            .map(liabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LiabilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Liabilities");
        return liabilityRepository.findAllBy(pageable).map(liabilityMapper::toDto);
    }

    public Mono<Long> countAll() {
        return liabilityRepository.count();
    }

    public Mono<Long> searchCount() {
        return liabilitySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<LiabilityDTO> findOne(Long id) {
        log.debug("Request to get Liability : {}", id);
        return liabilityRepository.findById(id).map(liabilityMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Liability : {}", id);
        return liabilityRepository.deleteById(id).then(liabilitySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<LiabilityDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Liabilities for query {}", query);
        return liabilitySearchRepository.search(query, pageable).map(liabilityMapper::toDto);
    }
}
