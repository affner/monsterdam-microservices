package com.fanflip.multimedia.service.impl;

import com.fanflip.multimedia.domain.SinglePhoto;
import com.fanflip.multimedia.repository.SinglePhotoRepository;
import com.fanflip.multimedia.service.SinglePhotoService;
import com.fanflip.multimedia.service.dto.SinglePhotoDTO;
import com.fanflip.multimedia.service.mapper.SinglePhotoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.multimedia.domain.SinglePhoto}.
 */
@Service
@Transactional
public class SinglePhotoServiceImpl implements SinglePhotoService {

    private final Logger log = LoggerFactory.getLogger(SinglePhotoServiceImpl.class);

    private final SinglePhotoRepository singlePhotoRepository;

    private final SinglePhotoMapper singlePhotoMapper;

    public SinglePhotoServiceImpl(SinglePhotoRepository singlePhotoRepository, SinglePhotoMapper singlePhotoMapper) {
        this.singlePhotoRepository = singlePhotoRepository;
        this.singlePhotoMapper = singlePhotoMapper;
    }

    @Override
    public SinglePhotoDTO save(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to save SinglePhoto : {}", singlePhotoDTO);
        SinglePhoto singlePhoto = singlePhotoMapper.toEntity(singlePhotoDTO);
        singlePhoto = singlePhotoRepository.save(singlePhoto);
        return singlePhotoMapper.toDto(singlePhoto);
    }

    @Override
    public SinglePhotoDTO update(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to update SinglePhoto : {}", singlePhotoDTO);
        SinglePhoto singlePhoto = singlePhotoMapper.toEntity(singlePhotoDTO);
        singlePhoto = singlePhotoRepository.save(singlePhoto);
        return singlePhotoMapper.toDto(singlePhoto);
    }

    @Override
    public Optional<SinglePhotoDTO> partialUpdate(SinglePhotoDTO singlePhotoDTO) {
        log.debug("Request to partially update SinglePhoto : {}", singlePhotoDTO);

        return singlePhotoRepository
            .findById(singlePhotoDTO.getId())
            .map(existingSinglePhoto -> {
                singlePhotoMapper.partialUpdate(existingSinglePhoto, singlePhotoDTO);

                return existingSinglePhoto;
            })
            .map(singlePhotoRepository::save)
            .map(singlePhotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SinglePhotoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SinglePhotos");
        return singlePhotoRepository.findAll(pageable).map(singlePhotoMapper::toDto);
    }

    public Page<SinglePhotoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return singlePhotoRepository.findAllWithEagerRelationships(pageable).map(singlePhotoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SinglePhotoDTO> findOne(Long id) {
        log.debug("Request to get SinglePhoto : {}", id);
        return singlePhotoRepository.findOneWithEagerRelationships(id).map(singlePhotoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SinglePhoto : {}", id);
        singlePhotoRepository.deleteById(id);
    }
}
