package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.AssetRepository;
import com.monsterdam.admin.service.AssetService;
import com.monsterdam.admin.service.dto.AssetDTO;
import com.monsterdam.admin.web.rest.errors.BadRequestAlertException;
import com.monsterdam.admin.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.Asset}.
 */
@RestController
@RequestMapping("/api/assets")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetService assetService;

    private final AssetRepository assetRepository;

    public AssetResource(AssetService assetService, AssetRepository assetRepository) {
        this.assetService = assetService;
        this.assetRepository = assetRepository;
    }

    /**
     * {@code POST  /assets} : Create a new asset.
     *
     * @param assetDTO the assetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDTO, or with status {@code 400 (Bad Request)} if the asset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AssetDTO>> createAsset(@Valid @RequestBody AssetDTO assetDTO) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", assetDTO);
        if (assetDTO.getId() != null) {
            throw new BadRequestAlertException("A new asset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return assetService
            .save(assetDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/assets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /assets/:id} : Updates an existing asset.
     *
     * @param id the id of the assetDTO to save.
     * @param assetDTO the assetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDTO,
     * or with status {@code 400 (Bad Request)} if the assetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AssetDTO>> updateAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetDTO assetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Asset : {}, {}", id, assetDTO);
        if (assetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return assetService
                    .update(assetDTO)
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
     * {@code PATCH  /assets/:id} : Partial updates given fields of an existing asset, field will ignore if it is null
     *
     * @param id the id of the assetDTO to save.
     * @param assetDTO the assetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDTO,
     * or with status {@code 400 (Bad Request)} if the assetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AssetDTO>> partialUpdateAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetDTO assetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Asset partially : {}, {}", id, assetDTO);
        if (assetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AssetDTO> result = assetService.partialUpdate(assetDTO);

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
     * {@code GET  /assets} : get all the assets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AssetDTO>> getAllAssets() {
        log.debug("REST request to get all Assets");
        return assetService.findAll().collectList();
    }

    /**
     * {@code GET  /assets} : get all the assets as a stream.
     * @return the {@link Flux} of assets.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AssetDTO> getAllAssetsAsStream() {
        log.debug("REST request to get all Assets as a stream");
        return assetService.findAll();
    }

    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the assetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AssetDTO>> getAsset(@PathVariable("id") Long id) {
        log.debug("REST request to get Asset : {}", id);
        Mono<AssetDTO> assetDTO = assetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDTO);
    }

    /**
     * {@code DELETE  /assets/:id} : delete the "id" asset.
     *
     * @param id the id of the assetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAsset(@PathVariable("id") Long id) {
        log.debug("REST request to delete Asset : {}", id);
        return assetService
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
     * {@code SEARCH  /assets/_search?query=:query} : search for the asset corresponding
     * to the query.
     *
     * @param query the query of the asset search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<AssetDTO>> searchAssets(@RequestParam("query") String query) {
        log.debug("REST request to search Assets for query {}", query);
        try {
            return assetService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
