package com.monsterdam.profile.web.rest;

import com.monsterdam.profile.repository.SpecialAwardRepository;
import com.monsterdam.profile.service.SpecialAwardService;
import com.monsterdam.profile.service.dto.SpecialAwardDTO;
import com.monsterdam.profile.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.profile.domain.SpecialAward}.
 */
@RestController
@RequestMapping("/api/special-awards")
public class SpecialAwardResource {

    private final Logger log = LoggerFactory.getLogger(SpecialAwardResource.class);

    private static final String ENTITY_NAME = "profileSpecialAward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialAwardService specialAwardService;

    private final SpecialAwardRepository specialAwardRepository;

    public SpecialAwardResource(SpecialAwardService specialAwardService, SpecialAwardRepository specialAwardRepository) {
        this.specialAwardService = specialAwardService;
        this.specialAwardRepository = specialAwardRepository;
    }

    /**
     * {@code POST  /special-awards} : Create a new specialAward.
     *
     * @param specialAwardDTO the specialAwardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialAwardDTO, or with status {@code 400 (Bad Request)} if the specialAward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialAwardDTO> createSpecialAward(@Valid @RequestBody SpecialAwardDTO specialAwardDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpecialAward : {}", specialAwardDTO);
        if (specialAwardDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialAward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialAwardDTO result = specialAwardService.save(specialAwardDTO);
        return ResponseEntity
            .created(new URI("/api/special-awards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /special-awards/:id} : Updates an existing specialAward.
     *
     * @param id the id of the specialAwardDTO to save.
     * @param specialAwardDTO the specialAwardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialAwardDTO,
     * or with status {@code 400 (Bad Request)} if the specialAwardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialAwardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialAwardDTO> updateSpecialAward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialAwardDTO specialAwardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpecialAward : {}, {}", id, specialAwardDTO);
        if (specialAwardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialAwardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialAwardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialAwardDTO result = specialAwardService.update(specialAwardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialAwardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /special-awards/:id} : Partial updates given fields of an existing specialAward, field will ignore if it is null
     *
     * @param id the id of the specialAwardDTO to save.
     * @param specialAwardDTO the specialAwardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialAwardDTO,
     * or with status {@code 400 (Bad Request)} if the specialAwardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialAwardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialAwardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialAwardDTO> partialUpdateSpecialAward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialAwardDTO specialAwardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecialAward partially : {}, {}", id, specialAwardDTO);
        if (specialAwardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialAwardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialAwardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialAwardDTO> result = specialAwardService.partialUpdate(specialAwardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialAwardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /special-awards} : get all the specialAwards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialAwards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialAwardDTO>> getAllSpecialAwards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SpecialAwards");
        Page<SpecialAwardDTO> page = specialAwardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /special-awards/:id} : get the "id" specialAward.
     *
     * @param id the id of the specialAwardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialAwardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialAwardDTO> getSpecialAward(@PathVariable("id") Long id) {
        log.debug("REST request to get SpecialAward : {}", id);
        Optional<SpecialAwardDTO> specialAwardDTO = specialAwardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialAwardDTO);
    }

    /**
     * {@code DELETE  /special-awards/:id} : delete the "id" specialAward.
     *
     * @param id the id of the specialAwardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialAward(@PathVariable("id") Long id) {
        log.debug("REST request to delete SpecialAward : {}", id);
        specialAwardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
