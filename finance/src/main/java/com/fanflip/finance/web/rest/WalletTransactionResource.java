package com.monsterdam.finance.web.rest;

import com.monsterdam.finance.repository.WalletTransactionRepository;
import com.monsterdam.finance.service.WalletTransactionService;
import com.monsterdam.finance.service.dto.WalletTransactionDTO;
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
 * REST controller for managing {@link com.monsterdam.finance.domain.WalletTransaction}.
 */
@RestController
@RequestMapping("/api/wallet-transactions")
public class WalletTransactionResource {

    private final Logger log = LoggerFactory.getLogger(WalletTransactionResource.class);

    private static final String ENTITY_NAME = "financeWalletTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletTransactionService walletTransactionService;

    private final WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionResource(
        WalletTransactionService walletTransactionService,
        WalletTransactionRepository walletTransactionRepository
    ) {
        this.walletTransactionService = walletTransactionService;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    /**
     * {@code POST  /wallet-transactions} : Create a new walletTransaction.
     *
     * @param walletTransactionDTO the walletTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walletTransactionDTO, or with status {@code 400 (Bad Request)} if the walletTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WalletTransactionDTO> createWalletTransaction(@Valid @RequestBody WalletTransactionDTO walletTransactionDTO)
        throws URISyntaxException {
        log.debug("REST request to save WalletTransaction : {}", walletTransactionDTO);
        if (walletTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new walletTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WalletTransactionDTO result = walletTransactionService.save(walletTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/wallet-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wallet-transactions/:id} : Updates an existing walletTransaction.
     *
     * @param id the id of the walletTransactionDTO to save.
     * @param walletTransactionDTO the walletTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the walletTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walletTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WalletTransactionDTO> updateWalletTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WalletTransactionDTO walletTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WalletTransaction : {}, {}", id, walletTransactionDTO);
        if (walletTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WalletTransactionDTO result = walletTransactionService.update(walletTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wallet-transactions/:id} : Partial updates given fields of an existing walletTransaction, field will ignore if it is null
     *
     * @param id the id of the walletTransactionDTO to save.
     * @param walletTransactionDTO the walletTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the walletTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the walletTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the walletTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WalletTransactionDTO> partialUpdateWalletTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WalletTransactionDTO walletTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WalletTransaction partially : {}, {}", id, walletTransactionDTO);
        if (walletTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WalletTransactionDTO> result = walletTransactionService.partialUpdate(walletTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wallet-transactions} : get all the walletTransactions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of walletTransactions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WalletTransactionDTO>> getAllWalletTransactions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("purchasedtip-is-null".equals(filter)) {
            log.debug("REST request to get all WalletTransactions where purchasedTip is null");
            return new ResponseEntity<>(walletTransactionService.findAllWherePurchasedTipIsNull(), HttpStatus.OK);
        }

        if ("purchasedcontent-is-null".equals(filter)) {
            log.debug("REST request to get all WalletTransactions where purchasedContent is null");
            return new ResponseEntity<>(walletTransactionService.findAllWherePurchasedContentIsNull(), HttpStatus.OK);
        }

        if ("purchasedsubscription-is-null".equals(filter)) {
            log.debug("REST request to get all WalletTransactions where purchasedSubscription is null");
            return new ResponseEntity<>(walletTransactionService.findAllWherePurchasedSubscriptionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of WalletTransactions");
        Page<WalletTransactionDTO> page;
        if (eagerload) {
            page = walletTransactionService.findAllWithEagerRelationships(pageable);
        } else {
            page = walletTransactionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wallet-transactions/:id} : get the "id" walletTransaction.
     *
     * @param id the id of the walletTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walletTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WalletTransactionDTO> getWalletTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to get WalletTransaction : {}", id);
        Optional<WalletTransactionDTO> walletTransactionDTO = walletTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(walletTransactionDTO);
    }

    /**
     * {@code DELETE  /wallet-transactions/:id} : delete the "id" walletTransaction.
     *
     * @param id the id of the walletTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to delete WalletTransaction : {}", id);
        walletTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
