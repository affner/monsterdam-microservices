package com.fanflip.profile.web.rest;

import com.fanflip.profile.repository.StateUserRelationRepository;
import com.fanflip.profile.service.StateUserRelationService;
import com.fanflip.profile.service.dto.StateUserRelationDTO;
import com.fanflip.profile.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.profile.domain.StateUserRelation}.
 */
@RestController
@RequestMapping("/api/state-user-relations")
public class StateUserRelationResource {

    private final Logger log = LoggerFactory.getLogger(StateUserRelationResource.class);

    private static final String ENTITY_NAME = "profileStateUserRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateUserRelationService stateUserRelationService;

    private final StateUserRelationRepository stateUserRelationRepository;

    public StateUserRelationResource(
        StateUserRelationService stateUserRelationService,
        StateUserRelationRepository stateUserRelationRepository
    ) {
        this.stateUserRelationService = stateUserRelationService;
        this.stateUserRelationRepository = stateUserRelationRepository;
    }

    /**
     * {@code POST  /state-user-relations} : Create a new stateUserRelation.
     *
     * @param stateUserRelationDTO the stateUserRelationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateUserRelationDTO, or with status {@code 400 (Bad Request)} if the stateUserRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StateUserRelationDTO> createStateUserRelation(@Valid @RequestBody StateUserRelationDTO stateUserRelationDTO)
        throws URISyntaxException {
        log.debug("REST request to save StateUserRelation : {}", stateUserRelationDTO);
        if (stateUserRelationDTO.getId() != null) {
            throw new BadRequestAlertException("A new stateUserRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StateUserRelationDTO result = stateUserRelationService.save(stateUserRelationDTO);
        return ResponseEntity
            .created(new URI("/api/state-user-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /state-user-relations/:id} : Updates an existing stateUserRelation.
     *
     * @param id the id of the stateUserRelationDTO to save.
     * @param stateUserRelationDTO the stateUserRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateUserRelationDTO,
     * or with status {@code 400 (Bad Request)} if the stateUserRelationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateUserRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StateUserRelationDTO> updateStateUserRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StateUserRelationDTO stateUserRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StateUserRelation : {}, {}", id, stateUserRelationDTO);
        if (stateUserRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateUserRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateUserRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StateUserRelationDTO result = stateUserRelationService.update(stateUserRelationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateUserRelationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /state-user-relations/:id} : Partial updates given fields of an existing stateUserRelation, field will ignore if it is null
     *
     * @param id the id of the stateUserRelationDTO to save.
     * @param stateUserRelationDTO the stateUserRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateUserRelationDTO,
     * or with status {@code 400 (Bad Request)} if the stateUserRelationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the stateUserRelationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateUserRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateUserRelationDTO> partialUpdateStateUserRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StateUserRelationDTO stateUserRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StateUserRelation partially : {}, {}", id, stateUserRelationDTO);
        if (stateUserRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateUserRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateUserRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateUserRelationDTO> result = stateUserRelationService.partialUpdate(stateUserRelationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateUserRelationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /state-user-relations} : get all the stateUserRelations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateUserRelations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StateUserRelationDTO>> getAllStateUserRelations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of StateUserRelations");
        Page<StateUserRelationDTO> page = stateUserRelationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /state-user-relations/:id} : get the "id" stateUserRelation.
     *
     * @param id the id of the stateUserRelationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateUserRelationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StateUserRelationDTO> getStateUserRelation(@PathVariable("id") Long id) {
        log.debug("REST request to get StateUserRelation : {}", id);
        Optional<StateUserRelationDTO> stateUserRelationDTO = stateUserRelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateUserRelationDTO);
    }

    /**
     * {@code DELETE  /state-user-relations/:id} : delete the "id" stateUserRelation.
     *
     * @param id the id of the stateUserRelationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStateUserRelation(@PathVariable("id") Long id) {
        log.debug("REST request to delete StateUserRelation : {}", id);
        stateUserRelationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
