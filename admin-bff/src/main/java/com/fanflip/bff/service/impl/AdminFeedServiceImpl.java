package com.fanflip.bff.service.impl;


import com.fanflip.bff.client.PostFeedClient;
import com.fanflip.bff.service.AdminFeedService;
import com.fanflip.bff.service.dto.AdminPostFeedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing.
 */
@Service
@Transactional
public class AdminFeedServiceImpl implements AdminFeedService {

    private final Logger log = LoggerFactory.getLogger(AdminFeedServiceImpl.class);


    private final PostFeedClient postFeedClient;

    public AdminFeedServiceImpl(
        PostFeedClient postFeedClient
    ) {
        this.postFeedClient = postFeedClient;

    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminPostFeedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFeeds");
        return postFeedClient.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public AdminPostFeedDTO findOne(Long id) {
        log.debug("Request to get PostFeed : {}", id);
        return postFeedClient.findOne(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AdminPostFeedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostFeeds for query {}", query);
        return postFeedClient.search(query, pageable);
    }
}
