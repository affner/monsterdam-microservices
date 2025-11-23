package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.HashTagRepository;
import com.fanflip.admin.repository.search.HashTagSearchRepository;
import com.fanflip.admin.service.HashTagService;
import com.fanflip.admin.service.dto.HashTagDTO;
import com.fanflip.admin.service.mapper.HashTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.HashTag}.
 */
@Service
@Transactional
public class HashTagServiceImpl implements HashTagService {

    private final Logger log = LoggerFactory.getLogger(HashTagServiceImpl.class);

    private final HashTagRepository hashTagRepository;

    private final HashTagMapper hashTagMapper;

    private final HashTagSearchRepository hashTagSearchRepository;

    public HashTagServiceImpl(
        HashTagRepository hashTagRepository,
        HashTagMapper hashTagMapper,
        HashTagSearchRepository hashTagSearchRepository
    ) {
        this.hashTagRepository = hashTagRepository;
        this.hashTagMapper = hashTagMapper;
        this.hashTagSearchRepository = hashTagSearchRepository;
    }

    @Override
    public Mono<HashTagDTO> save(HashTagDTO hashTagDTO) {
        log.debug("Request to save HashTag : {}", hashTagDTO);
        return hashTagRepository.save(hashTagMapper.toEntity(hashTagDTO)).flatMap(hashTagSearchRepository::save).map(hashTagMapper::toDto);
    }

    @Override
    public Mono<HashTagDTO> update(HashTagDTO hashTagDTO) {
        log.debug("Request to update HashTag : {}", hashTagDTO);
        return hashTagRepository.save(hashTagMapper.toEntity(hashTagDTO)).flatMap(hashTagSearchRepository::save).map(hashTagMapper::toDto);
    }

    @Override
    public Mono<HashTagDTO> partialUpdate(HashTagDTO hashTagDTO) {
        log.debug("Request to partially update HashTag : {}", hashTagDTO);

        return hashTagRepository
            .findById(hashTagDTO.getId())
            .map(existingHashTag -> {
                hashTagMapper.partialUpdate(existingHashTag, hashTagDTO);

                return existingHashTag;
            })
            .flatMap(hashTagRepository::save)
            .flatMap(savedHashTag -> {
                hashTagSearchRepository.save(savedHashTag);
                return Mono.just(savedHashTag);
            })
            .map(hashTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HashTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HashTags");
        return hashTagRepository.findAllBy(pageable).map(hashTagMapper::toDto);
    }

    public Mono<Long> countAll() {
        return hashTagRepository.count();
    }

    public Mono<Long> searchCount() {
        return hashTagSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<HashTagDTO> findOne(Long id) {
        log.debug("Request to get HashTag : {}", id);
        return hashTagRepository.findById(id).map(hashTagMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete HashTag : {}", id);
        return hashTagRepository.deleteById(id).then(hashTagSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<HashTagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HashTags for query {}", query);
        return hashTagSearchRepository.search(query, pageable).map(hashTagMapper::toDto);
    }
}
