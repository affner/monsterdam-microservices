package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.SingleDocumentRepository;
import com.monsterdam.admin.service.SingleDocumentService;
import com.monsterdam.admin.service.dto.SingleDocumentDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.SingleDocument}.
 */
@RestController
@RequestMapping("/api/single-documents")
public class SingleDocumentResource {

    private final Logger log = LoggerFactory.getLogger(SingleDocumentResource.class);

    private static final String ENTITY_NAME = "singleDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleDocumentService singleDocumentService;

    private final SingleDocumentRepository singleDocumentRepository;

    public SingleDocumentResource(SingleDocumentService singleDocumentService, SingleDocumentRepository singleDocumentRepository) {
        this.singleDocumentService = singleDocumentService;
        this.singleDocumentRepository = singleDocumentRepository;
    }

    /**
     * {@code POST  /single-documents} : Create a new singleDocument.
     *
     * @param singleDocumentDTO the singleDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleDocumentDTO, or with status {@code 400 (Bad Request)} if the singleDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SingleDocumentDTO>> createSingleDocument(@Valid @RequestBody SingleDocumentDTO singleDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleDocument : {}", singleDocumentDTO);
        if (singleDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singleDocumentService
            .save(singleDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-documents/:id} : Updates an existing singleDocument.
     *
     * @param id the id of the singleDocumentDTO to save.
     * @param singleDocumentDTO the singleDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the singleDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SingleDocumentDTO>> updateSingleDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleDocumentDTO singleDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleDocument : {}, {}", id, singleDocumentDTO);
        if (singleDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singleDocumentService
                    .update(singleDocumentDTO)
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
     * {@code PATCH  /single-documents/:id} : Partial updates given fields of an existing singleDocument, field will ignore if it is null
     *
     * @param id the id of the singleDocumentDTO to save.
     * @param singleDocumentDTO the singleDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the singleDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SingleDocumentDTO>> partialUpdateSingleDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleDocumentDTO singleDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleDocument partially : {}, {}", id, singleDocumentDTO);
        if (singleDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SingleDocumentDTO> result = singleDocumentService.partialUpdate(singleDocumentDTO);

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
     * {@code GET  /single-documents} : get all the singleDocuments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleDocuments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SingleDocumentDTO>>> getAllSingleDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SingleDocuments");
        return singleDocumentService
            .countAll()
            .zipWith(singleDocumentService.findAll(pageable).collectList())
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
     * {@code GET  /single-documents/:id} : get the "id" singleDocument.
     *
     * @param id the id of the singleDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SingleDocumentDTO>> getSingleDocument(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleDocument : {}", id);
        Mono<SingleDocumentDTO> singleDocumentDTO = singleDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleDocumentDTO);
    }

    /**
     * {@code DELETE  /single-documents/:id} : delete the "id" singleDocument.
     *
     * @param id the id of the singleDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSingleDocument(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleDocument : {}", id);
        return singleDocumentService
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
     * {@code SEARCH  /single-documents/_search?query=:query} : search for the singleDocument corresponding
     * to the query.
     *
     * @param query the query of the singleDocument search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SingleDocumentDTO>>> searchSingleDocuments(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SingleDocuments for query {}", query);
        return singleDocumentService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(singleDocumentService.search(query, pageable)));
    }
}
