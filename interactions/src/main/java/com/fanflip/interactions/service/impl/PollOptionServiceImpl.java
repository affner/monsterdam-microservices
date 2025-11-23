package com.fanflip.interactions.service.impl;

import com.fanflip.interactions.domain.PollOption;
import com.fanflip.interactions.repository.PollOptionRepository;
import com.fanflip.interactions.service.PollOptionService;
import com.fanflip.interactions.service.dto.PollOptionDTO;
import com.fanflip.interactions.service.mapper.PollOptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.interactions.domain.PollOption}.
 */
@Service
@Transactional
public class PollOptionServiceImpl implements PollOptionService {

    private final Logger log = LoggerFactory.getLogger(PollOptionServiceImpl.class);

    private final PollOptionRepository pollOptionRepository;

    private final PollOptionMapper pollOptionMapper;

    public PollOptionServiceImpl(PollOptionRepository pollOptionRepository, PollOptionMapper pollOptionMapper) {
        this.pollOptionRepository = pollOptionRepository;
        this.pollOptionMapper = pollOptionMapper;
    }

    @Override
    public PollOptionDTO save(PollOptionDTO pollOptionDTO) {
        log.debug("Request to save PollOption : {}", pollOptionDTO);
        PollOption pollOption = pollOptionMapper.toEntity(pollOptionDTO);
        pollOption = pollOptionRepository.save(pollOption);
        return pollOptionMapper.toDto(pollOption);
    }

    @Override
    public PollOptionDTO update(PollOptionDTO pollOptionDTO) {
        log.debug("Request to update PollOption : {}", pollOptionDTO);
        PollOption pollOption = pollOptionMapper.toEntity(pollOptionDTO);
        pollOption = pollOptionRepository.save(pollOption);
        return pollOptionMapper.toDto(pollOption);
    }

    @Override
    public Optional<PollOptionDTO> partialUpdate(PollOptionDTO pollOptionDTO) {
        log.debug("Request to partially update PollOption : {}", pollOptionDTO);

        return pollOptionRepository
            .findById(pollOptionDTO.getId())
            .map(existingPollOption -> {
                pollOptionMapper.partialUpdate(existingPollOption, pollOptionDTO);

                return existingPollOption;
            })
            .map(pollOptionRepository::save)
            .map(pollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PollOptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PollOptions");
        return pollOptionRepository.findAll(pageable).map(pollOptionMapper::toDto);
    }

    public Page<PollOptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pollOptionRepository.findAllWithEagerRelationships(pageable).map(pollOptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PollOptionDTO> findOne(Long id) {
        log.debug("Request to get PollOption : {}", id);
        return pollOptionRepository.findOneWithEagerRelationships(id).map(pollOptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PollOption : {}", id);
        pollOptionRepository.deleteById(id);
    }
}
