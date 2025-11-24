package com.monsterdam.notifications.web.rest;

import com.monsterdam.notifications.repository.AppNotificationRepository;
import com.monsterdam.notifications.service.AppNotificationService;
import com.monsterdam.notifications.service.dto.AppNotificationDTO;
import com.monsterdam.notifications.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.notifications.domain.AppNotification}.
 */
@RestController
@RequestMapping("/api/app-notifications")
public class AppNotificationResource {

    private final Logger log = LoggerFactory.getLogger(AppNotificationResource.class);

    private static final String ENTITY_NAME = "notificationsAppNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppNotificationService appNotificationService;

    private final AppNotificationRepository appNotificationRepository;

    public AppNotificationResource(AppNotificationService appNotificationService, AppNotificationRepository appNotificationRepository) {
        this.appNotificationService = appNotificationService;
        this.appNotificationRepository = appNotificationRepository;
    }

    /**
     * {@code POST  /app-notifications} : Create a new appNotification.
     *
     * @param appNotificationDTO the appNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appNotificationDTO, or with status {@code 400 (Bad Request)} if the appNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppNotificationDTO> createAppNotification(@Valid @RequestBody AppNotificationDTO appNotificationDTO)
        throws URISyntaxException {
        log.debug("REST request to save AppNotification : {}", appNotificationDTO);
        if (appNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new appNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppNotificationDTO result = appNotificationService.save(appNotificationDTO);
        return ResponseEntity
            .created(new URI("/api/app-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-notifications/:id} : Updates an existing appNotification.
     *
     * @param id the id of the appNotificationDTO to save.
     * @param appNotificationDTO the appNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the appNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppNotificationDTO> updateAppNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppNotificationDTO appNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppNotification : {}, {}", id, appNotificationDTO);
        if (appNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppNotificationDTO result = appNotificationService.update(appNotificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appNotificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /app-notifications/:id} : Partial updates given fields of an existing appNotification, field will ignore if it is null
     *
     * @param id the id of the appNotificationDTO to save.
     * @param appNotificationDTO the appNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the appNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppNotificationDTO> partialUpdateAppNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppNotificationDTO appNotificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppNotification partially : {}, {}", id, appNotificationDTO);
        if (appNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppNotificationDTO> result = appNotificationService.partialUpdate(appNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-notifications} : get all the appNotifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppNotificationDTO>> getAllAppNotifications(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AppNotifications");
        Page<AppNotificationDTO> page = appNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-notifications/:id} : get the "id" appNotification.
     *
     * @param id the id of the appNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppNotificationDTO> getAppNotification(@PathVariable("id") Long id) {
        log.debug("REST request to get AppNotification : {}", id);
        Optional<AppNotificationDTO> appNotificationDTO = appNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appNotificationDTO);
    }

    /**
     * {@code DELETE  /app-notifications/:id} : delete the "id" appNotification.
     *
     * @param id the id of the appNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppNotification(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppNotification : {}", id);
        appNotificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
