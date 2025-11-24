package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.CountryRepository;
import com.monsterdam.admin.repository.search.CountrySearchRepository;
import com.monsterdam.admin.service.CountryService;
import com.monsterdam.admin.service.dto.CountryDTO;
import com.monsterdam.admin.service.mapper.CountryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.Country}.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    private final CountrySearchRepository countrySearchRepository;

    public CountryServiceImpl(
        CountryRepository countryRepository,
        CountryMapper countryMapper,
        CountrySearchRepository countrySearchRepository
    ) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
        this.countrySearchRepository = countrySearchRepository;
    }

    @Override
    public Mono<CountryDTO> save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        return countryRepository.save(countryMapper.toEntity(countryDTO)).flatMap(countrySearchRepository::save).map(countryMapper::toDto);
    }

    @Override
    public Mono<CountryDTO> update(CountryDTO countryDTO) {
        log.debug("Request to update Country : {}", countryDTO);
        return countryRepository.save(countryMapper.toEntity(countryDTO)).flatMap(countrySearchRepository::save).map(countryMapper::toDto);
    }

    @Override
    public Mono<CountryDTO> partialUpdate(CountryDTO countryDTO) {
        log.debug("Request to partially update Country : {}", countryDTO);

        return countryRepository
            .findById(countryDTO.getId())
            .map(existingCountry -> {
                countryMapper.partialUpdate(existingCountry, countryDTO);

                return existingCountry;
            })
            .flatMap(countryRepository::save)
            .flatMap(savedCountry -> {
                countrySearchRepository.save(savedCountry);
                return Mono.just(savedCountry);
            })
            .map(countryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAllBy(pageable).map(countryMapper::toDto);
    }

    public Mono<Long> countAll() {
        return countryRepository.count();
    }

    public Mono<Long> searchCount() {
        return countrySearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CountryDTO> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id).map(countryMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        return countryRepository.deleteById(id).then(countrySearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CountryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Countries for query {}", query);
        return countrySearchRepository.search(query, pageable).map(countryMapper::toDto);
    }
}
