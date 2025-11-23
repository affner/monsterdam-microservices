package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PostPollRepository;
import com.fanflip.admin.service.PostPollService;
import com.fanflip.admin.service.dto.PostPollDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PostPoll}.
 */
@RestController
@RequestMapping("/api/post-polls")
public class PostPollResource {

    private final Logger log = LoggerFactory.getLogger(PostPollResource.class);

    private static final String ENTITY_NAME = "postPoll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostPollService postPollService;

    private final PostPollRepository postPollRepository;

    public PostPollResource(PostPollService postPollService, PostPollRepository postPollRepository) {
        this.postPollService = postPollService;
        this.postPollRepository = postPollRepository;
    }

    /**
     * {@code POST  /post-polls} : Create a new postPoll.
     *
     * @param postPollDTO the postPollDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postPollDTO, or with status {@code 400 (Bad Request)} if the postPoll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PostPollDTO>> createPostPoll(@Valid @RequestBody PostPollDTO postPollDTO) throws URISyntaxException {
        log.debug("REST request to save PostPoll : {}", postPollDTO);
        if (postPollDTO.getId() != null) {
            throw new BadRequestAlertException("A new postPoll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return postPollService
            .save(postPollDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/post-polls/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /post-polls/:id} : Updates an existing postPoll.
     *
     * @param id the id of the postPollDTO to save.
     * @param postPollDTO the postPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPollDTO,
     * or with status {@code 400 (Bad Request)} if the postPollDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostPollDTO>> updatePostPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostPollDTO postPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostPoll : {}, {}", id, postPollDTO);
        if (postPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postPollRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return postPollService
                    .update(postPollDTO)
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
     * {@code PATCH  /post-polls/:id} : Partial updates given fields of an existing postPoll, field will ignore if it is null
     *
     * @param id the id of the postPollDTO to save.
     * @param postPollDTO the postPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPollDTO,
     * or with status {@code 400 (Bad Request)} if the postPollDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postPollDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PostPollDTO>> partialUpdatePostPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostPollDTO postPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostPoll partially : {}, {}", id, postPollDTO);
        if (postPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postPollRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PostPollDTO> result = postPollService.partialUpdate(postPollDTO);

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
     * {@code GET  /post-polls} : get all the postPolls.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postPolls in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PostPollDTO>>> getAllPostPolls(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("post-is-null".equals(filter)) {
            log.debug("REST request to get all PostPolls where post is null");
            return postPollService.findAllWherePostIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of PostPolls");
        return postPollService
            .countAll()
            .zipWith(postPollService.findAll(pageable).collectList())
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
     * {@code GET  /post-polls/:id} : get the "id" postPoll.
     *
     * @param id the id of the postPollDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postPollDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostPollDTO>> getPostPoll(@PathVariable("id") Long id) {
        log.debug("REST request to get PostPoll : {}", id);
        Mono<PostPollDTO> postPollDTO = postPollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postPollDTO);
    }

    /**
     * {@code DELETE  /post-polls/:id} : delete the "id" postPoll.
     *
     * @param id the id of the postPollDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePostPoll(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostPoll : {}", id);
        return postPollService
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
     * {@code SEARCH  /post-polls/_search?query=:query} : search for the postPoll corresponding
     * to the query.
     *
     * @param query the query of the postPoll search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PostPollDTO>>> searchPostPolls(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PostPolls for query {}", query);
        return postPollService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(postPollService.search(query, pageable)));
    }
}
