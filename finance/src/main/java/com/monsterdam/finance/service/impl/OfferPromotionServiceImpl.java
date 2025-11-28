package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.OfferPromotion;
import com.monsterdam.finance.repository.OfferPromotionRepository;
import com.monsterdam.finance.service.OfferPromotionService;
import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import com.monsterdam.finance.service.mapper.OfferPromotionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.OfferPromotion}.
 */
@Service
@Transactional
public class OfferPromotionServiceImpl implements OfferPromotionService {

    private final Logger log = LoggerFactory.getLogger(OfferPromotionServiceImpl.class);

    private final OfferPromotionRepository offerPromotionRepository;

    private final OfferPromotionMapper offerPromotionMapper;

    public OfferPromotionServiceImpl(OfferPromotionRepository offerPromotionRepository, OfferPromotionMapper offerPromotionMapper) {
        this.offerPromotionRepository = offerPromotionRepository;
        this.offerPromotionMapper = offerPromotionMapper;
    }

    @Override
    public OfferPromotionDTO save(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to save OfferPromotion : {}", offerPromotionDTO);
        OfferPromotion offerPromotion = offerPromotionMapper.toEntity(offerPromotionDTO);
        offerPromotion = offerPromotionRepository.save(offerPromotion);
        return offerPromotionMapper.toDto(offerPromotion);
    }

    @Override
    public OfferPromotionDTO update(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to update OfferPromotion : {}", offerPromotionDTO);
        OfferPromotion offerPromotion = offerPromotionMapper.toEntity(offerPromotionDTO);
        offerPromotion = offerPromotionRepository.save(offerPromotion);
        return offerPromotionMapper.toDto(offerPromotion);
    }

    @Override
    public Optional<OfferPromotionDTO> partialUpdate(OfferPromotionDTO offerPromotionDTO) {
        log.debug("Request to partially update OfferPromotion : {}", offerPromotionDTO);

        return offerPromotionRepository
            .findById(offerPromotionDTO.getId())
            .map(existingOfferPromotion -> {
                offerPromotionMapper.partialUpdate(existingOfferPromotion, offerPromotionDTO);

                return existingOfferPromotion;
            })
            .map(offerPromotionRepository::save)
            .map(offerPromotionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OfferPromotionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OfferPromotions");
        return offerPromotionRepository.findAll(pageable).map(offerPromotionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferPromotionDTO> findOne(Long id) {
        log.debug("Request to get OfferPromotion : {}", id);
        return offerPromotionRepository.findById(id).map(offerPromotionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OfferPromotion : {}", id);
        offerPromotionRepository.deleteById(id);
    }
}
