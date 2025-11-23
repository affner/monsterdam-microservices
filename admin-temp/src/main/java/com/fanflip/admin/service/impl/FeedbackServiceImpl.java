package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.FeedbackRepository;
import com.fanflip.admin.repository.search.FeedbackSearchRepository;
import com.fanflip.admin.service.FeedbackService;
import com.fanflip.admin.service.dto.FeedbackDTO;
import com.fanflip.admin.service.mapper.FeedbackMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.Feedback}.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    private final FeedbackSearchRepository feedbackSearchRepository;

    public FeedbackServiceImpl(
        FeedbackRepository feedbackRepository,
        FeedbackMapper feedbackMapper,
        FeedbackSearchRepository feedbackSearchRepository
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.feedbackSearchRepository = feedbackSearchRepository;
    }

    @Override
    public Mono<FeedbackDTO> save(FeedbackDTO feedbackDTO) {
        log.debug("Request to save Feedback : {}", feedbackDTO);
        return feedbackRepository
            .save(feedbackMapper.toEntity(feedbackDTO))
            .flatMap(feedbackSearchRepository::save)
            .map(feedbackMapper::toDto);
    }

    @Override
    public Mono<FeedbackDTO> update(FeedbackDTO feedbackDTO) {
        log.debug("Request to update Feedback : {}", feedbackDTO);
        return feedbackRepository
            .save(feedbackMapper.toEntity(feedbackDTO))
            .flatMap(feedbackSearchRepository::save)
            .map(feedbackMapper::toDto);
    }

    @Override
    public Mono<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        log.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                feedbackMapper.partialUpdate(existingFeedback, feedbackDTO);

                return existingFeedback;
            })
            .flatMap(feedbackRepository::save)
            .flatMap(savedFeedback -> {
                feedbackSearchRepository.save(savedFeedback);
                return Mono.just(savedFeedback);
            })
            .map(feedbackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FeedbackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Feedbacks");
        return feedbackRepository.findAllBy(pageable).map(feedbackMapper::toDto);
    }

    public Mono<Long> countAll() {
        return feedbackRepository.count();
    }

    public Mono<Long> searchCount() {
        return feedbackSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<FeedbackDTO> findOne(Long id) {
        log.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).map(feedbackMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Feedback : {}", id);
        return feedbackRepository.deleteById(id).then(feedbackSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<FeedbackDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Feedbacks for query {}", query);
        return feedbackSearchRepository.search(query, pageable).map(feedbackMapper::toDto);
    }
}
