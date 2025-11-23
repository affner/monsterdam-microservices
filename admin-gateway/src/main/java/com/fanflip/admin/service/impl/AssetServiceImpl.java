package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.AssetRepository;
import com.fanflip.admin.repository.search.AssetSearchRepository;
import com.fanflip.admin.service.AssetService;
import com.fanflip.admin.service.dto.AssetDTO;
import com.fanflip.admin.service.mapper.AssetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Asset}.
 */
@Service
@Transactional
public class AssetServiceImpl implements AssetService {

    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);

    private final AssetRepository assetRepository;

    private final AssetMapper assetMapper;

    private final AssetSearchRepository assetSearchRepository;

    public AssetServiceImpl(AssetRepository assetRepository, AssetMapper assetMapper, AssetSearchRepository assetSearchRepository) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
        this.assetSearchRepository = assetSearchRepository;
    }

    @Override
    public Mono<AssetDTO> save(AssetDTO assetDTO) {
        log.debug("Request to save Asset : {}", assetDTO);
        return assetRepository.save(assetMapper.toEntity(assetDTO)).flatMap(assetSearchRepository::save).map(assetMapper::toDto);
    }

    @Override
    public Mono<AssetDTO> update(AssetDTO assetDTO) {
        log.debug("Request to update Asset : {}", assetDTO);
        return assetRepository.save(assetMapper.toEntity(assetDTO)).flatMap(assetSearchRepository::save).map(assetMapper::toDto);
    }

    @Override
    public Mono<AssetDTO> partialUpdate(AssetDTO assetDTO) {
        log.debug("Request to partially update Asset : {}", assetDTO);

        return assetRepository
            .findById(assetDTO.getId())
            .map(existingAsset -> {
                assetMapper.partialUpdate(existingAsset, assetDTO);

                return existingAsset;
            })
            .flatMap(assetRepository::save)
            .flatMap(savedAsset -> {
                assetSearchRepository.save(savedAsset);
                return Mono.just(savedAsset);
            })
            .map(assetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AssetDTO> findAll() {
        log.debug("Request to get all Assets");
        return assetRepository.findAll().map(assetMapper::toDto);
    }

    public Mono<Long> countAll() {
        return assetRepository.count();
    }

    public Mono<Long> searchCount() {
        return assetSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AssetDTO> findOne(Long id) {
        log.debug("Request to get Asset : {}", id);
        return assetRepository.findById(id).map(assetMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Asset : {}", id);
        return assetRepository.deleteById(id).then(assetSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AssetDTO> search(String query) {
        log.debug("Request to search Assets for query {}", query);
        try {
            return assetSearchRepository.search(query).map(assetMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
