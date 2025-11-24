package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.DirectMessageRepository;
import com.monsterdam.interactions.service.DirectMessageService;
import com.monsterdam.interactions.service.dto.DirectMessageDTO;
import com.monsterdam.interactions.web.rest.errors.BadRequestAlertException;
import com.monsterdam.interactions.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.monsterdam.interactions.domain.DirectMessage}.
 */
@RestController
@RequestMapping("/api/direct-messages")
public class DirectMessageResource {

    private final Logger log = LoggerFactory.getLogger(DirectMessageResource.class);

    private static final String ENTITY_NAME = "interactionsDirectMessage";

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
    public ResponseEntity<DirectMessageDTO> createDirectMessage(@Valid @RequestBody DirectMessageDTO directMessageDTO)
        throws URISyntaxException {
        log.debug("REST request to save DirectMessage : {}", directMessageDTO);
        if (directMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new directMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DirectMessageDTO result = directMessageService.save(directMessageDTO);
        return ResponseEntity
            .created(new URI("/api/direct-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<DirectMessageDTO> updateDirectMessage(
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

        if (!directMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DirectMessageDTO result = directMessageService.update(directMessageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directMessageDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<DirectMessageDTO> partialUpdateDirectMessage(
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

        if (!directMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DirectMessageDTO> result = directMessageService.partialUpdate(directMessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directMessageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /direct-messages} : get all the directMessages.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directMessages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DirectMessageDTO>> getAllDirectMessages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of DirectMessages");
        Page<DirectMessageDTO> page;
        if (eagerload) {
            page = directMessageService.findAllWithEagerRelationships(pageable);
        } else {
            page = directMessageService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /direct-messages/:id} : get the "id" directMessage.
     *
     * @param id the id of the directMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DirectMessageDTO> getDirectMessage(@PathVariable("id") Long id) {
        log.debug("REST request to get DirectMessage : {}", id);
        Optional<DirectMessageDTO> directMessageDTO = directMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(directMessageDTO);
    }

    /**
     * {@code DELETE  /direct-messages/:id} : delete the "id" directMessage.
     *
     * @param id the id of the directMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirectMessage(@PathVariable("id") Long id) {
        log.debug("REST request to delete DirectMessage : {}", id);
        directMessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /direct-messages/_search?query=:query} : search for the directMessage corresponding
     * to the query.
     *
     * @param query the query of the directMessage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DirectMessageDTO>> searchDirectMessages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of DirectMessages for query {}", query);
        try {
            Page<DirectMessageDTO> page = directMessageService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
