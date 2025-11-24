package com.monsterdam.profile.service.impl;

import com.monsterdam.profile.domain.HashTag;
import com.monsterdam.profile.repository.HashTagRepository;
import com.monsterdam.profile.repository.search.HashTagSearchRepository;
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

    private final HashTagSearchRepository hashTagSearchRepository;

    public HashTagServiceImpl(
        HashTagRepository hashTagRepository,
        HashTagMapper hashTagMapper,
        HashTagSearchRepository hashTagSearchRepository
    ) {
        this.hashTagRepository = hashTagRepository;
        this.hashTagMapper = hashTagMapper;
        this.hashTagSearchRepository = hashTagSearchRepository;
    }

    @Override
    public HashTagDTO save(HashTagDTO hashTagDTO) {
        log.debug("Request to save HashTag : {}", hashTagDTO);
        HashTag hashTag = hashTagMapper.toEntity(hashTagDTO);
        hashTag = hashTagRepository.save(hashTag);
        HashTagDTO result = hashTagMapper.toDto(hashTag);
        hashTagSearchRepository.index(hashTag);
        return result;
    }

    @Override
    public HashTagDTO update(HashTagDTO hashTagDTO) {
        log.debug("Request to update HashTag : {}", hashTagDTO);
        HashTag hashTag = hashTagMapper.toEntity(hashTagDTO);
        hashTag = hashTagRepository.save(hashTag);
        HashTagDTO result = hashTagMapper.toDto(hashTag);
        hashTagSearchRepository.index(hashTag);
        return result;
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
            .map(savedHashTag -> {
                hashTagSearchRepository.index(savedHashTag);
                return savedHashTag;
            })
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
        hashTagSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HashTagDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HashTags for query {}", query);
        return hashTagSearchRepository.search(query, pageable).map(hashTagMapper::toDto);
    }
}
