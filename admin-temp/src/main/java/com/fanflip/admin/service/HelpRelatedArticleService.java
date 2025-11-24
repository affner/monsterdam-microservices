package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.HelpRelatedArticleDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.HelpRelatedArticle}.
 */
public interface HelpRelatedArticleService {
    /**
     * Save a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<HelpRelatedArticleDTO> save(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Updates a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<HelpRelatedArticleDTO> update(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Partially updates a helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<HelpRelatedArticleDTO> partialUpdate(HelpRelatedArticleDTO helpRelatedArticleDTO);

    /**
     * Get all the helpRelatedArticles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpRelatedArticleDTO> findAll(Pageable pageable);

    /**
     * Returns the number of helpRelatedArticles available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of helpRelatedArticles available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" helpRelatedArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<HelpRelatedArticleDTO> findOne(Long id);

    /**
     * Delete the "id" helpRelatedArticle.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the helpRelatedArticle corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<HelpRelatedArticleDTO> search(String query, Pageable pageable);
}
