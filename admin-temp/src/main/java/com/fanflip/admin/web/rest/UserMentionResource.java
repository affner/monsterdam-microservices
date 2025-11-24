package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.UserMentionRepository;
import com.monsterdam.admin.service.UserMentionService;
import com.monsterdam.admin.service.dto.UserMentionDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.UserMention}.
 */
@RestController
@RequestMapping("/api/user-mentions")
public class UserMentionResource {

    private final Logger log = LoggerFactory.getLogger(UserMentionResource.class);

    private static final String ENTITY_NAME = "userMention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMentionService userMentionService;

    private final UserMentionRepository userMentionRepository;

    public UserMentionResource(UserMentionService userMentionService, UserMentionRepository userMentionRepository) {
        this.userMentionService = userMentionService;
        this.userMentionRepository = userMentionRepository;
    }

    /**
     * {@code POST  /user-mentions} : Create a new userMention.
     *
     * @param userMentionDTO the userMentionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMentionDTO, or with status {@code 400 (Bad Request)} if the userMention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserMentionDTO>> createUserMention(@Valid @RequestBody UserMentionDTO userMentionDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserMention : {}", userMentionDTO);
        if (userMentionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userMention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userMentionService
            .save(userMentionDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-mentions/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-mentions/:id} : Updates an existing userMention.
     *
     * @param id the id of the userMentionDTO to save.
     * @param userMentionDTO the userMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMentionDTO,
     * or with status {@code 400 (Bad Request)} if the userMentionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserMentionDTO>> updateUserMention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserMentionDTO userMentionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserMention : {}, {}", id, userMentionDTO);
        if (userMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userMentionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userMentionService
                    .update(userMentionDTO)
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
     * {@code PATCH  /user-mentions/:id} : Partial updates given fields of an existing userMention, field will ignore if it is null
     *
     * @param id the id of the userMentionDTO to save.
     * @param userMentionDTO the userMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMentionDTO,
     * or with status {@code 400 (Bad Request)} if the userMentionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userMentionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserMentionDTO>> partialUpdateUserMention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserMentionDTO userMentionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserMention partially : {}, {}", id, userMentionDTO);
        if (userMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userMentionRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserMentionDTO> result = userMentionService.partialUpdate(userMentionDTO);

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
     * {@code GET  /user-mentions} : get all the userMentions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMentions in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<UserMentionDTO>>> getAllUserMentions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserMentions");
        return userMentionService
            .countAll()
            .zipWith(userMentionService.findAll(pageable).collectList())
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
     * {@code GET  /user-mentions/:id} : get the "id" userMention.
     *
     * @param id the id of the userMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserMentionDTO>> getUserMention(@PathVariable("id") Long id) {
        log.debug("REST request to get UserMention : {}", id);
        Mono<UserMentionDTO> userMentionDTO = userMentionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMentionDTO);
    }

    /**
     * {@code DELETE  /user-mentions/:id} : delete the "id" userMention.
     *
     * @param id the id of the userMentionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserMention(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserMention : {}", id);
        return userMentionService
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
     * {@code SEARCH  /user-mentions/_search?query=:query} : search for the userMention corresponding
     * to the query.
     *
     * @param query the query of the userMention search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<UserMentionDTO>>> searchUserMentions(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of UserMentions for query {}", query);
        return userMentionService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(userMentionService.search(query, pageable)));
    }
}
