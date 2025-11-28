package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.PurchasedTip;
import com.monsterdam.finance.repository.PurchasedTipRepository;
import com.monsterdam.finance.service.PurchasedTipService;
import com.monsterdam.finance.service.dto.PurchasedTipDTO;
import com.monsterdam.finance.service.mapper.PurchasedTipMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.PurchasedTip}.
 */
@Service
@Transactional
public class PurchasedTipServiceImpl implements PurchasedTipService {

    private final Logger log = LoggerFactory.getLogger(PurchasedTipServiceImpl.class);

    private final PurchasedTipRepository purchasedTipRepository;

    private final PurchasedTipMapper purchasedTipMapper;

    public PurchasedTipServiceImpl(PurchasedTipRepository purchasedTipRepository, PurchasedTipMapper purchasedTipMapper) {
        this.purchasedTipRepository = purchasedTipRepository;
        this.purchasedTipMapper = purchasedTipMapper;
    }

    @Override
    public PurchasedTipDTO save(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to save PurchasedTip : {}", purchasedTipDTO);
        PurchasedTip purchasedTip = purchasedTipMapper.toEntity(purchasedTipDTO);
        purchasedTip = purchasedTipRepository.save(purchasedTip);
        return purchasedTipMapper.toDto(purchasedTip);
    }

    @Override
    public PurchasedTipDTO update(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to update PurchasedTip : {}", purchasedTipDTO);
        PurchasedTip purchasedTip = purchasedTipMapper.toEntity(purchasedTipDTO);
        purchasedTip = purchasedTipRepository.save(purchasedTip);
        return purchasedTipMapper.toDto(purchasedTip);
    }

    @Override
    public Optional<PurchasedTipDTO> partialUpdate(PurchasedTipDTO purchasedTipDTO) {
        log.debug("Request to partially update PurchasedTip : {}", purchasedTipDTO);

        return purchasedTipRepository
            .findById(purchasedTipDTO.getId())
            .map(existingPurchasedTip -> {
                purchasedTipMapper.partialUpdate(existingPurchasedTip, purchasedTipDTO);

                return existingPurchasedTip;
            })
            .map(purchasedTipRepository::save)
            .map(purchasedTipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedTipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedTips");
        return purchasedTipRepository.findAll(pageable).map(purchasedTipMapper::toDto);
    }

    public Page<PurchasedTipDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedTipRepository.findAllWithEagerRelationships(pageable).map(purchasedTipMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedTipDTO> findOne(Long id) {
        log.debug("Request to get PurchasedTip : {}", id);
        return purchasedTipRepository.findOneWithEagerRelationships(id).map(purchasedTipMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchasedTip : {}", id);
        purchasedTipRepository.deleteById(id);
    }
}
