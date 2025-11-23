package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.UserAssociationRepository;
import com.fanflip.admin.repository.search.UserAssociationSearchRepository;
import com.fanflip.admin.service.UserAssociationService;
import com.fanflip.admin.service.dto.UserAssociationDTO;
import com.fanflip.admin.service.mapper.UserAssociationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.UserAssociation}.
 */
@Service
@Transactional
public class UserAssociationServiceImpl implements UserAssociationService {

    private final Logger log = LoggerFactory.getLogger(UserAssociationServiceImpl.class);

    private final UserAssociationRepository userAssociationRepository;

    private final UserAssociationMapper userAssociationMapper;

    private final UserAssociationSearchRepository userAssociationSearchRepository;

    public UserAssociationServiceImpl(
        UserAssociationRepository userAssociationRepository,
        UserAssociationMapper userAssociationMapper,
        UserAssociationSearchRepository userAssociationSearchRepository
    ) {
        this.userAssociationRepository = userAssociationRepository;
        this.userAssociationMapper = userAssociationMapper;
        this.userAssociationSearchRepository = userAssociationSearchRepository;
    }

    @Override
    public Mono<UserAssociationDTO> save(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to save UserAssociation : {}", userAssociationDTO);
        return userAssociationRepository
            .save(userAssociationMapper.toEntity(userAssociationDTO))
            .flatMap(userAssociationSearchRepository::save)
            .map(userAssociationMapper::toDto);
    }

    @Override
    public Mono<UserAssociationDTO> update(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to update UserAssociation : {}", userAssociationDTO);
        return userAssociationRepository
            .save(userAssociationMapper.toEntity(userAssociationDTO))
            .flatMap(userAssociationSearchRepository::save)
            .map(userAssociationMapper::toDto);
    }

    @Override
    public Mono<UserAssociationDTO> partialUpdate(UserAssociationDTO userAssociationDTO) {
        log.debug("Request to partially update UserAssociation : {}", userAssociationDTO);

        return userAssociationRepository
            .findById(userAssociationDTO.getId())
            .map(existingUserAssociation -> {
                userAssociationMapper.partialUpdate(existingUserAssociation, userAssociationDTO);

                return existingUserAssociation;
            })
            .flatMap(userAssociationRepository::save)
            .flatMap(savedUserAssociation -> {
                userAssociationSearchRepository.save(savedUserAssociation);
                return Mono.just(savedUserAssociation);
            })
            .map(userAssociationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserAssociationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserAssociations");
        return userAssociationRepository.findAllBy(pageable).map(userAssociationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return userAssociationRepository.count();
    }

    public Mono<Long> searchCount() {
        return userAssociationSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<UserAssociationDTO> findOne(Long id) {
        log.debug("Request to get UserAssociation : {}", id);
        return userAssociationRepository.findById(id).map(userAssociationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete UserAssociation : {}", id);
        return userAssociationRepository.deleteById(id).then(userAssociationSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<UserAssociationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserAssociations for query {}", query);
        return userAssociationSearchRepository.search(query, pageable).map(userAssociationMapper::toDto);
    }
}
