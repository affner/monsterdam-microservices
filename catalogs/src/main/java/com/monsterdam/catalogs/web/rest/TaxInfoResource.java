package com.monsterdam.catalogs.web.rest;

import com.monsterdam.catalogs.repository.TaxInfoRepository;
import com.monsterdam.catalogs.service.TaxInfoService;
import com.monsterdam.catalogs.service.dto.TaxInfoDTO;
import com.monsterdam.catalogs.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.catalogs.domain.TaxInfo}.
 */
@RestController
@RequestMapping("/api/tax-infos")
public class TaxInfoResource {

    private final Logger log = LoggerFactory.getLogger(TaxInfoResource.class);

    private static final String ENTITY_NAME = "catalogsTaxInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxInfoService taxInfoService;

    private final TaxInfoRepository taxInfoRepository;

    public TaxInfoResource(TaxInfoService taxInfoService, TaxInfoRepository taxInfoRepository) {
        this.taxInfoService = taxInfoService;
        this.taxInfoRepository = taxInfoRepository;
    }

    /**
     * {@code POST  /tax-infos} : Create a new taxInfo.
     *
     * @param taxInfoDTO the taxInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxInfoDTO, or with status {@code 400 (Bad Request)} if the taxInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaxInfoDTO> createTaxInfo(@Valid @RequestBody TaxInfoDTO taxInfoDTO) throws URISyntaxException {
        log.debug("REST request to save TaxInfo : {}", taxInfoDTO);
        if (taxInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxInfoDTO result = taxInfoService.save(taxInfoDTO);
        return ResponseEntity
            .created(new URI("/api/tax-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-infos/:id} : Updates an existing taxInfo.
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaxInfoDTO> updateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxInfo : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxInfoDTO result = taxInfoService.update(taxInfoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tax-infos/:id} : Partial updates given fields of an existing taxInfo, field will ignore if it is null
     *
     * @param id the id of the taxInfoDTO to save.
     * @param taxInfoDTO the taxInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxInfoDTO,
     * or with status {@code 400 (Bad Request)} if the taxInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxInfoDTO> partialUpdateTaxInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaxInfoDTO taxInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxInfo partially : {}, {}", id, taxInfoDTO);
        if (taxInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxInfoDTO> result = taxInfoService.partialUpdate(taxInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taxInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-infos} : get all the taxInfos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaxInfoDTO>> getAllTaxInfos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of TaxInfos");
        Page<TaxInfoDTO> page;
        if (eagerload) {
            page = taxInfoService.findAllWithEagerRelationships(pageable);
        } else {
            page = taxInfoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-infos/:id} : get the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaxInfoDTO> getTaxInfo(@PathVariable("id") Long id) {
        log.debug("REST request to get TaxInfo : {}", id);
        Optional<TaxInfoDTO> taxInfoDTO = taxInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxInfoDTO);
    }

    /**
     * {@code DELETE  /tax-infos/:id} : delete the "id" taxInfo.
     *
     * @param id the id of the taxInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxInfo(@PathVariable("id") Long id) {
        log.debug("REST request to delete TaxInfo : {}", id);
        taxInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
