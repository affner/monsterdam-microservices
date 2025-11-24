package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.PersonalSocialLinksRepository;
import com.monsterdam.admin.repository.search.PersonalSocialLinksSearchRepository;
import com.monsterdam.admin.service.PersonalSocialLinksService;
import com.monsterdam.admin.service.dto.PersonalSocialLinksDTO;
import com.monsterdam.admin.service.mapper.PersonalSocialLinksMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.PersonalSocialLinks}.
 */
@Service
@Transactional
public class PersonalSocialLinksServiceImpl implements PersonalSocialLinksService {

    private final Logger log = LoggerFactory.getLogger(PersonalSocialLinksServiceImpl.class);

    private final PersonalSocialLinksRepository personalSocialLinksRepository;

    private final PersonalSocialLinksMapper personalSocialLinksMapper;

    private final PersonalSocialLinksSearchRepository personalSocialLinksSearchRepository;

    public PersonalSocialLinksServiceImpl(
        PersonalSocialLinksRepository personalSocialLinksRepository,
        PersonalSocialLinksMapper personalSocialLinksMapper,
        PersonalSocialLinksSearchRepository personalSocialLinksSearchRepository
    ) {
        this.personalSocialLinksRepository = personalSocialLinksRepository;
        this.personalSocialLinksMapper = personalSocialLinksMapper;
        this.personalSocialLinksSearchRepository = personalSocialLinksSearchRepository;
    }

    @Override
    public Mono<PersonalSocialLinksDTO> save(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to save PersonalSocialLinks : {}", personalSocialLinksDTO);
        return personalSocialLinksRepository
            .save(personalSocialLinksMapper.toEntity(personalSocialLinksDTO))
            .flatMap(personalSocialLinksSearchRepository::save)
            .map(personalSocialLinksMapper::toDto);
    }

    @Override
    public Mono<PersonalSocialLinksDTO> update(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to update PersonalSocialLinks : {}", personalSocialLinksDTO);
        return personalSocialLinksRepository
            .save(personalSocialLinksMapper.toEntity(personalSocialLinksDTO))
            .flatMap(personalSocialLinksSearchRepository::save)
            .map(personalSocialLinksMapper::toDto);
    }

    @Override
    public Mono<PersonalSocialLinksDTO> partialUpdate(PersonalSocialLinksDTO personalSocialLinksDTO) {
        log.debug("Request to partially update PersonalSocialLinks : {}", personalSocialLinksDTO);

        return personalSocialLinksRepository
            .findById(personalSocialLinksDTO.getId())
            .map(existingPersonalSocialLinks -> {
                personalSocialLinksMapper.partialUpdate(existingPersonalSocialLinks, personalSocialLinksDTO);

                return existingPersonalSocialLinks;
            })
            .flatMap(personalSocialLinksRepository::save)
            .flatMap(savedPersonalSocialLinks -> {
                personalSocialLinksSearchRepository.save(savedPersonalSocialLinks);
                return Mono.just(savedPersonalSocialLinks);
            })
            .map(personalSocialLinksMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PersonalSocialLinksDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalSocialLinks");
        return personalSocialLinksRepository.findAllBy(pageable).map(personalSocialLinksMapper::toDto);
    }

    public Flux<PersonalSocialLinksDTO> findAllWithEagerRelationships(Pageable pageable) {
        return personalSocialLinksRepository.findAllWithEagerRelationships(pageable).map(personalSocialLinksMapper::toDto);
    }

    public Mono<Long> countAll() {
        return personalSocialLinksRepository.count();
    }

    public Mono<Long> searchCount() {
        return personalSocialLinksSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PersonalSocialLinksDTO> findOne(Long id) {
        log.debug("Request to get PersonalSocialLinks : {}", id);
        return personalSocialLinksRepository.findOneWithEagerRelationships(id).map(personalSocialLinksMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PersonalSocialLinks : {}", id);
        return personalSocialLinksRepository.deleteById(id).then(personalSocialLinksSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PersonalSocialLinksDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PersonalSocialLinks for query {}", query);
        return personalSocialLinksSearchRepository.search(query, pageable).map(personalSocialLinksMapper::toDto);
    }
}
