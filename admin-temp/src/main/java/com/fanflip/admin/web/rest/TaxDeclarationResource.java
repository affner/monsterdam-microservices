package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.TaxDeclarationRepository;
import com.fanflip.admin.service.TaxDeclarationService;
import com.fanflip.admin.service.dto.TaxDeclarationDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.TaxDeclaration}.
 */
@RestController
@RequestMapping("/api/tax-declarations")
public class TaxDeclarationResource {

    private final Logger log = LoggerFactory.getLogger(TaxDeclarationResource.class);

    private static final String ENTITY_NAME = "taxDeclaration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxDeclarationService taxDeclarationService;

    private final TaxDeclarationRepository taxDeclarationRepository;

    public TaxDeclarationResource(TaxDeclarationService taxDeclarationService, TaxDeclarationRepository taxDeclarationRepository) {
        this.taxDeclarationService = taxDeclarationService;
        this.taxDeclarationRepository = taxDeclarationRepository;
    }

    /**
     * {@code POST  /tax-declarations} : Create a new taxDeclaration.
     *
     * @param taxDeclarationDTO the taxDeclarationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxDeclarationDTO, or with status {@code 400 (Bad Request)} if the taxDeclaration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TaxDeclarationDTO>> createTaxDeclaration(@Valid @RequestBody TaxDeclarationDTO taxDeclarationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TaxDeclaration : {}", taxDeclarationDTO);
        if (taxDeclarationDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxDeclaration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return taxDeclarationService
            .save(taxDeclarationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tax-declarations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tax-declarations/:id} : Updates an existing taxDeclaration.
     *
     * @param id the id of the taxDeclarationDTO to save.
     * @param taxDeclarationDTO the taxDeclarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxDeclarationDTO,
     * or with status {@code 400 (Bad Request)} if the taxDeclarationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxDeclarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaxDeclarationDTO>> updateTaxDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxDeclarationDTO taxDeclarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxDeclaration : {}, {}", id, taxDeclarationDTO);
        if (taxDeclarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxDeclarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxDeclarationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return taxDeclarationService
                    .update(taxDeclarationDTO)
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
     * {@code PATCH  /tax-declarations/:id} : Partial updates given fields of an existing taxDeclaration, field will ignore if it is null
     *
     * @param id the id of the taxDeclarationDTO to save.
     * @param taxDeclarationDTO the taxDeclarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxDeclarationDTO,
     * or with status {@code 400 (Bad Request)} if the taxDeclarationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxDeclarationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxDeclarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TaxDeclarationDTO>> partialUpdateTaxDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxDeclarationDTO taxDeclarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxDeclaration partially : {}, {}", id, taxDeclarationDTO);
        if (taxDeclarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxDeclarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxDeclarationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TaxDeclarationDTO> result = taxDeclarationService.partialUpdate(taxDeclarationDTO);

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
     * {@code GET  /tax-declarations} : get all the taxDeclarations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxDeclarations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TaxDeclarationDTO>>> getAllTaxDeclarations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TaxDeclarations");
        return taxDeclarationService
            .countAll()
            .zipWith(taxDeclarationService.findAll(pageable).collectList())
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
     * {@code GET  /tax-declarations/:id} : get the "id" taxDeclaration.
     *
     * @param id the id of the taxDeclarationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxDeclarationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaxDeclarationDTO>> getTaxDeclaration(@PathVariable("id") Long id) {
        log.debug("REST request to get TaxDeclaration : {}", id);
        Mono<TaxDeclarationDTO> taxDeclarationDTO = taxDeclarationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxDeclarationDTO);
    }

    /**
     * {@code DELETE  /tax-declarations/:id} : delete the "id" taxDeclaration.
     *
     * @param id the id of the taxDeclarationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaxDeclaration(@PathVariable("id") Long id) {
        log.debug("REST request to delete TaxDeclaration : {}", id);
        return taxDeclarationService
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
     * {@code SEARCH  /tax-declarations/_search?query=:query} : search for the taxDeclaration corresponding
     * to the query.
     *
     * @param query the query of the taxDeclaration search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<TaxDeclarationDTO>>> searchTaxDeclarations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of TaxDeclarations for query {}", query);
        return taxDeclarationService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(taxDeclarationService.search(query, pageable)));
    }
}
