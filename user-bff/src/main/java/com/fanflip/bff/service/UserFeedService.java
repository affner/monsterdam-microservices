package com.fanflip.bff.service;


import com.fanflip.bff.service.dto.UserPostFeedDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    List<UserPostFeedDTO> findAll(Pageable pageable);

    /**
     * Get the "id" postFeed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    UserPostFeedDTO findOne(Long id);

    /**
     * Search for the postFeed corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    List<UserPostFeedDTO> search(String query, Pageable pageable);

}
