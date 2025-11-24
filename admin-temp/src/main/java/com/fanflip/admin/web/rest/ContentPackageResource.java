package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.ContentPackageRepository;
import com.monsterdam.admin.service.ContentPackageService;
import com.monsterdam.admin.service.dto.ContentPackageDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.ContentPackage}.
 */
@RestController
@RequestMapping("/api/content-packages")
public class ContentPackageResource {

    private final Logger log = LoggerFactory.getLogger(ContentPackageResource.class);

    private static final String ENTITY_NAME = "contentPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentPackageService contentPackageService;

    private final ContentPackageRepository contentPackageRepository;

    public ContentPackageResource(ContentPackageService contentPackageService, ContentPackageRepository contentPackageRepository) {
        this.contentPackageService = contentPackageService;
        this.contentPackageRepository = contentPackageRepository;
    }

    /**
     * {@code POST  /content-packages} : Create a new contentPackage.
     *
     * @param contentPackageDTO the contentPackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentPackageDTO, or with status {@code 400 (Bad Request)} if the contentPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ContentPackageDTO>> createContentPackage(@Valid @RequestBody ContentPackageDTO contentPackageDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContentPackage : {}", contentPackageDTO);
        if (contentPackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return contentPackageService
            .save(contentPackageDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/content-packages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /content-packages/:id} : Updates an existing contentPackage.
     *
     * @param id the id of the contentPackageDTO to save.
     * @param contentPackageDTO the contentPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPackageDTO,
     * or with status {@code 400 (Bad Request)} if the contentPackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ContentPackageDTO>> updateContentPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentPackageDTO contentPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContentPackage : {}, {}", id, contentPackageDTO);
        if (contentPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contentPackageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return contentPackageService
                    .update(contentPackageDTO)
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
     * {@code PATCH  /content-packages/:id} : Partial updates given fields of an existing contentPackage, field will ignore if it is null
     *
     * @param id the id of the contentPackageDTO to save.
     * @param contentPackageDTO the contentPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPackageDTO,
     * or with status {@code 400 (Bad Request)} if the contentPackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contentPackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ContentPackageDTO>> partialUpdateContentPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentPackageDTO contentPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentPackage partially : {}, {}", id, contentPackageDTO);
        if (contentPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contentPackageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ContentPackageDTO> result = contentPackageService.partialUpdate(contentPackageDTO);

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
     * {@code GET  /content-packages} : get all the contentPackages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentPackages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ContentPackageDTO>>> getAllContentPackages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("message-is-null".equals(filter)) {
            log.debug("REST request to get all ContentPackages where message is null");
            return contentPackageService.findAllWhereMessageIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("post-is-null".equals(filter)) {
            log.debug("REST request to get all ContentPackages where post is null");
            return contentPackageService.findAllWherePostIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of ContentPackages");
        return contentPackageService
            .countAll()
            .zipWith(contentPackageService.findAll(pageable).collectList())
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
     * {@code GET  /content-packages/:id} : get the "id" contentPackage.
     *
     * @param id the id of the contentPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContentPackageDTO>> getContentPackage(@PathVariable("id") Long id) {
        log.debug("REST request to get ContentPackage : {}", id);
        Mono<ContentPackageDTO> contentPackageDTO = contentPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentPackageDTO);
    }

    /**
     * {@code DELETE  /content-packages/:id} : delete the "id" contentPackage.
     *
     * @param id the id of the contentPackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteContentPackage(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContentPackage : {}", id);
        return contentPackageService
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
     * {@code SEARCH  /content-packages/_search?query=:query} : search for the contentPackage corresponding
     * to the query.
     *
     * @param query the query of the contentPackage search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<ContentPackageDTO>>> searchContentPackages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of ContentPackages for query {}", query);
        return contentPackageService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(contentPackageService.search(query, pageable)));
    }
}
