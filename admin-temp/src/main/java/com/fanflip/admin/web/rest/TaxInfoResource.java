package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.TaxInfoRepository;
import com.fanflip.admin.service.TaxInfoService;
import com.fanflip.admin.service.dto.TaxInfoDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.TaxInfo}.
 */
@RestController
@RequestMapping("/api/tax-infos")
public class TaxInfoResource {

    private final Logger log = LoggerFactory.getLogger(TaxInfoResource.class);

    private static final String ENTITY_NAME = "taxInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxInfoService taxInfoService;

    private final TaxInfoRepository taxInfoRepository;

    public TaxInfoResource(TaxInfoService taxInfoService, TaxInfoRepository taxInfoRepository) {
        this.taxInfoService = taxInfoService;
        this.taxInfoRepository = taxInfoRepository;
    }

    /**
     * {@code POST  /tax-infos} : Create a new taxInfo.
     *
     * @param taxInfoDTO the taxInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxInfoDTO, or with status {@code 400 (Bad Request)} if the taxInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<TaxInfoDTO>> createTaxInfo(@Valid @RequestBody TaxInfoDTO taxInfoDTO) throws URISyntaxException {
        log.debug("REST request to save TaxInfo : {}", taxInfoDTO);
        if (taxInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return taxInfoService
            .save(taxInfoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/tax-infos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /tax-infos/:id} : Updates an existing taxInfo.
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaxInfoDTO>> updateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxInfo : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxInfoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return taxInfoService
                    .update(taxInfoDTO)
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
     * {@code PATCH  /tax-infos/:id} : Partial updates given fields of an existing taxInfo, field will ignore if it is null
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<TaxInfoDTO>> partialUpdateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxInfo partially : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return taxInfoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<TaxInfoDTO> result = taxInfoService.partialUpdate(taxInfoDTO);

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
     * {@code GET  /tax-infos} : get all the taxInfos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxInfos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<TaxInfoDTO>>> getAllTaxInfos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TaxInfos");
        return taxInfoService
            .countAll()
            .zipWith(taxInfoService.findAll(pageable).collectList())
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
     * {@code GET  /tax-infos/:id} : get the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaxInfoDTO>> getTaxInfo(@PathVariable("id") Long id) {
        log.debug("REST request to get TaxInfo : {}", id);
        Mono<TaxInfoDTO> taxInfoDTO = taxInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxInfoDTO);
    }

    /**
     * {@code DELETE  /tax-infos/:id} : delete the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaxInfo(@PathVariable("id") Long id) {
        log.debug("REST request to delete TaxInfo : {}", id);
        return taxInfoService
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
     * {@code SEARCH  /tax-infos/_search?query=:query} : search for the taxInfo corresponding
     * to the query.
     *
     * @param query the query of the taxInfo search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<TaxInfoDTO>>> searchTaxInfos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of TaxInfos for query {}", query);
        return taxInfoService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(taxInfoService.search(query, pageable)));
    }
}
