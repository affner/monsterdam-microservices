package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.SinglePhotoRepository;
import com.monsterdam.multimedia.service.SinglePhotoService;
import com.monsterdam.multimedia.service.dto.SinglePhotoDTO;
import com.monsterdam.multimedia.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.multimedia.domain.SinglePhoto}.
 */
@RestController
@RequestMapping("/api/single-photos")
public class SinglePhotoResource {

    private final Logger log = LoggerFactory.getLogger(SinglePhotoResource.class);

    private static final String ENTITY_NAME = "multimediaSinglePhoto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SinglePhotoService singlePhotoService;

    private final SinglePhotoRepository singlePhotoRepository;

    public SinglePhotoResource(SinglePhotoService singlePhotoService, SinglePhotoRepository singlePhotoRepository) {
        this.singlePhotoService = singlePhotoService;
        this.singlePhotoRepository = singlePhotoRepository;
    }

    /**
     * {@code POST  /single-photos} : Create a new singlePhoto.
     *
     * @param singlePhotoDTO the singlePhotoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singlePhotoDTO, or with status {@code 400 (Bad Request)} if the singlePhoto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SinglePhotoDTO> createSinglePhoto(@Valid @RequestBody SinglePhotoDTO singlePhotoDTO) throws URISyntaxException {
        log.debug("REST request to save SinglePhoto : {}", singlePhotoDTO);
        if (singlePhotoDTO.getId() != null) {
            throw new BadRequestAlertException("A new singlePhoto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SinglePhotoDTO result = singlePhotoService.save(singlePhotoDTO);
        return ResponseEntity
            .created(new URI("/api/single-photos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /single-photos/:id} : Updates an existing singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to save.
     * @param singlePhotoDTO the singlePhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singlePhotoDTO,
     * or with status {@code 400 (Bad Request)} if the singlePhotoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singlePhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SinglePhotoDTO> updateSinglePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SinglePhotoDTO singlePhotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SinglePhoto : {}, {}", id, singlePhotoDTO);
        if (singlePhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singlePhotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singlePhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SinglePhotoDTO result = singlePhotoService.update(singlePhotoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singlePhotoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /single-photos/:id} : Partial updates given fields of an existing singlePhoto, field will ignore if it is null
     *
     * @param id the id of the singlePhotoDTO to save.
     * @param singlePhotoDTO the singlePhotoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singlePhotoDTO,
     * or with status {@code 400 (Bad Request)} if the singlePhotoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singlePhotoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singlePhotoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SinglePhotoDTO> partialUpdateSinglePhoto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SinglePhotoDTO singlePhotoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SinglePhoto partially : {}, {}", id, singlePhotoDTO);
        if (singlePhotoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singlePhotoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singlePhotoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SinglePhotoDTO> result = singlePhotoService.partialUpdate(singlePhotoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singlePhotoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /single-photos} : get all the singlePhotos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singlePhotos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SinglePhotoDTO>> getAllSinglePhotos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SinglePhotos");
        Page<SinglePhotoDTO> page;
        if (eagerload) {
            page = singlePhotoService.findAllWithEagerRelationships(pageable);
        } else {
            page = singlePhotoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /single-photos/:id} : get the "id" singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singlePhotoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SinglePhotoDTO> getSinglePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to get SinglePhoto : {}", id);
        Optional<SinglePhotoDTO> singlePhotoDTO = singlePhotoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singlePhotoDTO);
    }

    /**
     * {@code DELETE  /single-photos/:id} : delete the "id" singlePhoto.
     *
     * @param id the id of the singlePhotoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSinglePhoto(@PathVariable("id") Long id) {
        log.debug("REST request to delete SinglePhoto : {}", id);
        singlePhotoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
