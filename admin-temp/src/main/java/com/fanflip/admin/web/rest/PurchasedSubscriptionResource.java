package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.PurchasedSubscriptionRepository;
import com.fanflip.admin.service.PurchasedSubscriptionService;
import com.fanflip.admin.service.dto.PurchasedSubscriptionDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.PurchasedSubscription}.
 */
@RestController
@RequestMapping("/api/purchased-subscriptions")
public class PurchasedSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedSubscriptionResource.class);

    private static final String ENTITY_NAME = "purchasedSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedSubscriptionService purchasedSubscriptionService;

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    public PurchasedSubscriptionResource(
        PurchasedSubscriptionService purchasedSubscriptionService,
        PurchasedSubscriptionRepository purchasedSubscriptionRepository
    ) {
        this.purchasedSubscriptionService = purchasedSubscriptionService;
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
    }

    /**
     * {@code POST  /purchased-subscriptions} : Create a new purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedSubscriptionDTO, or with status {@code 400 (Bad Request)} if the purchasedSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<PurchasedSubscriptionDTO>> createPurchasedSubscription(
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return purchasedSubscriptionService
            .save(purchasedSubscriptionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/purchased-subscriptions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /purchased-subscriptions/:id} : Updates an existing purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<PurchasedSubscriptionDTO>> updatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedSubscription : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return purchasedSubscriptionService
                    .update(purchasedSubscriptionDTO)
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
     * {@code PATCH  /purchased-subscriptions/:id} : Partial updates given fields of an existing purchasedSubscription, field will ignore if it is null
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PurchasedSubscriptionDTO>> partialUpdatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchasedSubscription partially : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return purchasedSubscriptionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PurchasedSubscriptionDTO> result = purchasedSubscriptionService.partialUpdate(purchasedSubscriptionDTO);

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
     * {@code GET  /purchased-subscriptions} : get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedSubscriptions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PurchasedSubscriptionDTO>>> getAllPurchasedSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchasedSubscriptions");
        return purchasedSubscriptionService
            .countAll()
            .zipWith(purchasedSubscriptionService.findAll(pageable).collectList())
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
     * {@code GET  /purchased-subscriptions/:id} : get the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PurchasedSubscriptionDTO>> getPurchasedSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to get PurchasedSubscription : {}", id);
        Mono<PurchasedSubscriptionDTO> purchasedSubscriptionDTO = purchasedSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedSubscriptionDTO);
    }

    /**
     * {@code DELETE  /purchased-subscriptions/:id} : delete the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePurchasedSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedSubscription : {}", id);
        return purchasedSubscriptionService
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
     * {@code SEARCH  /purchased-subscriptions/_search?query=:query} : search for the purchasedSubscription corresponding
     * to the query.
     *
     * @param query the query of the purchasedSubscription search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PurchasedSubscriptionDTO>>> searchPurchasedSubscriptions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PurchasedSubscriptions for query {}", query);
        return purchasedSubscriptionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(purchasedSubscriptionService.search(query, pageable)));
    }
}
