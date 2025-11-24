package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.HashTagRepository;
import com.monsterdam.admin.service.HashTagService;
import com.monsterdam.admin.service.dto.HashTagDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.HashTag}.
 */
@RestController
@RequestMapping("/api/hash-tags")
public class HashTagResource {

    private final Logger log = LoggerFactory.getLogger(HashTagResource.class);

    private static final String ENTITY_NAME = "hashTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashTagService hashTagService;

    private final HashTagRepository hashTagRepository;

    public HashTagResource(HashTagService hashTagService, HashTagRepository hashTagRepository) {
        this.hashTagService = hashTagService;
        this.hashTagRepository = hashTagRepository;
    }

    /**
     * {@code POST  /hash-tags} : Create a new hashTag.
     *
     * @param hashTagDTO the hashTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashTagDTO, or with status {@code 400 (Bad Request)} if the hashTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HashTagDTO>> createHashTag(@Valid @RequestBody HashTagDTO hashTagDTO) throws URISyntaxException {
        log.debug("REST request to save HashTag : {}", hashTagDTO);
        if (hashTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new hashTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return hashTagService
            .save(hashTagDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/hash-tags/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /hash-tags/:id} : Updates an existing hashTag.
     *
     * @param id the id of the hashTagDTO to save.
     * @param hashTagDTO the hashTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashTagDTO,
     * or with status {@code 400 (Bad Request)} if the hashTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HashTagDTO>> updateHashTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HashTagDTO hashTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HashTag : {}, {}", id, hashTagDTO);
        if (hashTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return hashTagRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return hashTagService
                    .update(hashTagDTO)
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
     * {@code PATCH  /hash-tags/:id} : Partial updates given fields of an existing hashTag, field will ignore if it is null
     *
     * @param id the id of the hashTagDTO to save.
     * @param hashTagDTO the hashTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashTagDTO,
     * or with status {@code 400 (Bad Request)} if the hashTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hashTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hashTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HashTagDTO>> partialUpdateHashTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HashTagDTO hashTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HashTag partially : {}, {}", id, hashTagDTO);
        if (hashTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return hashTagRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HashTagDTO> result = hashTagService.partialUpdate(hashTagDTO);

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
     * {@code GET  /hash-tags} : get all the hashTags.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashTags in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HashTagDTO>>> getAllHashTags(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HashTags");
        return hashTagService
            .countAll()
            .zipWith(hashTagService.findAll(pageable).collectList())
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
     * {@code GET  /hash-tags/:id} : get the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HashTagDTO>> getHashTag(@PathVariable("id") Long id) {
        log.debug("REST request to get HashTag : {}", id);
        Mono<HashTagDTO> hashTagDTO = hashTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashTagDTO);
    }

    /**
     * {@code DELETE  /hash-tags/:id} : delete the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHashTag(@PathVariable("id") Long id) {
        log.debug("REST request to delete HashTag : {}", id);
        return hashTagService
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
     * {@code SEARCH  /hash-tags/_search?query=:query} : search for the hashTag corresponding
     * to the query.
     *
     * @param query the query of the hashTag search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HashTagDTO>>> searchHashTags(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HashTags for query {}", query);
        return hashTagService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(hashTagService.search(query, pageable)));
    }
}
