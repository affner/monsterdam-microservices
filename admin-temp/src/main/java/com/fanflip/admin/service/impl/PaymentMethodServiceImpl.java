package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.PaymentMethodRepository;
import com.monsterdam.admin.repository.search.PaymentMethodSearchRepository;
import com.monsterdam.admin.service.PaymentMethodService;
import com.monsterdam.admin.service.dto.PaymentMethodDTO;
import com.monsterdam.admin.service.mapper.PaymentMethodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodRepository paymentMethodRepository;

    private final PaymentMethodMapper paymentMethodMapper;

    private final PaymentMethodSearchRepository paymentMethodSearchRepository;

    public PaymentMethodServiceImpl(
        PaymentMethodRepository paymentMethodRepository,
        PaymentMethodMapper paymentMethodMapper,
        PaymentMethodSearchRepository paymentMethodSearchRepository
    ) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
        this.paymentMethodSearchRepository = paymentMethodSearchRepository;
    }

    @Override
    public Mono<PaymentMethodDTO> save(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to save PaymentMethod : {}", paymentMethodDTO);
        return paymentMethodRepository
            .save(paymentMethodMapper.toEntity(paymentMethodDTO))
            .flatMap(paymentMethodSearchRepository::save)
            .map(paymentMethodMapper::toDto);
    }

    @Override
    public Mono<PaymentMethodDTO> update(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to update PaymentMethod : {}", paymentMethodDTO);
        return paymentMethodRepository
            .save(paymentMethodMapper.toEntity(paymentMethodDTO))
            .flatMap(paymentMethodSearchRepository::save)
            .map(paymentMethodMapper::toDto);
    }

    @Override
    public Mono<PaymentMethodDTO> partialUpdate(PaymentMethodDTO paymentMethodDTO) {
        log.debug("Request to partially update PaymentMethod : {}", paymentMethodDTO);

        return paymentMethodRepository
            .findById(paymentMethodDTO.getId())
            .map(existingPaymentMethod -> {
                paymentMethodMapper.partialUpdate(existingPaymentMethod, paymentMethodDTO);

                return existingPaymentMethod;
            })
            .flatMap(paymentMethodRepository::save)
            .flatMap(savedPaymentMethod -> {
                paymentMethodSearchRepository.save(savedPaymentMethod);
                return Mono.just(savedPaymentMethod);
            })
            .map(paymentMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentMethods");
        return paymentMethodRepository.findAllBy(pageable).map(paymentMethodMapper::toDto);
    }

    public Mono<Long> countAll() {
        return paymentMethodRepository.count();
    }

    public Mono<Long> searchCount() {
        return paymentMethodSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PaymentMethodDTO> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id).map(paymentMethodMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        return paymentMethodRepository.deleteById(id).then(paymentMethodSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PaymentMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PaymentMethods for query {}", query);
        return paymentMethodSearchRepository.search(query, pageable).map(paymentMethodMapper::toDto);
    }
}
