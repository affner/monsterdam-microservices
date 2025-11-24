package com.monsterdam.bff.web.rest;


import com.monsterdam.bff.service.AdminFeedService;
import com.monsterdam.bff.service.dto.AdminPostFeedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * REST controller for managing .
 */
@RestController
@RequestMapping("/api/post-feeds")
public class AdminFeedResource {

    private final Logger log = LoggerFactory.getLogger(AdminFeedResource.class);

    private static final String ENTITY_NAME = "postFeed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminFeedService adminFeedService;


    public AdminFeedResource(AdminFeedService adminFeedService) {
        this.adminFeedService = adminFeedService;
    }

//    /**
//     * {@code POST  /post-feeds} : Create a new postFeed.
//     *
//     * @param postFeedDTO the postFeedDTO to create.
//     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postFeedDTO, or with status {@code 400 (Bad Request)} if the postFeed has already an ID.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PostMapping("")
//    public Mono<ResponseEntity<PostFeedDTO>> createPostFeed(@Valid @RequestBody PostFeedDTO postFeedDTO) throws URISyntaxException {
//        log.debug("REST request to save PostFeed : {}", postFeedDTO);
//        if (postFeedDTO.getId() != null) {
//            throw new BadRequestAlertException("A new postFeed cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        return postFeedService
//            .save(postFeedDTO)
//            .map(result -> {
//                try {
//                    return ResponseEntity
//                        .created(new URI("/bff/post-feeds/" + result.getId()))
//                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                        .body(result);
//                } catch (URISyntaxException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//    }
//
//    /*
//     * {@code PUT  /post-feeds/:id} : Updates an existing postFeed.
//     *
//     * @param id the id of the postFeedDTO to save.
//     * @param postFeedDTO the postFeedDTO to update.
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedDTO,
//     * or with status {@code 400 (Bad Request)} if the postFeedDTO is not valid,
//     * or with status {@code 500 (Internal Server Error)} if the postFeedDTO couldn't be updated.
//     * @throws URISyntaxException if the Location URI syntax is incorrect.
//     */
//    @PutMapping("/{id}")
//    public Mono<ResponseEntity<PostFeedDTO>> updatePostFeed(
//        @PathVariable(value = "id", required = false) final Long id,
//        @Valid @RequestBody PostFeedDTO postFeedDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to update PostFeed : {}, {}", id, postFeedDTO);
//        if (postFeedDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, postFeedDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        return postFeedRepository
//            .existsById(id)
//            .flatMap(exists -> {
//                if (!exists) {
//                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
//                }
//
//                return postFeedService
//                    .update(postFeedDTO)
//                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
//                    .map(result ->
//                        ResponseEntity
//                            .ok()
//                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                            .body(result)
//                    );
//            });
//    }


    /*
     * {@code GET  /post-feeds/:id} : get the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFeedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminPostFeedDTO>> getPostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to get PostFeed : {}", id);
        return Mono.justOrEmpty(adminFeedService.findOne(id))
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    /*
     * {@code SEARCH  /post-feeds/_search?query=:query} : search for the postFeed corresponding
     * to the query.
     *
     * @param query the query of the postFeed search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AdminPostFeedDTO>>> searchPostFeeds(
        @RequestParam("query") String query,
        @ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PostFeeds for query {}", query);

        List<AdminPostFeedDTO> results = adminFeedService.search(query, pageable);
        Flux<AdminPostFeedDTO> fluxResults = Flux.fromIterable(results);

        return Mono.just(ResponseEntity.ok().body(fluxResults));
    }
}
