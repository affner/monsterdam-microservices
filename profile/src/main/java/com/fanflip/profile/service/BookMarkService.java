package com.monsterdam.profile.service;

import com.monsterdam.profile.service.dto.BookMarkDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.profile.domain.BookMark}.
 */
public interface BookMarkService {
    /**
     * Save a bookMark.
     *
     * @param bookMarkDTO the entity to save.
     * @return the persisted entity.
     */
    BookMarkDTO save(BookMarkDTO bookMarkDTO);

    /**
     * Updates a bookMark.
     *
     * @param bookMarkDTO the entity to update.
     * @return the persisted entity.
     */
    BookMarkDTO update(BookMarkDTO bookMarkDTO);

    /**
     * Partially updates a bookMark.
     *
     * @param bookMarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookMarkDTO> partialUpdate(BookMarkDTO bookMarkDTO);

    /**
     * Get all the bookMarks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookMarkDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bookMark.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookMarkDTO> findOne(Long id);

    /**
     * Delete the "id" bookMark.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
