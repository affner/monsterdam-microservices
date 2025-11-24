package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.HelpQuestionRepository;
import com.monsterdam.admin.repository.search.HelpQuestionSearchRepository;
import com.monsterdam.admin.service.HelpQuestionService;
import com.monsterdam.admin.service.dto.HelpQuestionDTO;
import com.monsterdam.admin.service.mapper.HelpQuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.HelpQuestion}.
 */
@Service
@Transactional
public class HelpQuestionServiceImpl implements HelpQuestionService {

    private final Logger log = LoggerFactory.getLogger(HelpQuestionServiceImpl.class);

    private final HelpQuestionRepository helpQuestionRepository;

    private final HelpQuestionMapper helpQuestionMapper;

    private final HelpQuestionSearchRepository helpQuestionSearchRepository;

    public HelpQuestionServiceImpl(
        HelpQuestionRepository helpQuestionRepository,
        HelpQuestionMapper helpQuestionMapper,
        HelpQuestionSearchRepository helpQuestionSearchRepository
    ) {
        this.helpQuestionRepository = helpQuestionRepository;
        this.helpQuestionMapper = helpQuestionMapper;
        this.helpQuestionSearchRepository = helpQuestionSearchRepository;
    }

    @Override
    public Mono<HelpQuestionDTO> save(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to save HelpQuestion : {}", helpQuestionDTO);
        return helpQuestionRepository
            .save(helpQuestionMapper.toEntity(helpQuestionDTO))
            .flatMap(helpQuestionSearchRepository::save)
            .map(helpQuestionMapper::toDto);
    }

    @Override
    public Mono<HelpQuestionDTO> update(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to update HelpQuestion : {}", helpQuestionDTO);
        return helpQuestionRepository
            .save(helpQuestionMapper.toEntity(helpQuestionDTO))
            .flatMap(helpQuestionSearchRepository::save)
            .map(helpQuestionMapper::toDto);
    }

    @Override
    public Mono<HelpQuestionDTO> partialUpdate(HelpQuestionDTO helpQuestionDTO) {
        log.debug("Request to partially update HelpQuestion : {}", helpQuestionDTO);

        return helpQuestionRepository
            .findById(helpQuestionDTO.getId())
            .map(existingHelpQuestion -> {
                helpQuestionMapper.partialUpdate(existingHelpQuestion, helpQuestionDTO);

                return existingHelpQuestion;
            })
            .flatMap(helpQuestionRepository::save)
            .flatMap(savedHelpQuestion -> {
                helpQuestionSearchRepository.save(savedHelpQuestion);
                return Mono.just(savedHelpQuestion);
            })
            .map(helpQuestionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HelpQuestions");
        return helpQuestionRepository.findAllBy(pageable).map(helpQuestionMapper::toDto);
    }

    public Flux<HelpQuestionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return helpQuestionRepository.findAllWithEagerRelationships(pageable).map(helpQuestionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return helpQuestionRepository.count();
    }

    public Mono<Long> searchCount() {
        return helpQuestionSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HelpQuestionDTO> findOne(Long id) {
        log.debug("Request to get HelpQuestion : {}", id);
        return helpQuestionRepository.findOneWithEagerRelationships(id).map(helpQuestionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HelpQuestion : {}", id);
        return helpQuestionRepository.deleteById(id).then(helpQuestionSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HelpQuestionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HelpQuestions for query {}", query);
        return helpQuestionSearchRepository.search(query, pageable).map(helpQuestionMapper::toDto);
    }
}
