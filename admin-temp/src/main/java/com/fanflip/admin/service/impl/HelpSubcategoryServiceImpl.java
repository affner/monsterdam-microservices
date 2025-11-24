package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.HelpSubcategoryRepository;
import com.monsterdam.admin.repository.search.HelpSubcategorySearchRepository;
import com.monsterdam.admin.service.HelpSubcategoryService;
import com.monsterdam.admin.service.dto.HelpSubcategoryDTO;
import com.monsterdam.admin.service.mapper.HelpSubcategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.HelpSubcategory}.
 */
@Service
@Transactional
public class HelpSubcategoryServiceImpl implements HelpSubcategoryService {

    private final Logger log = LoggerFactory.getLogger(HelpSubcategoryServiceImpl.class);

    private final HelpSubcategoryRepository helpSubcategoryRepository;

    private final HelpSubcategoryMapper helpSubcategoryMapper;

    private final HelpSubcategorySearchRepository helpSubcategorySearchRepository;

    public HelpSubcategoryServiceImpl(
        HelpSubcategoryRepository helpSubcategoryRepository,
        HelpSubcategoryMapper helpSubcategoryMapper,
        HelpSubcategorySearchRepository helpSubcategorySearchRepository
    ) {
        this.helpSubcategoryRepository = helpSubcategoryRepository;
        this.helpSubcategoryMapper = helpSubcategoryMapper;
        this.helpSubcategorySearchRepository = helpSubcategorySearchRepository;
    }

    @Override
    public Mono<HelpSubcategoryDTO> save(HelpSubcategoryDTO helpSubcategoryDTO) {
        log.debug("Request to save HelpSubcategory : {}", helpSubcategoryDTO);
        return helpSubcategoryRepository
            .save(helpSubcategoryMapper.toEntity(helpSubcategoryDTO))
            .flatMap(helpSubcategorySearchRepository::save)
            .map(helpSubcategoryMapper::toDto);
    }

    @Override
    public Mono<HelpSubcategoryDTO> update(HelpSubcategoryDTO helpSubcategoryDTO) {
        log.debug("Request to update HelpSubcategory : {}", helpSubcategoryDTO);
        return helpSubcategoryRepository
            .save(helpSubcategoryMapper.toEntity(helpSubcategoryDTO))
            .flatMap(helpSubcategorySearchRepository::save)
            .map(helpSubcategoryMapper::toDto);
    }

    @Override
    public Mono<HelpSubcategoryDTO> partialUpdate(HelpSubcategoryDTO helpSubcategoryDTO) {
        log.debug("Request to partially update HelpSubcategory : {}", helpSubcategoryDTO);

        return helpSubcategoryRepository
            .findById(helpSubcategoryDTO.getId())
            .map(existingHelpSubcategory -> {
                helpSubcategoryMapper.partialUpdate(existingHelpSubcategory, helpSubcategoryDTO);

                return existingHelpSubcategory;
            })
            .flatMap(helpSubcategoryRepository::save)
            .flatMap(savedHelpSubcategory -> {
                helpSubcategorySearchRepository.save(savedHelpSubcategory);
                return Mono.just(savedHelpSubcategory);
            })
            .map(helpSubcategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpSubcategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpSubcategories");
        return helpSubcategoryRepository.findAllBy(pageable).map(helpSubcategoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return helpSubcategoryRepository.count();
    }

    public Mono<Long> searchCount() {
        return helpSubcategorySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HelpSubcategoryDTO> findOne(Long id) {
        log.debug("Request to get HelpSubcategory : {}", id);
        return helpSubcategoryRepository.findById(id).map(helpSubcategoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpSubcategory : {}", id);
        return helpSubcategoryRepository.deleteById(id).then(helpSubcategorySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpSubcategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HelpSubcategories for query {}", query);
        return helpSubcategorySearchRepository.search(query, pageable).map(helpSubcategoryMapper::toDto);
    }
}
