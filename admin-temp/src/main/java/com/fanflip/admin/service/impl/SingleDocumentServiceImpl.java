package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.SingleDocumentRepository;
import com.fanflip.admin.repository.search.SingleDocumentSearchRepository;
import com.fanflip.admin.service.SingleDocumentService;
import com.fanflip.admin.service.dto.SingleDocumentDTO;
import com.fanflip.admin.service.mapper.SingleDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.SingleDocument}.
 */
@Service
@Transactional
public class SingleDocumentServiceImpl implements SingleDocumentService {

    private final Logger log = LoggerFactory.getLogger(SingleDocumentServiceImpl.class);

    private final SingleDocumentRepository singleDocumentRepository;

    private final SingleDocumentMapper singleDocumentMapper;

    private final SingleDocumentSearchRepository singleDocumentSearchRepository;

    public SingleDocumentServiceImpl(
        SingleDocumentRepository singleDocumentRepository,
        SingleDocumentMapper singleDocumentMapper,
        SingleDocumentSearchRepository singleDocumentSearchRepository
    ) {
        this.singleDocumentRepository = singleDocumentRepository;
        this.singleDocumentMapper = singleDocumentMapper;
        this.singleDocumentSearchRepository = singleDocumentSearchRepository;
    }

    @Override
    public Mono<SingleDocumentDTO> save(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to save SingleDocument : {}", singleDocumentDTO);
        return singleDocumentRepository
            .save(singleDocumentMapper.toEntity(singleDocumentDTO))
            .flatMap(singleDocumentSearchRepository::save)
            .map(singleDocumentMapper::toDto);
    }

    @Override
    public Mono<SingleDocumentDTO> update(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to update SingleDocument : {}", singleDocumentDTO);
        return singleDocumentRepository
            .save(singleDocumentMapper.toEntity(singleDocumentDTO))
            .flatMap(singleDocumentSearchRepository::save)
            .map(singleDocumentMapper::toDto);
    }

    @Override
    public Mono<SingleDocumentDTO> partialUpdate(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to partially update SingleDocument : {}", singleDocumentDTO);

        return singleDocumentRepository
            .findById(singleDocumentDTO.getId())
            .map(existingSingleDocument -> {
                singleDocumentMapper.partialUpdate(existingSingleDocument, singleDocumentDTO);

                return existingSingleDocument;
            })
            .flatMap(singleDocumentRepository::save)
            .flatMap(savedSingleDocument -> {
                singleDocumentSearchRepository.save(savedSingleDocument);
                return Mono.just(savedSingleDocument);
            })
            .map(singleDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleDocuments");
        return singleDocumentRepository.findAllBy(pageable).map(singleDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return singleDocumentRepository.count();
    }

    public Mono<Long> searchCount() {
        return singleDocumentSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<SingleDocumentDTO> findOne(Long id) {
        log.debug("Request to get SingleDocument : {}", id);
        return singleDocumentRepository.findById(id).map(singleDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete SingleDocument : {}", id);
        return singleDocumentRepository.deleteById(id).then(singleDocumentSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<SingleDocumentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SingleDocuments for query {}", query);
        return singleDocumentSearchRepository.search(query, pageable).map(singleDocumentMapper::toDto);
    }
}
