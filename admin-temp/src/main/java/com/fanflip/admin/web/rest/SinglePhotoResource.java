package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.SinglePhotoRepository;
import com.fanflip.admin.service.SinglePhotoService;
import com.fanflip.admin.service.dto.SinglePhotoDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.SinglePhoto}.
 */
@RestController
@RequestMapping("/api/single-photos")
public class SinglePhotoResource {

    private final Logger log = LoggerFactory.getLogger(SinglePhotoResource.class);

    private static final String ENTITY_NAME = "singlePhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SinglePhotoService singlePhotoService;

    private final SinglePhotoRepository singlePhotoRepository;

    public SinglePhotoResource(SinglePhotoService singlePhotoService, SinglePhotoRepository singlePhotoRepository) {
        this.singlePhotoService = singlePhotoService;
        this.singlePhotoRepository = singlePhotoRepository;
    }

    /**
     * {@code POST  /single-photos} : Create a new singlePhoto.
     *
     * @param singlePhotoDTO the singlePhotoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singlePhotoDTO, or with status {@code 400 (Bad Request)} if the singlePhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<SinglePhotoDTO>> createSinglePhoto(@Valid @RequestBody SinglePhotoDTO singlePhotoDTO)
        throws URISyntaxException {
        log.debug("REST request to save SinglePhoto : {}", singlePhotoDTO);
        if (singlePhotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new singlePhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return singlePhotoService
            .save(singlePhotoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/single-photos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /single-photos/:id} : Updates an existing singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to save.
     * @param singlePhotoDTO the singlePhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singlePhotoDTO,
     * or with status {@code 400 (Bad Request)} if the singlePhotoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singlePhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<SinglePhotoDTO>> updateSinglePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SinglePhotoDTO singlePhotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SinglePhoto : {}, {}", id, singlePhotoDTO);
        if (singlePhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singlePhotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singlePhotoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return singlePhotoService
                    .update(singlePhotoDTO)
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
     * {@code PATCH  /single-photos/:id} : Partial updates given fields of an existing singlePhoto, field will ignore if it is null
     *
     * @param id the id of the singlePhotoDTO to save.
     * @param singlePhotoDTO the singlePhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singlePhotoDTO,
     * or with status {@code 400 (Bad Request)} if the singlePhotoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singlePhotoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singlePhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SinglePhotoDTO>> partialUpdateSinglePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SinglePhotoDTO singlePhotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SinglePhoto partially : {}, {}", id, singlePhotoDTO);
        if (singlePhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singlePhotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return singlePhotoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SinglePhotoDTO> result = singlePhotoService.partialUpdate(singlePhotoDTO);

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
     * {@code GET  /single-photos} : get all the singlePhotos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singlePhotos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<SinglePhotoDTO>>> getAllSinglePhotos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SinglePhotos");
        return singlePhotoService
            .countAll()
            .zipWith(singlePhotoService.findAll(pageable).collectList())
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
     * {@code GET  /single-photos/:id} : get the "id" singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singlePhotoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<SinglePhotoDTO>> getSinglePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to get SinglePhoto : {}", id);
        Mono<SinglePhotoDTO> singlePhotoDTO = singlePhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singlePhotoDTO);
    }

    /**
     * {@code DELETE  /single-photos/:id} : delete the "id" singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteSinglePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to delete SinglePhoto : {}", id);
        return singlePhotoService
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
     * {@code SEARCH  /single-photos/_search?query=:query} : search for the singlePhoto corresponding
     * to the query.
     *
     * @param query the query of the singlePhoto search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<SinglePhotoDTO>>> searchSinglePhotos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of SinglePhotos for query {}", query);
        return singlePhotoService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(singlePhotoService.search(query, pageable)));
    }
}
