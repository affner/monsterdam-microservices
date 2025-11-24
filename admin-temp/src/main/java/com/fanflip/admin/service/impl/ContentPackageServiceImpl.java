package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.ContentPackageRepository;
import com.monsterdam.admin.repository.search.ContentPackageSearchRepository;
import com.monsterdam.admin.service.ContentPackageService;
import com.monsterdam.admin.service.dto.ContentPackageDTO;
import com.monsterdam.admin.service.mapper.ContentPackageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.ContentPackage}.
 */
@Service
@Transactional
public class ContentPackageServiceImpl implements ContentPackageService {

    private final Logger log = LoggerFactory.getLogger(ContentPackageServiceImpl.class);

    private final ContentPackageRepository contentPackageRepository;

    private final ContentPackageMapper contentPackageMapper;

    private final ContentPackageSearchRepository contentPackageSearchRepository;

    public ContentPackageServiceImpl(
        ContentPackageRepository contentPackageRepository,
        ContentPackageMapper contentPackageMapper,
        ContentPackageSearchRepository contentPackageSearchRepository
    ) {
        this.contentPackageRepository = contentPackageRepository;
        this.contentPackageMapper = contentPackageMapper;
        this.contentPackageSearchRepository = contentPackageSearchRepository;
    }

    @Override
    public Mono<ContentPackageDTO> save(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to save ContentPackage : {}", contentPackageDTO);
        return contentPackageRepository
            .save(contentPackageMapper.toEntity(contentPackageDTO))
            .flatMap(contentPackageSearchRepository::save)
            .map(contentPackageMapper::toDto);
    }

    @Override
    public Mono<ContentPackageDTO> update(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to update ContentPackage : {}", contentPackageDTO);
        return contentPackageRepository
            .save(contentPackageMapper.toEntity(contentPackageDTO))
            .flatMap(contentPackageSearchRepository::save)
            .map(contentPackageMapper::toDto);
    }

    @Override
    public Mono<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to partially update ContentPackage : {}", contentPackageDTO);

        return contentPackageRepository
            .findById(contentPackageDTO.getId())
            .map(existingContentPackage -> {
                contentPackageMapper.partialUpdate(existingContentPackage, contentPackageDTO);

                return existingContentPackage;
            })
            .flatMap(contentPackageRepository::save)
            .flatMap(savedContentPackage -> {
                contentPackageSearchRepository.save(savedContentPackage);
                return Mono.just(savedContentPackage);
            })
            .map(contentPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ContentPackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentPackages");
        return contentPackageRepository.findAllBy(pageable).map(contentPackageMapper::toDto);
    }

    public Flux<ContentPackageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contentPackageRepository.findAllWithEagerRelationships(pageable).map(contentPackageMapper::toDto);
    }

    /**
     *  Get all the contentPackages where Message is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContentPackageDTO> findAllWhereMessageIsNull() {
        log.debug("Request to get all contentPackages where Message is null");
        return contentPackageRepository.findAllWhereMessageIsNull().map(contentPackageMapper::toDto);
    }

    /**
     *  Get all the contentPackages where Post is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ContentPackageDTO> findAllWherePostIsNull() {
        log.debug("Request to get all contentPackages where Post is null");
        return contentPackageRepository.findAllWherePostIsNull().map(contentPackageMapper::toDto);
    }

    public Mono<Long> countAll() {
        return contentPackageRepository.count();
    }

    public Mono<Long> searchCount() {
        return contentPackageSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ContentPackageDTO> findOne(Long id) {
        log.debug("Request to get ContentPackage : {}", id);
        return contentPackageRepository.findOneWithEagerRelationships(id).map(contentPackageMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ContentPackage : {}", id);
        return contentPackageRepository.deleteById(id).then(contentPackageSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ContentPackageDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContentPackages for query {}", query);
        return contentPackageSearchRepository.search(query, pageable).map(contentPackageMapper::toDto);
    }
}
