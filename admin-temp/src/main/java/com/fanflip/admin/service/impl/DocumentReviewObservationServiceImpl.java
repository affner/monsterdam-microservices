package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.DocumentReviewObservationRepository;
import com.monsterdam.admin.repository.search.DocumentReviewObservationSearchRepository;
import com.monsterdam.admin.service.DocumentReviewObservationService;
import com.monsterdam.admin.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.admin.service.mapper.DocumentReviewObservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.DocumentReviewObservation}.
 */
@Service
@Transactional
public class DocumentReviewObservationServiceImpl implements DocumentReviewObservationService {

    private final Logger log = LoggerFactory.getLogger(DocumentReviewObservationServiceImpl.class);

    private final DocumentReviewObservationRepository documentReviewObservationRepository;

    private final DocumentReviewObservationMapper documentReviewObservationMapper;

    private final DocumentReviewObservationSearchRepository documentReviewObservationSearchRepository;

    public DocumentReviewObservationServiceImpl(
        DocumentReviewObservationRepository documentReviewObservationRepository,
        DocumentReviewObservationMapper documentReviewObservationMapper,
        DocumentReviewObservationSearchRepository documentReviewObservationSearchRepository
    ) {
        this.documentReviewObservationRepository = documentReviewObservationRepository;
        this.documentReviewObservationMapper = documentReviewObservationMapper;
        this.documentReviewObservationSearchRepository = documentReviewObservationSearchRepository;
    }

    @Override
    public Mono<DocumentReviewObservationDTO> save(DocumentReviewObservationDTO documentReviewObservationDTO) {
        log.debug("Request to save DocumentReviewObservation : {}", documentReviewObservationDTO);
        return documentReviewObservationRepository
            .save(documentReviewObservationMapper.toEntity(documentReviewObservationDTO))
            .flatMap(documentReviewObservationSearchRepository::save)
            .map(documentReviewObservationMapper::toDto);
    }

    @Override
    public Mono<DocumentReviewObservationDTO> update(DocumentReviewObservationDTO documentReviewObservationDTO) {
        log.debug("Request to update DocumentReviewObservation : {}", documentReviewObservationDTO);
        return documentReviewObservationRepository
            .save(documentReviewObservationMapper.toEntity(documentReviewObservationDTO))
            .flatMap(documentReviewObservationSearchRepository::save)
            .map(documentReviewObservationMapper::toDto);
    }

    @Override
    public Mono<DocumentReviewObservationDTO> partialUpdate(DocumentReviewObservationDTO documentReviewObservationDTO) {
        log.debug("Request to partially update DocumentReviewObservation : {}", documentReviewObservationDTO);

        return documentReviewObservationRepository
            .findById(documentReviewObservationDTO.getId())
            .map(existingDocumentReviewObservation -> {
                documentReviewObservationMapper.partialUpdate(existingDocumentReviewObservation, documentReviewObservationDTO);

                return existingDocumentReviewObservation;
            })
            .flatMap(documentReviewObservationRepository::save)
            .flatMap(savedDocumentReviewObservation -> {
                documentReviewObservationSearchRepository.save(savedDocumentReviewObservation);
                return Mono.just(savedDocumentReviewObservation);
            })
            .map(documentReviewObservationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DocumentReviewObservationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DocumentReviewObservations");
        return documentReviewObservationRepository.findAllBy(pageable).map(documentReviewObservationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return documentReviewObservationRepository.count();
    }

    public Mono<Long> searchCount() {
        return documentReviewObservationSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<DocumentReviewObservationDTO> findOne(Long id) {
        log.debug("Request to get DocumentReviewObservation : {}", id);
        return documentReviewObservationRepository.findById(id).map(documentReviewObservationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DocumentReviewObservation : {}", id);
        return documentReviewObservationRepository.deleteById(id).then(documentReviewObservationSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<DocumentReviewObservationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DocumentReviewObservations for query {}", query);
        return documentReviewObservationSearchRepository.search(query, pageable).map(documentReviewObservationMapper::toDto);
    }
}
