package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.UserLiteRepository;
import com.fanflip.admin.service.UserLiteService;
import com.fanflip.admin.service.dto.UserLiteDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.UserLite}.
 */
@RestController
@RequestMapping("/api/user-lites")
public class UserLiteResource {

    private final Logger log = LoggerFactory.getLogger(UserLiteResource.class);

    private static final String ENTITY_NAME = "userLite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserLiteService userLiteService;

    private final UserLiteRepository userLiteRepository;

    public UserLiteResource(UserLiteService userLiteService, UserLiteRepository userLiteRepository) {
        this.userLiteService = userLiteService;
        this.userLiteRepository = userLiteRepository;
    }

    /**
     * {@code POST  /user-lites} : Create a new userLite.
     *
     * @param userLiteDTO the userLiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userLiteDTO, or with status {@code 400 (Bad Request)} if the userLite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserLiteDTO>> createUserLite(@Valid @RequestBody UserLiteDTO userLiteDTO) throws URISyntaxException {
        log.debug("REST request to save UserLite : {}", userLiteDTO);
        if (userLiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new userLite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userLiteService
            .save(userLiteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-lites/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-lites/:id} : Updates an existing userLite.
     *
     * @param id the id of the userLiteDTO to save.
     * @param userLiteDTO the userLiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLiteDTO,
     * or with status {@code 400 (Bad Request)} if the userLiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userLiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserLiteDTO>> updateUserLite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserLiteDTO userLiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserLite : {}, {}", id, userLiteDTO);
        if (userLiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userLiteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userLiteService
                    .update(userLiteDTO)
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
     * {@code PATCH  /user-lites/:id} : Partial updates given fields of an existing userLite, field will ignore if it is null
     *
     * @param id the id of the userLiteDTO to save.
     * @param userLiteDTO the userLiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLiteDTO,
     * or with status {@code 400 (Bad Request)} if the userLiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userLiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userLiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserLiteDTO>> partialUpdateUserLite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserLiteDTO userLiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserLite partially : {}, {}", id, userLiteDTO);
        if (userLiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userLiteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserLiteDTO> result = userLiteService.partialUpdate(userLiteDTO);

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
     * {@code GET  /user-lites} : get all the userLites.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userLites in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserLiteDTO>>> getAllUserLites(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("userprofile-is-null".equals(filter)) {
            log.debug("REST request to get all UserLites where userProfile is null");
            return userLiteService.findAllWhereUserProfileIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of UserLites");
        return userLiteService
            .countAll()
            .zipWith(userLiteService.findAll(pageable).collectList())
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
     * {@code GET  /user-lites/:id} : get the "id" userLite.
     *
     * @param id the id of the userLiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userLiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserLiteDTO>> getUserLite(@PathVariable("id") Long id) {
        log.debug("REST request to get UserLite : {}", id);
        Mono<UserLiteDTO> userLiteDTO = userLiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userLiteDTO);
    }

    /**
     * {@code DELETE  /user-lites/:id} : delete the "id" userLite.
     *
     * @param id the id of the userLiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserLite(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserLite : {}", id);
        return userLiteService
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
     * {@code SEARCH  /user-lites/_search?query=:query} : search for the userLite corresponding
     * to the query.
     *
     * @param query the query of the userLite search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserLiteDTO>>> searchUserLites(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserLites for query {}", query);
        return userLiteService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userLiteService.search(query, pageable)));
    }
}
