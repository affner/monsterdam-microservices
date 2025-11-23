package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AdminAnnouncementRepository;
import com.fanflip.admin.service.AdminAnnouncementService;
import com.fanflip.admin.service.dto.AdminAnnouncementDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.AdminAnnouncement}.
 */
@RestController
@RequestMapping("/api/admin-announcements")
public class AdminAnnouncementResource {

    private final Logger log = LoggerFactory.getLogger(AdminAnnouncementResource.class);

    private static final String ENTITY_NAME = "adminAnnouncement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminAnnouncementService adminAnnouncementService;

    private final AdminAnnouncementRepository adminAnnouncementRepository;

    public AdminAnnouncementResource(
        AdminAnnouncementService adminAnnouncementService,
        AdminAnnouncementRepository adminAnnouncementRepository
    ) {
        this.adminAnnouncementService = adminAnnouncementService;
        this.adminAnnouncementRepository = adminAnnouncementRepository;
    }

    /**
     * {@code POST  /admin-announcements} : Create a new adminAnnouncement.
     *
     * @param adminAnnouncementDTO the adminAnnouncementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminAnnouncementDTO, or with status {@code 400 (Bad Request)} if the adminAnnouncement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AdminAnnouncementDTO>> createAdminAnnouncement(
        @Valid @RequestBody AdminAnnouncementDTO adminAnnouncementDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AdminAnnouncement : {}", adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminAnnouncement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return adminAnnouncementService
            .save(adminAnnouncementDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/admin-announcements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /admin-announcements/:id} : Updates an existing adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to save.
     * @param adminAnnouncementDTO the adminAnnouncementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAnnouncementDTO,
     * or with status {@code 400 (Bad Request)} if the adminAnnouncementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminAnnouncementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AdminAnnouncementDTO>> updateAdminAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdminAnnouncementDTO adminAnnouncementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AdminAnnouncement : {}, {}", id, adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAnnouncementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminAnnouncementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return adminAnnouncementService
                    .update(adminAnnouncementDTO)
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
     * {@code PATCH  /admin-announcements/:id} : Partial updates given fields of an existing adminAnnouncement, field will ignore if it is null
     *
     * @param id the id of the adminAnnouncementDTO to save.
     * @param adminAnnouncementDTO the adminAnnouncementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminAnnouncementDTO,
     * or with status {@code 400 (Bad Request)} if the adminAnnouncementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adminAnnouncementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminAnnouncementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AdminAnnouncementDTO>> partialUpdateAdminAnnouncement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdminAnnouncementDTO adminAnnouncementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminAnnouncement partially : {}, {}", id, adminAnnouncementDTO);
        if (adminAnnouncementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adminAnnouncementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return adminAnnouncementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AdminAnnouncementDTO> result = adminAnnouncementService.partialUpdate(adminAnnouncementDTO);

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
     * {@code GET  /admin-announcements} : get all the adminAnnouncements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminAnnouncements in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AdminAnnouncementDTO>> getAllAdminAnnouncements() {
        log.debug("REST request to get all AdminAnnouncements");
        return adminAnnouncementService.findAll().collectList();
    }

    /**
     * {@code GET  /admin-announcements} : get all the adminAnnouncements as a stream.
     * @return the {@link Flux} of adminAnnouncements.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AdminAnnouncementDTO> getAllAdminAnnouncementsAsStream() {
        log.debug("REST request to get all AdminAnnouncements as a stream");
        return adminAnnouncementService.findAll();
    }

    /**
     * {@code GET  /admin-announcements/:id} : get the "id" adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminAnnouncementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AdminAnnouncementDTO>> getAdminAnnouncement(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminAnnouncement : {}", id);
        Mono<AdminAnnouncementDTO> adminAnnouncementDTO = adminAnnouncementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminAnnouncementDTO);
    }

    /**
     * {@code DELETE  /admin-announcements/:id} : delete the "id" adminAnnouncement.
     *
     * @param id the id of the adminAnnouncementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAdminAnnouncement(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminAnnouncement : {}", id);
        return adminAnnouncementService
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
     * {@code SEARCH  /admin-announcements/_search?query=:query} : search for the adminAnnouncement corresponding
     * to the query.
     *
     * @param query the query of the adminAnnouncement search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<AdminAnnouncementDTO>> searchAdminAnnouncements(@RequestParam("query") String query) {
        log.debug("REST request to search AdminAnnouncements for query {}", query);
        try {
            return adminAnnouncementService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
