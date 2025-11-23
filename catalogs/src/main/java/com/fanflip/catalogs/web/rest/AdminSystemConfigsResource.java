package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.AdminSystemConfigsRepository;
import com.fanflip.catalogs.service.AdminSystemConfigsService;
import com.fanflip.catalogs.service.dto.AdminSystemConfigsDTO;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.AdminSystemConfigs}.
 */
@RestController
@RequestMapping("/api/admin-system-configs")
public class AdminSystemConfigsResource {

    private final Logger log = LoggerFactory.getLogger(AdminSystemConfigsResource.class);

    private static final String ENTITY_NAME = "catalogsAdminSystemConfigs";

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
    public ResponseEntity<AdminSystemConfigsDTO> createAdminSystemConfigs(@Valid @RequestBody AdminSystemConfigsDTO adminSystemConfigsDTO)
        throws URISyntaxException {
        log.debug("REST request to save AdminSystemConfigs : {}", adminSystemConfigsDTO);
        if (adminSystemConfigsDTO.getId() != null) {
            throw new BadRequestAlertException("A new adminSystemConfigs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminSystemConfigsDTO result = adminSystemConfigsService.save(adminSystemConfigsDTO);
        return ResponseEntity
            .created(new URI("/api/admin-system-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<AdminSystemConfigsDTO> updateAdminSystemConfigs(
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

        if (!adminSystemConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdminSystemConfigsDTO result = adminSystemConfigsService.update(adminSystemConfigsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminSystemConfigsDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<AdminSystemConfigsDTO> partialUpdateAdminSystemConfigs(
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

        if (!adminSystemConfigsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdminSystemConfigsDTO> result = adminSystemConfigsService.partialUpdate(adminSystemConfigsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminSystemConfigsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /admin-system-configs} : get all the adminSystemConfigs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminSystemConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdminSystemConfigsDTO>> getAllAdminSystemConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AdminSystemConfigs");
        Page<AdminSystemConfigsDTO> page = adminSystemConfigsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-system-configs/:id} : get the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminSystemConfigsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminSystemConfigsDTO> getAdminSystemConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to get AdminSystemConfigs : {}", id);
        Optional<AdminSystemConfigsDTO> adminSystemConfigsDTO = adminSystemConfigsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adminSystemConfigsDTO);
    }

    /**
     * {@code DELETE  /admin-system-configs/:id} : delete the "id" adminSystemConfigs.
     *
     * @param id the id of the adminSystemConfigsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminSystemConfigs(@PathVariable("id") Long id) {
        log.debug("REST request to delete AdminSystemConfigs : {}", id);
        adminSystemConfigsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
