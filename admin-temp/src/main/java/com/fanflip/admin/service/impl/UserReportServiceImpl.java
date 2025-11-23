package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.UserReportRepository;
import com.fanflip.admin.repository.search.UserReportSearchRepository;
import com.fanflip.admin.service.UserReportService;
import com.fanflip.admin.service.dto.UserReportDTO;
import com.fanflip.admin.service.mapper.UserReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.UserReport}.
 */
@Service
@Transactional
public class UserReportServiceImpl implements UserReportService {

    private final Logger log = LoggerFactory.getLogger(UserReportServiceImpl.class);

    private final UserReportRepository userReportRepository;

    private final UserReportMapper userReportMapper;

    private final UserReportSearchRepository userReportSearchRepository;

    public UserReportServiceImpl(
        UserReportRepository userReportRepository,
        UserReportMapper userReportMapper,
        UserReportSearchRepository userReportSearchRepository
    ) {
        this.userReportRepository = userReportRepository;
        this.userReportMapper = userReportMapper;
        this.userReportSearchRepository = userReportSearchRepository;
    }

    @Override
    public Mono<UserReportDTO> save(UserReportDTO userReportDTO) {
        log.debug("Request to save UserReport : {}", userReportDTO);
        return userReportRepository
            .save(userReportMapper.toEntity(userReportDTO))
            .flatMap(userReportSearchRepository::save)
            .map(userReportMapper::toDto);
    }

    @Override
    public Mono<UserReportDTO> update(UserReportDTO userReportDTO) {
        log.debug("Request to update UserReport : {}", userReportDTO);
        return userReportRepository
            .save(userReportMapper.toEntity(userReportDTO))
            .flatMap(userReportSearchRepository::save)
            .map(userReportMapper::toDto);
    }

    @Override
    public Mono<UserReportDTO> partialUpdate(UserReportDTO userReportDTO) {
        log.debug("Request to partially update UserReport : {}", userReportDTO);

        return userReportRepository
            .findById(userReportDTO.getId())
            .map(existingUserReport -> {
                userReportMapper.partialUpdate(existingUserReport, userReportDTO);

                return existingUserReport;
            })
            .flatMap(userReportRepository::save)
            .flatMap(savedUserReport -> {
                userReportSearchRepository.save(savedUserReport);
                return Mono.just(savedUserReport);
            })
            .map(userReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserReportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserReports");
        return userReportRepository.findAllBy(pageable).map(userReportMapper::toDto);
    }

    public Flux<UserReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userReportRepository.findAllWithEagerRelationships(pageable).map(userReportMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userReportRepository.count();
    }

    public Mono<Long> searchCount() {
        return userReportSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserReportDTO> findOne(Long id) {
        log.debug("Request to get UserReport : {}", id);
        return userReportRepository.findOneWithEagerRelationships(id).map(userReportMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserReport : {}", id);
        return userReportRepository.deleteById(id).then(userReportSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserReportDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserReports for query {}", query);
        return userReportSearchRepository.search(query, pageable).map(userReportMapper::toDto);
    }
}
