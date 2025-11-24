package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.SingleDocument;
import com.monsterdam.multimedia.repository.SingleDocumentRepository;
import com.monsterdam.multimedia.service.SingleDocumentService;
import com.monsterdam.multimedia.service.dto.SingleDocumentDTO;
import com.monsterdam.multimedia.service.mapper.SingleDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.SingleDocument}.
 */
@Service
@Transactional
public class SingleDocumentServiceImpl implements SingleDocumentService {

    private final Logger log = LoggerFactory.getLogger(SingleDocumentServiceImpl.class);

    private final SingleDocumentRepository singleDocumentRepository;

    private final SingleDocumentMapper singleDocumentMapper;

    public SingleDocumentServiceImpl(SingleDocumentRepository singleDocumentRepository, SingleDocumentMapper singleDocumentMapper) {
        this.singleDocumentRepository = singleDocumentRepository;
        this.singleDocumentMapper = singleDocumentMapper;
    }

    @Override
    public SingleDocumentDTO save(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to save SingleDocument : {}", singleDocumentDTO);
        SingleDocument singleDocument = singleDocumentMapper.toEntity(singleDocumentDTO);
        singleDocument = singleDocumentRepository.save(singleDocument);
        return singleDocumentMapper.toDto(singleDocument);
    }

    @Override
    public SingleDocumentDTO update(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to update SingleDocument : {}", singleDocumentDTO);
        SingleDocument singleDocument = singleDocumentMapper.toEntity(singleDocumentDTO);
        singleDocument = singleDocumentRepository.save(singleDocument);
        return singleDocumentMapper.toDto(singleDocument);
    }

    @Override
    public Optional<SingleDocumentDTO> partialUpdate(SingleDocumentDTO singleDocumentDTO) {
        log.debug("Request to partially update SingleDocument : {}", singleDocumentDTO);

        return singleDocumentRepository
            .findById(singleDocumentDTO.getId())
            .map(existingSingleDocument -> {
                singleDocumentMapper.partialUpdate(existingSingleDocument, singleDocumentDTO);

                return existingSingleDocument;
            })
            .map(singleDocumentRepository::save)
            .map(singleDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleDocuments");
        return singleDocumentRepository.findAll(pageable).map(singleDocumentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleDocumentDTO> findOne(Long id) {
        log.debug("Request to get SingleDocument : {}", id);
        return singleDocumentRepository.findById(id).map(singleDocumentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SingleDocument : {}", id);
        singleDocumentRepository.deleteById(id);
    }
}
