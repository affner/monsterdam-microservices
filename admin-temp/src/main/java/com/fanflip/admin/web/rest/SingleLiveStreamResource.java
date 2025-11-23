package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.SingleLiveStreamRepository;
import com.fanflip.admin.service.SingleLiveStreamService;
import com.fanflip.admin.service.dto.SingleLiveStreamDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.SingleLiveStream}.
 */
@RestController
@RequestMapping("/api/single-live-streams")
public class SingleLiveStreamResource {

    private final Logger log = LoggerFactory.getLogger(SingleLiveStreamResource.class);

    private static final String ENTITY_NAME = "singleLiveStream";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleLiveStreamService singleLiveStreamService;

    private final SingleLiveStreamRepository singleLiveStreamRepository;

    public SingleLiveStreamResource(
        SingleLiveStreamService singleLiveStreamService,
        SingleLiveStreamRepository singleLiveStreamRepository
    ) {
        this.singleLiveStreamService = singleLiveStreamService;
        this.singleLiveStreamRepository = singleLiveStreamRepository;
    }

    /**
     * {@code POST  /single-live-streams} : Create a new singleLiveStream.
     *
     * @param singleLiveStreamDTO the singleLiveStreamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleLiveStreamDTO, or with status {@code 400 (Bad Request)} if the singleLiveStream has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SingleLiveStreamDTO>> createSingleLiveStream(@Valid @RequestBody SingleLiveStreamDTO singleLiveStreamDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleLiveStream : {}", singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleLiveStream cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singleLiveStreamService
            .save(singleLiveStreamDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-live-streams/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-live-streams/:id} : Updates an existing singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to save.
     * @param singleLiveStreamDTO the singleLiveStreamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleLiveStreamDTO,
     * or with status {@code 400 (Bad Request)} if the singleLiveStreamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleLiveStreamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SingleLiveStreamDTO>> updateSingleLiveStream(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleLiveStreamDTO singleLiveStreamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleLiveStream : {}, {}", id, singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleLiveStreamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleLiveStreamRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singleLiveStreamService
                    .update(singleLiveStreamDTO)
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
     * {@code PATCH  /single-live-streams/:id} : Partial updates given fields of an existing singleLiveStream, field will ignore if it is null
     *
     * @param id the id of the singleLiveStreamDTO to save.
     * @param singleLiveStreamDTO the singleLiveStreamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleLiveStreamDTO,
     * or with status {@code 400 (Bad Request)} if the singleLiveStreamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleLiveStreamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleLiveStreamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SingleLiveStreamDTO>> partialUpdateSingleLiveStream(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleLiveStreamDTO singleLiveStreamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleLiveStream partially : {}, {}", id, singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleLiveStreamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleLiveStreamRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SingleLiveStreamDTO> result = singleLiveStreamService.partialUpdate(singleLiveStreamDTO);

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
     * {@code GET  /single-live-streams} : get all the singleLiveStreams.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleLiveStreams in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SingleLiveStreamDTO>>> getAllSingleLiveStreams(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SingleLiveStreams");
        return singleLiveStreamService
            .countAll()
            .zipWith(singleLiveStreamService.findAll(pageable).collectList())
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
     * {@code GET  /single-live-streams/:id} : get the "id" singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleLiveStreamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SingleLiveStreamDTO>> getSingleLiveStream(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleLiveStream : {}", id);
        Mono<SingleLiveStreamDTO> singleLiveStreamDTO = singleLiveStreamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleLiveStreamDTO);
    }

    /**
     * {@code DELETE  /single-live-streams/:id} : delete the "id" singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSingleLiveStream(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleLiveStream : {}", id);
        return singleLiveStreamService
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
     * {@code SEARCH  /single-live-streams/_search?query=:query} : search for the singleLiveStream corresponding
     * to the query.
     *
     * @param query the query of the singleLiveStream search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SingleLiveStreamDTO>>> searchSingleLiveStreams(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SingleLiveStreams for query {}", query);
        return singleLiveStreamService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(singleLiveStreamService.search(query, pageable)));
    }
}
