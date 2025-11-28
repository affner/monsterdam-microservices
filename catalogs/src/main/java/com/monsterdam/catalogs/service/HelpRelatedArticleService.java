package com.monsterdam.catalogs.service;

import com.monsterdam.catalogs.service.dto.HelpRelatedArticleDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.monsterdam.catalogs.domain.HelpRelatedArticle}.
 */
public interface HelpRelatedArticleService {
    /**
     * Save a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to save.
     * @return the persisted entity.
     */
    HelpRelatedArticleDTO save(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Updates a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to update.
     * @return the persisted entity.
     */
    HelpRelatedArticleDTO update(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Partially updates a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HelpRelatedArticleDTO> partialUpdate(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Get all the helpRelatedArticles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HelpRelatedArticleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" helpRelatedArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HelpRelatedArticleDTO> findOne(Long id);

    /**
     * Delete the "id" helpRelatedArticle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
