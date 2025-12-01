package com.monsterdam.bff.service;


import com.monsterdam.bff.service.dto.UserPostFeedDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing
 */
public interface UserFeedService {

    /**
     * Get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    List<UserPostFeedDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    UserPostFeedDTO findOne(Long id);

    /**
     * Search for the postFeed corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    List<UserPostFeedDTO> search(String query, Pageable pageable);

}
