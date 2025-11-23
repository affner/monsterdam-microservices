package com.fanflip.profile.service.impl;

import com.fanflip.profile.domain.BookMark;
import com.fanflip.profile.repository.BookMarkRepository;
import com.fanflip.profile.service.BookMarkService;
import com.fanflip.profile.service.dto.BookMarkDTO;
import com.fanflip.profile.service.mapper.BookMarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.fanflip.profile.domain.BookMark}.
 */
@Service
@Transactional
public class BookMarkServiceImpl implements BookMarkService {

    private final Logger log = LoggerFactory.getLogger(BookMarkServiceImpl.class);

    private final BookMarkRepository bookMarkRepository;

    private final BookMarkMapper bookMarkMapper;

    public BookMarkServiceImpl(BookMarkRepository bookMarkRepository, BookMarkMapper bookMarkMapper) {
        this.bookMarkRepository = bookMarkRepository;
        this.bookMarkMapper = bookMarkMapper;
    }

    @Override
    public BookMarkDTO save(BookMarkDTO bookMarkDTO) {
        log.debug("Request to save BookMark : {}", bookMarkDTO);
        BookMark bookMark = bookMarkMapper.toEntity(bookMarkDTO);
        bookMark = bookMarkRepository.save(bookMark);
        return bookMarkMapper.toDto(bookMark);
    }

    @Override
    public BookMarkDTO update(BookMarkDTO bookMarkDTO) {
        log.debug("Request to update BookMark : {}", bookMarkDTO);
        BookMark bookMark = bookMarkMapper.toEntity(bookMarkDTO);
        bookMark = bookMarkRepository.save(bookMark);
        return bookMarkMapper.toDto(bookMark);
    }

    @Override
    public Optional<BookMarkDTO> partialUpdate(BookMarkDTO bookMarkDTO) {
        log.debug("Request to partially update BookMark : {}", bookMarkDTO);

        return bookMarkRepository
            .findById(bookMarkDTO.getId())
            .map(existingBookMark -> {
                bookMarkMapper.partialUpdate(existingBookMark, bookMarkDTO);

                return existingBookMark;
            })
            .map(bookMarkRepository::save)
            .map(bookMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookMarkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BookMarks");
        return bookMarkRepository.findAll(pageable).map(bookMarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookMarkDTO> findOne(Long id) {
        log.debug("Request to get BookMark : {}", id);
        return bookMarkRepository.findById(id).map(bookMarkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookMark : {}", id);
        bookMarkRepository.deleteById(id);
    }
}
