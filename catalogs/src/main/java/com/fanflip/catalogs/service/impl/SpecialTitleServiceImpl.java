package com.fanflip.catalogs.service.impl;

import com.fanflip.catalogs.domain.SpecialTitle;
import com.fanflip.catalogs.repository.SpecialTitleRepository;
import com.fanflip.catalogs.service.SpecialTitleService;
import com.fanflip.catalogs.service.dto.SpecialTitleDTO;
import com.fanflip.catalogs.service.mapper.SpecialTitleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.catalogs.domain.SpecialTitle}.
 */
@Service
@Transactional
public class SpecialTitleServiceImpl implements SpecialTitleService {

    private final Logger log = LoggerFactory.getLogger(SpecialTitleServiceImpl.class);

    private final SpecialTitleRepository specialTitleRepository;

    private final SpecialTitleMapper specialTitleMapper;

    public SpecialTitleServiceImpl(SpecialTitleRepository specialTitleRepository, SpecialTitleMapper specialTitleMapper) {
        this.specialTitleRepository = specialTitleRepository;
        this.specialTitleMapper = specialTitleMapper;
    }

    @Override
    public SpecialTitleDTO save(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to save SpecialTitle : {}", specialTitleDTO);
        SpecialTitle specialTitle = specialTitleMapper.toEntity(specialTitleDTO);
        specialTitle = specialTitleRepository.save(specialTitle);
        return specialTitleMapper.toDto(specialTitle);
    }

    @Override
    public SpecialTitleDTO update(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to update SpecialTitle : {}", specialTitleDTO);
        SpecialTitle specialTitle = specialTitleMapper.toEntity(specialTitleDTO);
        specialTitle = specialTitleRepository.save(specialTitle);
        return specialTitleMapper.toDto(specialTitle);
    }

    @Override
    public Optional<SpecialTitleDTO> partialUpdate(SpecialTitleDTO specialTitleDTO) {
        log.debug("Request to partially update SpecialTitle : {}", specialTitleDTO);

        return specialTitleRepository
            .findById(specialTitleDTO.getId())
            .map(existingSpecialTitle -> {
                specialTitleMapper.partialUpdate(existingSpecialTitle, specialTitleDTO);

                return existingSpecialTitle;
            })
            .map(specialTitleRepository::save)
            .map(specialTitleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialTitleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialTitles");
        return specialTitleRepository.findAll(pageable).map(specialTitleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialTitleDTO> findOne(Long id) {
        log.debug("Request to get SpecialTitle : {}", id);
        return specialTitleRepository.findById(id).map(specialTitleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpecialTitle : {}", id);
        specialTitleRepository.deleteById(id);
    }
}
