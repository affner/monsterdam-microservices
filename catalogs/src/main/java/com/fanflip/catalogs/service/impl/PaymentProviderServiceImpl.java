package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.PaymentProvider;
import com.fanflip.catalogs.repository.PaymentProviderRepository;
import com.fanflip.catalogs.service.PaymentProviderService;
import com.fanflip.catalogs.service.dto.PaymentProviderDTO;
import com.fanflip.catalogs.service.mapper.PaymentProviderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.PaymentProvider}.
 */
@Service
@Transactional
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final Logger log = LoggerFactory.getLogger(PaymentProviderServiceImpl.class);

    private final PaymentProviderRepository paymentProviderRepository;

    private final PaymentProviderMapper paymentProviderMapper;

    public PaymentProviderServiceImpl(PaymentProviderRepository paymentProviderRepository, PaymentProviderMapper paymentProviderMapper) {
        this.paymentProviderRepository = paymentProviderRepository;
        this.paymentProviderMapper = paymentProviderMapper;
    }

    @Override
    public PaymentProviderDTO save(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to save PaymentProvider : {}", paymentProviderDTO);
        PaymentProvider paymentProvider = paymentProviderMapper.toEntity(paymentProviderDTO);
        paymentProvider = paymentProviderRepository.save(paymentProvider);
        return paymentProviderMapper.toDto(paymentProvider);
    }

    @Override
    public PaymentProviderDTO update(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to update PaymentProvider : {}", paymentProviderDTO);
        PaymentProvider paymentProvider = paymentProviderMapper.toEntity(paymentProviderDTO);
        paymentProvider = paymentProviderRepository.save(paymentProvider);
        return paymentProviderMapper.toDto(paymentProvider);
    }

    @Override
    public Optional<PaymentProviderDTO> partialUpdate(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to partially update PaymentProvider : {}", paymentProviderDTO);

        return paymentProviderRepository
            .findById(paymentProviderDTO.getId())
            .map(existingPaymentProvider -> {
                paymentProviderMapper.partialUpdate(existingPaymentProvider, paymentProviderDTO);

                return existingPaymentProvider;
            })
            .map(paymentProviderRepository::save)
            .map(paymentProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentProviders");
        return paymentProviderRepository.findAll(pageable).map(paymentProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentProviderDTO> findOne(Long id) {
        log.debug("Request to get PaymentProvider : {}", id);
        return paymentProviderRepository.findById(id).map(paymentProviderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentProvider : {}", id);
        paymentProviderRepository.deleteById(id);
    }
}
