package com.fanflip.notifications.service.impl;

import com.fanflip.notifications.domain.AppNotification;
import com.fanflip.notifications.repository.AppNotificationRepository;
import com.fanflip.notifications.service.AppNotificationService;
import com.fanflip.notifications.service.dto.AppNotificationDTO;
import com.fanflip.notifications.service.mapper.AppNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.notifications.domain.AppNotification}.
 */
@Service
@Transactional
public class AppNotificationServiceImpl implements AppNotificationService {

    private final Logger log = LoggerFactory.getLogger(AppNotificationServiceImpl.class);

    private final AppNotificationRepository appNotificationRepository;

    private final AppNotificationMapper appNotificationMapper;

    public AppNotificationServiceImpl(AppNotificationRepository appNotificationRepository, AppNotificationMapper appNotificationMapper) {
        this.appNotificationRepository = appNotificationRepository;
        this.appNotificationMapper = appNotificationMapper;
    }

    @Override
    public AppNotificationDTO save(AppNotificationDTO appNotificationDTO) {
        log.debug("Request to save AppNotification : {}", appNotificationDTO);
        AppNotification appNotification = appNotificationMapper.toEntity(appNotificationDTO);
        appNotification = appNotificationRepository.save(appNotification);
        return appNotificationMapper.toDto(appNotification);
    }

    @Override
    public AppNotificationDTO update(AppNotificationDTO appNotificationDTO) {
        log.debug("Request to update AppNotification : {}", appNotificationDTO);
        AppNotification appNotification = appNotificationMapper.toEntity(appNotificationDTO);
        appNotification = appNotificationRepository.save(appNotification);
        return appNotificationMapper.toDto(appNotification);
    }

    @Override
    public Optional<AppNotificationDTO> partialUpdate(AppNotificationDTO appNotificationDTO) {
        log.debug("Request to partially update AppNotification : {}", appNotificationDTO);

        return appNotificationRepository
            .findById(appNotificationDTO.getId())
            .map(existingAppNotification -> {
                appNotificationMapper.partialUpdate(existingAppNotification, appNotificationDTO);

                return existingAppNotification;
            })
            .map(appNotificationRepository::save)
            .map(appNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppNotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppNotifications");
        return appNotificationRepository.findAll(pageable).map(appNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppNotificationDTO> findOne(Long id) {
        log.debug("Request to get AppNotification : {}", id);
        return appNotificationRepository.findById(id).map(appNotificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppNotification : {}", id);
        appNotificationRepository.deleteById(id);
    }
}
