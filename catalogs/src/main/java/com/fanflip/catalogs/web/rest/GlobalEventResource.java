package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.GlobalEventRepository;
import com.fanflip.catalogs.service.GlobalEventService;
import com.fanflip.catalogs.service.dto.GlobalEventDTO;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.GlobalEvent}.
 */
@RestController
@RequestMapping("/api/global-events")
public class GlobalEventResource {

    private final Logger log = LoggerFactory.getLogger(GlobalEventResource.class);

    private static final String ENTITY_NAME = "catalogsGlobalEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalEventService globalEventService;

    private final GlobalEventRepository globalEventRepository;

    public GlobalEventResource(GlobalEventService globalEventService, GlobalEventRepository globalEventRepository) {
        this.globalEventService = globalEventService;
        this.globalEventRepository = globalEventRepository;
    }

    /**
     * {@code POST  /global-events} : Create a new globalEvent.
     *
     * @param globalEventDTO the globalEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalEventDTO, or with status {@code 400 (Bad Request)} if the globalEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GlobalEventDTO> createGlobalEvent(@Valid @RequestBody GlobalEventDTO globalEventDTO) throws URISyntaxException {
        log.debug("REST request to save GlobalEvent : {}", globalEventDTO);
        if (globalEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalEventDTO result = globalEventService.save(globalEventDTO);
        return ResponseEntity
            .created(new URI("/api/global-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /global-events/:id} : Updates an existing globalEvent.
     *
     * @param id the id of the globalEventDTO to save.
     * @param globalEventDTO the globalEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalEventDTO,
     * or with status {@code 400 (Bad Request)} if the globalEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GlobalEventDTO> updateGlobalEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GlobalEventDTO globalEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GlobalEvent : {}, {}", id, globalEventDTO);
        if (globalEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GlobalEventDTO result = globalEventService.update(globalEventDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /global-events/:id} : Partial updates given fields of an existing globalEvent, field will ignore if it is null
     *
     * @param id the id of the globalEventDTO to save.
     * @param globalEventDTO the globalEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalEventDTO,
     * or with status {@code 400 (Bad Request)} if the globalEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the globalEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the globalEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GlobalEventDTO> partialUpdateGlobalEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GlobalEventDTO globalEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlobalEvent partially : {}, {}", id, globalEventDTO);
        if (globalEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlobalEventDTO> result = globalEventService.partialUpdate(globalEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /global-events} : get all the globalEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GlobalEventDTO>> getAllGlobalEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GlobalEvents");
        Page<GlobalEventDTO> page = globalEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /global-events/:id} : get the "id" globalEvent.
     *
     * @param id the id of the globalEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GlobalEventDTO> getGlobalEvent(@PathVariable("id") Long id) {
        log.debug("REST request to get GlobalEvent : {}", id);
        Optional<GlobalEventDTO> globalEventDTO = globalEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalEventDTO);
    }

    /**
     * {@code DELETE  /global-events/:id} : delete the "id" globalEvent.
     *
     * @param id the id of the globalEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGlobalEvent(@PathVariable("id") Long id) {
        log.debug("REST request to delete GlobalEvent : {}", id);
        globalEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
