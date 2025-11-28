package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.SubscriptionBundle;
import com.monsterdam.finance.repository.SubscriptionBundleRepository;
import com.monsterdam.finance.service.SubscriptionBundleService;
import com.monsterdam.finance.service.dto.SubscriptionBundleDTO;
import com.monsterdam.finance.service.mapper.SubscriptionBundleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.SubscriptionBundle}.
 */
@Service
@Transactional
public class SubscriptionBundleServiceImpl implements SubscriptionBundleService {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleServiceImpl.class);

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    private final SubscriptionBundleMapper subscriptionBundleMapper;

    public SubscriptionBundleServiceImpl(
        SubscriptionBundleRepository subscriptionBundleRepository,
        SubscriptionBundleMapper subscriptionBundleMapper
    ) {
        this.subscriptionBundleRepository = subscriptionBundleRepository;
        this.subscriptionBundleMapper = subscriptionBundleMapper;
    }

    @Override
    public SubscriptionBundleDTO save(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        SubscriptionBundle subscriptionBundle = subscriptionBundleMapper.toEntity(subscriptionBundleDTO);
        subscriptionBundle = subscriptionBundleRepository.save(subscriptionBundle);
        return subscriptionBundleMapper.toDto(subscriptionBundle);
    }

    @Override
    public SubscriptionBundleDTO update(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to update SubscriptionBundle : {}", subscriptionBundleDTO);
        SubscriptionBundle subscriptionBundle = subscriptionBundleMapper.toEntity(subscriptionBundleDTO);
        subscriptionBundle = subscriptionBundleRepository.save(subscriptionBundle);
        return subscriptionBundleMapper.toDto(subscriptionBundle);
    }

    @Override
    public Optional<SubscriptionBundleDTO> partialUpdate(SubscriptionBundleDTO subscriptionBundleDTO) {
        log.debug("Request to partially update SubscriptionBundle : {}", subscriptionBundleDTO);

        return subscriptionBundleRepository
            .findById(subscriptionBundleDTO.getId())
            .map(existingSubscriptionBundle -> {
                subscriptionBundleMapper.partialUpdate(existingSubscriptionBundle, subscriptionBundleDTO);

                return existingSubscriptionBundle;
            })
            .map(subscriptionBundleRepository::save)
            .map(subscriptionBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionBundleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubscriptionBundles");
        return subscriptionBundleRepository.findAll(pageable).map(subscriptionBundleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriptionBundleDTO> findOne(Long id) {
        log.debug("Request to get SubscriptionBundle : {}", id);
        return subscriptionBundleRepository.findById(id).map(subscriptionBundleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SubscriptionBundle : {}", id);
        subscriptionBundleRepository.deleteById(id);
    }
}
