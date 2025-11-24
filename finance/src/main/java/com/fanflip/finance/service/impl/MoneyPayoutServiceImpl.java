package com.monsterdam.finance.service.impl;

import com.monsterdam.finance.domain.MoneyPayout;
import com.monsterdam.finance.repository.MoneyPayoutRepository;
import com.monsterdam.finance.service.MoneyPayoutService;
import com.monsterdam.finance.service.dto.MoneyPayoutDTO;
import com.monsterdam.finance.service.mapper.MoneyPayoutMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.finance.domain.MoneyPayout}.
 */
@Service
@Transactional
public class MoneyPayoutServiceImpl implements MoneyPayoutService {

    private final Logger log = LoggerFactory.getLogger(MoneyPayoutServiceImpl.class);

    private final MoneyPayoutRepository moneyPayoutRepository;

    private final MoneyPayoutMapper moneyPayoutMapper;

    public MoneyPayoutServiceImpl(MoneyPayoutRepository moneyPayoutRepository, MoneyPayoutMapper moneyPayoutMapper) {
        this.moneyPayoutRepository = moneyPayoutRepository;
        this.moneyPayoutMapper = moneyPayoutMapper;
    }

    @Override
    public MoneyPayoutDTO save(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to save MoneyPayout : {}", moneyPayoutDTO);
        MoneyPayout moneyPayout = moneyPayoutMapper.toEntity(moneyPayoutDTO);
        moneyPayout = moneyPayoutRepository.save(moneyPayout);
        return moneyPayoutMapper.toDto(moneyPayout);
    }

    @Override
    public MoneyPayoutDTO update(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to update MoneyPayout : {}", moneyPayoutDTO);
        MoneyPayout moneyPayout = moneyPayoutMapper.toEntity(moneyPayoutDTO);
        moneyPayout = moneyPayoutRepository.save(moneyPayout);
        return moneyPayoutMapper.toDto(moneyPayout);
    }

    @Override
    public Optional<MoneyPayoutDTO> partialUpdate(MoneyPayoutDTO moneyPayoutDTO) {
        log.debug("Request to partially update MoneyPayout : {}", moneyPayoutDTO);

        return moneyPayoutRepository
            .findById(moneyPayoutDTO.getId())
            .map(existingMoneyPayout -> {
                moneyPayoutMapper.partialUpdate(existingMoneyPayout, moneyPayoutDTO);

                return existingMoneyPayout;
            })
            .map(moneyPayoutRepository::save)
            .map(moneyPayoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MoneyPayoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MoneyPayouts");
        return moneyPayoutRepository.findAll(pageable).map(moneyPayoutMapper::toDto);
    }

    public Page<MoneyPayoutDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moneyPayoutRepository.findAllWithEagerRelationships(pageable).map(moneyPayoutMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MoneyPayoutDTO> findOne(Long id) {
        log.debug("Request to get MoneyPayout : {}", id);
        return moneyPayoutRepository.findOneWithEagerRelationships(id).map(moneyPayoutMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MoneyPayout : {}", id);
        moneyPayoutRepository.deleteById(id);
    }
}
