package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PaymentProviderRepository;
import com.fanflip.admin.repository.search.PaymentProviderSearchRepository;
import com.fanflip.admin.service.PaymentProviderService;
import com.fanflip.admin.service.dto.PaymentProviderDTO;
import com.fanflip.admin.service.mapper.PaymentProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PaymentProvider}.
 */
@Service
@Transactional
public class PaymentProviderServiceImpl implements PaymentProviderService {

    private final Logger log = LoggerFactory.getLogger(PaymentProviderServiceImpl.class);

    private final PaymentProviderRepository paymentProviderRepository;

    private final PaymentProviderMapper paymentProviderMapper;

    private final PaymentProviderSearchRepository paymentProviderSearchRepository;

    public PaymentProviderServiceImpl(
        PaymentProviderRepository paymentProviderRepository,
        PaymentProviderMapper paymentProviderMapper,
        PaymentProviderSearchRepository paymentProviderSearchRepository
    ) {
        this.paymentProviderRepository = paymentProviderRepository;
        this.paymentProviderMapper = paymentProviderMapper;
        this.paymentProviderSearchRepository = paymentProviderSearchRepository;
    }

    @Override
    public Mono<PaymentProviderDTO> save(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to save PaymentProvider : {}", paymentProviderDTO);
        return paymentProviderRepository
            .save(paymentProviderMapper.toEntity(paymentProviderDTO))
            .flatMap(paymentProviderSearchRepository::save)
            .map(paymentProviderMapper::toDto);
    }

    @Override
    public Mono<PaymentProviderDTO> update(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to update PaymentProvider : {}", paymentProviderDTO);
        return paymentProviderRepository
            .save(paymentProviderMapper.toEntity(paymentProviderDTO))
            .flatMap(paymentProviderSearchRepository::save)
            .map(paymentProviderMapper::toDto);
    }

    @Override
    public Mono<PaymentProviderDTO> partialUpdate(PaymentProviderDTO paymentProviderDTO) {
        log.debug("Request to partially update PaymentProvider : {}", paymentProviderDTO);

        return paymentProviderRepository
            .findById(paymentProviderDTO.getId())
            .map(existingPaymentProvider -> {
                paymentProviderMapper.partialUpdate(existingPaymentProvider, paymentProviderDTO);

                return existingPaymentProvider;
            })
            .flatMap(paymentProviderRepository::save)
            .flatMap(savedPaymentProvider -> {
                paymentProviderSearchRepository.save(savedPaymentProvider);
                return Mono.just(savedPaymentProvider);
            })
            .map(paymentProviderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentProviderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentProviders");
        return paymentProviderRepository.findAllBy(pageable).map(paymentProviderMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentProviderRepository.count();
    }

    public Mono<Long> searchCount() {
        return paymentProviderSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentProviderDTO> findOne(Long id) {
        log.debug("Request to get PaymentProvider : {}", id);
        return paymentProviderRepository.findById(id).map(paymentProviderMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PaymentProvider : {}", id);
        return paymentProviderRepository.deleteById(id).then(paymentProviderSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentProviderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentProviders for query {}", query);
        return paymentProviderSearchRepository.search(query, pageable).map(paymentProviderMapper::toDto);
    }
}
