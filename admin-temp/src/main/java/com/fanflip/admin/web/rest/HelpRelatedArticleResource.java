package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.HelpRelatedArticleRepository;
import com.fanflip.admin.service.HelpRelatedArticleService;
import com.fanflip.admin.service.dto.HelpRelatedArticleDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.admin.domain.HelpRelatedArticle}.
 */
@RestController
@RequestMapping("/api/help-related-articles")
public class HelpRelatedArticleResource {

    private final Logger log = LoggerFactory.getLogger(HelpRelatedArticleResource.class);

    private static final String ENTITY_NAME = "helpRelatedArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpRelatedArticleService helpRelatedArticleService;

    private final HelpRelatedArticleRepository helpRelatedArticleRepository;

    public HelpRelatedArticleResource(
        HelpRelatedArticleService helpRelatedArticleService,
        HelpRelatedArticleRepository helpRelatedArticleRepository
    ) {
        this.helpRelatedArticleService = helpRelatedArticleService;
        this.helpRelatedArticleRepository = helpRelatedArticleRepository;
    }

    /**
     * {@code POST  /help-related-articles} : Create a new helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpRelatedArticleDTO, or with status {@code 400 (Bad Request)} if the helpRelatedArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<HelpRelatedArticleDTO>> createHelpRelatedArticle(
        @Valid @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save HelpRelatedArticle : {}", helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpRelatedArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return helpRelatedArticleService
            .save(helpRelatedArticleDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/help-related-articles/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /help-related-articles/:id} : Updates an existing helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to save.
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpRelatedArticleDTO,
     * or with status {@code 400 (Bad Request)} if the helpRelatedArticleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpRelatedArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<HelpRelatedArticleDTO>> updateHelpRelatedArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpRelatedArticle : {}, {}", id, helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpRelatedArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpRelatedArticleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return helpRelatedArticleService
                    .update(helpRelatedArticleDTO)
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
     * {@code PATCH  /help-related-articles/:id} : Partial updates given fields of an existing helpRelatedArticle, field will ignore if it is null
     *
     * @param id the id of the helpRelatedArticleDTO to save.
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpRelatedArticleDTO,
     * or with status {@code 400 (Bad Request)} if the helpRelatedArticleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpRelatedArticleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpRelatedArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<HelpRelatedArticleDTO>> partialUpdateHelpRelatedArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpRelatedArticle partially : {}, {}", id, helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpRelatedArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return helpRelatedArticleRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<HelpRelatedArticleDTO> result = helpRelatedArticleService.partialUpdate(helpRelatedArticleDTO);

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
     * {@code GET  /help-related-articles} : get all the helpRelatedArticles.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpRelatedArticles in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<HelpRelatedArticleDTO>>> getAllHelpRelatedArticles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of HelpRelatedArticles");
        return helpRelatedArticleService
            .countAll()
            .zipWith(helpRelatedArticleService.findAll(pageable).collectList())
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
     * {@code GET  /help-related-articles/:id} : get the "id" helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpRelatedArticleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HelpRelatedArticleDTO>> getHelpRelatedArticle(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpRelatedArticle : {}", id);
        Mono<HelpRelatedArticleDTO> helpRelatedArticleDTO = helpRelatedArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpRelatedArticleDTO);
    }

    /**
     * {@code DELETE  /help-related-articles/:id} : delete the "id" helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteHelpRelatedArticle(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpRelatedArticle : {}", id);
        return helpRelatedArticleService
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
     * {@code SEARCH  /help-related-articles/_search?query=:query} : search for the helpRelatedArticle corresponding
     * to the query.
     *
     * @param query the query of the helpRelatedArticle search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<HelpRelatedArticleDTO>>> searchHelpRelatedArticles(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of HelpRelatedArticles for query {}", query);
        return helpRelatedArticleService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(helpRelatedArticleService.search(query, pageable)));
    }
}
