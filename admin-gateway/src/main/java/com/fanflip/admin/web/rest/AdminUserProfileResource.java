package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AdminUserProfileRepository;
import com.fanflip.admin.service.AdminUserProfileService;
import com.fanflip.admin.service.dto.AdminUserProfileDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
import com.fanflip.admin.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.fanflip.admin.domain.AdminUserProfile}.
 */
@RestController
@RequestMapping("/api/admin-user-profiles")
public class AdminUserProfileResource {

    private final Logger log = LoggerFactory.getLogger(AdminUserProfileResource.class);

    private static final String ENTITY_NAME = "adminUserProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminUserProfileService adminUserProfileService;

    private final AdminUserProfileRepository adminUserProfileRepository;

    public AdminUserProfileResource(
        AdminUserProfileService adminUserProfileService,
        AdminUserProfileRepository adminUserProfileRepository
    ) {
        this.adminUserProfileService = adminUserProfileService;
        this.adminUserProfileRepository = adminUserProfileRepository;
    }

    /**
     * {@code POST  /admin-user-profiles} : Create a new adminUserProfile.
     *
     * @param adminUserProfileDTO the adminUserProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminUserProfileDTO, or with status {@code 400 (Bad Request)} if the adminUserProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AdminUserProfileDTO>> createAdminUserProfile(@Valid @RequestBody AdminUserProfileDTO adminUserProfileDTO)
        throws URISyntaxException {
        log.debug("REST request to save AdminUserProfile : {}", adminUserProfileDTO);
        if (adminUserProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminUserProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return adminUserProfileService
            .save(adminUserProfileDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/admin-user-profiles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /admin-user-profiles/:id} : Updates an existing adminUserProfile.
     *
     * @param id the id of the adminUserProfileDTO to save.
     * @param adminUserProfileDTO the adminUserProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminUserProfileDTO,
     * or with status {@code 400 (Bad Request)} if the adminUserProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminUserProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AdminUserProfileDTO>> updateAdminUserProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminUserProfileDTO adminUserProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdminUserProfile : {}, {}", id, adminUserProfileDTO);
        if (adminUserProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminUserProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminUserProfileRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return adminUserProfileService
                    .update(adminUserProfileDTO)
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
     * {@code PATCH  /admin-user-profiles/:id} : Partial updates given fields of an existing adminUserProfile, field will ignore if it is null
     *
     * @param id the id of the adminUserProfileDTO to save.
     * @param adminUserProfileDTO the adminUserProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminUserProfileDTO,
     * or with status {@code 400 (Bad Request)} if the adminUserProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminUserProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminUserProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AdminUserProfileDTO>> partialUpdateAdminUserProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminUserProfileDTO adminUserProfileDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminUserProfile partially : {}, {}", id, adminUserProfileDTO);
        if (adminUserProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminUserProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminUserProfileRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AdminUserProfileDTO> result = adminUserProfileService.partialUpdate(adminUserProfileDTO);

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
     * {@code GET  /admin-user-profiles} : get all the adminUserProfiles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminUserProfiles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AdminUserProfileDTO>> getAllAdminUserProfiles() {
        log.debug("REST request to get all AdminUserProfiles");
        return adminUserProfileService.findAll().collectList();
    }

    /**
     * {@code GET  /admin-user-profiles} : get all the adminUserProfiles as a stream.
     * @return the {@link Flux} of adminUserProfiles.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AdminUserProfileDTO> getAllAdminUserProfilesAsStream() {
        log.debug("REST request to get all AdminUserProfiles as a stream");
        return adminUserProfileService.findAll();
    }

    /**
     * {@code GET  /admin-user-profiles/:id} : get the "id" adminUserProfile.
     *
     * @param id the id of the adminUserProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminUserProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminUserProfileDTO>> getAdminUserProfile(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminUserProfile : {}", id);
        Mono<AdminUserProfileDTO> adminUserProfileDTO = adminUserProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminUserProfileDTO);
    }

    /**
     * {@code DELETE  /admin-user-profiles/:id} : delete the "id" adminUserProfile.
     *
     * @param id the id of the adminUserProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAdminUserProfile(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminUserProfile : {}", id);
        return adminUserProfileService
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
     * {@code SEARCH  /admin-user-profiles/_search?query=:query} : search for the adminUserProfile corresponding
     * to the query.
     *
     * @param query the query of the adminUserProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<AdminUserProfileDTO>> searchAdminUserProfiles(@RequestParam("query") String query) {
        log.debug("REST request to search AdminUserProfiles for query {}", query);
        try {
            return adminUserProfileService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
