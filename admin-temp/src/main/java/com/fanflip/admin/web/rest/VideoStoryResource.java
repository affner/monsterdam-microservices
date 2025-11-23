package com.fanflip.admin.web.rest;

import com.fanflip.admin.repository.VideoStoryRepository;
import com.fanflip.admin.service.VideoStoryService;
import com.fanflip.admin.service.dto.VideoStoryDTO;
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
 * REST controller for managing {@link com.fanflip.admin.domain.VideoStory}.
 */
@RestController
@RequestMapping("/api/video-stories")
public class VideoStoryResource {

    private final Logger log = LoggerFactory.getLogger(VideoStoryResource.class);

    private static final String ENTITY_NAME = "videoStory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoStoryService videoStoryService;

    private final VideoStoryRepository videoStoryRepository;

    public VideoStoryResource(VideoStoryService videoStoryService, VideoStoryRepository videoStoryRepository) {
        this.videoStoryService = videoStoryService;
        this.videoStoryRepository = videoStoryRepository;
    }

    /**
     * {@code POST  /video-stories} : Create a new videoStory.
     *
     * @param videoStoryDTO the videoStoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoStoryDTO, or with status {@code 400 (Bad Request)} if the videoStory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<VideoStoryDTO>> createVideoStory(@Valid @RequestBody VideoStoryDTO videoStoryDTO) throws URISyntaxException {
        log.debug("REST request to save VideoStory : {}", videoStoryDTO);
        if (videoStoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new videoStory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return videoStoryService
            .save(videoStoryDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/video-stories/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /video-stories/:id} : Updates an existing videoStory.
     *
     * @param id the id of the videoStoryDTO to save.
     * @param videoStoryDTO the videoStoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoStoryDTO,
     * or with status {@code 400 (Bad Request)} if the videoStoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videoStoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VideoStoryDTO>> updateVideoStory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VideoStoryDTO videoStoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VideoStory : {}, {}", id, videoStoryDTO);
        if (videoStoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoStoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return videoStoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return videoStoryService
                    .update(videoStoryDTO)
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
     * {@code PATCH  /video-stories/:id} : Partial updates given fields of an existing videoStory, field will ignore if it is null
     *
     * @param id the id of the videoStoryDTO to save.
     * @param videoStoryDTO the videoStoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoStoryDTO,
     * or with status {@code 400 (Bad Request)} if the videoStoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the videoStoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the videoStoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<VideoStoryDTO>> partialUpdateVideoStory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VideoStoryDTO videoStoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VideoStory partially : {}, {}", id, videoStoryDTO);
        if (videoStoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videoStoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return videoStoryRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<VideoStoryDTO> result = videoStoryService.partialUpdate(videoStoryDTO);

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
     * {@code GET  /video-stories} : get all the videoStories.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoStories in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<VideoStoryDTO>>> getAllVideoStories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of VideoStories");
        return videoStoryService
            .countAll()
            .zipWith(videoStoryService.findAll(pageable).collectList())
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
     * {@code GET  /video-stories/:id} : get the "id" videoStory.
     *
     * @param id the id of the videoStoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoStoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VideoStoryDTO>> getVideoStory(@PathVariable("id") Long id) {
        log.debug("REST request to get VideoStory : {}", id);
        Mono<VideoStoryDTO> videoStoryDTO = videoStoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(videoStoryDTO);
    }

    /**
     * {@code DELETE  /video-stories/:id} : delete the "id" videoStory.
     *
     * @param id the id of the videoStoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteVideoStory(@PathVariable("id") Long id) {
        log.debug("REST request to delete VideoStory : {}", id);
        return videoStoryService
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
     * {@code SEARCH  /video-stories/_search?query=:query} : search for the videoStory corresponding
     * to the query.
     *
     * @param query the query of the videoStory search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<VideoStoryDTO>>> searchVideoStories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to search for a page of VideoStories for query {}", query);
        return videoStoryService
            .searchCount()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(videoStoryService.search(query, pageable)));
    }
}
