package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.UserSettingsRepository;
import com.fanflip.admin.service.UserSettingsService;
import com.fanflip.admin.service.dto.UserSettingsDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.UserSettings}.
 */
@RestController
@RequestMapping("/api/user-settings")
public class UserSettingsResource {

    private final Logger log = LoggerFactory.getLogger(UserSettingsResource.class);

    private static final String ENTITY_NAME = "userSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSettingsService userSettingsService;

    private final UserSettingsRepository userSettingsRepository;

    public UserSettingsResource(UserSettingsService userSettingsService, UserSettingsRepository userSettingsRepository) {
        this.userSettingsService = userSettingsService;
        this.userSettingsRepository = userSettingsRepository;
    }

    /**
     * {@code POST  /user-settings} : Create a new userSettings.
     *
     * @param userSettingsDTO the userSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSettingsDTO, or with status {@code 400 (Bad Request)} if the userSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserSettingsDTO>> createUserSettings(@Valid @RequestBody UserSettingsDTO userSettingsDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserSettings : {}", userSettingsDTO);
        if (userSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userSettingsService
            .save(userSettingsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-settings/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-settings/:id} : Updates an existing userSettings.
     *
     * @param id the id of the userSettingsDTO to save.
     * @param userSettingsDTO the userSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the userSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserSettingsDTO>> updateUserSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSettingsDTO userSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserSettings : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userSettingsService
                    .update(userSettingsDTO)
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
     * {@code PATCH  /user-settings/:id} : Partial updates given fields of an existing userSettings, field will ignore if it is null
     *
     * @param id the id of the userSettingsDTO to save.
     * @param userSettingsDTO the userSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the userSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserSettingsDTO>> partialUpdateUserSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSettingsDTO userSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSettings partially : {}, {}", id, userSettingsDTO);
        if (userSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userSettingsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserSettingsDTO> result = userSettingsService.partialUpdate(userSettingsDTO);

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
     * {@code GET  /user-settings} : get all the userSettings.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSettings in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserSettingsDTO>>> getAllUserSettings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("userprofile-is-null".equals(filter)) {
            log.debug("REST request to get all UserSettingss where userProfile is null");
            return userSettingsService.findAllWhereUserProfileIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of UserSettings");
        return userSettingsService
            .countAll()
            .zipWith(userSettingsService.findAll(pageable).collectList())
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
     * {@code GET  /user-settings/:id} : get the "id" userSettings.
     *
     * @param id the id of the userSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserSettingsDTO>> getUserSettings(@PathVariable("id") Long id) {
        log.debug("REST request to get UserSettings : {}", id);
        Mono<UserSettingsDTO> userSettingsDTO = userSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSettingsDTO);
    }

    /**
     * {@code DELETE  /user-settings/:id} : delete the "id" userSettings.
     *
     * @param id the id of the userSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserSettings(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserSettings : {}", id);
        return userSettingsService
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
     * {@code SEARCH  /user-settings/_search?query=:query} : search for the userSettings corresponding
     * to the query.
     *
     * @param query the query of the userSettings search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserSettingsDTO>>> searchUserSettings(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserSettings for query {}", query);
        return userSettingsService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userSettingsService.search(query, pageable)));
    }
}
