package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PostCommentRepository;
import com.fanflip.admin.service.PostCommentService;
import com.fanflip.admin.service.dto.PostCommentDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PostComment}.
 */
@RestController
@RequestMapping("/api/post-comments")
public class PostCommentResource {

    private final Logger log = LoggerFactory.getLogger(PostCommentResource.class);

    private static final String ENTITY_NAME = "postComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostCommentService postCommentService;

    private final PostCommentRepository postCommentRepository;

    public PostCommentResource(PostCommentService postCommentService, PostCommentRepository postCommentRepository) {
        this.postCommentService = postCommentService;
        this.postCommentRepository = postCommentRepository;
    }

    /**
     * {@code POST  /post-comments} : Create a new postComment.
     *
     * @param postCommentDTO the postCommentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postCommentDTO, or with status {@code 400 (Bad Request)} if the postComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PostCommentDTO>> createPostComment(@Valid @RequestBody PostCommentDTO postCommentDTO)
        throws URISyntaxException {
        log.debug("REST request to save PostComment : {}", postCommentDTO);
        if (postCommentDTO.getId() != null) {
            throw new BadRequestAlertException("A new postComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return postCommentService
            .save(postCommentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/post-comments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /post-comments/:id} : Updates an existing postComment.
     *
     * @param id the id of the postCommentDTO to save.
     * @param postCommentDTO the postCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCommentDTO,
     * or with status {@code 400 (Bad Request)} if the postCommentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PostCommentDTO>> updatePostComment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostCommentDTO postCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostComment : {}, {}", id, postCommentDTO);
        if (postCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return postCommentService
                    .update(postCommentDTO)
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
     * {@code PATCH  /post-comments/:id} : Partial updates given fields of an existing postComment, field will ignore if it is null
     *
     * @param id the id of the postCommentDTO to save.
     * @param postCommentDTO the postCommentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postCommentDTO,
     * or with status {@code 400 (Bad Request)} if the postCommentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postCommentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postCommentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PostCommentDTO>> partialUpdatePostComment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostCommentDTO postCommentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostComment partially : {}, {}", id, postCommentDTO);
        if (postCommentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postCommentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return postCommentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PostCommentDTO> result = postCommentService.partialUpdate(postCommentDTO);

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
     * {@code GET  /post-comments} : get all the postComments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postComments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PostCommentDTO>>> getAllPostComments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PostComments");
        return postCommentService
            .countAll()
            .zipWith(postCommentService.findAll(pageable).collectList())
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
     * {@code GET  /post-comments/:id} : get the "id" postComment.
     *
     * @param id the id of the postCommentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postCommentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostCommentDTO>> getPostComment(@PathVariable("id") Long id) {
        log.debug("REST request to get PostComment : {}", id);
        Mono<PostCommentDTO> postCommentDTO = postCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postCommentDTO);
    }

    /**
     * {@code DELETE  /post-comments/:id} : delete the "id" postComment.
     *
     * @param id the id of the postCommentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePostComment(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostComment : {}", id);
        return postCommentService
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
     * {@code SEARCH  /post-comments/_search?query=:query} : search for the postComment corresponding
     * to the query.
     *
     * @param query the query of the postComment search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PostCommentDTO>>> searchPostComments(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PostComments for query {}", query);
        return postCommentService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(postCommentService.search(query, pageable)));
    }
}
