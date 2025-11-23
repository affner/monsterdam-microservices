package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.PayoutMethodRepository;
import com.fanflip.admin.repository.search.PayoutMethodSearchRepository;
import com.fanflip.admin.service.PayoutMethodService;
import com.fanflip.admin.service.dto.PayoutMethodDTO;
import com.fanflip.admin.service.mapper.PayoutMethodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.PayoutMethod}.
 */
@Service
@Transactional
public class PayoutMethodServiceImpl implements PayoutMethodService {

    private final Logger log = LoggerFactory.getLogger(PayoutMethodServiceImpl.class);

    private final PayoutMethodRepository payoutMethodRepository;

    private final PayoutMethodMapper payoutMethodMapper;

    private final PayoutMethodSearchRepository payoutMethodSearchRepository;

    public PayoutMethodServiceImpl(
        PayoutMethodRepository payoutMethodRepository,
        PayoutMethodMapper payoutMethodMapper,
        PayoutMethodSearchRepository payoutMethodSearchRepository
    ) {
        this.payoutMethodRepository = payoutMethodRepository;
        this.payoutMethodMapper = payoutMethodMapper;
        this.payoutMethodSearchRepository = payoutMethodSearchRepository;
    }

    @Override
    public Mono<PayoutMethodDTO> save(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to save PayoutMethod : {}", payoutMethodDTO);
        return payoutMethodRepository
            .save(payoutMethodMapper.toEntity(payoutMethodDTO))
            .flatMap(payoutMethodSearchRepository::save)
            .map(payoutMethodMapper::toDto);
    }

    @Override
    public Mono<PayoutMethodDTO> update(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to update PayoutMethod : {}", payoutMethodDTO);
        return payoutMethodRepository
            .save(payoutMethodMapper.toEntity(payoutMethodDTO))
            .flatMap(payoutMethodSearchRepository::save)
            .map(payoutMethodMapper::toDto);
    }

    @Override
    public Mono<PayoutMethodDTO> partialUpdate(PayoutMethodDTO payoutMethodDTO) {
        log.debug("Request to partially update PayoutMethod : {}", payoutMethodDTO);

        return payoutMethodRepository
            .findById(payoutMethodDTO.getId())
            .map(existingPayoutMethod -> {
                payoutMethodMapper.partialUpdate(existingPayoutMethod, payoutMethodDTO);

                return existingPayoutMethod;
            })
            .flatMap(payoutMethodRepository::save)
            .flatMap(savedPayoutMethod -> {
                payoutMethodSearchRepository.save(savedPayoutMethod);
                return Mono.just(savedPayoutMethod);
            })
            .map(payoutMethodMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PayoutMethodDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PayoutMethods");
        return payoutMethodRepository.findAllBy(pageable).map(payoutMethodMapper::toDto);
    }

    public Mono<Long> countAll() {
        return payoutMethodRepository.count();
    }

    public Mono<Long> searchCount() {
        return payoutMethodSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PayoutMethodDTO> findOne(Long id) {
        log.debug("Request to get PayoutMethod : {}", id);
        return payoutMethodRepository.findById(id).map(payoutMethodMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PayoutMethod : {}", id);
        return payoutMethodRepository.deleteById(id).then(payoutMethodSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PayoutMethodDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PayoutMethods for query {}", query);
        return payoutMethodSearchRepository.search(query, pageable).map(payoutMethodMapper::toDto);
    }
}
