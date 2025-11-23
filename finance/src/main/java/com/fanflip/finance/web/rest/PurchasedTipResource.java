package com.fanflip.finance.web.rest;

import com.fanflip.finance.repository.PurchasedTipRepository;
import com.fanflip.finance.service.PurchasedTipService;
import com.fanflip.finance.service.dto.PurchasedTipDTO;
import com.fanflip.finance.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.finance.domain.PurchasedTip}.
 */
@RestController
@RequestMapping("/api/purchased-tips")
public class PurchasedTipResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedTipResource.class);

    private static final String ENTITY_NAME = "financePurchasedTip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedTipService purchasedTipService;

    private final PurchasedTipRepository purchasedTipRepository;

    public PurchasedTipResource(PurchasedTipService purchasedTipService, PurchasedTipRepository purchasedTipRepository) {
        this.purchasedTipService = purchasedTipService;
        this.purchasedTipRepository = purchasedTipRepository;
    }

    /**
     * {@code POST  /purchased-tips} : Create a new purchasedTip.
     *
     * @param purchasedTipDTO the purchasedTipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedTipDTO, or with status {@code 400 (Bad Request)} if the purchasedTip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchasedTipDTO> createPurchasedTip(@Valid @RequestBody PurchasedTipDTO purchasedTipDTO)
        throws URISyntaxException {
        log.debug("REST request to save PurchasedTip : {}", purchasedTipDTO);
        if (purchasedTipDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedTip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchasedTipDTO result = purchasedTipService.save(purchasedTipDTO);
        return ResponseEntity
            .created(new URI("/api/purchased-tips/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchased-tips/:id} : Updates an existing purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to save.
     * @param purchasedTipDTO the purchasedTipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedTipDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedTipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedTipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchasedTipDTO> updatePurchasedTip(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedTipDTO purchasedTipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedTip : {}, {}", id, purchasedTipDTO);
        if (purchasedTipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedTipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedTipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchasedTipDTO result = purchasedTipService.update(purchasedTipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedTipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchased-tips/:id} : Partial updates given fields of an existing purchasedTip, field will ignore if it is null
     *
     * @param id the id of the purchasedTipDTO to save.
     * @param purchasedTipDTO the purchasedTipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedTipDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedTipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedTipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedTipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchasedTipDTO> partialUpdatePurchasedTip(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedTipDTO purchasedTipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchasedTip partially : {}, {}", id, purchasedTipDTO);
        if (purchasedTipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedTipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedTipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchasedTipDTO> result = purchasedTipService.partialUpdate(purchasedTipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedTipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchased-tips} : get all the purchasedTips.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedTips in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PurchasedTipDTO>> getAllPurchasedTips(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchasedTips");
        Page<PurchasedTipDTO> page;
        if (eagerload) {
            page = purchasedTipService.findAllWithEagerRelationships(pageable);
        } else {
            page = purchasedTipService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchased-tips/:id} : get the "id" purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedTipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchasedTipDTO> getPurchasedTip(@PathVariable("id") Long id) {
        log.debug("REST request to get PurchasedTip : {}", id);
        Optional<PurchasedTipDTO> purchasedTipDTO = purchasedTipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedTipDTO);
    }

    /**
     * {@code DELETE  /purchased-tips/:id} : delete the "id" purchasedTip.
     *
     * @param id the id of the purchasedTipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchasedTip(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedTip : {}", id);
        purchasedTipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
