package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.AdminUserProfileRepository;
import com.fanflip.admin.repository.search.AdminUserProfileSearchRepository;
import com.fanflip.admin.service.AdminUserProfileService;
import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import com.fanflip.admin.service.mapper.AdminUserProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.AdminUserProfile}.
 */
@Service
@Transactional
public class AdminUserProfileServiceImpl implements AdminUserProfileService {

    private final Logger log = LoggerFactory.getLogger(AdminUserProfileServiceImpl.class);

    private final AdminUserProfileRepository adminUserProfileRepository;

    private final AdminUserProfileMapper adminUserProfileMapper;

    private final AdminUserProfileSearchRepository adminUserProfileSearchRepository;

    public AdminUserProfileServiceImpl(
        AdminUserProfileRepository adminUserProfileRepository,
        AdminUserProfileMapper adminUserProfileMapper,
        AdminUserProfileSearchRepository adminUserProfileSearchRepository
    ) {
        this.adminUserProfileRepository = adminUserProfileRepository;
        this.adminUserProfileMapper = adminUserProfileMapper;
        this.adminUserProfileSearchRepository = adminUserProfileSearchRepository;
    }

    @Override
    public Mono<AdminUserProfileDTO> save(AdminUserProfileDTO adminUserProfileDTO) {
        log.debug("Request to save AdminUserProfile : {}", adminUserProfileDTO);
        return adminUserProfileRepository
            .save(adminUserProfileMapper.toEntity(adminUserProfileDTO))
            .flatMap(adminUserProfileSearchRepository::save)
            .map(adminUserProfileMapper::toDto);
    }

    @Override
    public Mono<AdminUserProfileDTO> update(AdminUserProfileDTO adminUserProfileDTO) {
        log.debug("Request to update AdminUserProfile : {}", adminUserProfileDTO);
        return adminUserProfileRepository
            .save(adminUserProfileMapper.toEntity(adminUserProfileDTO))
            .flatMap(adminUserProfileSearchRepository::save)
            .map(adminUserProfileMapper::toDto);
    }

    @Override
    public Mono<AdminUserProfileDTO> partialUpdate(AdminUserProfileDTO adminUserProfileDTO) {
        log.debug("Request to partially update AdminUserProfile : {}", adminUserProfileDTO);

        return adminUserProfileRepository
            .findById(adminUserProfileDTO.getId())
            .map(existingAdminUserProfile -> {
                adminUserProfileMapper.partialUpdate(existingAdminUserProfile, adminUserProfileDTO);

                return existingAdminUserProfile;
            })
            .flatMap(adminUserProfileRepository::save)
            .flatMap(savedAdminUserProfile -> {
                adminUserProfileSearchRepository.save(savedAdminUserProfile);
                return Mono.just(savedAdminUserProfile);
            })
            .map(adminUserProfileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminUserProfileDTO> findAll() {
        log.debug("Request to get all AdminUserProfiles");
        return adminUserProfileRepository.findAll().map(adminUserProfileMapper::toDto);
    }

    public Mono<Long> countAll() {
        return adminUserProfileRepository.count();
    }

    public Mono<Long> searchCount() {
        return adminUserProfileSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AdminUserProfileDTO> findOne(Long id) {
        log.debug("Request to get AdminUserProfile : {}", id);
        return adminUserProfileRepository.findById(id).map(adminUserProfileMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AdminUserProfile : {}", id);
        return adminUserProfileRepository.deleteById(id).then(adminUserProfileSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminUserProfileDTO> search(String query) {
        log.debug("Request to search AdminUserProfiles for query {}", query);
        try {
            return adminUserProfileSearchRepository.search(query).map(adminUserProfileMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
