package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.CreatorEarningRepository;
import com.monsterdam.admin.service.CreatorEarningService;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.CreatorEarning}.
 */
@RestController
@RequestMapping("/api/creator-earnings")
public class CreatorEarningResource {

    private final Logger log = LoggerFactory.getLogger(CreatorEarningResource.class);

    private static final String ENTITY_NAME = "creatorEarning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreatorEarningService creatorEarningService;

    private final CreatorEarningRepository creatorEarningRepository;

    public CreatorEarningResource(CreatorEarningService creatorEarningService, CreatorEarningRepository creatorEarningRepository) {
        this.creatorEarningService = creatorEarningService;
        this.creatorEarningRepository = creatorEarningRepository;
    }

    /**
     * {@code POST  /creator-earnings} : Create a new creatorEarning.
     *
     * @param creatorEarningDTO the creatorEarningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creatorEarningDTO, or with status {@code 400 (Bad Request)} if the creatorEarning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CreatorEarningDTO>> createCreatorEarning(@Valid @RequestBody CreatorEarningDTO creatorEarningDTO)
        throws URISyntaxException {
        log.debug("REST request to save CreatorEarning : {}", creatorEarningDTO);
        if (creatorEarningDTO.getId() != null) {
            throw new BadRequestAlertException("A new creatorEarning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return creatorEarningService
            .save(creatorEarningDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/creator-earnings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /creator-earnings/:id} : Updates an existing creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to save.
     * @param creatorEarningDTO the creatorEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creatorEarningDTO,
     * or with status {@code 400 (Bad Request)} if the creatorEarningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creatorEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreatorEarningDTO>> updateCreatorEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreatorEarningDTO creatorEarningDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CreatorEarning : {}, {}", id, creatorEarningDTO);
        if (creatorEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creatorEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return creatorEarningRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return creatorEarningService
                    .update(creatorEarningDTO)
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
     * {@code PATCH  /creator-earnings/:id} : Partial updates given fields of an existing creatorEarning, field will ignore if it is null
     *
     * @param id the id of the creatorEarningDTO to save.
     * @param creatorEarningDTO the creatorEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creatorEarningDTO,
     * or with status {@code 400 (Bad Request)} if the creatorEarningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the creatorEarningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the creatorEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CreatorEarningDTO>> partialUpdateCreatorEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreatorEarningDTO creatorEarningDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreatorEarning partially : {}, {}", id, creatorEarningDTO);
        if (creatorEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creatorEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return creatorEarningRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CreatorEarningDTO> result = creatorEarningService.partialUpdate(creatorEarningDTO);

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
     * {@code GET  /creator-earnings} : get all the creatorEarnings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creatorEarnings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CreatorEarningDTO>>> getAllCreatorEarnings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("moneypayout-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where moneyPayout is null");
            return creatorEarningService.findAllWhereMoneyPayoutIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedcontent-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedContent is null");
            return creatorEarningService.findAllWherePurchasedContentIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedsubscription-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedSubscription is null");
            return creatorEarningService.findAllWherePurchasedSubscriptionIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedtip-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedTip is null");
            return creatorEarningService.findAllWherePurchasedTipIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of CreatorEarnings");
        return creatorEarningService
            .countAll()
            .zipWith(creatorEarningService.findAll(pageable).collectList())
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
     * {@code GET  /creator-earnings/:id} : get the "id" creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creatorEarningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CreatorEarningDTO>> getCreatorEarning(@PathVariable("id") Long id) {
        log.debug("REST request to get CreatorEarning : {}", id);
        Mono<CreatorEarningDTO> creatorEarningDTO = creatorEarningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creatorEarningDTO);
    }

    /**
     * {@code DELETE  /creator-earnings/:id} : delete the "id" creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCreatorEarning(@PathVariable("id") Long id) {
        log.debug("REST request to delete CreatorEarning : {}", id);
        return creatorEarningService
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
     * {@code SEARCH  /creator-earnings/_search?query=:query} : search for the creatorEarning corresponding
     * to the query.
     *
     * @param query the query of the creatorEarning search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<CreatorEarningDTO>>> searchCreatorEarnings(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of CreatorEarnings for query {}", query);
        return creatorEarningService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(creatorEarningService.search(query, pageable)));
    }
}
