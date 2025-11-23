package com.fanflip.admin.service;

import com.fanflip.admin.service.dto.DirectMessageDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.fanflip.admin.domain.DirectMessage}.
 */
public interface DirectMessageService {
    /**
     * Save a directMessage.
     *
     * @param directMessageDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DirectMessageDTO> save(DirectMessageDTO directMessageDTO);

    /**
     * Updates a directMessage.
     *
     * @param directMessageDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DirectMessageDTO> update(DirectMessageDTO directMessageDTO);

    /**
     * Partially updates a directMessage.
     *
     * @param directMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DirectMessageDTO> partialUpdate(DirectMessageDTO directMessageDTO);

    /**
     * Get all the directMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DirectMessageDTO> findAll(Pageable pageable);

    /**
     * Get all the DirectMessageDTO where AdminAnnouncement is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<DirectMessageDTO> findAllWhereAdminAnnouncementIsNull();
    /**
     * Get all the DirectMessageDTO where PurchasedTip is {@code null}.
     *
     * @return the {@link Flux} of entities.
     */
    Flux<DirectMessageDTO> findAllWherePurchasedTipIsNull();

    /**
     * Get all the directMessages with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DirectMessageDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of directMessages available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Returns the number of directMessages available in search repository.
     *
     */
    Mono<Long> searchCount();

    /**
     * Get the "id" directMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DirectMessageDTO> findOne(Long id);

    /**
     * Delete the "id" directMessage.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);

    /**
     * Search for the directMessage corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<DirectMessageDTO> search(String query, Pageable pageable);
}
