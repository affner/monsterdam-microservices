package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.HelpSubcategoryRepository;
import com.fanflip.admin.service.HelpSubcategoryService;
import com.fanflip.admin.service.dto.HelpSubcategoryDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.HelpSubcategory}.
 */
@RestController
@RequestMapping("/api/help-subcategories")
public class HelpSubcategoryResource {

    private final Logger log = LoggerFactory.getLogger(HelpSubcategoryResource.class);

    private static final String ENTITY_NAME = "helpSubcategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpSubcategoryService helpSubcategoryService;

    private final HelpSubcategoryRepository helpSubcategoryRepository;

    public HelpSubcategoryResource(HelpSubcategoryService helpSubcategoryService, HelpSubcategoryRepository helpSubcategoryRepository) {
        this.helpSubcategoryService = helpSubcategoryService;
        this.helpSubcategoryRepository = helpSubcategoryRepository;
    }

    /**
     * {@code POST  /help-subcategories} : Create a new helpSubcategory.
     *
     * @param helpSubcategoryDTO the helpSubcategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpSubcategoryDTO, or with status {@code 400 (Bad Request)} if the helpSubcategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HelpSubcategoryDTO>> createHelpSubcategory(@Valid @RequestBody HelpSubcategoryDTO helpSubcategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpSubcategory : {}", helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpSubcategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpSubcategoryService
            .save(helpSubcategoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-subcategories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-subcategories/:id} : Updates an existing helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to save.
     * @param helpSubcategoryDTO the helpSubcategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpSubcategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpSubcategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpSubcategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HelpSubcategoryDTO>> updateHelpSubcategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpSubcategoryDTO helpSubcategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpSubcategory : {}, {}", id, helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpSubcategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpSubcategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpSubcategoryService
                    .update(helpSubcategoryDTO)
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
     * {@code PATCH  /help-subcategories/:id} : Partial updates given fields of an existing helpSubcategory, field will ignore if it is null
     *
     * @param id the id of the helpSubcategoryDTO to save.
     * @param helpSubcategoryDTO the helpSubcategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpSubcategoryDTO,
     * or with status {@code 400 (Bad Request)} if the helpSubcategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpSubcategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpSubcategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpSubcategoryDTO>> partialUpdateHelpSubcategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpSubcategoryDTO helpSubcategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpSubcategory partially : {}, {}", id, helpSubcategoryDTO);
        if (helpSubcategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpSubcategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpSubcategoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpSubcategoryDTO> result = helpSubcategoryService.partialUpdate(helpSubcategoryDTO);

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
     * {@code GET  /help-subcategories} : get all the helpSubcategories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpSubcategories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HelpSubcategoryDTO>>> getAllHelpSubcategories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HelpSubcategories");
        return helpSubcategoryService
            .countAll()
            .zipWith(helpSubcategoryService.findAll(pageable).collectList())
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
     * {@code GET  /help-subcategories/:id} : get the "id" helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpSubcategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HelpSubcategoryDTO>> getHelpSubcategory(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpSubcategory : {}", id);
        Mono<HelpSubcategoryDTO> helpSubcategoryDTO = helpSubcategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpSubcategoryDTO);
    }

    /**
     * {@code DELETE  /help-subcategories/:id} : delete the "id" helpSubcategory.
     *
     * @param id the id of the helpSubcategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpSubcategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpSubcategory : {}", id);
        return helpSubcategoryService
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
     * {@code SEARCH  /help-subcategories/_search?query=:query} : search for the helpSubcategory corresponding
     * to the query.
     *
     * @param query the query of the helpSubcategory search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HelpSubcategoryDTO>>> searchHelpSubcategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HelpSubcategories for query {}", query);
        return helpSubcategoryService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(helpSubcategoryService.search(query, pageable)));
    }
}
