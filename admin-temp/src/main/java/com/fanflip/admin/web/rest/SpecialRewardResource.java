package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.SpecialRewardRepository;
import com.monsterdam.admin.service.SpecialRewardService;
import com.monsterdam.admin.service.dto.SpecialRewardDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.SpecialReward}.
 */
@RestController
@RequestMapping("/api/special-rewards")
public class SpecialRewardResource {

    private final Logger log = LoggerFactory.getLogger(SpecialRewardResource.class);

    private static final String ENTITY_NAME = "specialReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialRewardService specialRewardService;

    private final SpecialRewardRepository specialRewardRepository;

    public SpecialRewardResource(SpecialRewardService specialRewardService, SpecialRewardRepository specialRewardRepository) {
        this.specialRewardService = specialRewardService;
        this.specialRewardRepository = specialRewardRepository;
    }

    /**
     * {@code POST  /special-rewards} : Create a new specialReward.
     *
     * @param specialRewardDTO the specialRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialRewardDTO, or with status {@code 400 (Bad Request)} if the specialReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SpecialRewardDTO>> createSpecialReward(@Valid @RequestBody SpecialRewardDTO specialRewardDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpecialReward : {}", specialRewardDTO);
        if (specialRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return specialRewardService
            .save(specialRewardDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/special-rewards/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /special-rewards/:id} : Updates an existing specialReward.
     *
     * @param id the id of the specialRewardDTO to save.
     * @param specialRewardDTO the specialRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialRewardDTO,
     * or with status {@code 400 (Bad Request)} if the specialRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SpecialRewardDTO>> updateSpecialReward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialRewardDTO specialRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpecialReward : {}, {}", id, specialRewardDTO);
        if (specialRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return specialRewardRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return specialRewardService
                    .update(specialRewardDTO)
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
     * {@code PATCH  /special-rewards/:id} : Partial updates given fields of an existing specialReward, field will ignore if it is null
     *
     * @param id the id of the specialRewardDTO to save.
     * @param specialRewardDTO the specialRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialRewardDTO,
     * or with status {@code 400 (Bad Request)} if the specialRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SpecialRewardDTO>> partialUpdateSpecialReward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialRewardDTO specialRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecialReward partially : {}, {}", id, specialRewardDTO);
        if (specialRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return specialRewardRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SpecialRewardDTO> result = specialRewardService.partialUpdate(specialRewardDTO);

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
     * {@code GET  /special-rewards} : get all the specialRewards.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialRewards in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SpecialRewardDTO>>> getAllSpecialRewards(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SpecialRewards");
        return specialRewardService
            .countAll()
            .zipWith(specialRewardService.findAll(pageable).collectList())
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
     * {@code GET  /special-rewards/:id} : get the "id" specialReward.
     *
     * @param id the id of the specialRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SpecialRewardDTO>> getSpecialReward(@PathVariable("id") Long id) {
        log.debug("REST request to get SpecialReward : {}", id);
        Mono<SpecialRewardDTO> specialRewardDTO = specialRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialRewardDTO);
    }

    /**
     * {@code DELETE  /special-rewards/:id} : delete the "id" specialReward.
     *
     * @param id the id of the specialRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSpecialReward(@PathVariable("id") Long id) {
        log.debug("REST request to delete SpecialReward : {}", id);
        return specialRewardService
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
     * {@code SEARCH  /special-rewards/_search?query=:query} : search for the specialReward corresponding
     * to the query.
     *
     * @param query the query of the specialReward search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SpecialRewardDTO>>> searchSpecialRewards(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SpecialRewards for query {}", query);
        return specialRewardService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(specialRewardService.search(query, pageable)));
    }
}
