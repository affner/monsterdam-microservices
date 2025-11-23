package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.GlobalEventRepository;
import com.fanflip.admin.service.GlobalEventService;
import com.fanflip.admin.service.dto.GlobalEventDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.GlobalEvent}.
 */
@RestController
@RequestMapping("/api/global-events")
public class GlobalEventResource {

    private final Logger log = LoggerFactory.getLogger(GlobalEventResource.class);

    private static final String ENTITY_NAME = "globalEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalEventService globalEventService;

    private final GlobalEventRepository globalEventRepository;

    public GlobalEventResource(GlobalEventService globalEventService, GlobalEventRepository globalEventRepository) {
        this.globalEventService = globalEventService;
        this.globalEventRepository = globalEventRepository;
    }

    /**
     * {@code POST  /global-events} : Create a new globalEvent.
     *
     * @param globalEventDTO the globalEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalEventDTO, or with status {@code 400 (Bad Request)} if the globalEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<GlobalEventDTO>> createGlobalEvent(@Valid @RequestBody GlobalEventDTO globalEventDTO)
        throws URISyntaxException {
        log.debug("REST request to save GlobalEvent : {}", globalEventDTO);
        if (globalEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return globalEventService
            .save(globalEventDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/global-events/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /global-events/:id} : Updates an existing globalEvent.
     *
     * @param id the id of the globalEventDTO to save.
     * @param globalEventDTO the globalEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalEventDTO,
     * or with status {@code 400 (Bad Request)} if the globalEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GlobalEventDTO>> updateGlobalEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GlobalEventDTO globalEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GlobalEvent : {}, {}", id, globalEventDTO);
        if (globalEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return globalEventRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return globalEventService
                    .update(globalEventDTO)
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
     * {@code PATCH  /global-events/:id} : Partial updates given fields of an existing globalEvent, field will ignore if it is null
     *
     * @param id the id of the globalEventDTO to save.
     * @param globalEventDTO the globalEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalEventDTO,
     * or with status {@code 400 (Bad Request)} if the globalEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the globalEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the globalEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<GlobalEventDTO>> partialUpdateGlobalEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GlobalEventDTO globalEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlobalEvent partially : {}, {}", id, globalEventDTO);
        if (globalEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return globalEventRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<GlobalEventDTO> result = globalEventService.partialUpdate(globalEventDTO);

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
     * {@code GET  /global-events} : get all the globalEvents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalEvents in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<GlobalEventDTO>>> getAllGlobalEvents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of GlobalEvents");
        return globalEventService
            .countAll()
            .zipWith(globalEventService.findAll(pageable).collectList())
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
     * {@code GET  /global-events/:id} : get the "id" globalEvent.
     *
     * @param id the id of the globalEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GlobalEventDTO>> getGlobalEvent(@PathVariable("id") Long id) {
        log.debug("REST request to get GlobalEvent : {}", id);
        Mono<GlobalEventDTO> globalEventDTO = globalEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalEventDTO);
    }

    /**
     * {@code DELETE  /global-events/:id} : delete the "id" globalEvent.
     *
     * @param id the id of the globalEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGlobalEvent(@PathVariable("id") Long id) {
        log.debug("REST request to delete GlobalEvent : {}", id);
        return globalEventService
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
     * {@code SEARCH  /global-events/_search?query=:query} : search for the globalEvent corresponding
     * to the query.
     *
     * @param query the query of the globalEvent search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<GlobalEventDTO>>> searchGlobalEvents(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of GlobalEvents for query {}", query);
        return globalEventService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(globalEventService.search(query, pageable)));
    }
}
