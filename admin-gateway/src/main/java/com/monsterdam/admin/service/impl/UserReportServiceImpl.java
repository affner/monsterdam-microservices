package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.UserReportRepository;
import com.monsterdam.admin.repository.search.UserReportSearchRepository;
import com.monsterdam.admin.service.UserReportService;
import com.monsterdam.admin.service.dto.UserReportDTO;
import com.monsterdam.admin.service.mapper.UserReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.UserReport}.
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
    public Flux<UserReportDTO> findAll() {
        log.debug("Request to get all UserReports");
        return userReportRepository.findAll().map(userReportMapper::toDto);
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
        return userReportRepository.findById(id).map(userReportMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserReport : {}", id);
        return userReportRepository.deleteById(id).then(userReportSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserReportDTO> search(String query) {
        log.debug("Request to search UserReports for query {}", query);
        try {
            return userReportSearchRepository.search(query).map(userReportMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
