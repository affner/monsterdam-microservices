package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.AdminEmailConfigsRepository;
import com.monsterdam.admin.repository.search.AdminEmailConfigsSearchRepository;
import com.monsterdam.admin.service.AdminEmailConfigsService;
import com.monsterdam.admin.service.dto.AdminEmailConfigsDTO;
import com.monsterdam.admin.service.mapper.AdminEmailConfigsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.AdminEmailConfigs}.
 */
@Service
@Transactional
public class AdminEmailConfigsServiceImpl implements AdminEmailConfigsService {

    private final Logger log = LoggerFactory.getLogger(AdminEmailConfigsServiceImpl.class);

    private final AdminEmailConfigsRepository adminEmailConfigsRepository;

    private final AdminEmailConfigsMapper adminEmailConfigsMapper;

    private final AdminEmailConfigsSearchRepository adminEmailConfigsSearchRepository;

    public AdminEmailConfigsServiceImpl(
        AdminEmailConfigsRepository adminEmailConfigsRepository,
        AdminEmailConfigsMapper adminEmailConfigsMapper,
        AdminEmailConfigsSearchRepository adminEmailConfigsSearchRepository
    ) {
        this.adminEmailConfigsRepository = adminEmailConfigsRepository;
        this.adminEmailConfigsMapper = adminEmailConfigsMapper;
        this.adminEmailConfigsSearchRepository = adminEmailConfigsSearchRepository;
    }

    @Override
    public Mono<AdminEmailConfigsDTO> save(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to save AdminEmailConfigs : {}", adminEmailConfigsDTO);
        return adminEmailConfigsRepository
            .save(adminEmailConfigsMapper.toEntity(adminEmailConfigsDTO))
            .flatMap(adminEmailConfigsSearchRepository::save)
            .map(adminEmailConfigsMapper::toDto);
    }

    @Override
    public Mono<AdminEmailConfigsDTO> update(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to update AdminEmailConfigs : {}", adminEmailConfigsDTO);
        return adminEmailConfigsRepository
            .save(adminEmailConfigsMapper.toEntity(adminEmailConfigsDTO))
            .flatMap(adminEmailConfigsSearchRepository::save)
            .map(adminEmailConfigsMapper::toDto);
    }

    @Override
    public Mono<AdminEmailConfigsDTO> partialUpdate(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to partially update AdminEmailConfigs : {}", adminEmailConfigsDTO);

        return adminEmailConfigsRepository
            .findById(adminEmailConfigsDTO.getId())
            .map(existingAdminEmailConfigs -> {
                adminEmailConfigsMapper.partialUpdate(existingAdminEmailConfigs, adminEmailConfigsDTO);

                return existingAdminEmailConfigs;
            })
            .flatMap(adminEmailConfigsRepository::save)
            .flatMap(savedAdminEmailConfigs -> {
                adminEmailConfigsSearchRepository.save(savedAdminEmailConfigs);
                return Mono.just(savedAdminEmailConfigs);
            })
            .map(adminEmailConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminEmailConfigsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminEmailConfigs");
        return adminEmailConfigsRepository.findAllBy(pageable).map(adminEmailConfigsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return adminEmailConfigsRepository.count();
    }

    public Mono<Long> searchCount() {
        return adminEmailConfigsSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AdminEmailConfigsDTO> findOne(Long id) {
        log.debug("Request to get AdminEmailConfigs : {}", id);
        return adminEmailConfigsRepository.findById(id).map(adminEmailConfigsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AdminEmailConfigs : {}", id);
        return adminEmailConfigsRepository.deleteById(id).then(adminEmailConfigsSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminEmailConfigsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdminEmailConfigs for query {}", query);
        return adminEmailConfigsSearchRepository.search(query, pageable).map(adminEmailConfigsMapper::toDto);
    }
}
