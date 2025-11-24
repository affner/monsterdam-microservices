package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.PurchasedContent;
import com.monsterdam.finance.repository.PurchasedContentRepository;
import com.monsterdam.finance.service.PurchasedContentService;
import com.monsterdam.finance.service.dto.PurchasedContentDTO;
import com.monsterdam.finance.service.mapper.PurchasedContentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.PurchasedContent}.
 */
@Service
@Transactional
public class PurchasedContentServiceImpl implements PurchasedContentService {

    private final Logger log = LoggerFactory.getLogger(PurchasedContentServiceImpl.class);

    private final PurchasedContentRepository purchasedContentRepository;

    private final PurchasedContentMapper purchasedContentMapper;

    public PurchasedContentServiceImpl(
        PurchasedContentRepository purchasedContentRepository,
        PurchasedContentMapper purchasedContentMapper
    ) {
        this.purchasedContentRepository = purchasedContentRepository;
        this.purchasedContentMapper = purchasedContentMapper;
    }

    @Override
    public PurchasedContentDTO save(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to save PurchasedContent : {}", purchasedContentDTO);
        PurchasedContent purchasedContent = purchasedContentMapper.toEntity(purchasedContentDTO);
        purchasedContent = purchasedContentRepository.save(purchasedContent);
        return purchasedContentMapper.toDto(purchasedContent);
    }

    @Override
    public PurchasedContentDTO update(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to update PurchasedContent : {}", purchasedContentDTO);
        PurchasedContent purchasedContent = purchasedContentMapper.toEntity(purchasedContentDTO);
        purchasedContent = purchasedContentRepository.save(purchasedContent);
        return purchasedContentMapper.toDto(purchasedContent);
    }

    @Override
    public Optional<PurchasedContentDTO> partialUpdate(PurchasedContentDTO purchasedContentDTO) {
        log.debug("Request to partially update PurchasedContent : {}", purchasedContentDTO);

        return purchasedContentRepository
            .findById(purchasedContentDTO.getId())
            .map(existingPurchasedContent -> {
                purchasedContentMapper.partialUpdate(existingPurchasedContent, purchasedContentDTO);

                return existingPurchasedContent;
            })
            .map(purchasedContentRepository::save)
            .map(purchasedContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedContentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedContents");
        return purchasedContentRepository.findAll(pageable).map(purchasedContentMapper::toDto);
    }

    public Page<PurchasedContentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedContentRepository.findAllWithEagerRelationships(pageable).map(purchasedContentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedContentDTO> findOne(Long id) {
        log.debug("Request to get PurchasedContent : {}", id);
        return purchasedContentRepository.findOneWithEagerRelationships(id).map(purchasedContentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchasedContent : {}", id);
        purchasedContentRepository.deleteById(id);
    }
}
