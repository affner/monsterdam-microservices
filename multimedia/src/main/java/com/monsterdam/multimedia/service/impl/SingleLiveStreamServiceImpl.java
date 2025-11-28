package com.monsterdam.multimedia.service.impl;

import com.monsterdam.multimedia.domain.SingleLiveStream;
import com.monsterdam.multimedia.repository.SingleLiveStreamRepository;
import com.monsterdam.multimedia.service.SingleLiveStreamService;
import com.monsterdam.multimedia.service.dto.SingleLiveStreamDTO;
import com.monsterdam.multimedia.service.mapper.SingleLiveStreamMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.monsterdam.multimedia.domain.SingleLiveStream}.
 */
@Service
@Transactional
public class SingleLiveStreamServiceImpl implements SingleLiveStreamService {

    private final Logger log = LoggerFactory.getLogger(SingleLiveStreamServiceImpl.class);

    private final SingleLiveStreamRepository singleLiveStreamRepository;

    private final SingleLiveStreamMapper singleLiveStreamMapper;

    public SingleLiveStreamServiceImpl(
        SingleLiveStreamRepository singleLiveStreamRepository,
        SingleLiveStreamMapper singleLiveStreamMapper
    ) {
        this.singleLiveStreamRepository = singleLiveStreamRepository;
        this.singleLiveStreamMapper = singleLiveStreamMapper;
    }

    @Override
    public SingleLiveStreamDTO save(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to save SingleLiveStream : {}", singleLiveStreamDTO);
        SingleLiveStream singleLiveStream = singleLiveStreamMapper.toEntity(singleLiveStreamDTO);
        singleLiveStream = singleLiveStreamRepository.save(singleLiveStream);
        return singleLiveStreamMapper.toDto(singleLiveStream);
    }

    @Override
    public SingleLiveStreamDTO update(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to update SingleLiveStream : {}", singleLiveStreamDTO);
        SingleLiveStream singleLiveStream = singleLiveStreamMapper.toEntity(singleLiveStreamDTO);
        singleLiveStream = singleLiveStreamRepository.save(singleLiveStream);
        return singleLiveStreamMapper.toDto(singleLiveStream);
    }

    @Override
    public Optional<SingleLiveStreamDTO> partialUpdate(SingleLiveStreamDTO singleLiveStreamDTO) {
        log.debug("Request to partially update SingleLiveStream : {}", singleLiveStreamDTO);

        return singleLiveStreamRepository
            .findById(singleLiveStreamDTO.getId())
            .map(existingSingleLiveStream -> {
                singleLiveStreamMapper.partialUpdate(existingSingleLiveStream, singleLiveStreamDTO);

                return existingSingleLiveStream;
            })
            .map(singleLiveStreamRepository::save)
            .map(singleLiveStreamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SingleLiveStreamDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SingleLiveStreams");
        return singleLiveStreamRepository.findAll(pageable).map(singleLiveStreamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SingleLiveStreamDTO> findOne(Long id) {
        log.debug("Request to get SingleLiveStream : {}", id);
        return singleLiveStreamRepository.findById(id).map(singleLiveStreamMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SingleLiveStream : {}", id);
        singleLiveStreamRepository.deleteById(id);
    }
}
