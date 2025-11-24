package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.repository.ContentPackageRepository;
import com.monsterdam.multimedia.service.ContentPackageService;
import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
import com.monsterdam.multimedia.service.mapper.ContentPackageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.ContentPackage}.
 */
@Service
@Transactional
public class ContentPackageServiceImpl implements ContentPackageService {

    private final Logger log = LoggerFactory.getLogger(ContentPackageServiceImpl.class);

    private final ContentPackageRepository contentPackageRepository;

    private final ContentPackageMapper contentPackageMapper;

    public ContentPackageServiceImpl(ContentPackageRepository contentPackageRepository, ContentPackageMapper contentPackageMapper) {
        this.contentPackageRepository = contentPackageRepository;
        this.contentPackageMapper = contentPackageMapper;
    }

    @Override
    public ContentPackageDTO save(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to save ContentPackage : {}", contentPackageDTO);
        ContentPackage contentPackage = contentPackageMapper.toEntity(contentPackageDTO);
        contentPackage = contentPackageRepository.save(contentPackage);
        return contentPackageMapper.toDto(contentPackage);
    }

    @Override
    public ContentPackageDTO update(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to update ContentPackage : {}", contentPackageDTO);
        ContentPackage contentPackage = contentPackageMapper.toEntity(contentPackageDTO);
        contentPackage = contentPackageRepository.save(contentPackage);
        return contentPackageMapper.toDto(contentPackage);
    }

    @Override
    public Optional<ContentPackageDTO> partialUpdate(ContentPackageDTO contentPackageDTO) {
        log.debug("Request to partially update ContentPackage : {}", contentPackageDTO);

        return contentPackageRepository
            .findById(contentPackageDTO.getId())
            .map(existingContentPackage -> {
                contentPackageMapper.partialUpdate(existingContentPackage, contentPackageDTO);

                return existingContentPackage;
            })
            .map(contentPackageRepository::save)
            .map(contentPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentPackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContentPackages");
        return contentPackageRepository.findAll(pageable).map(contentPackageMapper::toDto);
    }

    /**
     *  Get all the contentPackages where SpecialReward is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ContentPackageDTO> findAllWhereSpecialRewardIsNull() {
        log.debug("Request to get all contentPackages where SpecialReward is null");
        return StreamSupport
            .stream(contentPackageRepository.findAll().spliterator(), false)
            .filter(contentPackage -> contentPackage.getSpecialReward() == null)
            .map(contentPackageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentPackageDTO> findOne(Long id) {
        log.debug("Request to get ContentPackage : {}", id);
        return contentPackageRepository.findById(id).map(contentPackageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContentPackage : {}", id);
        contentPackageRepository.deleteById(id);
    }

    @Override
    public Optional<ContentPackageDTO> findOneByPostFeedId(Long postId) {
        log.debug("Request to get ContentPackage by PostId : {}", postId);
        return contentPackageRepository.findByIdPostFeed(postId);
    }
}
