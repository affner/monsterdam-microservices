package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.HelpCategoryRepository;
import com.fanflip.admin.service.HelpCategoryService;
import com.fanflip.admin.service.dto.HelpCategoryDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.HelpCategory}.
 */
@RestController
@RequestMapping("/api/help-categories")
public class HelpCategoryResource {

    private final Logger log = LoggerFactory.getLogger(HelpCategoryResource.class);

    private static final String ENTITY_NAME = "helpCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpCategoryService helpCategoryService;

    private final HelpCategoryRepository helpCategoryRepository;

    public HelpCategoryResource(HelpCategoryService helpCategoryService, HelpCategoryRepository helpCategoryRepository) {
        this.helpCategoryService = helpCategoryService;
        this.helpCategoryRepository = helpCategoryRepository;
    }

    /**
     * {@code POST  /help-categories} : Create a new helpCategory.
     *
     * @param helpCategoryDTO the helpCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpCategoryDTO, or with status {@code 400 (Bad Request)} if the helpCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HelpCategoryDTO>> createHelpCategory(@Valid @RequestBody HelpCategoryDTO helpCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpCategory : {}", helpCategoryDTO);
        if (helpCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpCategoryService
            .save(helpCategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-categories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-categories/:id} : Updates an existing helpCategory.
     *
     * @param id the id of the helpCategoryDTO to save.
     * @param helpCategoryDTO the helpCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HelpCategoryDTO>> updateHelpCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpCategoryDTO helpCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpCategory : {}, {}", id, helpCategoryDTO);
        if (helpCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpCategoryService
                    .update(helpCategoryDTO)
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
     * {@code PATCH  /help-categories/:id} : Partial updates given fields of an existing helpCategory, field will ignore if it is null
     *
     * @param id the id of the helpCategoryDTO to save.
     * @param helpCategoryDTO the helpCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpCategoryDTO>> partialUpdateHelpCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpCategoryDTO helpCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpCategory partially : {}, {}", id, helpCategoryDTO);
        if (helpCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpCategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpCategoryDTO> result = helpCategoryService.partialUpdate(helpCategoryDTO);

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
     * {@code GET  /help-categories} : get all the helpCategories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpCategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HelpCategoryDTO>>> getAllHelpCategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HelpCategories");
        return helpCategoryService
            .countAll()
            .zipWith(helpCategoryService.findAll(pageable).collectList())
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
     * {@code GET  /help-categories/:id} : get the "id" helpCategory.
     *
     * @param id the id of the helpCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HelpCategoryDTO>> getHelpCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpCategory : {}", id);
        Mono<HelpCategoryDTO> helpCategoryDTO = helpCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpCategoryDTO);
    }

    /**
     * {@code DELETE  /help-categories/:id} : delete the "id" helpCategory.
     *
     * @param id the id of the helpCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpCategory : {}", id);
        return helpCategoryService
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
     * {@code SEARCH  /help-categories/_search?query=:query} : search for the helpCategory corresponding
     * to the query.
     *
     * @param query the query of the helpCategory search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HelpCategoryDTO>>> searchHelpCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HelpCategories for query {}", query);
        return helpCategoryService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(helpCategoryService.search(query, pageable)));
    }
}
