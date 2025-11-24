package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.PostFeedRepository;
import com.monsterdam.admin.service.PostFeedService;
import com.monsterdam.admin.service.dto.PostFeedDTO;
import com.monsterdam.admin.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.admin.domain.PostFeed}.
 */
@RestController
@RequestMapping("/api/post-feeds")
public class PostFeedResource {

    private final Logger log = LoggerFactory.getLogger(PostFeedResource.class);

    private static final String ENTITY_NAME = "postFeed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostFeedService postFeedService;

    private final PostFeedRepository postFeedRepository;

    public PostFeedResource(PostFeedService postFeedService, PostFeedRepository postFeedRepository) {
        this.postFeedService = postFeedService;
        this.postFeedRepository = postFeedRepository;
    }

    /**
     * {@code POST  /post-feeds} : Create a new postFeed.
     *
     * @param postFeedDTO the postFeedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postFeedDTO, or with status {@code 400 (Bad Request)} if the postFeed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PostFeedDTO>> createPostFeed(@Valid @RequestBody PostFeedDTO postFeedDTO) throws URISyntaxException {
        log.debug("REST request to save PostFeed : {}", postFeedDTO);
        if (postFeedDTO.getId() != null) {
            throw new BadRequestAlertException("A new postFeed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return postFeedService
            .save(postFeedDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/post-feeds/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /post-feeds/:id} : Updates an existing postFeed.
     *
     * @param id the id of the postFeedDTO to save.
     * @param postFeedDTO the postFeedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postFeedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostFeedDTO>> updatePostFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostFeedDTO postFeedDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostFeed : {}, {}", id, postFeedDTO);
        if (postFeedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postFeedRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return postFeedService
                    .update(postFeedDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /post-feeds/:id} : Partial updates given fields of an existing postFeed, field will ignore if it is null
     *
     * @param id the id of the postFeedDTO to save.
     * @param postFeedDTO the postFeedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postFeedDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postFeedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PostFeedDTO>> partialUpdatePostFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostFeedDTO postFeedDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostFeed partially : {}, {}", id, postFeedDTO);
        if (postFeedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postFeedRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PostFeedDTO> result = postFeedService.partialUpdate(postFeedDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /post-feeds} : get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postFeeds in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PostFeedDTO>>> getAllPostFeeds(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PostFeeds");
        return postFeedService
            .countAll()
            .zipWith(postFeedService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /post-feeds/:id} : get the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFeedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostFeedDTO>> getPostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to get PostFeed : {}", id);
        Mono<PostFeedDTO> postFeedDTO = postFeedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postFeedDTO);
    }

    /**
     * {@code DELETE  /post-feeds/:id} : delete the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostFeed : {}", id);
        return postFeedService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /post-feeds/_search?query=:query} : search for the postFeed corresponding
     * to the query.
     *
     * @param query the query of the postFeed search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PostFeedDTO>>> searchPostFeeds(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PostFeeds for query {}", query);
        return postFeedService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(postFeedService.search(query, pageable)));
    }
}
