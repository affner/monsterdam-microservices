package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.IdentityDocumentReviewRepository;
import com.monsterdam.admin.service.IdentityDocumentReviewService;
import com.monsterdam.admin.service.dto.IdentityDocumentReviewDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.IdentityDocumentReview}.
 */
@RestController
@RequestMapping("/api/identity-document-reviews")
public class IdentityDocumentReviewResource {

    private final Logger log = LoggerFactory.getLogger(IdentityDocumentReviewResource.class);

    private static final String ENTITY_NAME = "identityDocumentReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IdentityDocumentReviewService identityDocumentReviewService;

    private final IdentityDocumentReviewRepository identityDocumentReviewRepository;

    public IdentityDocumentReviewResource(
        IdentityDocumentReviewService identityDocumentReviewService,
        IdentityDocumentReviewRepository identityDocumentReviewRepository
    ) {
        this.identityDocumentReviewService = identityDocumentReviewService;
        this.identityDocumentReviewRepository = identityDocumentReviewRepository;
    }

    /**
     * {@code POST  /identity-document-reviews} : Create a new identityDocumentReview.
     *
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identityDocumentReviewDTO, or with status {@code 400 (Bad Request)} if the identityDocumentReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<IdentityDocumentReviewDTO>> createIdentityDocumentReview(
        @Valid @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IdentityDocumentReview : {}", identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() != null) {
            throw new BadRequestAlertException("A new identityDocumentReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return identityDocumentReviewService
            .save(identityDocumentReviewDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/identity-document-reviews/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /identity-document-reviews/:id} : Updates an existing identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to save.
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentReviewDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentReviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<IdentityDocumentReviewDTO>> updateIdentityDocumentReview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IdentityDocumentReview : {}, {}", id, identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identityDocumentReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return identityDocumentReviewService
                    .update(identityDocumentReviewDTO)
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
     * {@code PATCH  /identity-document-reviews/:id} : Partial updates given fields of an existing identityDocumentReview, field will ignore if it is null
     *
     * @param id the id of the identityDocumentReviewDTO to save.
     * @param identityDocumentReviewDTO the identityDocumentReviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentReviewDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentReviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the identityDocumentReviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentReviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IdentityDocumentReviewDTO>> partialUpdateIdentityDocumentReview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdentityDocumentReviewDTO identityDocumentReviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IdentityDocumentReview partially : {}, {}", id, identityDocumentReviewDTO);
        if (identityDocumentReviewDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentReviewDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return identityDocumentReviewRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IdentityDocumentReviewDTO> result = identityDocumentReviewService.partialUpdate(identityDocumentReviewDTO);

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
     * {@code GET  /identity-document-reviews} : get all the identityDocumentReviews.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of identityDocumentReviews in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<IdentityDocumentReviewDTO>> getAllIdentityDocumentReviews() {
        log.debug("REST request to get all IdentityDocumentReviews");
        return identityDocumentReviewService.findAll().collectList();
    }

    /**
     * {@code GET  /identity-document-reviews} : get all the identityDocumentReviews as a stream.
     * @return the {@link Flux} of identityDocumentReviews.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IdentityDocumentReviewDTO> getAllIdentityDocumentReviewsAsStream() {
        log.debug("REST request to get all IdentityDocumentReviews as a stream");
        return identityDocumentReviewService.findAll();
    }

    /**
     * {@code GET  /identity-document-reviews/:id} : get the "id" identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identityDocumentReviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<IdentityDocumentReviewDTO>> getIdentityDocumentReview(@PathVariable("id") Long id) {
        log.debug("REST request to get IdentityDocumentReview : {}", id);
        Mono<IdentityDocumentReviewDTO> identityDocumentReviewDTO = identityDocumentReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identityDocumentReviewDTO);
    }

    /**
     * {@code DELETE  /identity-document-reviews/:id} : delete the "id" identityDocumentReview.
     *
     * @param id the id of the identityDocumentReviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteIdentityDocumentReview(@PathVariable("id") Long id) {
        log.debug("REST request to delete IdentityDocumentReview : {}", id);
        return identityDocumentReviewService
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
     * {@code SEARCH  /identity-document-reviews/_search?query=:query} : search for the identityDocumentReview corresponding
     * to the query.
     *
     * @param query the query of the identityDocumentReview search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<IdentityDocumentReviewDTO>> searchIdentityDocumentReviews(@RequestParam("query") String query) {
        log.debug("REST request to search IdentityDocumentReviews for query {}", query);
        try {
            return identityDocumentReviewService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
