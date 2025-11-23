package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.LikeMarkRepository;
import com.fanflip.admin.service.LikeMarkService;
import com.fanflip.admin.service.dto.LikeMarkDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.LikeMark}.
 */
@RestController
@RequestMapping("/api/like-marks")
public class LikeMarkResource {

    private final Logger log = LoggerFactory.getLogger(LikeMarkResource.class);

    private static final String ENTITY_NAME = "likeMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeMarkService likeMarkService;

    private final LikeMarkRepository likeMarkRepository;

    public LikeMarkResource(LikeMarkService likeMarkService, LikeMarkRepository likeMarkRepository) {
        this.likeMarkService = likeMarkService;
        this.likeMarkRepository = likeMarkRepository;
    }

    /**
     * {@code POST  /like-marks} : Create a new likeMark.
     *
     * @param likeMarkDTO the likeMarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeMarkDTO, or with status {@code 400 (Bad Request)} if the likeMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LikeMarkDTO>> createLikeMark(@Valid @RequestBody LikeMarkDTO likeMarkDTO) throws URISyntaxException {
        log.debug("REST request to save LikeMark : {}", likeMarkDTO);
        if (likeMarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new likeMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return likeMarkService
            .save(likeMarkDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/like-marks/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /like-marks/:id} : Updates an existing likeMark.
     *
     * @param id the id of the likeMarkDTO to save.
     * @param likeMarkDTO the likeMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMarkDTO,
     * or with status {@code 400 (Bad Request)} if the likeMarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LikeMarkDTO>> updateLikeMark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LikeMarkDTO likeMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikeMark : {}, {}", id, likeMarkDTO);
        if (likeMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return likeMarkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return likeMarkService
                    .update(likeMarkDTO)
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
     * {@code PATCH  /like-marks/:id} : Partial updates given fields of an existing likeMark, field will ignore if it is null
     *
     * @param id the id of the likeMarkDTO to save.
     * @param likeMarkDTO the likeMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMarkDTO,
     * or with status {@code 400 (Bad Request)} if the likeMarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likeMarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LikeMarkDTO>> partialUpdateLikeMark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LikeMarkDTO likeMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeMark partially : {}, {}", id, likeMarkDTO);
        if (likeMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return likeMarkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LikeMarkDTO> result = likeMarkService.partialUpdate(likeMarkDTO);

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
     * {@code GET  /like-marks} : get all the likeMarks.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeMarks in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<LikeMarkDTO>>> getAllLikeMarks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of LikeMarks");
        return likeMarkService
            .countAll()
            .zipWith(likeMarkService.findAll(pageable).collectList())
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
     * {@code GET  /like-marks/:id} : get the "id" likeMark.
     *
     * @param id the id of the likeMarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeMarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LikeMarkDTO>> getLikeMark(@PathVariable("id") Long id) {
        log.debug("REST request to get LikeMark : {}", id);
        Mono<LikeMarkDTO> likeMarkDTO = likeMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likeMarkDTO);
    }

    /**
     * {@code DELETE  /like-marks/:id} : delete the "id" likeMark.
     *
     * @param id the id of the likeMarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLikeMark(@PathVariable("id") Long id) {
        log.debug("REST request to delete LikeMark : {}", id);
        return likeMarkService
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
     * {@code SEARCH  /like-marks/_search?query=:query} : search for the likeMark corresponding
     * to the query.
     *
     * @param query the query of the likeMark search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<LikeMarkDTO>>> searchLikeMarks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of LikeMarks for query {}", query);
        return likeMarkService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(likeMarkService.search(query, pageable)));
    }
}
