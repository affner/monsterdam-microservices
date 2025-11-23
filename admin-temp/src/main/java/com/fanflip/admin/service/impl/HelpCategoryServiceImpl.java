package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.HelpCategoryRepository;
import com.fanflip.admin.repository.search.HelpCategorySearchRepository;
import com.fanflip.admin.service.HelpCategoryService;
import com.fanflip.admin.service.dto.HelpCategoryDTO;
import com.fanflip.admin.service.mapper.HelpCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.HelpCategory}.
 */
@Service
@Transactional
public class HelpCategoryServiceImpl implements HelpCategoryService {

    private final Logger log = LoggerFactory.getLogger(HelpCategoryServiceImpl.class);

    private final HelpCategoryRepository helpCategoryRepository;

    private final HelpCategoryMapper helpCategoryMapper;

    private final HelpCategorySearchRepository helpCategorySearchRepository;

    public HelpCategoryServiceImpl(
        HelpCategoryRepository helpCategoryRepository,
        HelpCategoryMapper helpCategoryMapper,
        HelpCategorySearchRepository helpCategorySearchRepository
    ) {
        this.helpCategoryRepository = helpCategoryRepository;
        this.helpCategoryMapper = helpCategoryMapper;
        this.helpCategorySearchRepository = helpCategorySearchRepository;
    }

    @Override
    public Mono<HelpCategoryDTO> save(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to save HelpCategory : {}", helpCategoryDTO);
        return helpCategoryRepository
            .save(helpCategoryMapper.toEntity(helpCategoryDTO))
            .flatMap(helpCategorySearchRepository::save)
            .map(helpCategoryMapper::toDto);
    }

    @Override
    public Mono<HelpCategoryDTO> update(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to update HelpCategory : {}", helpCategoryDTO);
        return helpCategoryRepository
            .save(helpCategoryMapper.toEntity(helpCategoryDTO))
            .flatMap(helpCategorySearchRepository::save)
            .map(helpCategoryMapper::toDto);
    }

    @Override
    public Mono<HelpCategoryDTO> partialUpdate(HelpCategoryDTO helpCategoryDTO) {
        log.debug("Request to partially update HelpCategory : {}", helpCategoryDTO);

        return helpCategoryRepository
            .findById(helpCategoryDTO.getId())
            .map(existingHelpCategory -> {
                helpCategoryMapper.partialUpdate(existingHelpCategory, helpCategoryDTO);

                return existingHelpCategory;
            })
            .flatMap(helpCategoryRepository::save)
            .flatMap(savedHelpCategory -> {
                helpCategorySearchRepository.save(savedHelpCategory);
                return Mono.just(savedHelpCategory);
            })
            .map(helpCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpCategories");
        return helpCategoryRepository.findAllBy(pageable).map(helpCategoryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return helpCategoryRepository.count();
    }

    public Mono<Long> searchCount() {
        return helpCategorySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HelpCategoryDTO> findOne(Long id) {
        log.debug("Request to get HelpCategory : {}", id);
        return helpCategoryRepository.findById(id).map(helpCategoryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpCategory : {}", id);
        return helpCategoryRepository.deleteById(id).then(helpCategorySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpCategoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HelpCategories for query {}", query);
        return helpCategorySearchRepository.search(query, pageable).map(helpCategoryMapper::toDto);
    }
}
