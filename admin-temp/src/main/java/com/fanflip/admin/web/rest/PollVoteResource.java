package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.PollVoteRepository;
import com.monsterdam.admin.service.PollVoteService;
import com.monsterdam.admin.service.dto.PollVoteDTO;
import com.monsterdam.admin.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.admin.domain.PollVote}.
 */
@RestController
@RequestMapping("/api/poll-votes")
public class PollVoteResource {

    private final Logger log = LoggerFactory.getLogger(PollVoteResource.class);

    private static final String ENTITY_NAME = "pollVote";

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
    public Mono<ResponseEntity<PollVoteDTO>> createPollVote(@Valid @RequestBody PollVoteDTO pollVoteDTO) throws URISyntaxException {
        log.debug("REST request to save PollVote : {}", pollVoteDTO);
        if (pollVoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new pollVote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pollVoteService
            .save(pollVoteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/poll-votes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<PollVoteDTO>> updatePollVote(
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

        return pollVoteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pollVoteService
                    .update(pollVoteDTO)
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
    public Mono<ResponseEntity<PollVoteDTO>> partialUpdatePollVote(
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

        return pollVoteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PollVoteDTO> result = pollVoteService.partialUpdate(pollVoteDTO);

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
     * {@code GET  /poll-votes} : get all the pollVotes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pollVotes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<PollVoteDTO>>> getAllPollVotes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PollVotes");
        return pollVoteService
            .countAll()
            .zipWith(pollVoteService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /poll-votes/:id} : get the "id" pollVote.
     *
     * @param id the id of the pollVoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pollVoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<PollVoteDTO>> getPollVote(@PathVariable("id") Long id) {
        log.debug("REST request to get PollVote : {}", id);
        Mono<PollVoteDTO> pollVoteDTO = pollVoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pollVoteDTO);
    }

    /**
     * {@code DELETE  /poll-votes/:id} : delete the "id" pollVote.
     *
     * @param id the id of the pollVoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePollVote(@PathVariable("id") Long id) {
        log.debug("REST request to delete PollVote : {}", id);
        return pollVoteService
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
     * {@code SEARCH  /poll-votes/_search?query=:query} : search for the pollVote corresponding
     * to the query.
     *
     * @param query the query of the pollVote search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<PollVoteDTO>>> searchPollVotes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of PollVotes for query {}", query);
        return pollVoteService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(pollVoteService.search(query, pageable)));
    }
}
