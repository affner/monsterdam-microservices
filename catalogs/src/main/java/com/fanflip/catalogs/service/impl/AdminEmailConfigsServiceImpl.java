package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.AdminEmailConfigs;
import com.fanflip.catalogs.repository.AdminEmailConfigsRepository;
import com.fanflip.catalogs.service.AdminEmailConfigsService;
import com.fanflip.catalogs.service.dto.AdminEmailConfigsDTO;
import com.fanflip.catalogs.service.mapper.AdminEmailConfigsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.AdminEmailConfigs}.
 */
@Service
@Transactional
public class AdminEmailConfigsServiceImpl implements AdminEmailConfigsService {

    private final Logger log = LoggerFactory.getLogger(AdminEmailConfigsServiceImpl.class);

    private final AdminEmailConfigsRepository adminEmailConfigsRepository;

    private final AdminEmailConfigsMapper adminEmailConfigsMapper;

    public AdminEmailConfigsServiceImpl(
        AdminEmailConfigsRepository adminEmailConfigsRepository,
        AdminEmailConfigsMapper adminEmailConfigsMapper
    ) {
        this.adminEmailConfigsRepository = adminEmailConfigsRepository;
        this.adminEmailConfigsMapper = adminEmailConfigsMapper;
    }

    @Override
    public AdminEmailConfigsDTO save(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to save AdminEmailConfigs : {}", adminEmailConfigsDTO);
        AdminEmailConfigs adminEmailConfigs = adminEmailConfigsMapper.toEntity(adminEmailConfigsDTO);
        adminEmailConfigs = adminEmailConfigsRepository.save(adminEmailConfigs);
        return adminEmailConfigsMapper.toDto(adminEmailConfigs);
    }

    @Override
    public AdminEmailConfigsDTO update(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to update AdminEmailConfigs : {}", adminEmailConfigsDTO);
        AdminEmailConfigs adminEmailConfigs = adminEmailConfigsMapper.toEntity(adminEmailConfigsDTO);
        adminEmailConfigs = adminEmailConfigsRepository.save(adminEmailConfigs);
        return adminEmailConfigsMapper.toDto(adminEmailConfigs);
    }

    @Override
    public Optional<AdminEmailConfigsDTO> partialUpdate(AdminEmailConfigsDTO adminEmailConfigsDTO) {
        log.debug("Request to partially update AdminEmailConfigs : {}", adminEmailConfigsDTO);

        return adminEmailConfigsRepository
            .findById(adminEmailConfigsDTO.getId())
            .map(existingAdminEmailConfigs -> {
                adminEmailConfigsMapper.partialUpdate(existingAdminEmailConfigs, adminEmailConfigsDTO);

                return existingAdminEmailConfigs;
            })
            .map(adminEmailConfigsRepository::save)
            .map(adminEmailConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminEmailConfigsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AdminEmailConfigs");
        return adminEmailConfigsRepository.findAll(pageable).map(adminEmailConfigsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdminEmailConfigsDTO> findOne(Long id) {
        log.debug("Request to get AdminEmailConfigs : {}", id);
        return adminEmailConfigsRepository.findById(id).map(adminEmailConfigsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AdminEmailConfigs : {}", id);
        adminEmailConfigsRepository.deleteById(id);
    }
}
