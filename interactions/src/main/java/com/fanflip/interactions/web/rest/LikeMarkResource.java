package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.LikeMarkRepository;
import com.monsterdam.interactions.service.LikeMarkService;
import com.monsterdam.interactions.service.dto.LikeMarkDTO;
import com.monsterdam.interactions.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.interactions.domain.LikeMark}.
 */
@RestController
@RequestMapping("/api/like-marks")
public class LikeMarkResource {

    private final Logger log = LoggerFactory.getLogger(LikeMarkResource.class);

    private static final String ENTITY_NAME = "interactionsLikeMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeMarkService likeMarkService;

    private final LikeMarkRepository likeMarkRepository;

    public LikeMarkResource(LikeMarkService likeMarkService, LikeMarkRepository likeMarkRepository) {
        this.likeMarkService = likeMarkService;
        this.likeMarkRepository = likeMarkRepository;
    }

    /**
     * {@code POST  /like-marks} : Create a new likeMark.
     *
     * @param likeMarkDTO the likeMarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeMarkDTO, or with status {@code 400 (Bad Request)} if the likeMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LikeMarkDTO> createLikeMark(@Valid @RequestBody LikeMarkDTO likeMarkDTO) throws URISyntaxException {
        log.debug("REST request to save LikeMark : {}", likeMarkDTO);
        if (likeMarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new likeMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikeMarkDTO result = likeMarkService.save(likeMarkDTO);
        return ResponseEntity
            .created(new URI("/api/like-marks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-marks/:id} : Updates an existing likeMark.
     *
     * @param id the id of the likeMarkDTO to save.
     * @param likeMarkDTO the likeMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMarkDTO,
     * or with status {@code 400 (Bad Request)} if the likeMarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LikeMarkDTO> updateLikeMark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LikeMarkDTO likeMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LikeMark : {}, {}", id, likeMarkDTO);
        if (likeMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikeMarkDTO result = likeMarkService.update(likeMarkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeMarkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-marks/:id} : Partial updates given fields of an existing likeMark, field will ignore if it is null
     *
     * @param id the id of the likeMarkDTO to save.
     * @param likeMarkDTO the likeMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMarkDTO,
     * or with status {@code 400 (Bad Request)} if the likeMarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the likeMarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeMarkDTO> partialUpdateLikeMark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LikeMarkDTO likeMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeMark partially : {}, {}", id, likeMarkDTO);
        if (likeMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeMarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeMarkDTO> result = likeMarkService.partialUpdate(likeMarkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeMarkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /like-marks} : get all the likeMarks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeMarks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LikeMarkDTO>> getAllLikeMarks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of LikeMarks");
        Page<LikeMarkDTO> page = likeMarkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /like-marks/:id} : get the "id" likeMark.
     *
     * @param id the id of the likeMarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeMarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LikeMarkDTO> getLikeMark(@PathVariable("id") Long id) {
        log.debug("REST request to get LikeMark : {}", id);
        Optional<LikeMarkDTO> likeMarkDTO = likeMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(likeMarkDTO);
    }

    /**
     * {@code DELETE  /like-marks/:id} : delete the "id" likeMark.
     *
     * @param id the id of the likeMarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLikeMark(@PathVariable("id") Long id) {
        log.debug("REST request to delete LikeMark : {}", id);
        likeMarkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
