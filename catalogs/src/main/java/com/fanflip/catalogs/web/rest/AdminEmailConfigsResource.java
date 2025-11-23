package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.AdminEmailConfigsRepository;
import com.fanflip.catalogs.service.AdminEmailConfigsService;
import com.fanflip.catalogs.service.dto.AdminEmailConfigsDTO;
import com.fanflip.catalogs.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.AdminEmailConfigs}.
 */
@RestController
@RequestMapping("/api/admin-email-configs")
public class AdminEmailConfigsResource {

    private final Logger log = LoggerFactory.getLogger(AdminEmailConfigsResource.class);

    private static final String ENTITY_NAME = "catalogsAdminEmailConfigs";

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
    public ResponseEntity<AdminEmailConfigsDTO> createAdminEmailConfigs(@Valid @RequestBody AdminEmailConfigsDTO adminEmailConfigsDTO)
        throws URISyntaxException {
        log.debug("REST request to save AdminEmailConfigs : {}", adminEmailConfigsDTO);
        if (adminEmailConfigsDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminEmailConfigs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminEmailConfigsDTO result = adminEmailConfigsService.save(adminEmailConfigsDTO);
        return ResponseEntity
            .created(new URI("/api/admin-email-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<AdminEmailConfigsDTO> updateAdminEmailConfigs(
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

        if (!adminEmailConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdminEmailConfigsDTO result = adminEmailConfigsService.update(adminEmailConfigsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminEmailConfigsDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<AdminEmailConfigsDTO> partialUpdateAdminEmailConfigs(
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

        if (!adminEmailConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminEmailConfigsDTO> result = adminEmailConfigsService.partialUpdate(adminEmailConfigsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminEmailConfigsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-email-configs} : get all the adminEmailConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminEmailConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdminEmailConfigsDTO>> getAllAdminEmailConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AdminEmailConfigs");
        Page<AdminEmailConfigsDTO> page = adminEmailConfigsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-email-configs/:id} : get the "id" adminEmailConfigs.
     *
     * @param id the id of the adminEmailConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminEmailConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminEmailConfigsDTO> getAdminEmailConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminEmailConfigs : {}", id);
        Optional<AdminEmailConfigsDTO> adminEmailConfigsDTO = adminEmailConfigsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminEmailConfigsDTO);
    }

    /**
     * {@code DELETE  /admin-email-configs/:id} : delete the "id" adminEmailConfigs.
     *
     * @param id the id of the adminEmailConfigsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminEmailConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminEmailConfigs : {}", id);
        adminEmailConfigsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
