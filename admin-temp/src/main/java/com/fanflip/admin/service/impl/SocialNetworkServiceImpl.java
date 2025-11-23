package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SocialNetworkRepository;
import com.fanflip.admin.repository.search.SocialNetworkSearchRepository;
import com.fanflip.admin.service.SocialNetworkService;
import com.fanflip.admin.service.dto.SocialNetworkDTO;
import com.fanflip.admin.service.mapper.SocialNetworkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SocialNetwork}.
 */
@Service
@Transactional
public class SocialNetworkServiceImpl implements SocialNetworkService {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkServiceImpl.class);

    private final SocialNetworkRepository socialNetworkRepository;

    private final SocialNetworkMapper socialNetworkMapper;

    private final SocialNetworkSearchRepository socialNetworkSearchRepository;

    public SocialNetworkServiceImpl(
        SocialNetworkRepository socialNetworkRepository,
        SocialNetworkMapper socialNetworkMapper,
        SocialNetworkSearchRepository socialNetworkSearchRepository
    ) {
        this.socialNetworkRepository = socialNetworkRepository;
        this.socialNetworkMapper = socialNetworkMapper;
        this.socialNetworkSearchRepository = socialNetworkSearchRepository;
    }

    @Override
    public Mono<SocialNetworkDTO> save(SocialNetworkDTO socialNetworkDTO) {
        log.debug("Request to save SocialNetwork : {}", socialNetworkDTO);
        return socialNetworkRepository
            .save(socialNetworkMapper.toEntity(socialNetworkDTO))
            .flatMap(socialNetworkSearchRepository::save)
            .map(socialNetworkMapper::toDto);
    }

    @Override
    public Mono<SocialNetworkDTO> update(SocialNetworkDTO socialNetworkDTO) {
        log.debug("Request to update SocialNetwork : {}", socialNetworkDTO);
        return socialNetworkRepository
            .save(socialNetworkMapper.toEntity(socialNetworkDTO))
            .flatMap(socialNetworkSearchRepository::save)
            .map(socialNetworkMapper::toDto);
    }

    @Override
    public Mono<SocialNetworkDTO> partialUpdate(SocialNetworkDTO socialNetworkDTO) {
        log.debug("Request to partially update SocialNetwork : {}", socialNetworkDTO);

        return socialNetworkRepository
            .findById(socialNetworkDTO.getId())
            .map(existingSocialNetwork -> {
                socialNetworkMapper.partialUpdate(existingSocialNetwork, socialNetworkDTO);

                return existingSocialNetwork;
            })
            .flatMap(socialNetworkRepository::save)
            .flatMap(savedSocialNetwork -> {
                socialNetworkSearchRepository.save(savedSocialNetwork);
                return Mono.just(savedSocialNetwork);
            })
            .map(socialNetworkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SocialNetworkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SocialNetworks");
        return socialNetworkRepository.findAllBy(pageable).map(socialNetworkMapper::toDto);
    }

    public Mono<Long> countAll() {
        return socialNetworkRepository.count();
    }

    public Mono<Long> searchCount() {
        return socialNetworkSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SocialNetworkDTO> findOne(Long id) {
        log.debug("Request to get SocialNetwork : {}", id);
        return socialNetworkRepository.findById(id).map(socialNetworkMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SocialNetwork : {}", id);
        return socialNetworkRepository.deleteById(id).then(socialNetworkSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SocialNetworkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SocialNetworks for query {}", query);
        return socialNetworkSearchRepository.search(query, pageable).map(socialNetworkMapper::toDto);
    }
}
