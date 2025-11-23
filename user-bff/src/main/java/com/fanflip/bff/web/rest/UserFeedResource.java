package com.fanflip.bff.web.rest;


import com.fanflip.bff.service.UserFeedService;
import com.fanflip.bff.service.dto.UserPostFeedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing .
 */
@RestController
@RequestMapping("/api/bff/post-feeds/")
public class UserFeedResource {

    private final Logger log = LoggerFactory.getLogger(UserFeedResource.class);


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserFeedService userFeedService;


    public UserFeedResource(UserFeedService userFeedService) {
        this.userFeedService = userFeedService;
    }


    /*
     * {@code GET  /post-feeds/:id} : get the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFeedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserPostFeedDTO>> getPostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to get PostFeed : {}", id);
        return Mono.justOrEmpty(userFeedService.findOne(id))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
