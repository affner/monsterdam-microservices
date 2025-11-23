package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.LiabilityRepository;
import com.fanflip.admin.service.LiabilityService;
import com.fanflip.admin.service.dto.LiabilityDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
import com.fanflip.admin.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.fanflip.admin.domain.Liability}.
 */
@RestController
@RequestMapping("/api/liabilities")
public class LiabilityResource {

    private final Logger log = LoggerFactory.getLogger(LiabilityResource.class);

    private static final String ENTITY_NAME = "liability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LiabilityService liabilityService;

    private final LiabilityRepository liabilityRepository;

    public LiabilityResource(LiabilityService liabilityService, LiabilityRepository liabilityRepository) {
        this.liabilityService = liabilityService;
        this.liabilityRepository = liabilityRepository;
    }

    /**
     * {@code POST  /liabilities} : Create a new liability.
     *
     * @param liabilityDTO the liabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liabilityDTO, or with status {@code 400 (Bad Request)} if the liability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<LiabilityDTO>> createLiability(@Valid @RequestBody LiabilityDTO liabilityDTO) throws URISyntaxException {
        log.debug("REST request to save Liability : {}", liabilityDTO);
        if (liabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new liability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return liabilityService
            .save(liabilityDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/liabilities/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /liabilities/:id} : Updates an existing liability.
     *
     * @param id the id of the liabilityDTO to save.
     * @param liabilityDTO the liabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liabilityDTO,
     * or with status {@code 400 (Bad Request)} if the liabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<LiabilityDTO>> updateLiability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LiabilityDTO liabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Liability : {}, {}", id, liabilityDTO);
        if (liabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return liabilityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return liabilityService
                    .update(liabilityDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /liabilities/:id} : Partial updates given fields of an existing liability, field will ignore if it is null
     *
     * @param id the id of the liabilityDTO to save.
     * @param liabilityDTO the liabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liabilityDTO,
     * or with status {@code 400 (Bad Request)} if the liabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the liabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the liabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LiabilityDTO>> partialUpdateLiability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LiabilityDTO liabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Liability partially : {}, {}", id, liabilityDTO);
        if (liabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return liabilityRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LiabilityDTO> result = liabilityService.partialUpdate(liabilityDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /liabilities} : get all the liabilities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of liabilities in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<LiabilityDTO>> getAllLiabilities() {
        log.debug("REST request to get all Liabilities");
        return liabilityService.findAll().collectList();
    }

    /**
     * {@code GET  /liabilities} : get all the liabilities as a stream.
     * @return the {@link Flux} of liabilities.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LiabilityDTO> getAllLiabilitiesAsStream() {
        log.debug("REST request to get all Liabilities as a stream");
        return liabilityService.findAll();
    }

    /**
     * {@code GET  /liabilities/:id} : get the "id" liability.
     *
     * @param id the id of the liabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<LiabilityDTO>> getLiability(@PathVariable("id") Long id) {
        log.debug("REST request to get Liability : {}", id);
        Mono<LiabilityDTO> liabilityDTO = liabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(liabilityDTO);
    }

    /**
     * {@code DELETE  /liabilities/:id} : delete the "id" liability.
     *
     * @param id the id of the liabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLiability(@PathVariable("id") Long id) {
        log.debug("REST request to delete Liability : {}", id);
        return liabilityService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /liabilities/_search?query=:query} : search for the liability corresponding
     * to the query.
     *
     * @param query the query of the liability search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<LiabilityDTO>> searchLiabilities(@RequestParam("query") String query) {
        log.debug("REST request to search Liabilities for query {}", query);
        try {
            return liabilityService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
