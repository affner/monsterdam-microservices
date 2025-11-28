package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.SpecialRewardRepository;
import com.monsterdam.multimedia.service.SpecialRewardService;
import com.monsterdam.multimedia.service.dto.SpecialRewardDTO;
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
 * REST controller for managing {@link com.monsterdam.multimedia.domain.SpecialReward}.
 */
@RestController
@RequestMapping("/api/special-rewards")
public class SpecialRewardResource {

    private final Logger log = LoggerFactory.getLogger(SpecialRewardResource.class);

    private static final String ENTITY_NAME = "multimediaSpecialReward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialRewardService specialRewardService;

    private final SpecialRewardRepository specialRewardRepository;

    public SpecialRewardResource(SpecialRewardService specialRewardService, SpecialRewardRepository specialRewardRepository) {
        this.specialRewardService = specialRewardService;
        this.specialRewardRepository = specialRewardRepository;
    }

    /**
     * {@code POST  /special-rewards} : Create a new specialReward.
     *
     * @param specialRewardDTO the specialRewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialRewardDTO, or with status {@code 400 (Bad Request)} if the specialReward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SpecialRewardDTO> createSpecialReward(@Valid @RequestBody SpecialRewardDTO specialRewardDTO)
        throws URISyntaxException {
        log.debug("REST request to save SpecialReward : {}", specialRewardDTO);
        if (specialRewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new specialReward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpecialRewardDTO result = specialRewardService.save(specialRewardDTO);
        return ResponseEntity
            .created(new URI("/api/special-rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /special-rewards/:id} : Updates an existing specialReward.
     *
     * @param id the id of the specialRewardDTO to save.
     * @param specialRewardDTO the specialRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialRewardDTO,
     * or with status {@code 400 (Bad Request)} if the specialRewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpecialRewardDTO> updateSpecialReward(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SpecialRewardDTO specialRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SpecialReward : {}, {}", id, specialRewardDTO);
        if (specialRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SpecialRewardDTO result = specialRewardService.update(specialRewardDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialRewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /special-rewards/:id} : Partial updates given fields of an existing specialReward, field will ignore if it is null
     *
     * @param id the id of the specialRewardDTO to save.
     * @param specialRewardDTO the specialRewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialRewardDTO,
     * or with status {@code 400 (Bad Request)} if the specialRewardDTO is not valid,
     * or with status {@code 404 (Not Found)} if the specialRewardDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the specialRewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SpecialRewardDTO> partialUpdateSpecialReward(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SpecialRewardDTO specialRewardDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SpecialReward partially : {}, {}", id, specialRewardDTO);
        if (specialRewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specialRewardDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specialRewardRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SpecialRewardDTO> result = specialRewardService.partialUpdate(specialRewardDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialRewardDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /special-rewards} : get all the specialRewards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialRewards in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SpecialRewardDTO>> getAllSpecialRewards(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SpecialRewards");
        Page<SpecialRewardDTO> page = specialRewardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /special-rewards/:id} : get the "id" specialReward.
     *
     * @param id the id of the specialRewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialRewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpecialRewardDTO> getSpecialReward(@PathVariable("id") Long id) {
        log.debug("REST request to get SpecialReward : {}", id);
        Optional<SpecialRewardDTO> specialRewardDTO = specialRewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(specialRewardDTO);
    }

    /**
     * {@code DELETE  /special-rewards/:id} : delete the "id" specialReward.
     *
     * @param id the id of the specialRewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialReward(@PathVariable("id") Long id) {
        log.debug("REST request to delete SpecialReward : {}", id);
        specialRewardService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
