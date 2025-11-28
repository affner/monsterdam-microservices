package com.monsterdam.finance.web.rest;

import com.monsterdam.finance.repository.OfferPromotionRepository;
import com.monsterdam.finance.service.OfferPromotionService;
import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import com.monsterdam.finance.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.finance.domain.OfferPromotion}.
 */
@RestController
@RequestMapping("/api/offer-promotions")
public class OfferPromotionResource {

    private final Logger log = LoggerFactory.getLogger(OfferPromotionResource.class);

    private static final String ENTITY_NAME = "financeOfferPromotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfferPromotionService offerPromotionService;

    private final OfferPromotionRepository offerPromotionRepository;

    public OfferPromotionResource(OfferPromotionService offerPromotionService, OfferPromotionRepository offerPromotionRepository) {
        this.offerPromotionService = offerPromotionService;
        this.offerPromotionRepository = offerPromotionRepository;
    }

    /**
     * {@code POST  /offer-promotions} : Create a new offerPromotion.
     *
     * @param offerPromotionDTO the offerPromotionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new offerPromotionDTO, or with status {@code 400 (Bad Request)} if the offerPromotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OfferPromotionDTO> createOfferPromotion(@Valid @RequestBody OfferPromotionDTO offerPromotionDTO)
        throws URISyntaxException {
        log.debug("REST request to save OfferPromotion : {}", offerPromotionDTO);
        if (offerPromotionDTO.getId() != null) {
            throw new BadRequestAlertException("A new offerPromotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfferPromotionDTO result = offerPromotionService.save(offerPromotionDTO);
        return ResponseEntity
            .created(new URI("/api/offer-promotions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /offer-promotions/:id} : Updates an existing offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to save.
     * @param offerPromotionDTO the offerPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the offerPromotionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the offerPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfferPromotionDTO> updateOfferPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OfferPromotionDTO offerPromotionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OfferPromotion : {}, {}", id, offerPromotionDTO);
        if (offerPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerPromotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OfferPromotionDTO result = offerPromotionService.update(offerPromotionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offerPromotionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /offer-promotions/:id} : Partial updates given fields of an existing offerPromotion, field will ignore if it is null
     *
     * @param id the id of the offerPromotionDTO to save.
     * @param offerPromotionDTO the offerPromotionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated offerPromotionDTO,
     * or with status {@code 400 (Bad Request)} if the offerPromotionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the offerPromotionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the offerPromotionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OfferPromotionDTO> partialUpdateOfferPromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OfferPromotionDTO offerPromotionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OfferPromotion partially : {}, {}", id, offerPromotionDTO);
        if (offerPromotionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, offerPromotionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!offerPromotionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OfferPromotionDTO> result = offerPromotionService.partialUpdate(offerPromotionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, offerPromotionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /offer-promotions} : get all the offerPromotions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offerPromotions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OfferPromotionDTO>> getAllOfferPromotions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of OfferPromotions");
        Page<OfferPromotionDTO> page = offerPromotionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /offer-promotions/:id} : get the "id" offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the offerPromotionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferPromotionDTO> getOfferPromotion(@PathVariable("id") Long id) {
        log.debug("REST request to get OfferPromotion : {}", id);
        Optional<OfferPromotionDTO> offerPromotionDTO = offerPromotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(offerPromotionDTO);
    }

    /**
     * {@code DELETE  /offer-promotions/:id} : delete the "id" offerPromotion.
     *
     * @param id the id of the offerPromotionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOfferPromotion(@PathVariable("id") Long id) {
        log.debug("REST request to delete OfferPromotion : {}", id);
        offerPromotionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
