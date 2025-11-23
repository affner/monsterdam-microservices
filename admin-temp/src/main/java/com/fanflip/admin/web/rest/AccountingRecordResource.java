package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AccountingRecordRepository;
import com.fanflip.admin.service.AccountingRecordService;
import com.fanflip.admin.service.dto.AccountingRecordDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.AccountingRecord}.
 */
@RestController
@RequestMapping("/api/accounting-records")
public class AccountingRecordResource {

    private final Logger log = LoggerFactory.getLogger(AccountingRecordResource.class);

    private static final String ENTITY_NAME = "accountingRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountingRecordService accountingRecordService;

    private final AccountingRecordRepository accountingRecordRepository;

    public AccountingRecordResource(
        AccountingRecordService accountingRecordService,
        AccountingRecordRepository accountingRecordRepository
    ) {
        this.accountingRecordService = accountingRecordService;
        this.accountingRecordRepository = accountingRecordRepository;
    }

    /**
     * {@code POST  /accounting-records} : Create a new accountingRecord.
     *
     * @param accountingRecordDTO the accountingRecordDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountingRecordDTO, or with status {@code 400 (Bad Request)} if the accountingRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AccountingRecordDTO>> createAccountingRecord(@Valid @RequestBody AccountingRecordDTO accountingRecordDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountingRecord : {}", accountingRecordDTO);
        if (accountingRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountingRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return accountingRecordService
            .save(accountingRecordDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/accounting-records/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /accounting-records/:id} : Updates an existing accountingRecord.
     *
     * @param id the id of the accountingRecordDTO to save.
     * @param accountingRecordDTO the accountingRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingRecordDTO,
     * or with status {@code 400 (Bad Request)} if the accountingRecordDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountingRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountingRecordDTO>> updateAccountingRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountingRecordDTO accountingRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountingRecord : {}, {}", id, accountingRecordDTO);
        if (accountingRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return accountingRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return accountingRecordService
                    .update(accountingRecordDTO)
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
     * {@code PATCH  /accounting-records/:id} : Partial updates given fields of an existing accountingRecord, field will ignore if it is null
     *
     * @param id the id of the accountingRecordDTO to save.
     * @param accountingRecordDTO the accountingRecordDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountingRecordDTO,
     * or with status {@code 400 (Bad Request)} if the accountingRecordDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountingRecordDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountingRecordDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AccountingRecordDTO>> partialUpdateAccountingRecord(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountingRecordDTO accountingRecordDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountingRecord partially : {}, {}", id, accountingRecordDTO);
        if (accountingRecordDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountingRecordDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return accountingRecordRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AccountingRecordDTO> result = accountingRecordService.partialUpdate(accountingRecordDTO);

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
     * {@code GET  /accounting-records} : get all the accountingRecords.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountingRecords in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AccountingRecordDTO>>> getAllAccountingRecords(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AccountingRecords");
        return accountingRecordService
            .countAll()
            .zipWith(accountingRecordService.findAll(pageable).collectList())
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
     * {@code GET  /accounting-records/:id} : get the "id" accountingRecord.
     *
     * @param id the id of the accountingRecordDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountingRecordDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountingRecordDTO>> getAccountingRecord(@PathVariable("id") Long id) {
        log.debug("REST request to get AccountingRecord : {}", id);
        Mono<AccountingRecordDTO> accountingRecordDTO = accountingRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountingRecordDTO);
    }

    /**
     * {@code DELETE  /accounting-records/:id} : delete the "id" accountingRecord.
     *
     * @param id the id of the accountingRecordDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAccountingRecord(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccountingRecord : {}", id);
        return accountingRecordService
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
     * {@code SEARCH  /accounting-records/_search?query=:query} : search for the accountingRecord corresponding
     * to the query.
     *
     * @param query the query of the accountingRecord search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AccountingRecordDTO>>> searchAccountingRecords(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of AccountingRecords for query {}", query);
        return accountingRecordService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(accountingRecordService.search(query, pageable)));
    }
}
