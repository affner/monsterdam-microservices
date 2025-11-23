package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.CurrencyRepository;
import com.fanflip.admin.repository.search.CurrencySearchRepository;
import com.fanflip.admin.service.CurrencyService;
import com.fanflip.admin.service.dto.CurrencyDTO;
import com.fanflip.admin.service.mapper.CurrencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Currency}.
 */
@Service
@Transactional
public class CurrencyServiceImpl implements CurrencyService {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    private final CurrencySearchRepository currencySearchRepository;

    public CurrencyServiceImpl(
        CurrencyRepository currencyRepository,
        CurrencyMapper currencyMapper,
        CurrencySearchRepository currencySearchRepository
    ) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.currencySearchRepository = currencySearchRepository;
    }

    @Override
    public Mono<CurrencyDTO> save(CurrencyDTO currencyDTO) {
        log.debug("Request to save Currency : {}", currencyDTO);
        return currencyRepository
            .save(currencyMapper.toEntity(currencyDTO))
            .flatMap(currencySearchRepository::save)
            .map(currencyMapper::toDto);
    }

    @Override
    public Mono<CurrencyDTO> update(CurrencyDTO currencyDTO) {
        log.debug("Request to update Currency : {}", currencyDTO);
        return currencyRepository
            .save(currencyMapper.toEntity(currencyDTO))
            .flatMap(currencySearchRepository::save)
            .map(currencyMapper::toDto);
    }

    @Override
    public Mono<CurrencyDTO> partialUpdate(CurrencyDTO currencyDTO) {
        log.debug("Request to partially update Currency : {}", currencyDTO);

        return currencyRepository
            .findById(currencyDTO.getId())
            .map(existingCurrency -> {
                currencyMapper.partialUpdate(existingCurrency, currencyDTO);

                return existingCurrency;
            })
            .flatMap(currencyRepository::save)
            .flatMap(savedCurrency -> {
                currencySearchRepository.save(savedCurrency);
                return Mono.just(savedCurrency);
            })
            .map(currencyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CurrencyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Currencies");
        return currencyRepository.findAllBy(pageable).map(currencyMapper::toDto);
    }

    public Mono<Long> countAll() {
        return currencyRepository.count();
    }

    public Mono<Long> searchCount() {
        return currencySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CurrencyDTO> findOne(Long id) {
        log.debug("Request to get Currency : {}", id);
        return currencyRepository.findById(id).map(currencyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Currency : {}", id);
        return currencyRepository.deleteById(id).then(currencySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CurrencyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Currencies for query {}", query);
        return currencySearchRepository.search(query, pageable).map(currencyMapper::toDto);
    }
}
