package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.UserAssociationRepository;
import com.fanflip.admin.service.UserAssociationService;
import com.fanflip.admin.service.dto.UserAssociationDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.UserAssociation}.
 */
@RestController
@RequestMapping("/api/user-associations")
public class UserAssociationResource {

    private final Logger log = LoggerFactory.getLogger(UserAssociationResource.class);

    private static final String ENTITY_NAME = "userAssociation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAssociationService userAssociationService;

    private final UserAssociationRepository userAssociationRepository;

    public UserAssociationResource(UserAssociationService userAssociationService, UserAssociationRepository userAssociationRepository) {
        this.userAssociationService = userAssociationService;
        this.userAssociationRepository = userAssociationRepository;
    }

    /**
     * {@code POST  /user-associations} : Create a new userAssociation.
     *
     * @param userAssociationDTO the userAssociationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAssociationDTO, or with status {@code 400 (Bad Request)} if the userAssociation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserAssociationDTO>> createUserAssociation(@Valid @RequestBody UserAssociationDTO userAssociationDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserAssociation : {}", userAssociationDTO);
        if (userAssociationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAssociation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userAssociationService
            .save(userAssociationDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-associations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-associations/:id} : Updates an existing userAssociation.
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserAssociationDTO>> updateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAssociation : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAssociationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userAssociationService
                    .update(userAssociationDTO)
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
     * {@code PATCH  /user-associations/:id} : Partial updates given fields of an existing userAssociation, field will ignore if it is null
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAssociationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserAssociationDTO>> partialUpdateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAssociation partially : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userAssociationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserAssociationDTO> result = userAssociationService.partialUpdate(userAssociationDTO);

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
     * {@code GET  /user-associations} : get all the userAssociations.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAssociations in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserAssociationDTO>>> getAllUserAssociations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserAssociations");
        return userAssociationService
            .countAll()
            .zipWith(userAssociationService.findAll(pageable).collectList())
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
     * {@code GET  /user-associations/:id} : get the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserAssociationDTO>> getUserAssociation(@PathVariable("id") Long id) {
        log.debug("REST request to get UserAssociation : {}", id);
        Mono<UserAssociationDTO> userAssociationDTO = userAssociationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAssociationDTO);
    }

    /**
     * {@code DELETE  /user-associations/:id} : delete the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserAssociation(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserAssociation : {}", id);
        return userAssociationService
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
     * {@code SEARCH  /user-associations/_search?query=:query} : search for the userAssociation corresponding
     * to the query.
     *
     * @param query the query of the userAssociation search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserAssociationDTO>>> searchUserAssociations(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserAssociations for query {}", query);
        return userAssociationService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userAssociationService.search(query, pageable)));
    }
}
