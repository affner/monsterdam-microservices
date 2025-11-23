package com.fanflip.bff.service.impl;


import com.fanflip.bff.client.ContentPackageClient;
import com.fanflip.bff.client.PostFeedClient;
import com.fanflip.bff.service.UserFeedService;
import com.fanflip.bff.service.dto.UserPostFeedDTO;
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
public class UserFeedServiceImpl implements UserFeedService {

    private final Logger log = LoggerFactory.getLogger(UserFeedServiceImpl.class);

    private final PostFeedClient postFeedClient;

    private final ContentPackageClient contentPackageClient;

    public UserFeedServiceImpl(PostFeedClient postFeedClient, ContentPackageClient contentPackageClient) {
        this.postFeedClient = postFeedClient;
        this.contentPackageClient = contentPackageClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPostFeedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostFeeds");
        return postFeedClient.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public UserPostFeedDTO findOne(Long id) {
        log.debug("Request to get PostFeed : {}", id);
        UserPostFeedDTO post = postFeedClient.findOne(id);
        log.debug("Request to get ContentPackage : {}", post.getId());
        post.setContentPackage(contentPackageClient.findOne(post.getId()));
        return post;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserPostFeedDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostFeeds for query {}", query);
        return postFeedClient.search(query, pageable);
    }
}
