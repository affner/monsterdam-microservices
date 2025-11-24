package com.monsterdam.finance.web.rest;

import com.monsterdam.finance.repository.PurchasedSubscriptionRepository;
import com.monsterdam.finance.service.PurchasedSubscriptionService;
import com.monsterdam.finance.service.dto.PurchasedSubscriptionDTO;
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
 * REST controller for managing {@link com.monsterdam.finance.domain.PurchasedSubscription}.
 */
@RestController
@RequestMapping("/api/purchased-subscriptions")
public class PurchasedSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(PurchasedSubscriptionResource.class);

    private static final String ENTITY_NAME = "financePurchasedSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchasedSubscriptionService purchasedSubscriptionService;

    private final PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    public PurchasedSubscriptionResource(
        PurchasedSubscriptionService purchasedSubscriptionService,
        PurchasedSubscriptionRepository purchasedSubscriptionRepository
    ) {
        this.purchasedSubscriptionService = purchasedSubscriptionService;
        this.purchasedSubscriptionRepository = purchasedSubscriptionRepository;
    }

    /**
     * {@code POST  /purchased-subscriptions} : Create a new purchasedSubscription.
     *
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchasedSubscriptionDTO, or with status {@code 400 (Bad Request)} if the purchasedSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PurchasedSubscriptionDTO> createPurchasedSubscription(
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PurchasedSubscription : {}", purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchasedSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchasedSubscriptionDTO result = purchasedSubscriptionService.save(purchasedSubscriptionDTO);
        return ResponseEntity
            .created(new URI("/api/purchased-subscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchased-subscriptions/:id} : Updates an existing purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PurchasedSubscriptionDTO> updatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PurchasedSubscription : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PurchasedSubscriptionDTO result = purchasedSubscriptionService.update(purchasedSubscriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedSubscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /purchased-subscriptions/:id} : Partial updates given fields of an existing purchasedSubscription, field will ignore if it is null
     *
     * @param id the id of the purchasedSubscriptionDTO to save.
     * @param purchasedSubscriptionDTO the purchasedSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchasedSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the purchasedSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the purchasedSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the purchasedSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PurchasedSubscriptionDTO> partialUpdatePurchasedSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PurchasedSubscriptionDTO purchasedSubscriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PurchasedSubscription partially : {}, {}", id, purchasedSubscriptionDTO);
        if (purchasedSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, purchasedSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!purchasedSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PurchasedSubscriptionDTO> result = purchasedSubscriptionService.partialUpdate(purchasedSubscriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchasedSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /purchased-subscriptions} : get all the purchasedSubscriptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchasedSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PurchasedSubscriptionDTO>> getAllPurchasedSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PurchasedSubscriptions");
        Page<PurchasedSubscriptionDTO> page;
        if (eagerload) {
            page = purchasedSubscriptionService.findAllWithEagerRelationships(pageable);
        } else {
            page = purchasedSubscriptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /purchased-subscriptions/:id} : get the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchasedSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchasedSubscriptionDTO> getPurchasedSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to get PurchasedSubscription : {}", id);
        Optional<PurchasedSubscriptionDTO> purchasedSubscriptionDTO = purchasedSubscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchasedSubscriptionDTO);
    }

    /**
     * {@code DELETE  /purchased-subscriptions/:id} : delete the "id" purchasedSubscription.
     *
     * @param id the id of the purchasedSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchasedSubscription(@PathVariable("id") Long id) {
        log.debug("REST request to delete PurchasedSubscription : {}", id);
        purchasedSubscriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
