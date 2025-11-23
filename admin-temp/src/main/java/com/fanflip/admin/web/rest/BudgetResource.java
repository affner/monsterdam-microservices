package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.BudgetRepository;
import com.fanflip.admin.service.BudgetService;
import com.fanflip.admin.service.dto.BudgetDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.Budget}.
 */
@RestController
@RequestMapping("/api/budgets")
public class BudgetResource {

    private final Logger log = LoggerFactory.getLogger(BudgetResource.class);

    private static final String ENTITY_NAME = "budget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BudgetService budgetService;

    private final BudgetRepository budgetRepository;

    public BudgetResource(BudgetService budgetService, BudgetRepository budgetRepository) {
        this.budgetService = budgetService;
        this.budgetRepository = budgetRepository;
    }

    /**
     * {@code POST  /budgets} : Create a new budget.
     *
     * @param budgetDTO the budgetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new budgetDTO, or with status {@code 400 (Bad Request)} if the budget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<BudgetDTO>> createBudget(@Valid @RequestBody BudgetDTO budgetDTO) throws URISyntaxException {
        log.debug("REST request to save Budget : {}", budgetDTO);
        if (budgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new budget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return budgetService
            .save(budgetDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/budgets/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /budgets/:id} : Updates an existing budget.
     *
     * @param id the id of the budgetDTO to save.
     * @param budgetDTO the budgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO,
     * or with status {@code 400 (Bad Request)} if the budgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the budgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BudgetDTO>> updateBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BudgetDTO budgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Budget : {}, {}", id, budgetDTO);
        if (budgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return budgetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return budgetService
                    .update(budgetDTO)
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
     * {@code PATCH  /budgets/:id} : Partial updates given fields of an existing budget, field will ignore if it is null
     *
     * @param id the id of the budgetDTO to save.
     * @param budgetDTO the budgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated budgetDTO,
     * or with status {@code 400 (Bad Request)} if the budgetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the budgetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the budgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BudgetDTO>> partialUpdateBudget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BudgetDTO budgetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Budget partially : {}, {}", id, budgetDTO);
        if (budgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, budgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return budgetRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BudgetDTO> result = budgetService.partialUpdate(budgetDTO);

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
     * {@code GET  /budgets} : get all the budgets.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of budgets in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<BudgetDTO>>> getAllBudgets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Budgets");
        return budgetService
            .countAll()
            .zipWith(budgetService.findAll(pageable).collectList())
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
     * {@code GET  /budgets/:id} : get the "id" budget.
     *
     * @param id the id of the budgetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the budgetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BudgetDTO>> getBudget(@PathVariable("id") Long id) {
        log.debug("REST request to get Budget : {}", id);
        Mono<BudgetDTO> budgetDTO = budgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(budgetDTO);
    }

    /**
     * {@code DELETE  /budgets/:id} : delete the "id" budget.
     *
     * @param id the id of the budgetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBudget(@PathVariable("id") Long id) {
        log.debug("REST request to delete Budget : {}", id);
        return budgetService
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
     * {@code SEARCH  /budgets/_search?query=:query} : search for the budget corresponding
     * to the query.
     *
     * @param query the query of the budget search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<BudgetDTO>>> searchBudgets(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of Budgets for query {}", query);
        return budgetService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(budgetService.search(query, pageable)));
    }
}
