package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.AdminAnnouncementRepository;
import com.monsterdam.admin.repository.search.AdminAnnouncementSearchRepository;
import com.monsterdam.admin.service.AdminAnnouncementService;
import com.monsterdam.admin.service.dto.AdminAnnouncementDTO;
import com.monsterdam.admin.service.mapper.AdminAnnouncementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.AdminAnnouncement}.
 */
@Service
@Transactional
public class AdminAnnouncementServiceImpl implements AdminAnnouncementService {

    private final Logger log = LoggerFactory.getLogger(AdminAnnouncementServiceImpl.class);

    private final AdminAnnouncementRepository adminAnnouncementRepository;

    private final AdminAnnouncementMapper adminAnnouncementMapper;

    private final AdminAnnouncementSearchRepository adminAnnouncementSearchRepository;

    public AdminAnnouncementServiceImpl(
        AdminAnnouncementRepository adminAnnouncementRepository,
        AdminAnnouncementMapper adminAnnouncementMapper,
        AdminAnnouncementSearchRepository adminAnnouncementSearchRepository
    ) {
        this.adminAnnouncementRepository = adminAnnouncementRepository;
        this.adminAnnouncementMapper = adminAnnouncementMapper;
        this.adminAnnouncementSearchRepository = adminAnnouncementSearchRepository;
    }

    @Override
    public Mono<AdminAnnouncementDTO> save(AdminAnnouncementDTO adminAnnouncementDTO) {
        log.debug("Request to save AdminAnnouncement : {}", adminAnnouncementDTO);
        return adminAnnouncementRepository
            .save(adminAnnouncementMapper.toEntity(adminAnnouncementDTO))
            .flatMap(adminAnnouncementSearchRepository::save)
            .map(adminAnnouncementMapper::toDto);
    }

    @Override
    public Mono<AdminAnnouncementDTO> update(AdminAnnouncementDTO adminAnnouncementDTO) {
        log.debug("Request to update AdminAnnouncement : {}", adminAnnouncementDTO);
        return adminAnnouncementRepository
            .save(adminAnnouncementMapper.toEntity(adminAnnouncementDTO))
            .flatMap(adminAnnouncementSearchRepository::save)
            .map(adminAnnouncementMapper::toDto);
    }

    @Override
    public Mono<AdminAnnouncementDTO> partialUpdate(AdminAnnouncementDTO adminAnnouncementDTO) {
        log.debug("Request to partially update AdminAnnouncement : {}", adminAnnouncementDTO);

        return adminAnnouncementRepository
            .findById(adminAnnouncementDTO.getId())
            .map(existingAdminAnnouncement -> {
                adminAnnouncementMapper.partialUpdate(existingAdminAnnouncement, adminAnnouncementDTO);

                return existingAdminAnnouncement;
            })
            .flatMap(adminAnnouncementRepository::save)
            .flatMap(savedAdminAnnouncement -> {
                adminAnnouncementSearchRepository.save(savedAdminAnnouncement);
                return Mono.just(savedAdminAnnouncement);
            })
            .map(adminAnnouncementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminAnnouncementDTO> findAll() {
        log.debug("Request to get all AdminAnnouncements");
        return adminAnnouncementRepository.findAll().map(adminAnnouncementMapper::toDto);
    }

    public Mono<Long> countAll() {
        return adminAnnouncementRepository.count();
    }

    public Mono<Long> searchCount() {
        return adminAnnouncementSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AdminAnnouncementDTO> findOne(Long id) {
        log.debug("Request to get AdminAnnouncement : {}", id);
        return adminAnnouncementRepository.findById(id).map(adminAnnouncementMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AdminAnnouncement : {}", id);
        return adminAnnouncementRepository.deleteById(id).then(adminAnnouncementSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AdminAnnouncementDTO> search(String query) {
        log.debug("Request to search AdminAnnouncements for query {}", query);
        try {
            return adminAnnouncementSearchRepository.search(query).map(adminAnnouncementMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
