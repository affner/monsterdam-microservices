package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.DirectMessageRepository;
import com.fanflip.admin.service.DirectMessageService;
import com.fanflip.admin.service.dto.DirectMessageDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.DirectMessage}.
 */
@RestController
@RequestMapping("/api/direct-messages")
public class DirectMessageResource {

    private final Logger log = LoggerFactory.getLogger(DirectMessageResource.class);

    private static final String ENTITY_NAME = "directMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirectMessageService directMessageService;

    private final DirectMessageRepository directMessageRepository;

    public DirectMessageResource(DirectMessageService directMessageService, DirectMessageRepository directMessageRepository) {
        this.directMessageService = directMessageService;
        this.directMessageRepository = directMessageRepository;
    }

    /**
     * {@code POST  /direct-messages} : Create a new directMessage.
     *
     * @param directMessageDTO the directMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directMessageDTO, or with status {@code 400 (Bad Request)} if the directMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<DirectMessageDTO>> createDirectMessage(@Valid @RequestBody DirectMessageDTO directMessageDTO)
        throws URISyntaxException {
        log.debug("REST request to save DirectMessage : {}", directMessageDTO);
        if (directMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new directMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return directMessageService
            .save(directMessageDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/direct-messages/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /direct-messages/:id} : Updates an existing directMessage.
     *
     * @param id the id of the directMessageDTO to save.
     * @param directMessageDTO the directMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directMessageDTO,
     * or with status {@code 400 (Bad Request)} if the directMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<DirectMessageDTO>> updateDirectMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DirectMessageDTO directMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DirectMessage : {}, {}", id, directMessageDTO);
        if (directMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return directMessageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return directMessageService
                    .update(directMessageDTO)
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
     * {@code PATCH  /direct-messages/:id} : Partial updates given fields of an existing directMessage, field will ignore if it is null
     *
     * @param id the id of the directMessageDTO to save.
     * @param directMessageDTO the directMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directMessageDTO,
     * or with status {@code 400 (Bad Request)} if the directMessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the directMessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the directMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DirectMessageDTO>> partialUpdateDirectMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DirectMessageDTO directMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DirectMessage partially : {}, {}", id, directMessageDTO);
        if (directMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return directMessageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DirectMessageDTO> result = directMessageService.partialUpdate(directMessageDTO);

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
     * {@code GET  /direct-messages} : get all the directMessages.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directMessages in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<DirectMessageDTO>>> getAllDirectMessages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("adminannouncement-is-null".equals(filter)) {
            log.debug("REST request to get all DirectMessages where adminAnnouncement is null");
            return directMessageService.findAllWhereAdminAnnouncementIsNull().collectList().map(ResponseEntity::ok);
        }

        if ("purchasedtip-is-null".equals(filter)) {
            log.debug("REST request to get all DirectMessages where purchasedTip is null");
            return directMessageService.findAllWherePurchasedTipIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of DirectMessages");
        return directMessageService
            .countAll()
            .zipWith(directMessageService.findAll(pageable).collectList())
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
     * {@code GET  /direct-messages/:id} : get the "id" directMessage.
     *
     * @param id the id of the directMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<DirectMessageDTO>> getDirectMessage(@PathVariable("id") Long id) {
        log.debug("REST request to get DirectMessage : {}", id);
        Mono<DirectMessageDTO> directMessageDTO = directMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(directMessageDTO);
    }

    /**
     * {@code DELETE  /direct-messages/:id} : delete the "id" directMessage.
     *
     * @param id the id of the directMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDirectMessage(@PathVariable("id") Long id) {
        log.debug("REST request to delete DirectMessage : {}", id);
        return directMessageService
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
     * {@code SEARCH  /direct-messages/_search?query=:query} : search for the directMessage corresponding
     * to the query.
     *
     * @param query the query of the directMessage search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<DirectMessageDTO>>> searchDirectMessages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of DirectMessages for query {}", query);
        return directMessageService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(directMessageService.search(query, pageable)));
    }
}
