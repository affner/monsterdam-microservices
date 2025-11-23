package com.fanflip.interactions.service.impl;

import com.fanflip.interactions.domain.PostPoll;
import com.fanflip.interactions.repository.PostPollRepository;
import com.fanflip.interactions.service.PostPollService;
import com.fanflip.interactions.service.dto.PostPollDTO;
import com.fanflip.interactions.service.mapper.PostPollMapper;
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
 * Service Implementation for managing {@link com.fanflip.interactions.domain.PostPoll}.
 */
@Service
@Transactional
public class PostPollServiceImpl implements PostPollService {

    private final Logger log = LoggerFactory.getLogger(PostPollServiceImpl.class);

    private final PostPollRepository postPollRepository;

    private final PostPollMapper postPollMapper;

    public PostPollServiceImpl(PostPollRepository postPollRepository, PostPollMapper postPollMapper) {
        this.postPollRepository = postPollRepository;
        this.postPollMapper = postPollMapper;
    }

    @Override
    public PostPollDTO save(PostPollDTO postPollDTO) {
        log.debug("Request to save PostPoll : {}", postPollDTO);
        PostPoll postPoll = postPollMapper.toEntity(postPollDTO);
        postPoll = postPollRepository.save(postPoll);
        return postPollMapper.toDto(postPoll);
    }

    @Override
    public PostPollDTO update(PostPollDTO postPollDTO) {
        log.debug("Request to update PostPoll : {}", postPollDTO);
        PostPoll postPoll = postPollMapper.toEntity(postPollDTO);
        postPoll = postPollRepository.save(postPoll);
        return postPollMapper.toDto(postPoll);
    }

    @Override
    public Optional<PostPollDTO> partialUpdate(PostPollDTO postPollDTO) {
        log.debug("Request to partially update PostPoll : {}", postPollDTO);

        return postPollRepository
            .findById(postPollDTO.getId())
            .map(existingPostPoll -> {
                postPollMapper.partialUpdate(existingPostPoll, postPollDTO);

                return existingPostPoll;
            })
            .map(postPollRepository::save)
            .map(postPollMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostPollDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostPolls");
        return postPollRepository.findAll(pageable).map(postPollMapper::toDto);
    }

    /**
     *  Get all the postPolls where Post is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PostPollDTO> findAllWherePostIsNull() {
        log.debug("Request to get all postPolls where Post is null");
        return StreamSupport
            .stream(postPollRepository.findAll().spliterator(), false)
            .filter(postPoll -> postPoll.getPost() == null)
            .map(postPollMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostPollDTO> findOne(Long id) {
        log.debug("Request to get PostPoll : {}", id);
        return postPollRepository.findById(id).map(postPollMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PostPoll : {}", id);
        postPollRepository.deleteById(id);
    }
}
