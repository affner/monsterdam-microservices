package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.SpecialReward;
import com.monsterdam.multimedia.repository.SpecialRewardRepository;
import com.monsterdam.multimedia.service.SpecialRewardService;
import com.monsterdam.multimedia.service.dto.SpecialRewardDTO;
import com.monsterdam.multimedia.service.mapper.SpecialRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.SpecialReward}.
 */
@Service
@Transactional
public class SpecialRewardServiceImpl implements SpecialRewardService {

    private final Logger log = LoggerFactory.getLogger(SpecialRewardServiceImpl.class);

    private final SpecialRewardRepository specialRewardRepository;

    private final SpecialRewardMapper specialRewardMapper;

    public SpecialRewardServiceImpl(SpecialRewardRepository specialRewardRepository, SpecialRewardMapper specialRewardMapper) {
        this.specialRewardRepository = specialRewardRepository;
        this.specialRewardMapper = specialRewardMapper;
    }

    @Override
    public SpecialRewardDTO save(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to save SpecialReward : {}", specialRewardDTO);
        SpecialReward specialReward = specialRewardMapper.toEntity(specialRewardDTO);
        specialReward = specialRewardRepository.save(specialReward);
        return specialRewardMapper.toDto(specialReward);
    }

    @Override
    public SpecialRewardDTO update(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to update SpecialReward : {}", specialRewardDTO);
        SpecialReward specialReward = specialRewardMapper.toEntity(specialRewardDTO);
        specialReward = specialRewardRepository.save(specialReward);
        return specialRewardMapper.toDto(specialReward);
    }

    @Override
    public Optional<SpecialRewardDTO> partialUpdate(SpecialRewardDTO specialRewardDTO) {
        log.debug("Request to partially update SpecialReward : {}", specialRewardDTO);

        return specialRewardRepository
            .findById(specialRewardDTO.getId())
            .map(existingSpecialReward -> {
                specialRewardMapper.partialUpdate(existingSpecialReward, specialRewardDTO);

                return existingSpecialReward;
            })
            .map(specialRewardRepository::save)
            .map(specialRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpecialRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SpecialRewards");
        return specialRewardRepository.findAll(pageable).map(specialRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpecialRewardDTO> findOne(Long id) {
        log.debug("Request to get SpecialReward : {}", id);
        return specialRewardRepository.findById(id).map(specialRewardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SpecialReward : {}", id);
        specialRewardRepository.deleteById(id);
    }
}
