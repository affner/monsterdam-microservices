package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.HelpRelatedArticleRepository;
import com.monsterdam.admin.repository.search.HelpRelatedArticleSearchRepository;
import com.monsterdam.admin.service.HelpRelatedArticleService;
import com.monsterdam.admin.service.dto.HelpRelatedArticleDTO;
import com.monsterdam.admin.service.mapper.HelpRelatedArticleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.HelpRelatedArticle}.
 */
@Service
@Transactional
public class HelpRelatedArticleServiceImpl implements HelpRelatedArticleService {

    private final Logger log = LoggerFactory.getLogger(HelpRelatedArticleServiceImpl.class);

    private final HelpRelatedArticleRepository helpRelatedArticleRepository;

    private final HelpRelatedArticleMapper helpRelatedArticleMapper;

    private final HelpRelatedArticleSearchRepository helpRelatedArticleSearchRepository;

    public HelpRelatedArticleServiceImpl(
        HelpRelatedArticleRepository helpRelatedArticleRepository,
        HelpRelatedArticleMapper helpRelatedArticleMapper,
        HelpRelatedArticleSearchRepository helpRelatedArticleSearchRepository
    ) {
        this.helpRelatedArticleRepository = helpRelatedArticleRepository;
        this.helpRelatedArticleMapper = helpRelatedArticleMapper;
        this.helpRelatedArticleSearchRepository = helpRelatedArticleSearchRepository;
    }

    @Override
    public Mono<HelpRelatedArticleDTO> save(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to save HelpRelatedArticle : {}", helpRelatedArticleDTO);
        return helpRelatedArticleRepository
            .save(helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO))
            .flatMap(helpRelatedArticleSearchRepository::save)
            .map(helpRelatedArticleMapper::toDto);
    }

    @Override
    public Mono<HelpRelatedArticleDTO> update(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to update HelpRelatedArticle : {}", helpRelatedArticleDTO);
        return helpRelatedArticleRepository
            .save(helpRelatedArticleMapper.toEntity(helpRelatedArticleDTO))
            .flatMap(helpRelatedArticleSearchRepository::save)
            .map(helpRelatedArticleMapper::toDto);
    }

    @Override
    public Mono<HelpRelatedArticleDTO> partialUpdate(HelpRelatedArticleDTO helpRelatedArticleDTO) {
        log.debug("Request to partially update HelpRelatedArticle : {}", helpRelatedArticleDTO);

        return helpRelatedArticleRepository
            .findById(helpRelatedArticleDTO.getId())
            .map(existingHelpRelatedArticle -> {
                helpRelatedArticleMapper.partialUpdate(existingHelpRelatedArticle, helpRelatedArticleDTO);

                return existingHelpRelatedArticle;
            })
            .flatMap(helpRelatedArticleRepository::save)
            .flatMap(savedHelpRelatedArticle -> {
                helpRelatedArticleSearchRepository.save(savedHelpRelatedArticle);
                return Mono.just(savedHelpRelatedArticle);
            })
            .map(helpRelatedArticleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpRelatedArticleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpRelatedArticles");
        return helpRelatedArticleRepository.findAllBy(pageable).map(helpRelatedArticleMapper::toDto);
    }

    public Mono<Long> countAll() {
        return helpRelatedArticleRepository.count();
    }

    public Mono<Long> searchCount() {
        return helpRelatedArticleSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HelpRelatedArticleDTO> findOne(Long id) {
        log.debug("Request to get HelpRelatedArticle : {}", id);
        return helpRelatedArticleRepository.findById(id).map(helpRelatedArticleMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpRelatedArticle : {}", id);
        return helpRelatedArticleRepository.deleteById(id).then(helpRelatedArticleSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpRelatedArticleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HelpRelatedArticles for query {}", query);
        return helpRelatedArticleSearchRepository.search(query, pageable).map(helpRelatedArticleMapper::toDto);
    }
}
