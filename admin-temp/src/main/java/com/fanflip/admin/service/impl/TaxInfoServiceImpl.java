package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.TaxInfoRepository;
import com.fanflip.admin.repository.search.TaxInfoSearchRepository;
import com.fanflip.admin.service.TaxInfoService;
import com.fanflip.admin.service.dto.TaxInfoDTO;
import com.fanflip.admin.service.mapper.TaxInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.TaxInfo}.
 */
@Service
@Transactional
public class TaxInfoServiceImpl implements TaxInfoService {

    private final Logger log = LoggerFactory.getLogger(TaxInfoServiceImpl.class);

    private final TaxInfoRepository taxInfoRepository;

    private final TaxInfoMapper taxInfoMapper;

    private final TaxInfoSearchRepository taxInfoSearchRepository;

    public TaxInfoServiceImpl(
        TaxInfoRepository taxInfoRepository,
        TaxInfoMapper taxInfoMapper,
        TaxInfoSearchRepository taxInfoSearchRepository
    ) {
        this.taxInfoRepository = taxInfoRepository;
        this.taxInfoMapper = taxInfoMapper;
        this.taxInfoSearchRepository = taxInfoSearchRepository;
    }

    @Override
    public Mono<TaxInfoDTO> save(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to save TaxInfo : {}", taxInfoDTO);
        return taxInfoRepository.save(taxInfoMapper.toEntity(taxInfoDTO)).flatMap(taxInfoSearchRepository::save).map(taxInfoMapper::toDto);
    }

    @Override
    public Mono<TaxInfoDTO> update(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to update TaxInfo : {}", taxInfoDTO);
        return taxInfoRepository.save(taxInfoMapper.toEntity(taxInfoDTO)).flatMap(taxInfoSearchRepository::save).map(taxInfoMapper::toDto);
    }

    @Override
    public Mono<TaxInfoDTO> partialUpdate(TaxInfoDTO taxInfoDTO) {
        log.debug("Request to partially update TaxInfo : {}", taxInfoDTO);

        return taxInfoRepository
            .findById(taxInfoDTO.getId())
            .map(existingTaxInfo -> {
                taxInfoMapper.partialUpdate(existingTaxInfo, taxInfoDTO);

                return existingTaxInfo;
            })
            .flatMap(taxInfoRepository::save)
            .flatMap(savedTaxInfo -> {
                taxInfoSearchRepository.save(savedTaxInfo);
                return Mono.just(savedTaxInfo);
            })
            .map(taxInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TaxInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TaxInfos");
        return taxInfoRepository.findAllBy(pageable).map(taxInfoMapper::toDto);
    }

    public Flux<TaxInfoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taxInfoRepository.findAllWithEagerRelationships(pageable).map(taxInfoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return taxInfoRepository.count();
    }

    public Mono<Long> searchCount() {
        return taxInfoSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TaxInfoDTO> findOne(Long id) {
        log.debug("Request to get TaxInfo : {}", id);
        return taxInfoRepository.findOneWithEagerRelationships(id).map(taxInfoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TaxInfo : {}", id);
        return taxInfoRepository.deleteById(id).then(taxInfoSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TaxInfoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TaxInfos for query {}", query);
        return taxInfoSearchRepository.search(query, pageable).map(taxInfoMapper::toDto);
    }
}
