package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.SubscriptionBundleRepository;
import com.fanflip.admin.service.SubscriptionBundleService;
import com.fanflip.admin.service.dto.SubscriptionBundleDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.SubscriptionBundle}.
 */
@RestController
@RequestMapping("/api/subscription-bundles")
public class SubscriptionBundleResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleResource.class);

    private static final String ENTITY_NAME = "subscriptionBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBundleService subscriptionBundleService;

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    public SubscriptionBundleResource(
        SubscriptionBundleService subscriptionBundleService,
        SubscriptionBundleRepository subscriptionBundleRepository
    ) {
        this.subscriptionBundleService = subscriptionBundleService;
        this.subscriptionBundleRepository = subscriptionBundleRepository;
    }

    /**
     * {@code POST  /subscription-bundles} : Create a new subscriptionBundle.
     *
     * @param subscriptionBundleDTO the subscriptionBundleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBundleDTO, or with status {@code 400 (Bad Request)} if the subscriptionBundle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SubscriptionBundleDTO>> createSubscriptionBundle(
        @Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionBundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subscriptionBundleService
            .save(subscriptionBundleDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/subscription-bundles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /subscription-bundles/:id} : Updates an existing subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SubscriptionBundleDTO>> updateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionBundle : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionBundleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subscriptionBundleService
                    .update(subscriptionBundleDTO)
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
     * {@code PATCH  /subscription-bundles/:id} : Partial updates given fields of an existing subscriptionBundle, field will ignore if it is null
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionBundleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SubscriptionBundleDTO>> partialUpdateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionBundle partially : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subscriptionBundleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SubscriptionBundleDTO> result = subscriptionBundleService.partialUpdate(subscriptionBundleDTO);

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
     * {@code GET  /subscription-bundles} : get all the subscriptionBundles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionBundles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SubscriptionBundleDTO>>> getAllSubscriptionBundles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SubscriptionBundles");
        return subscriptionBundleService
            .countAll()
            .zipWith(subscriptionBundleService.findAll(pageable).collectList())
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
     * {@code GET  /subscription-bundles/:id} : get the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBundleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SubscriptionBundleDTO>> getSubscriptionBundle(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscriptionBundle : {}", id);
        Mono<SubscriptionBundleDTO> subscriptionBundleDTO = subscriptionBundleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleDTO);
    }

    /**
     * {@code DELETE  /subscription-bundles/:id} : delete the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSubscriptionBundle(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscriptionBundle : {}", id);
        return subscriptionBundleService
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
     * {@code SEARCH  /subscription-bundles/_search?query=:query} : search for the subscriptionBundle corresponding
     * to the query.
     *
     * @param query the query of the subscriptionBundle search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SubscriptionBundleDTO>>> searchSubscriptionBundles(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SubscriptionBundles for query {}", query);
        return subscriptionBundleService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(subscriptionBundleService.search(query, pageable)));
    }
}
