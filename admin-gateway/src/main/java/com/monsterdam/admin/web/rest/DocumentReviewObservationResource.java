package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.DocumentReviewObservationRepository;
import com.monsterdam.admin.service.DocumentReviewObservationService;
import com.monsterdam.admin.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.admin.web.rest.errors.BadRequestAlertException;
import com.monsterdam.admin.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.DocumentReviewObservation}.
 */
@RestController
@RequestMapping("/api/document-review-observations")
public class DocumentReviewObservationResource {

    private final Logger log = LoggerFactory.getLogger(DocumentReviewObservationResource.class);

    private static final String ENTITY_NAME = "documentReviewObservation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentReviewObservationService documentReviewObservationService;

    private final DocumentReviewObservationRepository documentReviewObservationRepository;

    public DocumentReviewObservationResource(
        DocumentReviewObservationService documentReviewObservationService,
        DocumentReviewObservationRepository documentReviewObservationRepository
    ) {
        this.documentReviewObservationService = documentReviewObservationService;
        this.documentReviewObservationRepository = documentReviewObservationRepository;
    }

    /**
     * {@code POST  /document-review-observations} : Create a new documentReviewObservation.
     *
     * @param documentReviewObservationDTO the documentReviewObservationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentReviewObservationDTO, or with status {@code 400 (Bad Request)} if the documentReviewObservation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DocumentReviewObservationDTO>> createDocumentReviewObservation(
        @Valid @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DocumentReviewObservation : {}", documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentReviewObservation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return documentReviewObservationService
            .save(documentReviewObservationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/document-review-observations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /document-review-observations/:id} : Updates an existing documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to save.
     * @param documentReviewObservationDTO the documentReviewObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReviewObservationDTO,
     * or with status {@code 400 (Bad Request)} if the documentReviewObservationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentReviewObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DocumentReviewObservationDTO>> updateDocumentReviewObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DocumentReviewObservation : {}, {}", id, documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReviewObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return documentReviewObservationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return documentReviewObservationService
                    .update(documentReviewObservationDTO)
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
     * {@code PATCH  /document-review-observations/:id} : Partial updates given fields of an existing documentReviewObservation, field will ignore if it is null
     *
     * @param id the id of the documentReviewObservationDTO to save.
     * @param documentReviewObservationDTO the documentReviewObservationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentReviewObservationDTO,
     * or with status {@code 400 (Bad Request)} if the documentReviewObservationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentReviewObservationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentReviewObservationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DocumentReviewObservationDTO>> partialUpdateDocumentReviewObservation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentReviewObservationDTO documentReviewObservationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DocumentReviewObservation partially : {}, {}", id, documentReviewObservationDTO);
        if (documentReviewObservationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentReviewObservationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return documentReviewObservationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DocumentReviewObservationDTO> result = documentReviewObservationService.partialUpdate(documentReviewObservationDTO);

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
     * {@code GET  /document-review-observations} : get all the documentReviewObservations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentReviewObservations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<DocumentReviewObservationDTO>> getAllDocumentReviewObservations() {
        log.debug("REST request to get all DocumentReviewObservations");
        return documentReviewObservationService.findAll().collectList();
    }

    /**
     * {@code GET  /document-review-observations} : get all the documentReviewObservations as a stream.
     * @return the {@link Flux} of documentReviewObservations.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DocumentReviewObservationDTO> getAllDocumentReviewObservationsAsStream() {
        log.debug("REST request to get all DocumentReviewObservations as a stream");
        return documentReviewObservationService.findAll();
    }

    /**
     * {@code GET  /document-review-observations/:id} : get the "id" documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentReviewObservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DocumentReviewObservationDTO>> getDocumentReviewObservation(@PathVariable("id") Long id) {
        log.debug("REST request to get DocumentReviewObservation : {}", id);
        Mono<DocumentReviewObservationDTO> documentReviewObservationDTO = documentReviewObservationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentReviewObservationDTO);
    }

    /**
     * {@code DELETE  /document-review-observations/:id} : delete the "id" documentReviewObservation.
     *
     * @param id the id of the documentReviewObservationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDocumentReviewObservation(@PathVariable("id") Long id) {
        log.debug("REST request to delete DocumentReviewObservation : {}", id);
        return documentReviewObservationService
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
     * {@code SEARCH  /document-review-observations/_search?query=:query} : search for the documentReviewObservation corresponding
     * to the query.
     *
     * @param query the query of the documentReviewObservation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<DocumentReviewObservationDTO>> searchDocumentReviewObservations(@RequestParam("query") String query) {
        log.debug("REST request to search DocumentReviewObservations for query {}", query);
        try {
            return documentReviewObservationService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
