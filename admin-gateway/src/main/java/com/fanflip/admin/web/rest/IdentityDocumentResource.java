package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.IdentityDocumentRepository;
import com.fanflip.admin.service.IdentityDocumentService;
import com.fanflip.admin.service.dto.IdentityDocumentDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
import com.fanflip.admin.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.fanflip.admin.domain.IdentityDocument}.
 */
@RestController
@RequestMapping("/api/identity-documents")
public class IdentityDocumentResource {

    private final Logger log = LoggerFactory.getLogger(IdentityDocumentResource.class);

    private static final String ENTITY_NAME = "identityDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentityDocumentService identityDocumentService;

    private final IdentityDocumentRepository identityDocumentRepository;

    public IdentityDocumentResource(
        IdentityDocumentService identityDocumentService,
        IdentityDocumentRepository identityDocumentRepository
    ) {
        this.identityDocumentService = identityDocumentService;
        this.identityDocumentRepository = identityDocumentRepository;
    }

    /**
     * {@code POST  /identity-documents} : Create a new identityDocument.
     *
     * @param identityDocumentDTO the identityDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identityDocumentDTO, or with status {@code 400 (Bad Request)} if the identityDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<IdentityDocumentDTO>> createIdentityDocument(@Valid @RequestBody IdentityDocumentDTO identityDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save IdentityDocument : {}", identityDocumentDTO);
        if (identityDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new identityDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return identityDocumentService
            .save(identityDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/identity-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /identity-documents/:id} : Updates an existing identityDocument.
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<IdentityDocumentDTO>> updateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IdentityDocument : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identityDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return identityDocumentService
                    .update(identityDocumentDTO)
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
     * {@code PATCH  /identity-documents/:id} : Partial updates given fields of an existing identityDocument, field will ignore if it is null
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the identityDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IdentityDocumentDTO>> partialUpdateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IdentityDocument partially : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identityDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IdentityDocumentDTO> result = identityDocumentService.partialUpdate(identityDocumentDTO);

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
     * {@code GET  /identity-documents} : get all the identityDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identityDocuments in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<IdentityDocumentDTO>> getAllIdentityDocuments() {
        log.debug("REST request to get all IdentityDocuments");
        return identityDocumentService.findAll().collectList();
    }

    /**
     * {@code GET  /identity-documents} : get all the identityDocuments as a stream.
     * @return the {@link Flux} of identityDocuments.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IdentityDocumentDTO> getAllIdentityDocumentsAsStream() {
        log.debug("REST request to get all IdentityDocuments as a stream");
        return identityDocumentService.findAll();
    }

    /**
     * {@code GET  /identity-documents/:id} : get the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identityDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<IdentityDocumentDTO>> getIdentityDocument(@PathVariable("id") Long id) {
        log.debug("REST request to get IdentityDocument : {}", id);
        Mono<IdentityDocumentDTO> identityDocumentDTO = identityDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identityDocumentDTO);
    }

    /**
     * {@code DELETE  /identity-documents/:id} : delete the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteIdentityDocument(@PathVariable("id") Long id) {
        log.debug("REST request to delete IdentityDocument : {}", id);
        return identityDocumentService
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
     * {@code SEARCH  /identity-documents/_search?query=:query} : search for the identityDocument corresponding
     * to the query.
     *
     * @param query the query of the identityDocument search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<IdentityDocumentDTO>> searchIdentityDocuments(@RequestParam("query") String query) {
        log.debug("REST request to search IdentityDocuments for query {}", query);
        try {
            return identityDocumentService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
