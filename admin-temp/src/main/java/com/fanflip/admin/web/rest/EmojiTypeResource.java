package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.EmojiTypeRepository;
import com.fanflip.admin.service.EmojiTypeService;
import com.fanflip.admin.service.dto.EmojiTypeDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.EmojiType}.
 */
@RestController
@RequestMapping("/api/emoji-types")
public class EmojiTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmojiTypeResource.class);

    private static final String ENTITY_NAME = "emojiType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmojiTypeService emojiTypeService;

    private final EmojiTypeRepository emojiTypeRepository;

    public EmojiTypeResource(EmojiTypeService emojiTypeService, EmojiTypeRepository emojiTypeRepository) {
        this.emojiTypeService = emojiTypeService;
        this.emojiTypeRepository = emojiTypeRepository;
    }

    /**
     * {@code POST  /emoji-types} : Create a new emojiType.
     *
     * @param emojiTypeDTO the emojiTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emojiTypeDTO, or with status {@code 400 (Bad Request)} if the emojiType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<EmojiTypeDTO>> createEmojiType(@Valid @RequestBody EmojiTypeDTO emojiTypeDTO) throws URISyntaxException {
        log.debug("REST request to save EmojiType : {}", emojiTypeDTO);
        if (emojiTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new emojiType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return emojiTypeService
            .save(emojiTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/emoji-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /emoji-types/:id} : Updates an existing emojiType.
     *
     * @param id the id of the emojiTypeDTO to save.
     * @param emojiTypeDTO the emojiTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emojiTypeDTO,
     * or with status {@code 400 (Bad Request)} if the emojiTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emojiTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmojiTypeDTO>> updateEmojiType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmojiTypeDTO emojiTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmojiType : {}, {}", id, emojiTypeDTO);
        if (emojiTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emojiTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emojiTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return emojiTypeService
                    .update(emojiTypeDTO)
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
     * {@code PATCH  /emoji-types/:id} : Partial updates given fields of an existing emojiType, field will ignore if it is null
     *
     * @param id the id of the emojiTypeDTO to save.
     * @param emojiTypeDTO the emojiTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emojiTypeDTO,
     * or with status {@code 400 (Bad Request)} if the emojiTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emojiTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emojiTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<EmojiTypeDTO>> partialUpdateEmojiType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmojiTypeDTO emojiTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmojiType partially : {}, {}", id, emojiTypeDTO);
        if (emojiTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emojiTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return emojiTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<EmojiTypeDTO> result = emojiTypeService.partialUpdate(emojiTypeDTO);

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
     * {@code GET  /emoji-types} : get all the emojiTypes.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emojiTypes in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<EmojiTypeDTO>>> getAllEmojiTypes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of EmojiTypes");
        return emojiTypeService
            .countAll()
            .zipWith(emojiTypeService.findAll(pageable).collectList())
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
     * {@code GET  /emoji-types/:id} : get the "id" emojiType.
     *
     * @param id the id of the emojiTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emojiTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmojiTypeDTO>> getEmojiType(@PathVariable("id") Long id) {
        log.debug("REST request to get EmojiType : {}", id);
        Mono<EmojiTypeDTO> emojiTypeDTO = emojiTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emojiTypeDTO);
    }

    /**
     * {@code DELETE  /emoji-types/:id} : delete the "id" emojiType.
     *
     * @param id the id of the emojiTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmojiType(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmojiType : {}", id);
        return emojiTypeService
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
     * {@code SEARCH  /emoji-types/_search?query=:query} : search for the emojiType corresponding
     * to the query.
     *
     * @param query the query of the emojiType search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<EmojiTypeDTO>>> searchEmojiTypes(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of EmojiTypes for query {}", query);
        return emojiTypeService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(emojiTypeService.search(query, pageable)));
    }
}
