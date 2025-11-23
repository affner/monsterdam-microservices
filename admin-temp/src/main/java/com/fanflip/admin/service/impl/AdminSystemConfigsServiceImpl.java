package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.AdminSystemConfigsRepository;
import com.fanflip.admin.repository.search.AdminSystemConfigsSearchRepository;
import com.fanflip.admin.service.AdminSystemConfigsService;
import com.fanflip.admin.service.dto.AdminSystemConfigsDTO;
import com.fanflip.admin.service.mapper.AdminSystemConfigsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.AdminSystemConfigs}.
 */
@Service
@Transactional
public class AdminSystemConfigsServiceImpl implements AdminSystemConfigsService {

    private final Logger log = LoggerFactory.getLogger(AdminSystemConfigsServiceImpl.class);

    private final AdminSystemConfigsRepository adminSystemConfigsRepository;

    private final AdminSystemConfigsMapper adminSystemConfigsMapper;

    private final AdminSystemConfigsSearchRepository adminSystemConfigsSearchRepository;

    public AdminSystemConfigsServiceImpl(
        AdminSystemConfigsRepository adminSystemConfigsRepository,
        AdminSystemConfigsMapper adminSystemConfigsMapper,
        AdminSystemConfigsSearchRepository adminSystemConfigsSearchRepository
    ) {
        this.adminSystemConfigsRepository = adminSystemConfigsRepository;
        this.adminSystemConfigsMapper = adminSystemConfigsMapper;
        this.adminSystemConfigsSearchRepository = adminSystemConfigsSearchRepository;
    }

    @Override
    public Mono<AdminSystemConfigsDTO> save(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        log.debug("Request to save AdminSystemConfigs : {}", adminSystemConfigsDTO);
        return adminSystemConfigsRepository
            .save(adminSystemConfigsMapper.toEntity(adminSystemConfigsDTO))
            .flatMap(adminSystemConfigsSearchRepository::save)
            .map(adminSystemConfigsMapper::toDto);
    }

    @Override
    public Mono<AdminSystemConfigsDTO> update(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        log.debug("Request to update AdminSystemConfigs : {}", adminSystemConfigsDTO);
        return adminSystemConfigsRepository
            .save(adminSystemConfigsMapper.toEntity(adminSystemConfigsDTO))
            .flatMap(adminSystemConfigsSearchRepository::save)
            .map(adminSystemConfigsMapper::toDto);
    }

    @Override
    public Mono<AdminSystemConfigsDTO> partialUpdate(AdminSystemConfigsDTO adminSystemConfigsDTO) {
        log.debug("Request to partially update AdminSystemConfigs : {}", adminSystemConfigsDTO);

        return adminSystemConfigsRepository
            .findById(adminSystemConfigsDTO.getId())
            .map(existingAdminSystemConfigs -> {
                adminSystemConfigsMapper.partialUpdate(existingAdminSystemConfigs, adminSystemConfigsDTO);

                return existingAdminSystemConfigs;
            })
            .flatMap(adminSystemConfigsRepository::save)
            .flatMap(savedAdminSystemConfigs -> {
                adminSystemConfigsSearchRepository.save(savedAdminSystemConfigs);
                return Mono.just(savedAdminSystemConfigs);
            })
            .map(adminSystemConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminSystemConfigsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminSystemConfigs");
        return adminSystemConfigsRepository.findAllBy(pageable).map(adminSystemConfigsMapper::toDto);
    }

    public Mono<Long> countAll() {
        return adminSystemConfigsRepository.count();
    }

    public Mono<Long> searchCount() {
        return adminSystemConfigsSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AdminSystemConfigsDTO> findOne(Long id) {
        log.debug("Request to get AdminSystemConfigs : {}", id);
        return adminSystemConfigsRepository.findById(id).map(adminSystemConfigsMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AdminSystemConfigs : {}", id);
        return adminSystemConfigsRepository.deleteById(id).then(adminSystemConfigsSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminSystemConfigsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AdminSystemConfigs for query {}", query);
        return adminSystemConfigsSearchRepository.search(query, pageable).map(adminSystemConfigsMapper::toDto);
    }
}
