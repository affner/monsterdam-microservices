package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PurchasedContentRepository;
import com.fanflip.admin.service.PurchasedContentService;
import com.fanflip.admin.service.dto.PurchasedContentDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PurchasedContent}.
 */
@RestController
@RequestMapping("/api/purchased-contents")
public class PurchasedContentResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedContentResource.class);

    private static final String ENTITY_NAME = "purchasedContent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedContentService purchasedContentService;

    private final PurchasedContentRepository purchasedContentRepository;

    public PurchasedContentResource(
        PurchasedContentService purchasedContentService,
        PurchasedContentRepository purchasedContentRepository
    ) {
        this.purchasedContentService = purchasedContentService;
        this.purchasedContentRepository = purchasedContentRepository;
    }

    /**
     * {@code POST  /purchased-contents} : Create a new purchasedContent.
     *
     * @param purchasedContentDTO the purchasedContentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedContentDTO, or with status {@code 400 (Bad Request)} if the purchasedContent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PurchasedContentDTO>> createPurchasedContent(@Valid @RequestBody PurchasedContentDTO purchasedContentDTO)
        throws URISyntaxException {
        log.debug("REST request to save PurchasedContent : {}", purchasedContentDTO);
        if (purchasedContentDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedContent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return purchasedContentService
            .save(purchasedContentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/purchased-contents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /purchased-contents/:id} : Updates an existing purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to save.
     * @param purchasedContentDTO the purchasedContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedContentDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedContentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PurchasedContentDTO>> updatePurchasedContent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedContentDTO purchasedContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedContent : {}, {}", id, purchasedContentDTO);
        if (purchasedContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return purchasedContentService
                    .update(purchasedContentDTO)
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
     * {@code PATCH  /purchased-contents/:id} : Partial updates given fields of an existing purchasedContent, field will ignore if it is null
     *
     * @param id the id of the purchasedContentDTO to save.
     * @param purchasedContentDTO the purchasedContentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedContentDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedContentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedContentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedContentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PurchasedContentDTO>> partialUpdatePurchasedContent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedContentDTO purchasedContentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchasedContent partially : {}, {}", id, purchasedContentDTO);
        if (purchasedContentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedContentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedContentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PurchasedContentDTO> result = purchasedContentService.partialUpdate(purchasedContentDTO);

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
     * {@code GET  /purchased-contents} : get all the purchasedContents.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedContents in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PurchasedContentDTO>>> getAllPurchasedContents(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchasedContents");
        return purchasedContentService
            .countAll()
            .zipWith(purchasedContentService.findAll(pageable).collectList())
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
     * {@code GET  /purchased-contents/:id} : get the "id" purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedContentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PurchasedContentDTO>> getPurchasedContent(@PathVariable("id") Long id) {
        log.debug("REST request to get PurchasedContent : {}", id);
        Mono<PurchasedContentDTO> purchasedContentDTO = purchasedContentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedContentDTO);
    }

    /**
     * {@code DELETE  /purchased-contents/:id} : delete the "id" purchasedContent.
     *
     * @param id the id of the purchasedContentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePurchasedContent(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedContent : {}", id);
        return purchasedContentService
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
     * {@code SEARCH  /purchased-contents/_search?query=:query} : search for the purchasedContent corresponding
     * to the query.
     *
     * @param query the query of the purchasedContent search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PurchasedContentDTO>>> searchPurchasedContents(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PurchasedContents for query {}", query);
        return purchasedContentService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(purchasedContentService.search(query, pageable)));
    }
}
