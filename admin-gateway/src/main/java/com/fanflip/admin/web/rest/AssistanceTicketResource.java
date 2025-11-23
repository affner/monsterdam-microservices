package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.AssistanceTicketRepository;
import com.fanflip.admin.service.AssistanceTicketService;
import com.fanflip.admin.service.dto.AssistanceTicketDTO;
import com.fanflip.admin.web.rest.errors.BadRequestAlertException;
import com.fanflip.admin.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.fanflip.admin.domain.AssistanceTicket}.
 */
@RestController
@RequestMapping("/api/assistance-tickets")
public class AssistanceTicketResource {

    private final Logger log = LoggerFactory.getLogger(AssistanceTicketResource.class);

    private static final String ENTITY_NAME = "assistanceTicket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssistanceTicketService assistanceTicketService;

    private final AssistanceTicketRepository assistanceTicketRepository;

    public AssistanceTicketResource(
        AssistanceTicketService assistanceTicketService,
        AssistanceTicketRepository assistanceTicketRepository
    ) {
        this.assistanceTicketService = assistanceTicketService;
        this.assistanceTicketRepository = assistanceTicketRepository;
    }

    /**
     * {@code POST  /assistance-tickets} : Create a new assistanceTicket.
     *
     * @param assistanceTicketDTO the assistanceTicketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assistanceTicketDTO, or with status {@code 400 (Bad Request)} if the assistanceTicket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<AssistanceTicketDTO>> createAssistanceTicket(@Valid @RequestBody AssistanceTicketDTO assistanceTicketDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssistanceTicket : {}", assistanceTicketDTO);
        if (assistanceTicketDTO.getId() != null) {
            throw new BadRequestAlertException("A new assistanceTicket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return assistanceTicketService
            .save(assistanceTicketDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/assistance-tickets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /assistance-tickets/:id} : Updates an existing assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to save.
     * @param assistanceTicketDTO the assistanceTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assistanceTicketDTO,
     * or with status {@code 400 (Bad Request)} if the assistanceTicketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assistanceTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AssistanceTicketDTO>> updateAssistanceTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssistanceTicketDTO assistanceTicketDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssistanceTicket : {}, {}", id, assistanceTicketDTO);
        if (assistanceTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assistanceTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assistanceTicketRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return assistanceTicketService
                    .update(assistanceTicketDTO)
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
     * {@code PATCH  /assistance-tickets/:id} : Partial updates given fields of an existing assistanceTicket, field will ignore if it is null
     *
     * @param id the id of the assistanceTicketDTO to save.
     * @param assistanceTicketDTO the assistanceTicketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assistanceTicketDTO,
     * or with status {@code 400 (Bad Request)} if the assistanceTicketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assistanceTicketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assistanceTicketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AssistanceTicketDTO>> partialUpdateAssistanceTicket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssistanceTicketDTO assistanceTicketDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssistanceTicket partially : {}, {}", id, assistanceTicketDTO);
        if (assistanceTicketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assistanceTicketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return assistanceTicketRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AssistanceTicketDTO> result = assistanceTicketService.partialUpdate(assistanceTicketDTO);

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
     * {@code GET  /assistance-tickets} : get all the assistanceTickets.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assistanceTickets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<AssistanceTicketDTO>> getAllAssistanceTickets(@RequestParam(name = "filter", required = false) String filter) {
        if ("report-is-null".equals(filter)) {
            log.debug("REST request to get all AssistanceTickets where report is null");
            return assistanceTicketService.findAllWhereReportIsNull().collectList();
        }

        if ("documentsreview-is-null".equals(filter)) {
            log.debug("REST request to get all AssistanceTickets where documentsReview is null");
            return assistanceTicketService.findAllWhereDocumentsReviewIsNull().collectList();
        }
        log.debug("REST request to get all AssistanceTickets");
        return assistanceTicketService.findAll().collectList();
    }

    /**
     * {@code GET  /assistance-tickets} : get all the assistanceTickets as a stream.
     * @return the {@link Flux} of assistanceTickets.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AssistanceTicketDTO> getAllAssistanceTicketsAsStream() {
        log.debug("REST request to get all AssistanceTickets as a stream");
        return assistanceTicketService.findAll();
    }

    /**
     * {@code GET  /assistance-tickets/:id} : get the "id" assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assistanceTicketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AssistanceTicketDTO>> getAssistanceTicket(@PathVariable("id") Long id) {
        log.debug("REST request to get AssistanceTicket : {}", id);
        Mono<AssistanceTicketDTO> assistanceTicketDTO = assistanceTicketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assistanceTicketDTO);
    }

    /**
     * {@code DELETE  /assistance-tickets/:id} : delete the "id" assistanceTicket.
     *
     * @param id the id of the assistanceTicketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAssistanceTicket(@PathVariable("id") Long id) {
        log.debug("REST request to delete AssistanceTicket : {}", id);
        return assistanceTicketService
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
     * {@code SEARCH  /assistance-tickets/_search?query=:query} : search for the assistanceTicket corresponding
     * to the query.
     *
     * @param query the query of the assistanceTicket search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<AssistanceTicketDTO>> searchAssistanceTickets(@RequestParam("query") String query) {
        log.debug("REST request to search AssistanceTickets for query {}", query);
        try {
            return assistanceTicketService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
