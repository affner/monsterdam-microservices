package com.fanflip.finance.service.impl;

import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.repository.CreatorEarningRepository;
import com.fanflip.finance.service.CreatorEarningService;
import com.fanflip.finance.service.dto.CreatorEarningDTO;
import com.fanflip.finance.service.mapper.CreatorEarningMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.finance.domain.CreatorEarning}.
 */
@Service
@Transactional
public class CreatorEarningServiceImpl implements CreatorEarningService {

    private final Logger log = LoggerFactory.getLogger(CreatorEarningServiceImpl.class);

    private final CreatorEarningRepository creatorEarningRepository;

    private final CreatorEarningMapper creatorEarningMapper;

    public CreatorEarningServiceImpl(CreatorEarningRepository creatorEarningRepository, CreatorEarningMapper creatorEarningMapper) {
        this.creatorEarningRepository = creatorEarningRepository;
        this.creatorEarningMapper = creatorEarningMapper;
    }

    @Override
    public CreatorEarningDTO save(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to save CreatorEarning : {}", creatorEarningDTO);
        CreatorEarning creatorEarning = creatorEarningMapper.toEntity(creatorEarningDTO);
        creatorEarning = creatorEarningRepository.save(creatorEarning);
        return creatorEarningMapper.toDto(creatorEarning);
    }

    @Override
    public CreatorEarningDTO update(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to update CreatorEarning : {}", creatorEarningDTO);
        CreatorEarning creatorEarning = creatorEarningMapper.toEntity(creatorEarningDTO);
        creatorEarning = creatorEarningRepository.save(creatorEarning);
        return creatorEarningMapper.toDto(creatorEarning);
    }

    @Override
    public Optional<CreatorEarningDTO> partialUpdate(CreatorEarningDTO creatorEarningDTO) {
        log.debug("Request to partially update CreatorEarning : {}", creatorEarningDTO);

        return creatorEarningRepository
            .findById(creatorEarningDTO.getId())
            .map(existingCreatorEarning -> {
                creatorEarningMapper.partialUpdate(existingCreatorEarning, creatorEarningDTO);

                return existingCreatorEarning;
            })
            .map(creatorEarningRepository::save)
            .map(creatorEarningMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreatorEarningDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreatorEarnings");
        return creatorEarningRepository.findAll(pageable).map(creatorEarningMapper::toDto);
    }

    /**
     *  Get all the creatorEarnings where MoneyPayout is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CreatorEarningDTO> findAllWhereMoneyPayoutIsNull() {
        log.debug("Request to get all creatorEarnings where MoneyPayout is null");
        return StreamSupport
            .stream(creatorEarningRepository.findAll().spliterator(), false)
            .filter(creatorEarning -> creatorEarning.getMoneyPayout() == null)
            .map(creatorEarningMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the creatorEarnings where PurchasedTip is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CreatorEarningDTO> findAllWherePurchasedTipIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedTip is null");
        return StreamSupport
            .stream(creatorEarningRepository.findAll().spliterator(), false)
            .filter(creatorEarning -> creatorEarning.getPurchasedTip() == null)
            .map(creatorEarningMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the creatorEarnings where PurchasedContent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CreatorEarningDTO> findAllWherePurchasedContentIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedContent is null");
        return StreamSupport
            .stream(creatorEarningRepository.findAll().spliterator(), false)
            .filter(creatorEarning -> creatorEarning.getPurchasedContent() == null)
            .map(creatorEarningMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the creatorEarnings where PurchasedSubscription is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CreatorEarningDTO> findAllWherePurchasedSubscriptionIsNull() {
        log.debug("Request to get all creatorEarnings where PurchasedSubscription is null");
        return StreamSupport
            .stream(creatorEarningRepository.findAll().spliterator(), false)
            .filter(creatorEarning -> creatorEarning.getPurchasedSubscription() == null)
            .map(creatorEarningMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreatorEarningDTO> findOne(Long id) {
        log.debug("Request to get CreatorEarning : {}", id);
        return creatorEarningRepository.findById(id).map(creatorEarningMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreatorEarning : {}", id);
        creatorEarningRepository.deleteById(id);
    }
}
