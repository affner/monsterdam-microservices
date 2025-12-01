package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.repository.HashTagRepository;
import com.monsterdam.profile.service.HashTagService;
import com.monsterdam.profile.service.dto.HashTagDTO;
import com.monsterdam.profile.service.mapper.HashTagMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.profile.domain.HashTag}.
 */
@Service
@Transactional
public class HashTagServiceImpl implements HashTagService {

    private final Logger log = LoggerFactory.getLogger(HashTagServiceImpl.class);

    private final HashTagRepository hashTagRepository;

    private final HashTagMapper hashTagMapper;

    public HashTagServiceImpl(
        HashTagRepository hashTagRepository,
        HashTagMapper hashTagMapper
    ) {
        this.hashTagRepository = hashTagRepository;
        this.hashTagMapper = hashTagMapper;
    }

    @Override
    public HashTagDTO save(HashTagDTO hashTagDTO) {
        log.debug("Request to save HashTag : {}", hashTagDTO);
        HashTag hashTag = hashTagMapper.toEntity(hashTagDTO);
        hashTag = hashTagRepository.save(hashTag);
        return hashTagMapper.toDto(hashTag);
    }

    @Override
    public HashTagDTO update(HashTagDTO hashTagDTO) {
        log.debug("Request to update HashTag : {}", hashTagDTO);
        HashTag hashTag = hashTagMapper.toEntity(hashTagDTO);
        hashTag = hashTagRepository.save(hashTag);
        return hashTagMapper.toDto(hashTag);
    }

    @Override
    public Optional<HashTagDTO> partialUpdate(HashTagDTO hashTagDTO) {
        log.debug("Request to partially update HashTag : {}", hashTagDTO);

        return hashTagRepository
            .findById(hashTagDTO.getId())
            .map(existingHashTag -> {
                hashTagMapper.partialUpdate(existingHashTag, hashTagDTO);

                return existingHashTag;
            })
            .map(hashTagRepository::save)
            .map(hashTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HashTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HashTags");
        return hashTagRepository.findAll(pageable).map(hashTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HashTagDTO> findOne(Long id) {
        log.debug("Request to get HashTag : {}", id);
        return hashTagRepository.findById(id).map(hashTagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HashTag : {}", id);
        hashTagRepository.deleteById(id);
    }
}
