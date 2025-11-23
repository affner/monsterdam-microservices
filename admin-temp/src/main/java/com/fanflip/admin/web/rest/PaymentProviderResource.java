package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PaymentProviderRepository;
import com.fanflip.admin.service.PaymentProviderService;
import com.fanflip.admin.service.dto.PaymentProviderDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PaymentProvider}.
 */
@RestController
@RequestMapping("/api/payment-providers")
public class PaymentProviderResource {

    private final Logger log = LoggerFactory.getLogger(PaymentProviderResource.class);

    private static final String ENTITY_NAME = "paymentProvider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentProviderService paymentProviderService;

    private final PaymentProviderRepository paymentProviderRepository;

    public PaymentProviderResource(PaymentProviderService paymentProviderService, PaymentProviderRepository paymentProviderRepository) {
        this.paymentProviderService = paymentProviderService;
        this.paymentProviderRepository = paymentProviderRepository;
    }

    /**
     * {@code POST  /payment-providers} : Create a new paymentProvider.
     *
     * @param paymentProviderDTO the paymentProviderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentProviderDTO, or with status {@code 400 (Bad Request)} if the paymentProvider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PaymentProviderDTO>> createPaymentProvider(@Valid @RequestBody PaymentProviderDTO paymentProviderDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentProvider : {}", paymentProviderDTO);
        if (paymentProviderDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return paymentProviderService
            .save(paymentProviderDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/payment-providers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /payment-providers/:id} : Updates an existing paymentProvider.
     *
     * @param id the id of the paymentProviderDTO to save.
     * @param paymentProviderDTO the paymentProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentProviderDTO,
     * or with status {@code 400 (Bad Request)} if the paymentProviderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PaymentProviderDTO>> updatePaymentProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentProviderDTO paymentProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentProvider : {}, {}", id, paymentProviderDTO);
        if (paymentProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentProviderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return paymentProviderService
                    .update(paymentProviderDTO)
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
     * {@code PATCH  /payment-providers/:id} : Partial updates given fields of an existing paymentProvider, field will ignore if it is null
     *
     * @param id the id of the paymentProviderDTO to save.
     * @param paymentProviderDTO the paymentProviderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentProviderDTO,
     * or with status {@code 400 (Bad Request)} if the paymentProviderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentProviderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentProviderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PaymentProviderDTO>> partialUpdatePaymentProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentProviderDTO paymentProviderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentProvider partially : {}, {}", id, paymentProviderDTO);
        if (paymentProviderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentProviderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return paymentProviderRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PaymentProviderDTO> result = paymentProviderService.partialUpdate(paymentProviderDTO);

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
     * {@code GET  /payment-providers} : get all the paymentProviders.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentProviders in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PaymentProviderDTO>>> getAllPaymentProviders(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of PaymentProviders");
        return paymentProviderService
            .countAll()
            .zipWith(paymentProviderService.findAll(pageable).collectList())
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
     * {@code GET  /payment-providers/:id} : get the "id" paymentProvider.
     *
     * @param id the id of the paymentProviderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentProviderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PaymentProviderDTO>> getPaymentProvider(@PathVariable("id") Long id) {
        log.debug("REST request to get PaymentProvider : {}", id);
        Mono<PaymentProviderDTO> paymentProviderDTO = paymentProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentProviderDTO);
    }

    /**
     * {@code DELETE  /payment-providers/:id} : delete the "id" paymentProvider.
     *
     * @param id the id of the paymentProviderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePaymentProvider(@PathVariable("id") Long id) {
        log.debug("REST request to delete PaymentProvider : {}", id);
        return paymentProviderService
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
     * {@code SEARCH  /payment-providers/_search?query=:query} : search for the paymentProvider corresponding
     * to the query.
     *
     * @param query the query of the paymentProvider search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PaymentProviderDTO>>> searchPaymentProviders(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PaymentProviders for query {}", query);
        return paymentProviderService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(paymentProviderService.search(query, pageable)));
    }
}
