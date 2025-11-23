package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.PayoutMethod;
import com.fanflip.catalogs.repository.PayoutMethodRepository;
import com.fanflip.catalogs.service.PayoutMethodService;
import com.fanflip.catalogs.service.dto.PayoutMethodDTO;
import com.fanflip.catalogs.service.mapper.PayoutMethodMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.PayoutMethod}.
 */
@Service
@Transactional
public class PayoutMethodServiceImpl implements PayoutMethodService {

    private final Logger log = LoggerFactory.getLogger(PayoutMethodServiceImpl.class);

    private final PayoutMethodRepository payoutMethodRepository;

    private final PayoutMethodMapper payoutMethodMapper;

    public PayoutMethodServiceImpl(PayoutMethodRepository payoutMethodRepository, PayoutMethodMapper payoutMethodMapper) {
        this.payoutMethodRepository = payoutMethodRepository;
        this.payoutMethodMapper = payoutMethodMapper;
    }

    @Override
    public PayoutMethodDTO save(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to save PayoutMethod : {}", payoutMethodDTO);
        PayoutMethod payoutMethod = payoutMethodMapper.toEntity(payoutMethodDTO);
        payoutMethod = payoutMethodRepository.save(payoutMethod);
        return payoutMethodMapper.toDto(payoutMethod);
    }

    @Override
    public PayoutMethodDTO update(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to update PayoutMethod : {}", payoutMethodDTO);
        PayoutMethod payoutMethod = payoutMethodMapper.toEntity(payoutMethodDTO);
        payoutMethod = payoutMethodRepository.save(payoutMethod);
        return payoutMethodMapper.toDto(payoutMethod);
    }

    @Override
    public Optional<PayoutMethodDTO> partialUpdate(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to partially update PayoutMethod : {}", payoutMethodDTO);

        return payoutMethodRepository
            .findById(payoutMethodDTO.getId())
            .map(existingPayoutMethod -> {
                payoutMethodMapper.partialUpdate(existingPayoutMethod, payoutMethodDTO);

                return existingPayoutMethod;
            })
            .map(payoutMethodRepository::save)
            .map(payoutMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayoutMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutMethods");
        return payoutMethodRepository.findAll(pageable).map(payoutMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayoutMethodDTO> findOne(Long id) {
        log.debug("Request to get PayoutMethod : {}", id);
        return payoutMethodRepository.findById(id).map(payoutMethodMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayoutMethod : {}", id);
        payoutMethodRepository.deleteById(id);
    }
}
