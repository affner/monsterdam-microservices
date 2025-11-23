package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.SingleAudioRepository;
import com.fanflip.admin.service.SingleAudioService;
import com.fanflip.admin.service.dto.SingleAudioDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.SingleAudio}.
 */
@RestController
@RequestMapping("/api/single-audios")
public class SingleAudioResource {

    private final Logger log = LoggerFactory.getLogger(SingleAudioResource.class);

    private static final String ENTITY_NAME = "singleAudio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleAudioService singleAudioService;

    private final SingleAudioRepository singleAudioRepository;

    public SingleAudioResource(SingleAudioService singleAudioService, SingleAudioRepository singleAudioRepository) {
        this.singleAudioService = singleAudioService;
        this.singleAudioRepository = singleAudioRepository;
    }

    /**
     * {@code POST  /single-audios} : Create a new singleAudio.
     *
     * @param singleAudioDTO the singleAudioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleAudioDTO, or with status {@code 400 (Bad Request)} if the singleAudio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SingleAudioDTO>> createSingleAudio(@Valid @RequestBody SingleAudioDTO singleAudioDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleAudio : {}", singleAudioDTO);
        if (singleAudioDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleAudio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singleAudioService
            .save(singleAudioDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-audios/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-audios/:id} : Updates an existing singleAudio.
     *
     * @param id the id of the singleAudioDTO to save.
     * @param singleAudioDTO the singleAudioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleAudioDTO,
     * or with status {@code 400 (Bad Request)} if the singleAudioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleAudioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SingleAudioDTO>> updateSingleAudio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleAudioDTO singleAudioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleAudio : {}, {}", id, singleAudioDTO);
        if (singleAudioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleAudioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleAudioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singleAudioService
                    .update(singleAudioDTO)
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
     * {@code PATCH  /single-audios/:id} : Partial updates given fields of an existing singleAudio, field will ignore if it is null
     *
     * @param id the id of the singleAudioDTO to save.
     * @param singleAudioDTO the singleAudioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleAudioDTO,
     * or with status {@code 400 (Bad Request)} if the singleAudioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleAudioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleAudioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SingleAudioDTO>> partialUpdateSingleAudio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleAudioDTO singleAudioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleAudio partially : {}, {}", id, singleAudioDTO);
        if (singleAudioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleAudioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singleAudioRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SingleAudioDTO> result = singleAudioService.partialUpdate(singleAudioDTO);

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
     * {@code GET  /single-audios} : get all the singleAudios.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleAudios in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SingleAudioDTO>>> getAllSingleAudios(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("contentpackage-is-null".equals(filter)) {
            log.debug("REST request to get all SingleAudios where contentPackage is null");
            return singleAudioService.findAllWhereContentPackageIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of SingleAudios");
        return singleAudioService
            .countAll()
            .zipWith(singleAudioService.findAll(pageable).collectList())
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
     * {@code GET  /single-audios/:id} : get the "id" singleAudio.
     *
     * @param id the id of the singleAudioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleAudioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SingleAudioDTO>> getSingleAudio(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleAudio : {}", id);
        Mono<SingleAudioDTO> singleAudioDTO = singleAudioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleAudioDTO);
    }

    /**
     * {@code DELETE  /single-audios/:id} : delete the "id" singleAudio.
     *
     * @param id the id of the singleAudioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSingleAudio(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleAudio : {}", id);
        return singleAudioService
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
     * {@code SEARCH  /single-audios/_search?query=:query} : search for the singleAudio corresponding
     * to the query.
     *
     * @param query the query of the singleAudio search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SingleAudioDTO>>> searchSingleAudios(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SingleAudios for query {}", query);
        return singleAudioService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(singleAudioService.search(query, pageable)));
    }
}
