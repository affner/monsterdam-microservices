package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.OfferPromotionRepository;
import com.monsterdam.admin.service.OfferPromotionService;
import com.monsterdam.admin.service.dto.OfferPromotionDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.OfferPromotion}.
 */
@RestController
@RequestMapping("/api/offer-promotions")
public class OfferPromotionResource {

    private final Logger log = LoggerFactory.getLogger(OfferPromotionResource.class);

    private static final String ENTITY_NAME = "offerPromotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferPromotionService offerPromotionService;

    private final OfferPromotionRepository offerPromotionRepository;

    public OfferPromotionResource(OfferPromotionService offerPromotionService, OfferPromotionRepository offerPromotionRepository) {
        this.offerPromotionService = offerPromotionService;
        this.offerPromotionRepository = offerPromotionRepository;
    }

    /**
     * {@code POST  /offer-promotions} : Create a new offerPromotion.
     *
     * @param offerPromotionDTO the offerPromotionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offerPromotionDTO, or with status {@code 400 (Bad Request)} if the offerPromotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<OfferPromotionDTO>> createOfferPromotion(@Valid @RequestBody OfferPromotionDTO offerPromotionDTO)
        throws URISyntaxException {
        log.debug("REST request to save OfferPromotion : {}", offerPromotionDTO);
        if (offerPromotionDTO.getId() != null) {
            throw new BadRequestAlertException("A new offerPromotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return offerPromotionService
            .save(offerPromotionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/offer-promotions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /offer-promotions/:id} : Updates an existing offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to save.
     * @param offerPromotionDTO the offerPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the offerPromotionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offerPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<OfferPromotionDTO>> updateOfferPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OfferPromotionDTO offerPromotionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OfferPromotion : {}, {}", id, offerPromotionDTO);
        if (offerPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return offerPromotionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return offerPromotionService
                    .update(offerPromotionDTO)
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
     * {@code PATCH  /offer-promotions/:id} : Partial updates given fields of an existing offerPromotion, field will ignore if it is null
     *
     * @param id the id of the offerPromotionDTO to save.
     * @param offerPromotionDTO the offerPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the offerPromotionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the offerPromotionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the offerPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<OfferPromotionDTO>> partialUpdateOfferPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OfferPromotionDTO offerPromotionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OfferPromotion partially : {}, {}", id, offerPromotionDTO);
        if (offerPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return offerPromotionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<OfferPromotionDTO> result = offerPromotionService.partialUpdate(offerPromotionDTO);

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
     * {@code GET  /offer-promotions} : get all the offerPromotions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offerPromotions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<OfferPromotionDTO>>> getAllOfferPromotions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of OfferPromotions");
        return offerPromotionService
            .countAll()
            .zipWith(offerPromotionService.findAll(pageable).collectList())
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
     * {@code GET  /offer-promotions/:id} : get the "id" offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offerPromotionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<OfferPromotionDTO>> getOfferPromotion(@PathVariable("id") Long id) {
        log.debug("REST request to get OfferPromotion : {}", id);
        Mono<OfferPromotionDTO> offerPromotionDTO = offerPromotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offerPromotionDTO);
    }

    /**
     * {@code DELETE  /offer-promotions/:id} : delete the "id" offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOfferPromotion(@PathVariable("id") Long id) {
        log.debug("REST request to delete OfferPromotion : {}", id);
        return offerPromotionService
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
     * {@code SEARCH  /offer-promotions/_search?query=:query} : search for the offerPromotion corresponding
     * to the query.
     *
     * @param query the query of the offerPromotion search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<OfferPromotionDTO>>> searchOfferPromotions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of OfferPromotions for query {}", query);
        return offerPromotionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(offerPromotionService.search(query, pageable)));
    }
}
