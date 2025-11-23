package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.FinancialStatementRepository;
import com.fanflip.admin.service.FinancialStatementService;
import com.fanflip.admin.service.dto.FinancialStatementDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.FinancialStatement}.
 */
@RestController
@RequestMapping("/api/financial-statements")
public class FinancialStatementResource {

    private final Logger log = LoggerFactory.getLogger(FinancialStatementResource.class);

    private static final String ENTITY_NAME = "financialStatement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialStatementService financialStatementService;

    private final FinancialStatementRepository financialStatementRepository;

    public FinancialStatementResource(
        FinancialStatementService financialStatementService,
        FinancialStatementRepository financialStatementRepository
    ) {
        this.financialStatementService = financialStatementService;
        this.financialStatementRepository = financialStatementRepository;
    }

    /**
     * {@code POST  /financial-statements} : Create a new financialStatement.
     *
     * @param financialStatementDTO the financialStatementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialStatementDTO, or with status {@code 400 (Bad Request)} if the financialStatement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<FinancialStatementDTO>> createFinancialStatement(
        @Valid @RequestBody FinancialStatementDTO financialStatementDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FinancialStatement : {}", financialStatementDTO);
        if (financialStatementDTO.getId() != null) {
            throw new BadRequestAlertException("A new financialStatement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return financialStatementService
            .save(financialStatementDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/financial-statements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /financial-statements/:id} : Updates an existing financialStatement.
     *
     * @param id the id of the financialStatementDTO to save.
     * @param financialStatementDTO the financialStatementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialStatementDTO,
     * or with status {@code 400 (Bad Request)} if the financialStatementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialStatementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<FinancialStatementDTO>> updateFinancialStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialStatementDTO financialStatementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinancialStatement : {}, {}", id, financialStatementDTO);
        if (financialStatementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialStatementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return financialStatementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return financialStatementService
                    .update(financialStatementDTO)
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
     * {@code PATCH  /financial-statements/:id} : Partial updates given fields of an existing financialStatement, field will ignore if it is null
     *
     * @param id the id of the financialStatementDTO to save.
     * @param financialStatementDTO the financialStatementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialStatementDTO,
     * or with status {@code 400 (Bad Request)} if the financialStatementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialStatementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialStatementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FinancialStatementDTO>> partialUpdateFinancialStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialStatementDTO financialStatementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinancialStatement partially : {}, {}", id, financialStatementDTO);
        if (financialStatementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialStatementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return financialStatementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FinancialStatementDTO> result = financialStatementService.partialUpdate(financialStatementDTO);

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
     * {@code GET  /financial-statements} : get all the financialStatements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialStatements in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FinancialStatementDTO>> getAllFinancialStatements(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all FinancialStatements");
        return financialStatementService.findAll().collectList();
    }

    /**
     * {@code GET  /financial-statements} : get all the financialStatements as a stream.
     * @return the {@link Flux} of financialStatements.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FinancialStatementDTO> getAllFinancialStatementsAsStream() {
        log.debug("REST request to get all FinancialStatements as a stream");
        return financialStatementService.findAll();
    }

    /**
     * {@code GET  /financial-statements/:id} : get the "id" financialStatement.
     *
     * @param id the id of the financialStatementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialStatementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<FinancialStatementDTO>> getFinancialStatement(@PathVariable("id") Long id) {
        log.debug("REST request to get FinancialStatement : {}", id);
        Mono<FinancialStatementDTO> financialStatementDTO = financialStatementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialStatementDTO);
    }

    /**
     * {@code DELETE  /financial-statements/:id} : delete the "id" financialStatement.
     *
     * @param id the id of the financialStatementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteFinancialStatement(@PathVariable("id") Long id) {
        log.debug("REST request to delete FinancialStatement : {}", id);
        return financialStatementService
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
     * {@code SEARCH  /financial-statements/_search?query=:query} : search for the financialStatement corresponding
     * to the query.
     *
     * @param query the query of the financialStatement search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<FinancialStatementDTO>> searchFinancialStatements(@RequestParam("query") String query) {
        log.debug("REST request to search FinancialStatements for query {}", query);
        try {
            return financialStatementService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
