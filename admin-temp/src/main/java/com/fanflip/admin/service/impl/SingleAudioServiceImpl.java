package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SingleAudioRepository;
import com.fanflip.admin.repository.search.SingleAudioSearchRepository;
import com.fanflip.admin.service.SingleAudioService;
import com.fanflip.admin.service.dto.SingleAudioDTO;
import com.fanflip.admin.service.mapper.SingleAudioMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SingleAudio}.
 */
@Service
@Transactional
public class SingleAudioServiceImpl implements SingleAudioService {

    private final Logger log = LoggerFactory.getLogger(SingleAudioServiceImpl.class);

    private final SingleAudioRepository singleAudioRepository;

    private final SingleAudioMapper singleAudioMapper;

    private final SingleAudioSearchRepository singleAudioSearchRepository;

    public SingleAudioServiceImpl(
        SingleAudioRepository singleAudioRepository,
        SingleAudioMapper singleAudioMapper,
        SingleAudioSearchRepository singleAudioSearchRepository
    ) {
        this.singleAudioRepository = singleAudioRepository;
        this.singleAudioMapper = singleAudioMapper;
        this.singleAudioSearchRepository = singleAudioSearchRepository;
    }

    @Override
    public Mono<SingleAudioDTO> save(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to save SingleAudio : {}", singleAudioDTO);
        return singleAudioRepository
            .save(singleAudioMapper.toEntity(singleAudioDTO))
            .flatMap(singleAudioSearchRepository::save)
            .map(singleAudioMapper::toDto);
    }

    @Override
    public Mono<SingleAudioDTO> update(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to update SingleAudio : {}", singleAudioDTO);
        return singleAudioRepository
            .save(singleAudioMapper.toEntity(singleAudioDTO))
            .flatMap(singleAudioSearchRepository::save)
            .map(singleAudioMapper::toDto);
    }

    @Override
    public Mono<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to partially update SingleAudio : {}", singleAudioDTO);

        return singleAudioRepository
            .findById(singleAudioDTO.getId())
            .map(existingSingleAudio -> {
                singleAudioMapper.partialUpdate(existingSingleAudio, singleAudioDTO);

                return existingSingleAudio;
            })
            .flatMap(singleAudioRepository::save)
            .flatMap(savedSingleAudio -> {
                singleAudioSearchRepository.save(savedSingleAudio);
                return Mono.just(savedSingleAudio);
            })
            .map(singleAudioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleAudioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleAudios");
        return singleAudioRepository.findAllBy(pageable).map(singleAudioMapper::toDto);
    }

    /**
     *  Get all the singleAudios where ContentPackage is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SingleAudioDTO> findAllWhereContentPackageIsNull() {
        log.debug("Request to get all singleAudios where ContentPackage is null");
        return singleAudioRepository.findAllWhereContentPackageIsNull().map(singleAudioMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singleAudioRepository.count();
    }

    public Mono<Long> searchCount() {
        return singleAudioSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SingleAudioDTO> findOne(Long id) {
        log.debug("Request to get SingleAudio : {}", id);
        return singleAudioRepository.findById(id).map(singleAudioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SingleAudio : {}", id);
        return singleAudioRepository.deleteById(id).then(singleAudioSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleAudioDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SingleAudios for query {}", query);
        return singleAudioSearchRepository.search(query, pageable).map(singleAudioMapper::toDto);
    }
}
