package com.monsterdam.admin.web.rest;

import com.monsterdam.admin.repository.BookMarkRepository;
import com.monsterdam.admin.service.BookMarkService;
import com.monsterdam.admin.service.dto.BookMarkDTO;
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
 * REST controller for managing {@link com.monsterdam.admin.domain.BookMark}.
 */
@RestController
@RequestMapping("/api/book-marks")
public class BookMarkResource {

    private final Logger log = LoggerFactory.getLogger(BookMarkResource.class);

    private static final String ENTITY_NAME = "bookMark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookMarkService bookMarkService;

    private final BookMarkRepository bookMarkRepository;

    public BookMarkResource(BookMarkService bookMarkService, BookMarkRepository bookMarkRepository) {
        this.bookMarkService = bookMarkService;
        this.bookMarkRepository = bookMarkRepository;
    }

    /**
     * {@code POST  /book-marks} : Create a new bookMark.
     *
     * @param bookMarkDTO the bookMarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookMarkDTO, or with status {@code 400 (Bad Request)} if the bookMark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<BookMarkDTO>> createBookMark(@Valid @RequestBody BookMarkDTO bookMarkDTO) throws URISyntaxException {
        log.debug("REST request to save BookMark : {}", bookMarkDTO);
        if (bookMarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookMark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bookMarkService
            .save(bookMarkDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/book-marks/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /book-marks/:id} : Updates an existing bookMark.
     *
     * @param id the id of the bookMarkDTO to save.
     * @param bookMarkDTO the bookMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookMarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookMarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<BookMarkDTO>> updateBookMark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookMarkDTO bookMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BookMark : {}, {}", id, bookMarkDTO);
        if (bookMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bookMarkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bookMarkService
                    .update(bookMarkDTO)
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
     * {@code PATCH  /book-marks/:id} : Partial updates given fields of an existing bookMark, field will ignore if it is null
     *
     * @param id the id of the bookMarkDTO to save.
     * @param bookMarkDTO the bookMarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookMarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookMarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookMarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookMarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BookMarkDTO>> partialUpdateBookMark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookMarkDTO bookMarkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookMark partially : {}, {}", id, bookMarkDTO);
        if (bookMarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookMarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bookMarkRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BookMarkDTO> result = bookMarkService.partialUpdate(bookMarkDTO);

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
     * {@code GET  /book-marks} : get all the bookMarks.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookMarks in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<BookMarkDTO>>> getAllBookMarks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BookMarks");
        return bookMarkService
            .countAll()
            .zipWith(bookMarkService.findAll(pageable).collectList())
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
     * {@code GET  /book-marks/:id} : get the "id" bookMark.
     *
     * @param id the id of the bookMarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookMarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<BookMarkDTO>> getBookMark(@PathVariable("id") Long id) {
        log.debug("REST request to get BookMark : {}", id);
        Mono<BookMarkDTO> bookMarkDTO = bookMarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookMarkDTO);
    }

    /**
     * {@code DELETE  /book-marks/:id} : delete the "id" bookMark.
     *
     * @param id the id of the bookMarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBookMark(@PathVariable("id") Long id) {
        log.debug("REST request to delete BookMark : {}", id);
        return bookMarkService
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
     * {@code SEARCH  /book-marks/_search?query=:query} : search for the bookMark corresponding
     * to the query.
     *
     * @param query the query of the bookMark search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<BookMarkDTO>>> searchBookMarks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of BookMarks for query {}", query);
        return bookMarkService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(bookMarkService.search(query, pageable)));
    }
}
