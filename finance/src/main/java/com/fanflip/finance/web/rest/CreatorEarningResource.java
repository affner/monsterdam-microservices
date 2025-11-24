package com.monsterdam.finance.web.rest;

import com.monsterdam.finance.repository.CreatorEarningRepository;
import com.monsterdam.finance.service.CreatorEarningService;
import com.monsterdam.finance.service.dto.CreatorEarningDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.finance.domain.CreatorEarning}.
 */
@RestController
@RequestMapping("/api/creator-earnings")
public class CreatorEarningResource {

    private final Logger log = LoggerFactory.getLogger(CreatorEarningResource.class);

    private static final String ENTITY_NAME = "financeCreatorEarning";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreatorEarningService creatorEarningService;

    private final CreatorEarningRepository creatorEarningRepository;

    public CreatorEarningResource(CreatorEarningService creatorEarningService, CreatorEarningRepository creatorEarningRepository) {
        this.creatorEarningService = creatorEarningService;
        this.creatorEarningRepository = creatorEarningRepository;
    }

    /**
     * {@code POST  /creator-earnings} : Create a new creatorEarning.
     *
     * @param creatorEarningDTO the creatorEarningDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creatorEarningDTO, or with status {@code 400 (Bad Request)} if the creatorEarning has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CreatorEarningDTO> createCreatorEarning(@Valid @RequestBody CreatorEarningDTO creatorEarningDTO)
        throws URISyntaxException {
        log.debug("REST request to save CreatorEarning : {}", creatorEarningDTO);
        if (creatorEarningDTO.getId() != null) {
            throw new BadRequestAlertException("A new creatorEarning cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreatorEarningDTO result = creatorEarningService.save(creatorEarningDTO);
        return ResponseEntity
            .created(new URI("/api/creator-earnings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /creator-earnings/:id} : Updates an existing creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to save.
     * @param creatorEarningDTO the creatorEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creatorEarningDTO,
     * or with status {@code 400 (Bad Request)} if the creatorEarningDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creatorEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreatorEarningDTO> updateCreatorEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreatorEarningDTO creatorEarningDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CreatorEarning : {}, {}", id, creatorEarningDTO);
        if (creatorEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creatorEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creatorEarningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CreatorEarningDTO result = creatorEarningService.update(creatorEarningDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creatorEarningDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /creator-earnings/:id} : Partial updates given fields of an existing creatorEarning, field will ignore if it is null
     *
     * @param id the id of the creatorEarningDTO to save.
     * @param creatorEarningDTO the creatorEarningDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creatorEarningDTO,
     * or with status {@code 400 (Bad Request)} if the creatorEarningDTO is not valid,
     * or with status {@code 404 (Not Found)} if the creatorEarningDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the creatorEarningDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreatorEarningDTO> partialUpdateCreatorEarning(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreatorEarningDTO creatorEarningDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreatorEarning partially : {}, {}", id, creatorEarningDTO);
        if (creatorEarningDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creatorEarningDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creatorEarningRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreatorEarningDTO> result = creatorEarningService.partialUpdate(creatorEarningDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creatorEarningDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /creator-earnings} : get all the creatorEarnings.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creatorEarnings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CreatorEarningDTO>> getAllCreatorEarnings(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("moneypayout-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where moneyPayout is null");
            return new ResponseEntity<>(creatorEarningService.findAllWhereMoneyPayoutIsNull(), HttpStatus.OK);
        }

        if ("purchasedtip-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedTip is null");
            return new ResponseEntity<>(creatorEarningService.findAllWherePurchasedTipIsNull(), HttpStatus.OK);
        }

        if ("purchasedcontent-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedContent is null");
            return new ResponseEntity<>(creatorEarningService.findAllWherePurchasedContentIsNull(), HttpStatus.OK);
        }

        if ("purchasedsubscription-is-null".equals(filter)) {
            log.debug("REST request to get all CreatorEarnings where purchasedSubscription is null");
            return new ResponseEntity<>(creatorEarningService.findAllWherePurchasedSubscriptionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of CreatorEarnings");
        Page<CreatorEarningDTO> page = creatorEarningService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /creator-earnings/:id} : get the "id" creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creatorEarningDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreatorEarningDTO> getCreatorEarning(@PathVariable("id") Long id) {
        log.debug("REST request to get CreatorEarning : {}", id);
        Optional<CreatorEarningDTO> creatorEarningDTO = creatorEarningService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creatorEarningDTO);
    }

    /**
     * {@code DELETE  /creator-earnings/:id} : delete the "id" creatorEarning.
     *
     * @param id the id of the creatorEarningDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreatorEarning(@PathVariable("id") Long id) {
        log.debug("REST request to delete CreatorEarning : {}", id);
        creatorEarningService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
