package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PaymentTransactionRepository;
import com.fanflip.admin.service.PaymentTransactionService;
import com.fanflip.admin.service.dto.PaymentTransactionDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PaymentTransaction}.
 */
@RestController
@RequestMapping("/api/payment-transactions")
public class PaymentTransactionResource {

    private final Logger log = LoggerFactory.getLogger(PaymentTransactionResource.class);

    private static final String ENTITY_NAME = "paymentTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentTransactionService paymentTransactionService;

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionResource(
        PaymentTransactionService paymentTransactionService,
        PaymentTransactionRepository paymentTransactionRepository
    ) {
        this.paymentTransactionService = paymentTransactionService;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    /**
     * {@code POST  /payment-transactions} : Create a new paymentTransaction.
     *
     * @param paymentTransactionDTO the paymentTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTransactionDTO, or with status {@code 400 (Bad Request)} if the paymentTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PaymentTransactionDTO>> createPaymentTransaction(
        @Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PaymentTransaction : {}", paymentTransactionDTO);
        if (paymentTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return paymentTransactionService
            .save(paymentTransactionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payment-transactions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-transactions/:id} : Updates an existing paymentTransaction.
     *
     * @param id the id of the paymentTransactionDTO to save.
     * @param paymentTransactionDTO the paymentTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the paymentTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PaymentTransactionDTO>> updatePaymentTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentTransactionDTO paymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentTransaction : {}, {}", id, paymentTransactionDTO);
        if (paymentTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentTransactionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentTransactionService
                    .update(paymentTransactionDTO)
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
     * {@code PATCH  /payment-transactions/:id} : Partial updates given fields of an existing paymentTransaction, field will ignore if it is null
     *
     * @param id the id of the paymentTransactionDTO to save.
     * @param paymentTransactionDTO the paymentTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the paymentTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentTransactionDTO>> partialUpdatePaymentTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentTransactionDTO paymentTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentTransaction partially : {}, {}", id, paymentTransactionDTO);
        if (paymentTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentTransactionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentTransactionDTO> result = paymentTransactionService.partialUpdate(paymentTransactionDTO);

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
     * {@code GET  /payment-transactions} : get all the paymentTransactions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentTransactions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PaymentTransactionDTO>>> getAllPaymentTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("accountingrecord-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentTransactions where accountingRecord is null");
            return paymentTransactionService.findAllWhereAccountingRecordIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedcontent-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentTransactions where purchasedContent is null");
            return paymentTransactionService.findAllWherePurchasedContentIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedsubscription-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentTransactions where purchasedSubscription is null");
            return paymentTransactionService.findAllWherePurchasedSubscriptionIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("wallettransaction-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentTransactions where walletTransaction is null");
            return paymentTransactionService.findAllWhereWalletTransactionIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedtip-is-null".equals(filter)) {
            log.debug("REST request to get all PaymentTransactions where purchasedTip is null");
            return paymentTransactionService.findAllWherePurchasedTipIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of PaymentTransactions");
        return paymentTransactionService
            .countAll()
            .zipWith(paymentTransactionService.findAll(pageable).collectList())
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
     * {@code GET  /payment-transactions/:id} : get the "id" paymentTransaction.
     *
     * @param id the id of the paymentTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentTransactionDTO>> getPaymentTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to get PaymentTransaction : {}", id);
        Mono<PaymentTransactionDTO> paymentTransactionDTO = paymentTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentTransactionDTO);
    }

    /**
     * {@code DELETE  /payment-transactions/:id} : delete the "id" paymentTransaction.
     *
     * @param id the id of the paymentTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaymentTransaction : {}", id);
        return paymentTransactionService
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
     * {@code SEARCH  /payment-transactions/_search?query=:query} : search for the paymentTransaction corresponding
     * to the query.
     *
     * @param query the query of the paymentTransaction search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PaymentTransactionDTO>>> searchPaymentTransactions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PaymentTransactions for query {}", query);
        return paymentTransactionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(paymentTransactionService.search(query, pageable)));
    }
}
