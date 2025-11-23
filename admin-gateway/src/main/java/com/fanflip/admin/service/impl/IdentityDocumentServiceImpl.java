package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.IdentityDocumentRepository;
import com.fanflip.admin.repository.search.IdentityDocumentSearchRepository;
import com.fanflip.admin.service.IdentityDocumentService;
import com.fanflip.admin.service.dto.IdentityDocumentDTO;
import com.fanflip.admin.service.mapper.IdentityDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.IdentityDocument}.
 */
@Service
@Transactional
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final Logger log = LoggerFactory.getLogger(IdentityDocumentServiceImpl.class);

    private final IdentityDocumentRepository identityDocumentRepository;

    private final IdentityDocumentMapper identityDocumentMapper;

    private final IdentityDocumentSearchRepository identityDocumentSearchRepository;

    public IdentityDocumentServiceImpl(
        IdentityDocumentRepository identityDocumentRepository,
        IdentityDocumentMapper identityDocumentMapper,
        IdentityDocumentSearchRepository identityDocumentSearchRepository
    ) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.identityDocumentMapper = identityDocumentMapper;
        this.identityDocumentSearchRepository = identityDocumentSearchRepository;
    }

    @Override
    public Mono<IdentityDocumentDTO> save(IdentityDocumentDTO identityDocumentDTO) {
        log.debug("Request to save IdentityDocument : {}", identityDocumentDTO);
        return identityDocumentRepository
            .save(identityDocumentMapper.toEntity(identityDocumentDTO))
            .flatMap(identityDocumentSearchRepository::save)
            .map(identityDocumentMapper::toDto);
    }

    @Override
    public Mono<IdentityDocumentDTO> update(IdentityDocumentDTO identityDocumentDTO) {
        log.debug("Request to update IdentityDocument : {}", identityDocumentDTO);
        return identityDocumentRepository
            .save(identityDocumentMapper.toEntity(identityDocumentDTO))
            .flatMap(identityDocumentSearchRepository::save)
            .map(identityDocumentMapper::toDto);
    }

    @Override
    public Mono<IdentityDocumentDTO> partialUpdate(IdentityDocumentDTO identityDocumentDTO) {
        log.debug("Request to partially update IdentityDocument : {}", identityDocumentDTO);

        return identityDocumentRepository
            .findById(identityDocumentDTO.getId())
            .map(existingIdentityDocument -> {
                identityDocumentMapper.partialUpdate(existingIdentityDocument, identityDocumentDTO);

                return existingIdentityDocument;
            })
            .flatMap(identityDocumentRepository::save)
            .flatMap(savedIdentityDocument -> {
                identityDocumentSearchRepository.save(savedIdentityDocument);
                return Mono.just(savedIdentityDocument);
            })
            .map(identityDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IdentityDocumentDTO> findAll() {
        log.debug("Request to get all IdentityDocuments");
        return identityDocumentRepository.findAll().map(identityDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return identityDocumentRepository.count();
    }

    public Mono<Long> searchCount() {
        return identityDocumentSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<IdentityDocumentDTO> findOne(Long id) {
        log.debug("Request to get IdentityDocument : {}", id);
        return identityDocumentRepository.findById(id).map(identityDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IdentityDocument : {}", id);
        return identityDocumentRepository.deleteById(id).then(identityDocumentSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<IdentityDocumentDTO> search(String query) {
        log.debug("Request to search IdentityDocuments for query {}", query);
        try {
            return identityDocumentSearchRepository.search(query).map(identityDocumentMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
