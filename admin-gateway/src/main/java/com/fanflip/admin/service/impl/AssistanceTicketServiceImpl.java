package com.fanflip.admin.service.impl;

import com.fanflip.admin.repository.AssistanceTicketRepository;
import com.fanflip.admin.repository.search.AssistanceTicketSearchRepository;
import com.fanflip.admin.service.AssistanceTicketService;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.service.mapper.AssistanceTicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.fanflip.admin.domain.AssistanceTicket}.
 */
@Service
@Transactional
public class AssistanceTicketServiceImpl implements AssistanceTicketService {

    private final Logger log = LoggerFactory.getLogger(AssistanceTicketServiceImpl.class);

    private final AssistanceTicketRepository assistanceTicketRepository;

    private final AssistanceTicketMapper assistanceTicketMapper;

    private final AssistanceTicketSearchRepository assistanceTicketSearchRepository;

    public AssistanceTicketServiceImpl(
        AssistanceTicketRepository assistanceTicketRepository,
        AssistanceTicketMapper assistanceTicketMapper,
        AssistanceTicketSearchRepository assistanceTicketSearchRepository
    ) {
        this.assistanceTicketRepository = assistanceTicketRepository;
        this.assistanceTicketMapper = assistanceTicketMapper;
        this.assistanceTicketSearchRepository = assistanceTicketSearchRepository;
    }

    @Override
    public Mono<AssistanceTicketDTO> save(AssistanceTicketDTO assistanceTicketDTO) {
        log.debug("Request to save AssistanceTicket : {}", assistanceTicketDTO);
        return assistanceTicketRepository
            .save(assistanceTicketMapper.toEntity(assistanceTicketDTO))
            .flatMap(assistanceTicketSearchRepository::save)
            .map(assistanceTicketMapper::toDto);
    }

    @Override
    public Mono<AssistanceTicketDTO> update(AssistanceTicketDTO assistanceTicketDTO) {
        log.debug("Request to update AssistanceTicket : {}", assistanceTicketDTO);
        return assistanceTicketRepository
            .save(assistanceTicketMapper.toEntity(assistanceTicketDTO))
            .flatMap(assistanceTicketSearchRepository::save)
            .map(assistanceTicketMapper::toDto);
    }

    @Override
    public Mono<AssistanceTicketDTO> partialUpdate(AssistanceTicketDTO assistanceTicketDTO) {
        log.debug("Request to partially update AssistanceTicket : {}", assistanceTicketDTO);

        return assistanceTicketRepository
            .findById(assistanceTicketDTO.getId())
            .map(existingAssistanceTicket -> {
                assistanceTicketMapper.partialUpdate(existingAssistanceTicket, assistanceTicketDTO);

                return existingAssistanceTicket;
            })
            .flatMap(assistanceTicketRepository::save)
            .flatMap(savedAssistanceTicket -> {
                assistanceTicketSearchRepository.save(savedAssistanceTicket);
                return Mono.just(savedAssistanceTicket);
            })
            .map(assistanceTicketMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AssistanceTicketDTO> findAll() {
        log.debug("Request to get all AssistanceTickets");
        return assistanceTicketRepository.findAll().map(assistanceTicketMapper::toDto);
    }

    /**
     *  Get all the assistanceTickets where Report is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssistanceTicketDTO> findAllWhereReportIsNull() {
        log.debug("Request to get all assistanceTickets where Report is null");
        return assistanceTicketRepository.findAllWhereReportIsNull().map(assistanceTicketMapper::toDto);
    }

    /**
     *  Get all the assistanceTickets where DocumentsReview is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AssistanceTicketDTO> findAllWhereDocumentsReviewIsNull() {
        log.debug("Request to get all assistanceTickets where DocumentsReview is null");
        return assistanceTicketRepository.findAllWhereDocumentsReviewIsNull().map(assistanceTicketMapper::toDto);
    }

    public Mono<Long> countAll() {
        return assistanceTicketRepository.count();
    }

    public Mono<Long> searchCount() {
        return assistanceTicketSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<AssistanceTicketDTO> findOne(Long id) {
        log.debug("Request to get AssistanceTicket : {}", id);
        return assistanceTicketRepository.findById(id).map(assistanceTicketMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete AssistanceTicket : {}", id);
        return assistanceTicketRepository.deleteById(id).then(assistanceTicketSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<AssistanceTicketDTO> search(String query) {
        log.debug("Request to search AssistanceTickets for query {}", query);
        try {
            return assistanceTicketSearchRepository.search(query).map(assistanceTicketMapper::toDto);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
