package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.SocialNetworkRepository;
import com.monsterdam.admin.service.SocialNetworkService;
import com.monsterdam.admin.service.dto.SocialNetworkDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.SocialNetwork}.
 */
@RestController
@RequestMapping("/api/social-networks")
public class SocialNetworkResource {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkResource.class);

    private static final String ENTITY_NAME = "socialNetwork";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialNetworkService socialNetworkService;

    private final SocialNetworkRepository socialNetworkRepository;

    public SocialNetworkResource(SocialNetworkService socialNetworkService, SocialNetworkRepository socialNetworkRepository) {
        this.socialNetworkService = socialNetworkService;
        this.socialNetworkRepository = socialNetworkRepository;
    }

    /**
     * {@code POST  /social-networks} : Create a new socialNetwork.
     *
     * @param socialNetworkDTO the socialNetworkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialNetworkDTO, or with status {@code 400 (Bad Request)} if the socialNetwork has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SocialNetworkDTO>> createSocialNetwork(@Valid @RequestBody SocialNetworkDTO socialNetworkDTO)
        throws URISyntaxException {
        log.debug("REST request to save SocialNetwork : {}", socialNetworkDTO);
        if (socialNetworkDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialNetwork cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return socialNetworkService
            .save(socialNetworkDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/social-networks/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /social-networks/:id} : Updates an existing socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to save.
     * @param socialNetworkDTO the socialNetworkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetworkDTO,
     * or with status {@code 400 (Bad Request)} if the socialNetworkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialNetworkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SocialNetworkDTO>> updateSocialNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialNetworkDTO socialNetworkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialNetwork : {}, {}", id, socialNetworkDTO);
        if (socialNetworkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetworkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return socialNetworkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return socialNetworkService
                    .update(socialNetworkDTO)
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
     * {@code PATCH  /social-networks/:id} : Partial updates given fields of an existing socialNetwork, field will ignore if it is null
     *
     * @param id the id of the socialNetworkDTO to save.
     * @param socialNetworkDTO the socialNetworkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetworkDTO,
     * or with status {@code 400 (Bad Request)} if the socialNetworkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialNetworkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialNetworkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SocialNetworkDTO>> partialUpdateSocialNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialNetworkDTO socialNetworkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialNetwork partially : {}, {}", id, socialNetworkDTO);
        if (socialNetworkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetworkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return socialNetworkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SocialNetworkDTO> result = socialNetworkService.partialUpdate(socialNetworkDTO);

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
     * {@code GET  /social-networks} : get all the socialNetworks.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialNetworks in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SocialNetworkDTO>>> getAllSocialNetworks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of SocialNetworks");
        return socialNetworkService
            .countAll()
            .zipWith(socialNetworkService.findAll(pageable).collectList())
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
     * {@code GET  /social-networks/:id} : get the "id" socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialNetworkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SocialNetworkDTO>> getSocialNetwork(@PathVariable("id") Long id) {
        log.debug("REST request to get SocialNetwork : {}", id);
        Mono<SocialNetworkDTO> socialNetworkDTO = socialNetworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialNetworkDTO);
    }

    /**
     * {@code DELETE  /social-networks/:id} : delete the "id" socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSocialNetwork(@PathVariable("id") Long id) {
        log.debug("REST request to delete SocialNetwork : {}", id);
        return socialNetworkService
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
     * {@code SEARCH  /social-networks/_search?query=:query} : search for the socialNetwork corresponding
     * to the query.
     *
     * @param query the query of the socialNetwork search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SocialNetworkDTO>>> searchSocialNetworks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SocialNetworks for query {}", query);
        return socialNetworkService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(socialNetworkService.search(query, pageable)));
    }
}
