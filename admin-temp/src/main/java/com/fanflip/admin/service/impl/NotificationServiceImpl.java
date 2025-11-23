package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.NotificationRepository;
import com.fanflip.admin.repository.search.NotificationSearchRepository;
import com.fanflip.admin.service.NotificationService;
import com.fanflip.admin.service.dto.NotificationDTO;
import com.fanflip.admin.service.mapper.NotificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final NotificationSearchRepository notificationSearchRepository;

    public NotificationServiceImpl(
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper,
        NotificationSearchRepository notificationSearchRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.notificationSearchRepository = notificationSearchRepository;
    }

    @Override
    public Mono<NotificationDTO> save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        return notificationRepository
            .save(notificationMapper.toEntity(notificationDTO))
            .flatMap(notificationSearchRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    public Mono<NotificationDTO> update(NotificationDTO notificationDTO) {
        log.debug("Request to update Notification : {}", notificationDTO);
        return notificationRepository
            .save(notificationMapper.toEntity(notificationDTO))
            .flatMap(notificationSearchRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    public Mono<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        log.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .flatMap(notificationRepository::save)
            .flatMap(savedNotification -> {
                notificationSearchRepository.save(savedNotification);
                return Mono.just(savedNotification);
            })
            .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAllBy(pageable).map(notificationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return notificationRepository.count();
    }

    public Mono<Long> searchCount() {
        return notificationSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        return notificationRepository.deleteById(id).then(notificationSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<NotificationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notifications for query {}", query);
        return notificationSearchRepository.search(query, pageable).map(notificationMapper::toDto);
    }
}
