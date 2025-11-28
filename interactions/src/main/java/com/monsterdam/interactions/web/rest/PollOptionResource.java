package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.PollOptionRepository;
import com.monsterdam.interactions.service.PollOptionService;
import com.monsterdam.interactions.service.dto.PollOptionDTO;
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
 * REST controller for managing {@link com.monsterdam.interactions.domain.PollOption}.
 */
@RestController
@RequestMapping("/api/poll-options")
public class PollOptionResource {

    private final Logger log = LoggerFactory.getLogger(PollOptionResource.class);

    private static final String ENTITY_NAME = "interactionsPollOption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PollOptionService pollOptionService;

    private final PollOptionRepository pollOptionRepository;

    public PollOptionResource(PollOptionService pollOptionService, PollOptionRepository pollOptionRepository) {
        this.pollOptionService = pollOptionService;
        this.pollOptionRepository = pollOptionRepository;
    }

    /**
     * {@code POST  /poll-options} : Create a new pollOption.
     *
     * @param pollOptionDTO the pollOptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pollOptionDTO, or with status {@code 400 (Bad Request)} if the pollOption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PollOptionDTO> createPollOption(@Valid @RequestBody PollOptionDTO pollOptionDTO) throws URISyntaxException {
        log.debug("REST request to save PollOption : {}", pollOptionDTO);
        if (pollOptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new pollOption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PollOptionDTO result = pollOptionService.save(pollOptionDTO);
        return ResponseEntity
            .created(new URI("/api/poll-options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poll-options/:id} : Updates an existing pollOption.
     *
     * @param id the id of the pollOptionDTO to save.
     * @param pollOptionDTO the pollOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollOptionDTO,
     * or with status {@code 400 (Bad Request)} if the pollOptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pollOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PollOptionDTO> updatePollOption(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PollOptionDTO pollOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PollOption : {}, {}", id, pollOptionDTO);
        if (pollOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PollOptionDTO result = pollOptionService.update(pollOptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollOptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poll-options/:id} : Partial updates given fields of an existing pollOption, field will ignore if it is null
     *
     * @param id the id of the pollOptionDTO to save.
     * @param pollOptionDTO the pollOptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollOptionDTO,
     * or with status {@code 400 (Bad Request)} if the pollOptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pollOptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pollOptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PollOptionDTO> partialUpdatePollOption(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PollOptionDTO pollOptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PollOption partially : {}, {}", id, pollOptionDTO);
        if (pollOptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollOptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollOptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PollOptionDTO> result = pollOptionService.partialUpdate(pollOptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollOptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /poll-options} : get all the pollOptions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pollOptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PollOptionDTO>> getAllPollOptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PollOptions");
        Page<PollOptionDTO> page;
        if (eagerload) {
            page = pollOptionService.findAllWithEagerRelationships(pageable);
        } else {
            page = pollOptionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /poll-options/:id} : get the "id" pollOption.
     *
     * @param id the id of the pollOptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pollOptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PollOptionDTO> getPollOption(@PathVariable("id") Long id) {
        log.debug("REST request to get PollOption : {}", id);
        Optional<PollOptionDTO> pollOptionDTO = pollOptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollOptionDTO);
    }

    /**
     * {@code DELETE  /poll-options/:id} : delete the "id" pollOption.
     *
     * @param id the id of the pollOptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePollOption(@PathVariable("id") Long id) {
        log.debug("REST request to delete PollOption : {}", id);
        pollOptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
