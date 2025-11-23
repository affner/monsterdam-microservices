package com.fanflip.interactions.web.rest;

import com.fanflip.interactions.repository.PollVoteRepository;
import com.fanflip.interactions.service.PollVoteService;
import com.fanflip.interactions.service.dto.PollVoteDTO;
import com.fanflip.interactions.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.interactions.domain.PollVote}.
 */
@RestController
@RequestMapping("/api/poll-votes")
public class PollVoteResource {

    private final Logger log = LoggerFactory.getLogger(PollVoteResource.class);

    private static final String ENTITY_NAME = "interactionsPollVote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PollVoteService pollVoteService;

    private final PollVoteRepository pollVoteRepository;

    public PollVoteResource(PollVoteService pollVoteService, PollVoteRepository pollVoteRepository) {
        this.pollVoteService = pollVoteService;
        this.pollVoteRepository = pollVoteRepository;
    }

    /**
     * {@code POST  /poll-votes} : Create a new pollVote.
     *
     * @param pollVoteDTO the pollVoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pollVoteDTO, or with status {@code 400 (Bad Request)} if the pollVote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PollVoteDTO> createPollVote(@Valid @RequestBody PollVoteDTO pollVoteDTO) throws URISyntaxException {
        log.debug("REST request to save PollVote : {}", pollVoteDTO);
        if (pollVoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new pollVote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PollVoteDTO result = pollVoteService.save(pollVoteDTO);
        return ResponseEntity
            .created(new URI("/api/poll-votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poll-votes/:id} : Updates an existing pollVote.
     *
     * @param id the id of the pollVoteDTO to save.
     * @param pollVoteDTO the pollVoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollVoteDTO,
     * or with status {@code 400 (Bad Request)} if the pollVoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pollVoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PollVoteDTO> updatePollVote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PollVoteDTO pollVoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PollVote : {}, {}", id, pollVoteDTO);
        if (pollVoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollVoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollVoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PollVoteDTO result = pollVoteService.update(pollVoteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollVoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poll-votes/:id} : Partial updates given fields of an existing pollVote, field will ignore if it is null
     *
     * @param id the id of the pollVoteDTO to save.
     * @param pollVoteDTO the pollVoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pollVoteDTO,
     * or with status {@code 400 (Bad Request)} if the pollVoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pollVoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pollVoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PollVoteDTO> partialUpdatePollVote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PollVoteDTO pollVoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PollVote partially : {}, {}", id, pollVoteDTO);
        if (pollVoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pollVoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pollVoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PollVoteDTO> result = pollVoteService.partialUpdate(pollVoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pollVoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /poll-votes} : get all the pollVotes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pollVotes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PollVoteDTO>> getAllPollVotes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PollVotes");
        Page<PollVoteDTO> page;
        if (eagerload) {
            page = pollVoteService.findAllWithEagerRelationships(pageable);
        } else {
            page = pollVoteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /poll-votes/:id} : get the "id" pollVote.
     *
     * @param id the id of the pollVoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pollVoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PollVoteDTO> getPollVote(@PathVariable("id") Long id) {
        log.debug("REST request to get PollVote : {}", id);
        Optional<PollVoteDTO> pollVoteDTO = pollVoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollVoteDTO);
    }

    /**
     * {@code DELETE  /poll-votes/:id} : delete the "id" pollVote.
     *
     * @param id the id of the pollVoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePollVote(@PathVariable("id") Long id) {
        log.debug("REST request to delete PollVote : {}", id);
        pollVoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
