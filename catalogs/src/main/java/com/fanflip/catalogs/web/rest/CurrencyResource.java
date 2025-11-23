package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.CurrencyRepository;
import com.fanflip.catalogs.service.CurrencyService;
import com.fanflip.catalogs.service.dto.CurrencyDTO;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.Currency}.
 */
@RestController
@RequestMapping("/api/currencies")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "catalogsCurrency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyService currencyService;

    private final CurrencyRepository currencyRepository;

    public CurrencyResource(CurrencyService currencyService, CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    /**
     * {@code POST  /currencies} : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyDTO, or with status {@code 400 (Bad Request)} if the currency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new currency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyDTO result = currencyService.save(currencyDTO);
        return ResponseEntity
            .created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currencies/:id} : Updates an existing currency.
     *
     * @param id the id of the currencyDTO to save.
     * @param currencyDTO the currencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDTO,
     * or with status {@code 400 (Bad Request)} if the currencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDTO> updateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Currency : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurrencyDTO result = currencyService.update(currencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /currencies/:id} : Partial updates given fields of an existing currency, field will ignore if it is null
     *
     * @param id the id of the currencyDTO to save.
     * @param currencyDTO the currencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyDTO,
     * or with status {@code 400 (Bad Request)} if the currencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the currencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyDTO> partialUpdateCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyDTO currencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Currency partially : {}, {}", id, currencyDTO);
        if (currencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyDTO> result = currencyService.partialUpdate(currencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /currencies} : get all the currencies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Currencies");
        Page<CurrencyDTO> page = currencyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currencies/:id} : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable("id") Long id) {
        log.debug("REST request to get Currency : {}", id);
        Optional<CurrencyDTO> currencyDTO = currencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyDTO);
    }

    /**
     * {@code DELETE  /currencies/:id} : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable("id") Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
