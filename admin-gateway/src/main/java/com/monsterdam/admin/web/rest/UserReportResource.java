package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.UserReportRepository;
import com.monsterdam.admin.service.UserReportService;
import com.monsterdam.admin.service.dto.UserReportDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.UserReport}.
 */
@RestController
@RequestMapping("/api/user-reports")
public class UserReportResource {

    private final Logger log = LoggerFactory.getLogger(UserReportResource.class);

    private static final String ENTITY_NAME = "userReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserReportService userReportService;

    private final UserReportRepository userReportRepository;

    public UserReportResource(UserReportService userReportService, UserReportRepository userReportRepository) {
        this.userReportService = userReportService;
        this.userReportRepository = userReportRepository;
    }

    /**
     * {@code POST  /user-reports} : Create a new userReport.
     *
     * @param userReportDTO the userReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userReportDTO, or with status {@code 400 (Bad Request)} if the userReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<UserReportDTO>> createUserReport(@Valid @RequestBody UserReportDTO userReportDTO) throws URISyntaxException {
        log.debug("REST request to save UserReport : {}", userReportDTO);
        if (userReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new userReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return userReportService
            .save(userReportDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/user-reports/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /user-reports/:id} : Updates an existing userReport.
     *
     * @param id the id of the userReportDTO to save.
     * @param userReportDTO the userReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReportDTO,
     * or with status {@code 400 (Bad Request)} if the userReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserReportDTO>> updateUserReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserReportDTO userReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserReport : {}, {}", id, userReportDTO);
        if (userReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userReportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return userReportService
                    .update(userReportDTO)
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
     * {@code PATCH  /user-reports/:id} : Partial updates given fields of an existing userReport, field will ignore if it is null
     *
     * @param id the id of the userReportDTO to save.
     * @param userReportDTO the userReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userReportDTO,
     * or with status {@code 400 (Bad Request)} if the userReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<UserReportDTO>> partialUpdateUserReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserReportDTO userReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserReport partially : {}, {}", id, userReportDTO);
        if (userReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return userReportRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<UserReportDTO> result = userReportService.partialUpdate(userReportDTO);

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
     * {@code GET  /user-reports} : get all the userReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userReports in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<UserReportDTO>> getAllUserReports() {
        log.debug("REST request to get all UserReports");
        return userReportService.findAll().collectList();
    }

    /**
     * {@code GET  /user-reports} : get all the userReports as a stream.
     * @return the {@link Flux} of userReports.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<UserReportDTO> getAllUserReportsAsStream() {
        log.debug("REST request to get all UserReports as a stream");
        return userReportService.findAll();
    }

    /**
     * {@code GET  /user-reports/:id} : get the "id" userReport.
     *
     * @param id the id of the userReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserReportDTO>> getUserReport(@PathVariable("id") Long id) {
        log.debug("REST request to get UserReport : {}", id);
        Mono<UserReportDTO> userReportDTO = userReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userReportDTO);
    }

    /**
     * {@code DELETE  /user-reports/:id} : delete the "id" userReport.
     *
     * @param id the id of the userReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUserReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserReport : {}", id);
        return userReportService
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
     * {@code SEARCH  /user-reports/_search?query=:query} : search for the userReport corresponding
     * to the query.
     *
     * @param query the query of the userReport search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<List<UserReportDTO>> searchUserReports(@RequestParam("query") String query) {
        log.debug("REST request to search UserReports for query {}", query);
        try {
            return userReportService.search(query).collectList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
