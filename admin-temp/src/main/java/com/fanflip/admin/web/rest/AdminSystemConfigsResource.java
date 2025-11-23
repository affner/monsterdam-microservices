package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AdminSystemConfigsRepository;
import com.fanflip.admin.service.AdminSystemConfigsService;
import com.fanflip.admin.service.dto.AdminSystemConfigsDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.AdminSystemConfigs}.
 */
@RestController
@RequestMapping("/api/admin-system-configs")
public class AdminSystemConfigsResource {

    private final Logger log = LoggerFactory.getLogger(AdminSystemConfigsResource.class);

    private static final String ENTITY_NAME = "adminSystemConfigs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminSystemConfigsService adminSystemConfigsService;

    private final AdminSystemConfigsRepository adminSystemConfigsRepository;

    public AdminSystemConfigsResource(
        AdminSystemConfigsService adminSystemConfigsService,
        AdminSystemConfigsRepository adminSystemConfigsRepository
    ) {
        this.adminSystemConfigsService = adminSystemConfigsService;
        this.adminSystemConfigsRepository = adminSystemConfigsRepository;
    }

    /**
     * {@code POST  /admin-system-configs} : Create a new adminSystemConfigs.
     *
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminSystemConfigsDTO, or with status {@code 400 (Bad Request)} if the adminSystemConfigs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AdminSystemConfigsDTO>> createAdminSystemConfigs(
        @Valid @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AdminSystemConfigs : {}", adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminSystemConfigs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return adminSystemConfigsService
            .save(adminSystemConfigsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/admin-system-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /admin-system-configs/:id} : Updates an existing adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to save.
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminSystemConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminSystemConfigsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminSystemConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AdminSystemConfigsDTO>> updateAdminSystemConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdminSystemConfigs : {}, {}", id, adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminSystemConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminSystemConfigsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return adminSystemConfigsService
                    .update(adminSystemConfigsDTO)
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
     * {@code PATCH  /admin-system-configs/:id} : Partial updates given fields of an existing adminSystemConfigs, field will ignore if it is null
     *
     * @param id the id of the adminSystemConfigsDTO to save.
     * @param adminSystemConfigsDTO the adminSystemConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminSystemConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminSystemConfigsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminSystemConfigsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminSystemConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AdminSystemConfigsDTO>> partialUpdateAdminSystemConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminSystemConfigs partially : {}, {}", id, adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminSystemConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminSystemConfigsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AdminSystemConfigsDTO> result = adminSystemConfigsService.partialUpdate(adminSystemConfigsDTO);

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
     * {@code GET  /admin-system-configs} : get all the adminSystemConfigs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminSystemConfigs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AdminSystemConfigsDTO>>> getAllAdminSystemConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AdminSystemConfigs");
        return adminSystemConfigsService
            .countAll()
            .zipWith(adminSystemConfigsService.findAll(pageable).collectList())
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
     * {@code GET  /admin-system-configs/:id} : get the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminSystemConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminSystemConfigsDTO>> getAdminSystemConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminSystemConfigs : {}", id);
        Mono<AdminSystemConfigsDTO> adminSystemConfigsDTO = adminSystemConfigsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminSystemConfigsDTO);
    }

    /**
     * {@code DELETE  /admin-system-configs/:id} : delete the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAdminSystemConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminSystemConfigs : {}", id);
        return adminSystemConfigsService
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
     * {@code SEARCH  /admin-system-configs/_search?query=:query} : search for the adminSystemConfigs corresponding
     * to the query.
     *
     * @param query the query of the adminSystemConfigs search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AdminSystemConfigsDTO>>> searchAdminSystemConfigs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of AdminSystemConfigs for query {}", query);
        return adminSystemConfigsService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(adminSystemConfigsService.search(query, pageable)));
    }
}
