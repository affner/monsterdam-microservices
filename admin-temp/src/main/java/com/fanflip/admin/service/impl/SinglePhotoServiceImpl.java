package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.SinglePhotoRepository;
import com.monsterdam.admin.repository.search.SinglePhotoSearchRepository;
import com.monsterdam.admin.service.SinglePhotoService;
import com.monsterdam.admin.service.dto.SinglePhotoDTO;
import com.monsterdam.admin.service.mapper.SinglePhotoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.SinglePhoto}.
 */
@Service
@Transactional
public class SinglePhotoServiceImpl implements SinglePhotoService {

    private final Logger log = LoggerFactory.getLogger(SinglePhotoServiceImpl.class);

    private final SinglePhotoRepository singlePhotoRepository;

    private final SinglePhotoMapper singlePhotoMapper;

    private final SinglePhotoSearchRepository singlePhotoSearchRepository;

    public SinglePhotoServiceImpl(
        SinglePhotoRepository singlePhotoRepository,
        SinglePhotoMapper singlePhotoMapper,
        SinglePhotoSearchRepository singlePhotoSearchRepository
    ) {
        this.singlePhotoRepository = singlePhotoRepository;
        this.singlePhotoMapper = singlePhotoMapper;
        this.singlePhotoSearchRepository = singlePhotoSearchRepository;
    }

    @Override
    public Mono<SinglePhotoDTO> save(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to save SinglePhoto : {}", singlePhotoDTO);
        return singlePhotoRepository
            .save(singlePhotoMapper.toEntity(singlePhotoDTO))
            .flatMap(singlePhotoSearchRepository::save)
            .map(singlePhotoMapper::toDto);
    }

    @Override
    public Mono<SinglePhotoDTO> update(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to update SinglePhoto : {}", singlePhotoDTO);
        return singlePhotoRepository
            .save(singlePhotoMapper.toEntity(singlePhotoDTO))
            .flatMap(singlePhotoSearchRepository::save)
            .map(singlePhotoMapper::toDto);
    }

    @Override
    public Mono<SinglePhotoDTO> partialUpdate(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to partially update SinglePhoto : {}", singlePhotoDTO);

        return singlePhotoRepository
            .findById(singlePhotoDTO.getId())
            .map(existingSinglePhoto -> {
                singlePhotoMapper.partialUpdate(existingSinglePhoto, singlePhotoDTO);

                return existingSinglePhoto;
            })
            .flatMap(singlePhotoRepository::save)
            .flatMap(savedSinglePhoto -> {
                singlePhotoSearchRepository.save(savedSinglePhoto);
                return Mono.just(savedSinglePhoto);
            })
            .map(singlePhotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SinglePhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SinglePhotos");
        return singlePhotoRepository.findAllBy(pageable).map(singlePhotoMapper::toDto);
    }

    public Flux<SinglePhotoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return singlePhotoRepository.findAllWithEagerRelationships(pageable).map(singlePhotoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singlePhotoRepository.count();
    }

    public Mono<Long> searchCount() {
        return singlePhotoSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SinglePhotoDTO> findOne(Long id) {
        log.debug("Request to get SinglePhoto : {}", id);
        return singlePhotoRepository.findOneWithEagerRelationships(id).map(singlePhotoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SinglePhoto : {}", id);
        return singlePhotoRepository.deleteById(id).then(singlePhotoSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SinglePhotoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SinglePhotos for query {}", query);
        return singlePhotoSearchRepository.search(query, pageable).map(singlePhotoMapper::toDto);
    }
}
