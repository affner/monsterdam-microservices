package com.monsterdam.admin.service;

import com.monsterdam.admin.service.dto.PersonalSocialLinksDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.monsterdam.admin.domain.PersonalSocialLinks}.
 */
public interface PersonalSocialLinksService {
    /**
     * Save a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PersonalSocialLinksDTO> save(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Updates a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PersonalSocialLinksDTO> update(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Partially updates a personalSocialLinks.
     *
     * @param personalSocialLinksDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PersonalSocialLinksDTO> partialUpdate(PersonalSocialLinksDTO personalSocialLinksDTO);

    /**
     * Get all the personalSocialLinks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PersonalSocialLinksDTO> findAll(Pageable pageable);

    /**
     * Get all the personalSocialLinks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PersonalSocialLinksDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of personalSocialLinks available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of personalSocialLinks available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" personalSocialLinks.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PersonalSocialLinksDTO> findOne(Long id);

    /**
     * Delete the "id" personalSocialLinks.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the personalSocialLinks corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PersonalSocialLinksDTO> search(String query, Pageable pageable);
}
