package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SpecialTitleRepository;
import com.fanflip.admin.repository.search.SpecialTitleSearchRepository;
import com.fanflip.admin.service.SpecialTitleService;
import com.fanflip.admin.service.dto.SpecialTitleDTO;
import com.fanflip.admin.service.mapper.SpecialTitleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SpecialTitle}.
 */
@Service
@Transactional
public class SpecialTitleServiceImpl implements SpecialTitleService {

    private final Logger log = LoggerFactory.getLogger(SpecialTitleServiceImpl.class);

    private final SpecialTitleRepository specialTitleRepository;

    private final SpecialTitleMapper specialTitleMapper;

    private final SpecialTitleSearchRepository specialTitleSearchRepository;

    public SpecialTitleServiceImpl(
        SpecialTitleRepository specialTitleRepository,
        SpecialTitleMapper specialTitleMapper,
        SpecialTitleSearchRepository specialTitleSearchRepository
    ) {
        this.specialTitleRepository = specialTitleRepository;
        this.specialTitleMapper = specialTitleMapper;
        this.specialTitleSearchRepository = specialTitleSearchRepository;
    }

    @Override
    public Mono<SpecialTitleDTO> save(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to save SpecialTitle : {}", specialTitleDTO);
        return specialTitleRepository
            .save(specialTitleMapper.toEntity(specialTitleDTO))
            .flatMap(specialTitleSearchRepository::save)
            .map(specialTitleMapper::toDto);
    }

    @Override
    public Mono<SpecialTitleDTO> update(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to update SpecialTitle : {}", specialTitleDTO);
        return specialTitleRepository
            .save(specialTitleMapper.toEntity(specialTitleDTO))
            .flatMap(specialTitleSearchRepository::save)
            .map(specialTitleMapper::toDto);
    }

    @Override
    public Mono<SpecialTitleDTO> partialUpdate(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to partially update SpecialTitle : {}", specialTitleDTO);

        return specialTitleRepository
            .findById(specialTitleDTO.getId())
            .map(existingSpecialTitle -> {
                specialTitleMapper.partialUpdate(existingSpecialTitle, specialTitleDTO);

                return existingSpecialTitle;
            })
            .flatMap(specialTitleRepository::save)
            .flatMap(savedSpecialTitle -> {
                specialTitleSearchRepository.save(savedSpecialTitle);
                return Mono.just(savedSpecialTitle);
            })
            .map(specialTitleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialTitleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialTitles");
        return specialTitleRepository.findAllBy(pageable).map(specialTitleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return specialTitleRepository.count();
    }

    public Mono<Long> searchCount() {
        return specialTitleSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SpecialTitleDTO> findOne(Long id) {
        log.debug("Request to get SpecialTitle : {}", id);
        return specialTitleRepository.findById(id).map(specialTitleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SpecialTitle : {}", id);
        return specialTitleRepository.deleteById(id).then(specialTitleSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SpecialTitleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SpecialTitles for query {}", query);
        return specialTitleSearchRepository.search(query, pageable).map(specialTitleMapper::toDto);
    }
}
