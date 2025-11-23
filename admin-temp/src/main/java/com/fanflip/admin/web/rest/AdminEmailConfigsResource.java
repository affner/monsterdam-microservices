package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AdminEmailConfigsRepository;
import com.fanflip.admin.service.AdminEmailConfigsService;
import com.fanflip.admin.service.dto.AdminEmailConfigsDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.AdminEmailConfigs}.
 */
@RestController
@RequestMapping("/api/admin-email-configs")
public class AdminEmailConfigsResource {

    private final Logger log = LoggerFactory.getLogger(AdminEmailConfigsResource.class);

    private static final String ENTITY_NAME = "adminEmailConfigs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminEmailConfigsService adminEmailConfigsService;

    private final AdminEmailConfigsRepository adminEmailConfigsRepository;

    public AdminEmailConfigsResource(
        AdminEmailConfigsService adminEmailConfigsService,
        AdminEmailConfigsRepository adminEmailConfigsRepository
    ) {
        this.adminEmailConfigsService = adminEmailConfigsService;
        this.adminEmailConfigsRepository = adminEmailConfigsRepository;
    }

    /**
     * {@code POST  /admin-email-configs} : Create a new adminEmailConfigs.
     *
     * @param adminEmailConfigsDTO the adminEmailConfigsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminEmailConfigsDTO, or with status {@code 400 (Bad Request)} if the adminEmailConfigs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AdminEmailConfigsDTO>> createAdminEmailConfigs(
        @Valid @RequestBody AdminEmailConfigsDTO adminEmailConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AdminEmailConfigs : {}", adminEmailConfigsDTO);
        if (adminEmailConfigsDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminEmailConfigs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return adminEmailConfigsService
            .save(adminEmailConfigsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/admin-email-configs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /admin-email-configs/:id} : Updates an existing adminEmailConfigs.
     *
     * @param id the id of the adminEmailConfigsDTO to save.
     * @param adminEmailConfigsDTO the adminEmailConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminEmailConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminEmailConfigsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminEmailConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AdminEmailConfigsDTO>> updateAdminEmailConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminEmailConfigsDTO adminEmailConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdminEmailConfigs : {}, {}", id, adminEmailConfigsDTO);
        if (adminEmailConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminEmailConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminEmailConfigsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return adminEmailConfigsService
                    .update(adminEmailConfigsDTO)
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
     * {@code PATCH  /admin-email-configs/:id} : Partial updates given fields of an existing adminEmailConfigs, field will ignore if it is null
     *
     * @param id the id of the adminEmailConfigsDTO to save.
     * @param adminEmailConfigsDTO the adminEmailConfigsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminEmailConfigsDTO,
     * or with status {@code 400 (Bad Request)} if the adminEmailConfigsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminEmailConfigsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminEmailConfigsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AdminEmailConfigsDTO>> partialUpdateAdminEmailConfigs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminEmailConfigsDTO adminEmailConfigsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminEmailConfigs partially : {}, {}", id, adminEmailConfigsDTO);
        if (adminEmailConfigsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminEmailConfigsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminEmailConfigsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AdminEmailConfigsDTO> result = adminEmailConfigsService.partialUpdate(adminEmailConfigsDTO);

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
     * {@code GET  /admin-email-configs} : get all the adminEmailConfigs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminEmailConfigs in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<AdminEmailConfigsDTO>>> getAllAdminEmailConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AdminEmailConfigs");
        return adminEmailConfigsService
            .countAll()
            .zipWith(adminEmailConfigsService.findAll(pageable).collectList())
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
     * {@code GET  /admin-email-configs/:id} : get the "id" adminEmailConfigs.
     *
     * @param id the id of the adminEmailConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminEmailConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminEmailConfigsDTO>> getAdminEmailConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminEmailConfigs : {}", id);
        Mono<AdminEmailConfigsDTO> adminEmailConfigsDTO = adminEmailConfigsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminEmailConfigsDTO);
    }

    /**
     * {@code DELETE  /admin-email-configs/:id} : delete the "id" adminEmailConfigs.
     *
     * @param id the id of the adminEmailConfigsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAdminEmailConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminEmailConfigs : {}", id);
        return adminEmailConfigsService
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
     * {@code SEARCH  /admin-email-configs/_search?query=:query} : search for the adminEmailConfigs corresponding
     * to the query.
     *
     * @param query the query of the adminEmailConfigs search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<AdminEmailConfigsDTO>>> searchAdminEmailConfigs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of AdminEmailConfigs for query {}", query);
        return adminEmailConfigsService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(adminEmailConfigsService.search(query, pageable)));
    }
}
