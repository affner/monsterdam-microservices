package com.monsterdam.catalogs.web.rest;

import com.monsterdam.catalogs.repository.PayoutMethodRepository;
import com.monsterdam.catalogs.service.PayoutMethodService;
import com.monsterdam.catalogs.service.dto.PayoutMethodDTO;
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
 * REST controller for managing {@link com.monsterdam.catalogs.domain.PayoutMethod}.
 */
@RestController
@RequestMapping("/api/payout-methods")
public class PayoutMethodResource {

    private final Logger log = LoggerFactory.getLogger(PayoutMethodResource.class);

    private static final String ENTITY_NAME = "catalogsPayoutMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayoutMethodService payoutMethodService;

    private final PayoutMethodRepository payoutMethodRepository;

    public PayoutMethodResource(PayoutMethodService payoutMethodService, PayoutMethodRepository payoutMethodRepository) {
        this.payoutMethodService = payoutMethodService;
        this.payoutMethodRepository = payoutMethodRepository;
    }

    /**
     * {@code POST  /payout-methods} : Create a new payoutMethod.
     *
     * @param payoutMethodDTO the payoutMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new payoutMethodDTO, or with status {@code 400 (Bad Request)} if the payoutMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PayoutMethodDTO> createPayoutMethod(@Valid @RequestBody PayoutMethodDTO payoutMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save PayoutMethod : {}", payoutMethodDTO);
        if (payoutMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new payoutMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayoutMethodDTO result = payoutMethodService.save(payoutMethodDTO);
        return ResponseEntity
            .created(new URI("/api/payout-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payout-methods/:id} : Updates an existing payoutMethod.
     *
     * @param id the id of the payoutMethodDTO to save.
     * @param payoutMethodDTO the payoutMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutMethodDTO,
     * or with status {@code 400 (Bad Request)} if the payoutMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the payoutMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PayoutMethodDTO> updatePayoutMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PayoutMethodDTO payoutMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PayoutMethod : {}, {}", id, payoutMethodDTO);
        if (payoutMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payoutMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payoutMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PayoutMethodDTO result = payoutMethodService.update(payoutMethodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payout-methods/:id} : Partial updates given fields of an existing payoutMethod, field will ignore if it is null
     *
     * @param id the id of the payoutMethodDTO to save.
     * @param payoutMethodDTO the payoutMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated payoutMethodDTO,
     * or with status {@code 400 (Bad Request)} if the payoutMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the payoutMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the payoutMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PayoutMethodDTO> partialUpdatePayoutMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PayoutMethodDTO payoutMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PayoutMethod partially : {}, {}", id, payoutMethodDTO);
        if (payoutMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, payoutMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payoutMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PayoutMethodDTO> result = payoutMethodService.partialUpdate(payoutMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, payoutMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payout-methods} : get all the payoutMethods.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of payoutMethods in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PayoutMethodDTO>> getAllPayoutMethods(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PayoutMethods");
        Page<PayoutMethodDTO> page = payoutMethodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payout-methods/:id} : get the "id" payoutMethod.
     *
     * @param id the id of the payoutMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the payoutMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PayoutMethodDTO> getPayoutMethod(@PathVariable("id") Long id) {
        log.debug("REST request to get PayoutMethod : {}", id);
        Optional<PayoutMethodDTO> payoutMethodDTO = payoutMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(payoutMethodDTO);
    }

    /**
     * {@code DELETE  /payout-methods/:id} : delete the "id" payoutMethod.
     *
     * @param id the id of the payoutMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayoutMethod(@PathVariable("id") Long id) {
        log.debug("REST request to delete PayoutMethod : {}", id);
        payoutMethodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
