package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.SingleVideoRepository;
import com.monsterdam.multimedia.service.SingleVideoService;
import com.monsterdam.multimedia.service.dto.SingleVideoDTO;
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
 * REST controller for managing {@link com.monsterdam.multimedia.domain.SingleVideo}.
 */
@RestController
@RequestMapping("/api/single-videos")
public class SingleVideoResource {

    private final Logger log = LoggerFactory.getLogger(SingleVideoResource.class);

    private static final String ENTITY_NAME = "multimediaSingleVideo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleVideoService singleVideoService;

    private final SingleVideoRepository singleVideoRepository;

    public SingleVideoResource(SingleVideoService singleVideoService, SingleVideoRepository singleVideoRepository) {
        this.singleVideoService = singleVideoService;
        this.singleVideoRepository = singleVideoRepository;
    }

    /**
     * {@code POST  /single-videos} : Create a new singleVideo.
     *
     * @param singleVideoDTO the singleVideoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleVideoDTO, or with status {@code 400 (Bad Request)} if the singleVideo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SingleVideoDTO> createSingleVideo(@Valid @RequestBody SingleVideoDTO singleVideoDTO) throws URISyntaxException {
        log.debug("REST request to save SingleVideo : {}", singleVideoDTO);
        if (singleVideoDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleVideo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SingleVideoDTO result = singleVideoService.save(singleVideoDTO);
        return ResponseEntity
            .created(new URI("/api/single-videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /single-videos/:id} : Updates an existing singleVideo.
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SingleVideoDTO> updateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleVideo : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SingleVideoDTO result = singleVideoService.update(singleVideoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleVideoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /single-videos/:id} : Partial updates given fields of an existing singleVideo, field will ignore if it is null
     *
     * @param id the id of the singleVideoDTO to save.
     * @param singleVideoDTO the singleVideoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleVideoDTO,
     * or with status {@code 400 (Bad Request)} if the singleVideoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleVideoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleVideoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SingleVideoDTO> partialUpdateSingleVideo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleVideoDTO singleVideoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleVideo partially : {}, {}", id, singleVideoDTO);
        if (singleVideoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleVideoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleVideoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SingleVideoDTO> result = singleVideoService.partialUpdate(singleVideoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleVideoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /single-videos} : get all the singleVideos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleVideos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SingleVideoDTO>> getAllSingleVideos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SingleVideos");
        Page<SingleVideoDTO> page;
        if (eagerload) {
            page = singleVideoService.findAllWithEagerRelationships(pageable);
        } else {
            page = singleVideoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /single-videos/:id} : get the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleVideoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SingleVideoDTO> getSingleVideo(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleVideo : {}", id);
        Optional<SingleVideoDTO> singleVideoDTO = singleVideoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleVideoDTO);
    }

    /**
     * {@code DELETE  /single-videos/:id} : delete the "id" singleVideo.
     *
     * @param id the id of the singleVideoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSingleVideo(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleVideo : {}", id);
        singleVideoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
