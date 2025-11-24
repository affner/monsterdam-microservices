package com.monsterdam.admin.service.impl;

import com.monsterdam.admin.repository.BookMarkRepository;
import com.monsterdam.admin.repository.search.BookMarkSearchRepository;
import com.monsterdam.admin.service.BookMarkService;
import com.monsterdam.admin.service.dto.BookMarkDTO;
import com.monsterdam.admin.service.mapper.BookMarkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link com.monsterdam.admin.domain.BookMark}.
 */
@Service
@Transactional
public class BookMarkServiceImpl implements BookMarkService {

    private final Logger log = LoggerFactory.getLogger(BookMarkServiceImpl.class);

    private final BookMarkRepository bookMarkRepository;

    private final BookMarkMapper bookMarkMapper;

    private final BookMarkSearchRepository bookMarkSearchRepository;

    public BookMarkServiceImpl(
        BookMarkRepository bookMarkRepository,
        BookMarkMapper bookMarkMapper,
        BookMarkSearchRepository bookMarkSearchRepository
    ) {
        this.bookMarkRepository = bookMarkRepository;
        this.bookMarkMapper = bookMarkMapper;
        this.bookMarkSearchRepository = bookMarkSearchRepository;
    }

    @Override
    public Mono<BookMarkDTO> save(BookMarkDTO bookMarkDTO) {
        log.debug("Request to save BookMark : {}", bookMarkDTO);
        return bookMarkRepository
            .save(bookMarkMapper.toEntity(bookMarkDTO))
            .flatMap(bookMarkSearchRepository::save)
            .map(bookMarkMapper::toDto);
    }

    @Override
    public Mono<BookMarkDTO> update(BookMarkDTO bookMarkDTO) {
        log.debug("Request to update BookMark : {}", bookMarkDTO);
        return bookMarkRepository
            .save(bookMarkMapper.toEntity(bookMarkDTO))
            .flatMap(bookMarkSearchRepository::save)
            .map(bookMarkMapper::toDto);
    }

    @Override
    public Mono<BookMarkDTO> partialUpdate(BookMarkDTO bookMarkDTO) {
        log.debug("Request to partially update BookMark : {}", bookMarkDTO);

        return bookMarkRepository
            .findById(bookMarkDTO.getId())
            .map(existingBookMark -> {
                bookMarkMapper.partialUpdate(existingBookMark, bookMarkDTO);

                return existingBookMark;
            })
            .flatMap(bookMarkRepository::save)
            .flatMap(savedBookMark -> {
                bookMarkSearchRepository.save(savedBookMark);
                return Mono.just(savedBookMark);
            })
            .map(bookMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookMarkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookMarks");
        return bookMarkRepository.findAllBy(pageable).map(bookMarkMapper::toDto);
    }

    public Flux<BookMarkDTO> findAllWithEagerRelationships(Pageable pageable) {
        return bookMarkRepository.findAllWithEagerRelationships(pageable).map(bookMarkMapper::toDto);
    }

    public Mono<Long> countAll() {
        return bookMarkRepository.count();
    }

    public Mono<Long> searchCount() {
        return bookMarkSearchRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<BookMarkDTO> findOne(Long id) {
        log.debug("Request to get BookMark : {}", id);
        return bookMarkRepository.findOneWithEagerRelationships(id).map(bookMarkMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete BookMark : {}", id);
        return bookMarkRepository.deleteById(id).then(bookMarkSearchRepository.deleteById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookMarkDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BookMarks for query {}", query);
        return bookMarkSearchRepository.search(query, pageable).map(bookMarkMapper::toDto);
    }
}
