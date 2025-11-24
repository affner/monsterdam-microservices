package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.UserUIPreferencesRepository;
import com.monsterdam.app.service.UserUIPreferencesService;
import com.monsterdam.app.service.dto.UserUIPreferencesDTO;
import com.monsterdam.app.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.app.domain.UserUIPreferences}.
 */
@RestController
@RequestMapping("/api/user-ui-preferences")
public class UserUIPreferencesResource {

    private final Logger log = LoggerFactory.getLogger(UserUIPreferencesResource.class);

    private static final String ENTITY_NAME = "userUIPreferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserUIPreferencesService userUIPreferencesService;

    private final UserUIPreferencesRepository userUIPreferencesRepository;

    public UserUIPreferencesResource(
        UserUIPreferencesService userUIPreferencesService,
        UserUIPreferencesRepository userUIPreferencesRepository
    ) {
        this.userUIPreferencesService = userUIPreferencesService;
        this.userUIPreferencesRepository = userUIPreferencesRepository;
    }

    /**
     * {@code POST  /user-ui-preferences} : Create a new userUIPreferences.
     *
     * @param userUIPreferencesDTO the userUIPreferencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userUIPreferencesDTO, or with status {@code 400 (Bad Request)} if the userUIPreferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserUIPreferencesDTO>> createUserUIPreferences(
        @Valid @RequestBody UserUIPreferencesDTO userUIPreferencesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UserUIPreferences : {}", userUIPreferencesDTO);
        if (userUIPreferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new userUIPreferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userUIPreferencesService
            .save(userUIPreferencesDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-ui-preferences/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-ui-preferences/:id} : Updates an existing userUIPreferences.
     *
     * @param id the id of the userUIPreferencesDTO to save.
     * @param userUIPreferencesDTO the userUIPreferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userUIPreferencesDTO,
     * or with status {@code 400 (Bad Request)} if the userUIPreferencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userUIPreferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserUIPreferencesDTO>> updateUserUIPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserUIPreferencesDTO userUIPreferencesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserUIPreferences : {}, {}", id, userUIPreferencesDTO);
        if (userUIPreferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userUIPreferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userUIPreferencesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userUIPreferencesService
                    .update(userUIPreferencesDTO)
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
     * {@code PATCH  /user-ui-preferences/:id} : Partial updates given fields of an existing userUIPreferences, field will ignore if it is null
     *
     * @param id the id of the userUIPreferencesDTO to save.
     * @param userUIPreferencesDTO the userUIPreferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userUIPreferencesDTO,
     * or with status {@code 400 (Bad Request)} if the userUIPreferencesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userUIPreferencesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userUIPreferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserUIPreferencesDTO>> partialUpdateUserUIPreferences(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserUIPreferencesDTO userUIPreferencesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserUIPreferences partially : {}, {}", id, userUIPreferencesDTO);
        if (userUIPreferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userUIPreferencesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userUIPreferencesRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserUIPreferencesDTO> result = userUIPreferencesService.partialUpdate(userUIPreferencesDTO);

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
     * {@code GET  /user-ui-preferences} : get all the userUIPreferences.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userUIPreferences in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserUIPreferencesDTO>>> getAllUserUIPreferences(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of UserUIPreferences");
        return userUIPreferencesService
            .countAll()
            .zipWith(userUIPreferencesService.findAll(pageable).collectList())
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
     * {@code GET  /user-ui-preferences/:id} : get the "id" userUIPreferences.
     *
     * @param id the id of the userUIPreferencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userUIPreferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserUIPreferencesDTO>> getUserUIPreferences(@PathVariable("id") Long id) {
        log.debug("REST request to get UserUIPreferences : {}", id);
        Mono<UserUIPreferencesDTO> userUIPreferencesDTO = userUIPreferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userUIPreferencesDTO);
    }

    /**
     * {@code DELETE  /user-ui-preferences/:id} : delete the "id" userUIPreferences.
     *
     * @param id the id of the userUIPreferencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserUIPreferences(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserUIPreferences : {}", id);
        return userUIPreferencesService
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
     * {@code SEARCH  /user-ui-preferences/_search?query=:query} : search for the userUIPreferences corresponding
     * to the query.
     *
     * @param query the query of the userUIPreferences search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserUIPreferencesDTO>>> searchUserUIPreferences(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserUIPreferences for query {}", query);
        return userUIPreferencesService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userUIPreferencesService.search(query, pageable)));
    }
}
