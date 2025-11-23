package com.fanflip.finance.web.rest;

import com.fanflip.finance.repository.MoneyPayoutRepository;
import com.fanflip.finance.service.MoneyPayoutService;
import com.fanflip.finance.service.dto.MoneyPayoutDTO;
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
 * REST controller for managing {@link com.fanflip.finance.domain.MoneyPayout}.
 */
@RestController
@RequestMapping("/api/money-payouts")
public class MoneyPayoutResource {

    private final Logger log = LoggerFactory.getLogger(MoneyPayoutResource.class);

    private static final String ENTITY_NAME = "financeMoneyPayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoneyPayoutService moneyPayoutService;

    private final MoneyPayoutRepository moneyPayoutRepository;

    public MoneyPayoutResource(MoneyPayoutService moneyPayoutService, MoneyPayoutRepository moneyPayoutRepository) {
        this.moneyPayoutService = moneyPayoutService;
        this.moneyPayoutRepository = moneyPayoutRepository;
    }

    /**
     * {@code POST  /money-payouts} : Create a new moneyPayout.
     *
     * @param moneyPayoutDTO the moneyPayoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moneyPayoutDTO, or with status {@code 400 (Bad Request)} if the moneyPayout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoneyPayoutDTO> createMoneyPayout(@Valid @RequestBody MoneyPayoutDTO moneyPayoutDTO) throws URISyntaxException {
        log.debug("REST request to save MoneyPayout : {}", moneyPayoutDTO);
        if (moneyPayoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new moneyPayout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoneyPayoutDTO result = moneyPayoutService.save(moneyPayoutDTO);
        return ResponseEntity
            .created(new URI("/api/money-payouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /money-payouts/:id} : Updates an existing moneyPayout.
     *
     * @param id the id of the moneyPayoutDTO to save.
     * @param moneyPayoutDTO the moneyPayoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyPayoutDTO,
     * or with status {@code 400 (Bad Request)} if the moneyPayoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moneyPayoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoneyPayoutDTO> updateMoneyPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoneyPayoutDTO moneyPayoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MoneyPayout : {}, {}", id, moneyPayoutDTO);
        if (moneyPayoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyPayoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoneyPayoutDTO result = moneyPayoutService.update(moneyPayoutDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyPayoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /money-payouts/:id} : Partial updates given fields of an existing moneyPayout, field will ignore if it is null
     *
     * @param id the id of the moneyPayoutDTO to save.
     * @param moneyPayoutDTO the moneyPayoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moneyPayoutDTO,
     * or with status {@code 400 (Bad Request)} if the moneyPayoutDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moneyPayoutDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moneyPayoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoneyPayoutDTO> partialUpdateMoneyPayout(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoneyPayoutDTO moneyPayoutDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MoneyPayout partially : {}, {}", id, moneyPayoutDTO);
        if (moneyPayoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moneyPayoutDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moneyPayoutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoneyPayoutDTO> result = moneyPayoutService.partialUpdate(moneyPayoutDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moneyPayoutDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /money-payouts} : get all the moneyPayouts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moneyPayouts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoneyPayoutDTO>> getAllMoneyPayouts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of MoneyPayouts");
        Page<MoneyPayoutDTO> page;
        if (eagerload) {
            page = moneyPayoutService.findAllWithEagerRelationships(pageable);
        } else {
            page = moneyPayoutService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /money-payouts/:id} : get the "id" moneyPayout.
     *
     * @param id the id of the moneyPayoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moneyPayoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoneyPayoutDTO> getMoneyPayout(@PathVariable("id") Long id) {
        log.debug("REST request to get MoneyPayout : {}", id);
        Optional<MoneyPayoutDTO> moneyPayoutDTO = moneyPayoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moneyPayoutDTO);
    }

    /**
     * {@code DELETE  /money-payouts/:id} : delete the "id" moneyPayout.
     *
     * @param id the id of the moneyPayoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyPayout(@PathVariable("id") Long id) {
        log.debug("REST request to delete MoneyPayout : {}", id);
        moneyPayoutService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
