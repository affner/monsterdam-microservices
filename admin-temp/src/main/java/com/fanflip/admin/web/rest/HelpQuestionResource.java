package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.HelpQuestionRepository;
import com.monsterdam.admin.service.HelpQuestionService;
import com.monsterdam.admin.service.dto.HelpQuestionDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.HelpQuestion}.
 */
@RestController
@RequestMapping("/api/help-questions")
public class HelpQuestionResource {

    private final Logger log = LoggerFactory.getLogger(HelpQuestionResource.class);

    private static final String ENTITY_NAME = "helpQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpQuestionService helpQuestionService;

    private final HelpQuestionRepository helpQuestionRepository;

    public HelpQuestionResource(HelpQuestionService helpQuestionService, HelpQuestionRepository helpQuestionRepository) {
        this.helpQuestionService = helpQuestionService;
        this.helpQuestionRepository = helpQuestionRepository;
    }

    /**
     * {@code POST  /help-questions} : Create a new helpQuestion.
     *
     * @param helpQuestionDTO the helpQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpQuestionDTO, or with status {@code 400 (Bad Request)} if the helpQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HelpQuestionDTO>> createHelpQuestion(@Valid @RequestBody HelpQuestionDTO helpQuestionDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpQuestion : {}", helpQuestionDTO);
        if (helpQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpQuestionService
            .save(helpQuestionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-questions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-questions/:id} : Updates an existing helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to save.
     * @param helpQuestionDTO the helpQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the helpQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HelpQuestionDTO>> updateHelpQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpQuestionDTO helpQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpQuestion : {}, {}", id, helpQuestionDTO);
        if (helpQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpQuestionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpQuestionService
                    .update(helpQuestionDTO)
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
     * {@code PATCH  /help-questions/:id} : Partial updates given fields of an existing helpQuestion, field will ignore if it is null
     *
     * @param id the id of the helpQuestionDTO to save.
     * @param helpQuestionDTO the helpQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the helpQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpQuestionDTO>> partialUpdateHelpQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpQuestionDTO helpQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpQuestion partially : {}, {}", id, helpQuestionDTO);
        if (helpQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpQuestionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpQuestionDTO> result = helpQuestionService.partialUpdate(helpQuestionDTO);

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
     * {@code GET  /help-questions} : get all the helpQuestions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpQuestions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HelpQuestionDTO>>> getAllHelpQuestions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of HelpQuestions");
        return helpQuestionService
            .countAll()
            .zipWith(helpQuestionService.findAll(pageable).collectList())
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
     * {@code GET  /help-questions/:id} : get the "id" helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HelpQuestionDTO>> getHelpQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpQuestion : {}", id);
        Mono<HelpQuestionDTO> helpQuestionDTO = helpQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpQuestionDTO);
    }

    /**
     * {@code DELETE  /help-questions/:id} : delete the "id" helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpQuestion : {}", id);
        return helpQuestionService
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
     * {@code SEARCH  /help-questions/_search?query=:query} : search for the helpQuestion corresponding
     * to the query.
     *
     * @param query the query of the helpQuestion search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HelpQuestionDTO>>> searchHelpQuestions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HelpQuestions for query {}", query);
        return helpQuestionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(helpQuestionService.search(query, pageable)));
    }
}
