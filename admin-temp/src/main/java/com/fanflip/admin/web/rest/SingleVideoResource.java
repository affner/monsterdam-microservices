package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.SingleVideoRepository;
import com.fanflip.admin.service.SingleVideoService;
import com.fanflip.admin.service.dto.SingleVideoDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.SingleVideo}.
 */
@RestController
@RequestMapping("/api/single-videos")
public class SingleVideoResource {

    private final Logger log = LoggerFactory.getLogger(SingleVideoResource.class);

    private static final String ENTITY_NAME = "singleVideo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleVideoService singleVideoService;

    private final SingleVideoRepository singleVideoRepository;

    public SingleVideoResource(SingleVideoService singleVideoService, SingleVideoRepository singleVideoRepository) {
        this.singleVideoService = singleVideoService;
        this.singleVideoRepository = singleVideoRepository;
    }

    /**
     * {@code POST  /single-videos} : Create a new singleVideo.
     *
     * @param singleVideoDTO the singleVideoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleVideoDTO, or with status {@code 400 (Bad Request)} if the singleVideo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SingleVideoDTO>> createSingleVideo(@Valid @RequestBody SingleVideoDTO singleVideoDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleVideo : {}", singleVideoDTO);
        if (singleVideoDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleVideo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singleVideoService
            .save(singleVideoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-videos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-videos/:id} : Updates an existing singleVideo.
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SingleVideoDTO>> updateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleVideo : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleVideoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singleVideoService
                    .update(singleVideoDTO)
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
     * {@code PATCH  /single-videos/:id} : Partial updates given fields of an existing singleVideo, field will ignore if it is null
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleVideoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SingleVideoDTO>> partialUpdateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleVideo partially : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleVideoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SingleVideoDTO> result = singleVideoService.partialUpdate(singleVideoDTO);

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
     * {@code GET  /single-videos} : get all the singleVideos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleVideos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SingleVideoDTO>>> getAllSingleVideos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SingleVideos");
        return singleVideoService
            .countAll()
            .zipWith(singleVideoService.findAll(pageable).collectList())
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
     * {@code GET  /single-videos/:id} : get the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleVideoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SingleVideoDTO>> getSingleVideo(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleVideo : {}", id);
        Mono<SingleVideoDTO> singleVideoDTO = singleVideoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleVideoDTO);
    }

    /**
     * {@code DELETE  /single-videos/:id} : delete the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSingleVideo(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleVideo : {}", id);
        return singleVideoService
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
     * {@code SEARCH  /single-videos/_search?query=:query} : search for the singleVideo corresponding
     * to the query.
     *
     * @param query the query of the singleVideo search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SingleVideoDTO>>> searchSingleVideos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SingleVideos for query {}", query);
        return singleVideoService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(singleVideoService.search(query, pageable)));
    }
}
