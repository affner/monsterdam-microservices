package com.fanflip.profile.service.impl;

import com.fanflip.profile.domain.SpecialAward;
import com.fanflip.profile.repository.SpecialAwardRepository;
import com.fanflip.profile.service.SpecialAwardService;
import com.fanflip.profile.service.dto.SpecialAwardDTO;
import com.fanflip.profile.service.mapper.SpecialAwardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.profile.domain.SpecialAward}.
 */
@Service
@Transactional
public class SpecialAwardServiceImpl implements SpecialAwardService {

    private final Logger log = LoggerFactory.getLogger(SpecialAwardServiceImpl.class);

    private final SpecialAwardRepository specialAwardRepository;

    private final SpecialAwardMapper specialAwardMapper;

    public SpecialAwardServiceImpl(SpecialAwardRepository specialAwardRepository, SpecialAwardMapper specialAwardMapper) {
        this.specialAwardRepository = specialAwardRepository;
        this.specialAwardMapper = specialAwardMapper;
    }

    @Override
    public SpecialAwardDTO save(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to save SpecialAward : {}", specialAwardDTO);
        SpecialAward specialAward = specialAwardMapper.toEntity(specialAwardDTO);
        specialAward = specialAwardRepository.save(specialAward);
        return specialAwardMapper.toDto(specialAward);
    }

    @Override
    public SpecialAwardDTO update(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to update SpecialAward : {}", specialAwardDTO);
        SpecialAward specialAward = specialAwardMapper.toEntity(specialAwardDTO);
        specialAward = specialAwardRepository.save(specialAward);
        return specialAwardMapper.toDto(specialAward);
    }

    @Override
    public Optional<SpecialAwardDTO> partialUpdate(SpecialAwardDTO specialAwardDTO) {
        log.debug("Request to partially update SpecialAward : {}", specialAwardDTO);

        return specialAwardRepository
            .findById(specialAwardDTO.getId())
            .map(existingSpecialAward -> {
                specialAwardMapper.partialUpdate(existingSpecialAward, specialAwardDTO);

                return existingSpecialAward;
            })
            .map(specialAwardRepository::save)
            .map(specialAwardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialAwardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialAwards");
        return specialAwardRepository.findAll(pageable).map(specialAwardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialAwardDTO> findOne(Long id) {
        log.debug("Request to get SpecialAward : {}", id);
        return specialAwardRepository.findById(id).map(specialAwardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpecialAward : {}", id);
        specialAwardRepository.deleteById(id);
    }
}
