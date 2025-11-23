package com.fanflip.finance.web.rest;

import com.fanflip.finance.repository.SubscriptionBundleRepository;
import com.fanflip.finance.service.SubscriptionBundleService;
import com.fanflip.finance.service.dto.SubscriptionBundleDTO;
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
 * REST controller for managing {@link com.fanflip.finance.domain.SubscriptionBundle}.
 */
@RestController
@RequestMapping("/api/subscription-bundles")
public class SubscriptionBundleResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionBundleResource.class);

    private static final String ENTITY_NAME = "financeSubscriptionBundle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionBundleService subscriptionBundleService;

    private final SubscriptionBundleRepository subscriptionBundleRepository;

    public SubscriptionBundleResource(
        SubscriptionBundleService subscriptionBundleService,
        SubscriptionBundleRepository subscriptionBundleRepository
    ) {
        this.subscriptionBundleService = subscriptionBundleService;
        this.subscriptionBundleRepository = subscriptionBundleRepository;
    }

    /**
     * {@code POST  /subscription-bundles} : Create a new subscriptionBundle.
     *
     * @param subscriptionBundleDTO the subscriptionBundleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subscriptionBundleDTO, or with status {@code 400 (Bad Request)} if the subscriptionBundle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubscriptionBundleDTO> createSubscriptionBundle(@Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO)
        throws URISyntaxException {
        log.debug("REST request to save SubscriptionBundle : {}", subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscriptionBundle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionBundleDTO result = subscriptionBundleService.save(subscriptionBundleDTO);
        return ResponseEntity
            .created(new URI("/api/subscription-bundles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subscription-bundles/:id} : Updates an existing subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionBundleDTO> updateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubscriptionBundle : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubscriptionBundleDTO result = subscriptionBundleService.update(subscriptionBundleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionBundleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /subscription-bundles/:id} : Partial updates given fields of an existing subscriptionBundle, field will ignore if it is null
     *
     * @param id the id of the subscriptionBundleDTO to save.
     * @param subscriptionBundleDTO the subscriptionBundleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subscriptionBundleDTO,
     * or with status {@code 400 (Bad Request)} if the subscriptionBundleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subscriptionBundleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subscriptionBundleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubscriptionBundleDTO> partialUpdateSubscriptionBundle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubscriptionBundleDTO subscriptionBundleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubscriptionBundle partially : {}, {}", id, subscriptionBundleDTO);
        if (subscriptionBundleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subscriptionBundleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subscriptionBundleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubscriptionBundleDTO> result = subscriptionBundleService.partialUpdate(subscriptionBundleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subscriptionBundleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /subscription-bundles} : get all the subscriptionBundles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subscriptionBundles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubscriptionBundleDTO>> getAllSubscriptionBundles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SubscriptionBundles");
        Page<SubscriptionBundleDTO> page = subscriptionBundleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /subscription-bundles/:id} : get the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subscriptionBundleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionBundleDTO> getSubscriptionBundle(@PathVariable("id") Long id) {
        log.debug("REST request to get SubscriptionBundle : {}", id);
        Optional<SubscriptionBundleDTO> subscriptionBundleDTO = subscriptionBundleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subscriptionBundleDTO);
    }

    /**
     * {@code DELETE  /subscription-bundles/:id} : delete the "id" subscriptionBundle.
     *
     * @param id the id of the subscriptionBundleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscriptionBundle(@PathVariable("id") Long id) {
        log.debug("REST request to delete SubscriptionBundle : {}", id);
        subscriptionBundleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
