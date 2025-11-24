package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.VideoStoryRepository;
import com.monsterdam.multimedia.service.VideoStoryService;
import com.monsterdam.multimedia.service.dto.VideoStoryDTO;
import com.monsterdam.multimedia.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.multimedia.domain.VideoStory}.
 */
@RestController
@RequestMapping("/api/video-stories")
public class VideoStoryResource {

    private final Logger log = LoggerFactory.getLogger(VideoStoryResource.class);

    private static final String ENTITY_NAME = "multimediaVideoStory";

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
    public ResponseEntity<VideoStoryDTO> createVideoStory(@Valid @RequestBody VideoStoryDTO videoStoryDTO) throws URISyntaxException {
        log.debug("REST request to save VideoStory : {}", videoStoryDTO);
        if (videoStoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new videoStory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoStoryDTO result = videoStoryService.save(videoStoryDTO);
        return ResponseEntity
            .created(new URI("/api/video-stories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
    public ResponseEntity<VideoStoryDTO> updateVideoStory(
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

        if (!videoStoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VideoStoryDTO result = videoStoryService.update(videoStoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videoStoryDTO.getId().toString()))
            .body(result);
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
    public ResponseEntity<VideoStoryDTO> partialUpdateVideoStory(
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

        if (!videoStoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VideoStoryDTO> result = videoStoryService.partialUpdate(videoStoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videoStoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /video-stories} : get all the videoStories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoStories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VideoStoryDTO>> getAllVideoStories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VideoStories");
        Page<VideoStoryDTO> page = videoStoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /video-stories/:id} : get the "id" videoStory.
     *
     * @param id the id of the videoStoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoStoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoStoryDTO> getVideoStory(@PathVariable("id") Long id) {
        log.debug("REST request to get VideoStory : {}", id);
        Optional<VideoStoryDTO> videoStoryDTO = videoStoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(videoStoryDTO);
    }

    /**
     * {@code DELETE  /video-stories/:id} : delete the "id" videoStory.
     *
     * @param id the id of the videoStoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoStory(@PathVariable("id") Long id) {
        log.debug("REST request to delete VideoStory : {}", id);
        videoStoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
