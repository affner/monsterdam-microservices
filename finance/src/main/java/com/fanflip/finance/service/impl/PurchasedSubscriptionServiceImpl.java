package com.fanflip.finance.service.impl;

import com.fanflip.finance.domain.PurchasedSubscription;
import com.fanflip.finance.repository.PurchasedSubscriptionRepository;
import com.fanflip.finance.service.PurchasedSubscriptionService;
import com.fanflip.finance.service.dto.PurchasedSubscriptionDTO;
import com.fanflip.finance.service.mapper.PurchasedSubscriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.finance.domain.PurchasedSubscription}.
 */
@Service
@Transactional
public class PurchasedSubscriptionServiceImpl implements PurchasedSubscriptionService {

    private final Logger log = LoggerFactory.getLogger(PurchasedSubscriptionServiceImpl.class);

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    private final PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    public PurchasedSubscriptionServiceImpl(
        PurchasedSubscriptionRepository purchasedSubscriptionRepository,
        PurchasedSubscriptionMapper purchasedSubscriptionMapper
    ) {
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
        this.purchasedSubscriptionMapper = purchasedSubscriptionMapper;
    }

    @Override
    public PurchasedSubscriptionDTO save(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        PurchasedSubscription purchasedSubscription = purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO);
        purchasedSubscription = purchasedSubscriptionRepository.save(purchasedSubscription);
        return purchasedSubscriptionMapper.toDto(purchasedSubscription);
    }

    @Override
    public PurchasedSubscriptionDTO update(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to update PurchasedSubscription : {}", purchasedSubscriptionDTO);
        PurchasedSubscription purchasedSubscription = purchasedSubscriptionMapper.toEntity(purchasedSubscriptionDTO);
        purchasedSubscription = purchasedSubscriptionRepository.save(purchasedSubscription);
        return purchasedSubscriptionMapper.toDto(purchasedSubscription);
    }

    @Override
    public Optional<PurchasedSubscriptionDTO> partialUpdate(PurchasedSubscriptionDTO purchasedSubscriptionDTO) {
        log.debug("Request to partially update PurchasedSubscription : {}", purchasedSubscriptionDTO);

        return purchasedSubscriptionRepository
            .findById(purchasedSubscriptionDTO.getId())
            .map(existingPurchasedSubscription -> {
                purchasedSubscriptionMapper.partialUpdate(existingPurchasedSubscription, purchasedSubscriptionDTO);

                return existingPurchasedSubscription;
            })
            .map(purchasedSubscriptionRepository::save)
            .map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchasedSubscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurchasedSubscriptions");
        return purchasedSubscriptionRepository.findAll(pageable).map(purchasedSubscriptionMapper::toDto);
    }

    public Page<PurchasedSubscriptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return purchasedSubscriptionRepository.findAllWithEagerRelationships(pageable).map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchasedSubscriptionDTO> findOne(Long id) {
        log.debug("Request to get PurchasedSubscription : {}", id);
        return purchasedSubscriptionRepository.findOneWithEagerRelationships(id).map(purchasedSubscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PurchasedSubscription : {}", id);
        purchasedSubscriptionRepository.deleteById(id);
    }
}
