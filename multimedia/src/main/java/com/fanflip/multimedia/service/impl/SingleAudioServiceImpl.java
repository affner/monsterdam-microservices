package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.SingleAudio;
import com.monsterdam.multimedia.repository.SingleAudioRepository;
import com.monsterdam.multimedia.service.SingleAudioService;
import com.monsterdam.multimedia.service.dto.SingleAudioDTO;
import com.monsterdam.multimedia.service.mapper.SingleAudioMapper;
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
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.SingleAudio}.
 */
@Service
@Transactional
public class SingleAudioServiceImpl implements SingleAudioService {

    private final Logger log = LoggerFactory.getLogger(SingleAudioServiceImpl.class);

    private final SingleAudioRepository singleAudioRepository;

    private final SingleAudioMapper singleAudioMapper;

    public SingleAudioServiceImpl(SingleAudioRepository singleAudioRepository, SingleAudioMapper singleAudioMapper) {
        this.singleAudioRepository = singleAudioRepository;
        this.singleAudioMapper = singleAudioMapper;
    }

    @Override
    public SingleAudioDTO save(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to save SingleAudio : {}", singleAudioDTO);
        SingleAudio singleAudio = singleAudioMapper.toEntity(singleAudioDTO);
        singleAudio = singleAudioRepository.save(singleAudio);
        return singleAudioMapper.toDto(singleAudio);
    }

    @Override
    public SingleAudioDTO update(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to update SingleAudio : {}", singleAudioDTO);
        SingleAudio singleAudio = singleAudioMapper.toEntity(singleAudioDTO);
        singleAudio = singleAudioRepository.save(singleAudio);
        return singleAudioMapper.toDto(singleAudio);
    }

    @Override
    public Optional<SingleAudioDTO> partialUpdate(SingleAudioDTO singleAudioDTO) {
        log.debug("Request to partially update SingleAudio : {}", singleAudioDTO);

        return singleAudioRepository
            .findById(singleAudioDTO.getId())
            .map(existingSingleAudio -> {
                singleAudioMapper.partialUpdate(existingSingleAudio, singleAudioDTO);

                return existingSingleAudio;
            })
            .map(singleAudioRepository::save)
            .map(singleAudioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleAudioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleAudios");
        return singleAudioRepository.findAll(pageable).map(singleAudioMapper::toDto);
    }

    /**
     *  Get all the singleAudios where ContentPackage is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SingleAudioDTO> findAllWhereContentPackageIsNull() {
        log.debug("Request to get all singleAudios where ContentPackage is null");
        return StreamSupport
            .stream(singleAudioRepository.findAll().spliterator(), false)
            .filter(singleAudio -> singleAudio.getContentPackage() == null)
            .map(singleAudioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleAudioDTO> findOne(Long id) {
        log.debug("Request to get SingleAudio : {}", id);
        return singleAudioRepository.findById(id).map(singleAudioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SingleAudio : {}", id);
        singleAudioRepository.deleteById(id);
    }
}
