package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.ModerationActionRepository;
import com.monsterdam.admin.service.ModerationActionService;
import com.monsterdam.admin.service.dto.ModerationActionDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.ModerationAction}.
 */
@RestController
@RequestMapping("/api/moderation-actions")
public class ModerationActionResource {

    private final Logger log = LoggerFactory.getLogger(ModerationActionResource.class);

    private static final String ENTITY_NAME = "moderationAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModerationActionService moderationActionService;

    private final ModerationActionRepository moderationActionRepository;

    public ModerationActionResource(
        ModerationActionService moderationActionService,
        ModerationActionRepository moderationActionRepository
    ) {
        this.moderationActionService = moderationActionService;
        this.moderationActionRepository = moderationActionRepository;
    }

    /**
     * {@code POST  /moderation-actions} : Create a new moderationAction.
     *
     * @param moderationActionDTO the moderationActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moderationActionDTO, or with status {@code 400 (Bad Request)} if the moderationAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<ModerationActionDTO>> createModerationAction(@Valid @RequestBody ModerationActionDTO moderationActionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ModerationAction : {}", moderationActionDTO);
        if (moderationActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new moderationAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return moderationActionService
            .save(moderationActionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/moderation-actions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /moderation-actions/:id} : Updates an existing moderationAction.
     *
     * @param id the id of the moderationActionDTO to save.
     * @param moderationActionDTO the moderationActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationActionDTO,
     * or with status {@code 400 (Bad Request)} if the moderationActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moderationActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<ModerationActionDTO>> updateModerationAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModerationActionDTO moderationActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModerationAction : {}, {}", id, moderationActionDTO);
        if (moderationActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moderationActionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return moderationActionService
                    .update(moderationActionDTO)
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
     * {@code PATCH  /moderation-actions/:id} : Partial updates given fields of an existing moderationAction, field will ignore if it is null
     *
     * @param id the id of the moderationActionDTO to save.
     * @param moderationActionDTO the moderationActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationActionDTO,
     * or with status {@code 400 (Bad Request)} if the moderationActionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moderationActionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moderationActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModerationActionDTO>> partialUpdateModerationAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModerationActionDTO moderationActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModerationAction partially : {}, {}", id, moderationActionDTO);
        if (moderationActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return moderationActionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModerationActionDTO> result = moderationActionService.partialUpdate(moderationActionDTO);

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
     * {@code GET  /moderation-actions} : get all the moderationActions.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moderationActions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ModerationActionDTO>> getAllModerationActions(@RequestParam(name = "filter", required = false) String filter) {
        if ("assistanceticket-is-null".equals(filter)) {
            log.debug("REST request to get all ModerationActions where assistanceTicket is null");
            return moderationActionService.findAllWhereAssistanceTicketIsNull().collectList();
        }
        log.debug("REST request to get all ModerationActions");
        return moderationActionService.findAll().collectList();
    }

    /**
     * {@code GET  /moderation-actions} : get all the moderationActions as a stream.
     * @return the {@link Flux} of moderationActions.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModerationActionDTO> getAllModerationActionsAsStream() {
        log.debug("REST request to get all ModerationActions as a stream");
        return moderationActionService.findAll();
    }

    /**
     * {@code GET  /moderation-actions/:id} : get the "id" moderationAction.
     *
     * @param id the id of the moderationActionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moderationActionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ModerationActionDTO>> getModerationAction(@PathVariable("id") Long id) {
        log.debug("REST request to get ModerationAction : {}", id);
        Mono<ModerationActionDTO> moderationActionDTO = moderationActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moderationActionDTO);
    }

    /**
     * {@code DELETE  /moderation-actions/:id} : delete the "id" moderationAction.
     *
     * @param id the id of the moderationActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteModerationAction(@PathVariable("id") Long id) {
        log.debug("REST request to delete ModerationAction : {}", id);
        return moderationActionService
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
     * {@code SEARCH  /moderation-actions/_search?query=:query} : search for the moderationAction corresponding
     * to the query.
     *
     * @param query the query of the moderationAction search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<ModerationActionDTO>> searchModerationActions(@RequestParam("query") String query) {
        log.debug("REST request to search ModerationActions for query {}", query);
        try {
            return moderationActionService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
