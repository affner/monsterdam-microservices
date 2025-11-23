package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.SpecialTitleRepository;
import com.fanflip.catalogs.service.SpecialTitleService;
import com.fanflip.catalogs.service.dto.SpecialTitleDTO;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.SpecialTitle}.
 */
@RestController
@RequestMapping("/api/special-titles")
public class SpecialTitleResource {

    private final Logger log = LoggerFactory.getLogger(SpecialTitleResource.class);

    private static final String ENTITY_NAME = "catalogsSpecialTitle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialTitleService specialTitleService;

    private final SpecialTitleRepository specialTitleRepository;

    public SpecialTitleResource(SpecialTitleService specialTitleService, SpecialTitleRepository specialTitleRepository) {
        this.specialTitleService = specialTitleService;
        this.specialTitleRepository = specialTitleRepository;
    }

    /**
     * {@code POST  /special-titles} : Create a new specialTitle.
     *
     * @param specialTitleDTO the specialTitleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialTitleDTO, or with status {@code 400 (Bad Request)} if the specialTitle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialTitleDTO> createSpecialTitle(@Valid @RequestBody SpecialTitleDTO specialTitleDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpecialTitle : {}", specialTitleDTO);
        if (specialTitleDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialTitle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialTitleDTO result = specialTitleService.save(specialTitleDTO);
        return ResponseEntity
            .created(new URI("/api/special-titles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /special-titles/:id} : Updates an existing specialTitle.
     *
     * @param id the id of the specialTitleDTO to save.
     * @param specialTitleDTO the specialTitleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialTitleDTO,
     * or with status {@code 400 (Bad Request)} if the specialTitleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialTitleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialTitleDTO> updateSpecialTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialTitleDTO specialTitleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpecialTitle : {}, {}", id, specialTitleDTO);
        if (specialTitleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialTitleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialTitleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialTitleDTO result = specialTitleService.update(specialTitleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialTitleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /special-titles/:id} : Partial updates given fields of an existing specialTitle, field will ignore if it is null
     *
     * @param id the id of the specialTitleDTO to save.
     * @param specialTitleDTO the specialTitleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialTitleDTO,
     * or with status {@code 400 (Bad Request)} if the specialTitleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialTitleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialTitleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialTitleDTO> partialUpdateSpecialTitle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialTitleDTO specialTitleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecialTitle partially : {}, {}", id, specialTitleDTO);
        if (specialTitleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialTitleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialTitleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialTitleDTO> result = specialTitleService.partialUpdate(specialTitleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialTitleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /special-titles} : get all the specialTitles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialTitles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialTitleDTO>> getAllSpecialTitles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SpecialTitles");
        Page<SpecialTitleDTO> page = specialTitleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /special-titles/:id} : get the "id" specialTitle.
     *
     * @param id the id of the specialTitleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialTitleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialTitleDTO> getSpecialTitle(@PathVariable("id") Long id) {
        log.debug("REST request to get SpecialTitle : {}", id);
        Optional<SpecialTitleDTO> specialTitleDTO = specialTitleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialTitleDTO);
    }

    /**
     * {@code DELETE  /special-titles/:id} : delete the "id" specialTitle.
     *
     * @param id the id of the specialTitleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialTitle(@PathVariable("id") Long id) {
        log.debug("REST request to delete SpecialTitle : {}", id);
        specialTitleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
