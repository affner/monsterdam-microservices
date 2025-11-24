package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.PurchasedTipRepository;
import com.monsterdam.admin.service.PurchasedTipService;
import com.monsterdam.admin.service.dto.PurchasedTipDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.PurchasedTip}.
 */
@RestController
@RequestMapping("/api/purchased-tips")
public class PurchasedTipResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedTipResource.class);

    private static final String ENTITY_NAME = "purchasedTip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedTipService purchasedTipService;

    private final PurchasedTipRepository purchasedTipRepository;

    public PurchasedTipResource(PurchasedTipService purchasedTipService, PurchasedTipRepository purchasedTipRepository) {
        this.purchasedTipService = purchasedTipService;
        this.purchasedTipRepository = purchasedTipRepository;
    }

    /**
     * {@code POST  /purchased-tips} : Create a new purchasedTip.
     *
     * @param purchasedTipDTO the purchasedTipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedTipDTO, or with status {@code 400 (Bad Request)} if the purchasedTip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PurchasedTipDTO>> createPurchasedTip(@Valid @RequestBody PurchasedTipDTO purchasedTipDTO)
        throws URISyntaxException {
        log.debug("REST request to save PurchasedTip : {}", purchasedTipDTO);
        if (purchasedTipDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedTip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return purchasedTipService
            .save(purchasedTipDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/purchased-tips/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /purchased-tips/:id} : Updates an existing purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to save.
     * @param purchasedTipDTO the purchasedTipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedTipDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedTipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedTipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PurchasedTipDTO>> updatePurchasedTip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedTipDTO purchasedTipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedTip : {}, {}", id, purchasedTipDTO);
        if (purchasedTipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedTipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedTipRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return purchasedTipService
                    .update(purchasedTipDTO)
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
     * {@code PATCH  /purchased-tips/:id} : Partial updates given fields of an existing purchasedTip, field will ignore if it is null
     *
     * @param id the id of the purchasedTipDTO to save.
     * @param purchasedTipDTO the purchasedTipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedTipDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedTipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedTipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedTipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PurchasedTipDTO>> partialUpdatePurchasedTip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedTipDTO purchasedTipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchasedTip partially : {}, {}", id, purchasedTipDTO);
        if (purchasedTipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedTipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedTipRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PurchasedTipDTO> result = purchasedTipService.partialUpdate(purchasedTipDTO);

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
     * {@code GET  /purchased-tips} : get all the purchasedTips.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedTips in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PurchasedTipDTO>>> getAllPurchasedTips(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchasedTips");
        return purchasedTipService
            .countAll()
            .zipWith(purchasedTipService.findAll(pageable).collectList())
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
     * {@code GET  /purchased-tips/:id} : get the "id" purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedTipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PurchasedTipDTO>> getPurchasedTip(@PathVariable("id") Long id) {
        log.debug("REST request to get PurchasedTip : {}", id);
        Mono<PurchasedTipDTO> purchasedTipDTO = purchasedTipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedTipDTO);
    }

    /**
     * {@code DELETE  /purchased-tips/:id} : delete the "id" purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePurchasedTip(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedTip : {}", id);
        return purchasedTipService
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
     * {@code SEARCH  /purchased-tips/_search?query=:query} : search for the purchasedTip corresponding
     * to the query.
     *
     * @param query the query of the purchasedTip search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PurchasedTipDTO>>> searchPurchasedTips(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PurchasedTips for query {}", query);
        return purchasedTipService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(purchasedTipService.search(query, pageable)));
    }
}
