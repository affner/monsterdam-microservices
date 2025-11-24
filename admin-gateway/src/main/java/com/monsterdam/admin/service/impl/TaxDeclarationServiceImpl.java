package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.TaxDeclarationRepository;
import com.monsterdam.admin.repository.search.TaxDeclarationSearchRepository;
import com.monsterdam.admin.service.TaxDeclarationService;
import com.monsterdam.admin.service.dto.TaxDeclarationDTO;
import com.monsterdam.admin.service.mapper.TaxDeclarationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.TaxDeclaration}.
 */
@Service
@Transactional
public class TaxDeclarationServiceImpl implements TaxDeclarationService {

    private final Logger log = LoggerFactory.getLogger(TaxDeclarationServiceImpl.class);

    private final TaxDeclarationRepository taxDeclarationRepository;

    private final TaxDeclarationMapper taxDeclarationMapper;

    private final TaxDeclarationSearchRepository taxDeclarationSearchRepository;

    public TaxDeclarationServiceImpl(
        TaxDeclarationRepository taxDeclarationRepository,
        TaxDeclarationMapper taxDeclarationMapper,
        TaxDeclarationSearchRepository taxDeclarationSearchRepository
    ) {
        this.taxDeclarationRepository = taxDeclarationRepository;
        this.taxDeclarationMapper = taxDeclarationMapper;
        this.taxDeclarationSearchRepository = taxDeclarationSearchRepository;
    }

    @Override
    public Mono<TaxDeclarationDTO> save(TaxDeclarationDTO taxDeclarationDTO) {
        log.debug("Request to save TaxDeclaration : {}", taxDeclarationDTO);
        return taxDeclarationRepository
            .save(taxDeclarationMapper.toEntity(taxDeclarationDTO))
            .flatMap(taxDeclarationSearchRepository::save)
            .map(taxDeclarationMapper::toDto);
    }

    @Override
    public Mono<TaxDeclarationDTO> update(TaxDeclarationDTO taxDeclarationDTO) {
        log.debug("Request to update TaxDeclaration : {}", taxDeclarationDTO);
        return taxDeclarationRepository
            .save(taxDeclarationMapper.toEntity(taxDeclarationDTO))
            .flatMap(taxDeclarationSearchRepository::save)
            .map(taxDeclarationMapper::toDto);
    }

    @Override
    public Mono<TaxDeclarationDTO> partialUpdate(TaxDeclarationDTO taxDeclarationDTO) {
        log.debug("Request to partially update TaxDeclaration : {}", taxDeclarationDTO);

        return taxDeclarationRepository
            .findById(taxDeclarationDTO.getId())
            .map(existingTaxDeclaration -> {
                taxDeclarationMapper.partialUpdate(existingTaxDeclaration, taxDeclarationDTO);

                return existingTaxDeclaration;
            })
            .flatMap(taxDeclarationRepository::save)
            .flatMap(savedTaxDeclaration -> {
                taxDeclarationSearchRepository.save(savedTaxDeclaration);
                return Mono.just(savedTaxDeclaration);
            })
            .map(taxDeclarationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TaxDeclarationDTO> findAll() {
        log.debug("Request to get all TaxDeclarations");
        return taxDeclarationRepository.findAll().map(taxDeclarationMapper::toDto);
    }

    public Flux<TaxDeclarationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taxDeclarationRepository.findAllWithEagerRelationships(pageable).map(taxDeclarationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return taxDeclarationRepository.count();
    }

    public Mono<Long> searchCount() {
        return taxDeclarationSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<TaxDeclarationDTO> findOne(Long id) {
        log.debug("Request to get TaxDeclaration : {}", id);
        return taxDeclarationRepository.findOneWithEagerRelationships(id).map(taxDeclarationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TaxDeclaration : {}", id);
        return taxDeclarationRepository.deleteById(id).then(taxDeclarationSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<TaxDeclarationDTO> search(String query) {
        log.debug("Request to search TaxDeclarations for query {}", query);
        try {
            return taxDeclarationSearchRepository.search(query).map(taxDeclarationMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
