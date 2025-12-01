package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.PostFeedRepository;
import com.monsterdam.interactions.service.PostFeedService;
import com.monsterdam.interactions.service.dto.PostFeedDTO;
import com.monsterdam.interactions.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
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
 * REST controller for managing {@link com.monsterdam.interactions.domain.PostFeed}.
 */
@RestController
@RequestMapping("/api/post-feeds")
public class PostFeedResource {

    private final Logger log = LoggerFactory.getLogger(PostFeedResource.class);

    private static final String ENTITY_NAME = "interactionsPostFeed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostFeedService postFeedService;

    private final PostFeedRepository postFeedRepository;

    public PostFeedResource(PostFeedService postFeedService, PostFeedRepository postFeedRepository) {
        this.postFeedService = postFeedService;
        this.postFeedRepository = postFeedRepository;
    }

    /**
     * {@code POST  /post-feeds} : Create a new postFeed.
     *
     * @param postFeedDTO the postFeedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postFeedDTO, or with status {@code 400 (Bad Request)} if the postFeed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostFeedDTO> createPostFeed(@Valid @RequestBody PostFeedDTO postFeedDTO) throws URISyntaxException {
        log.debug("REST request to save PostFeed : {}", postFeedDTO);
        if (postFeedDTO.getId() != null) {
            throw new BadRequestAlertException("A new postFeed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostFeedDTO result = postFeedService.save(postFeedDTO);
        return ResponseEntity
            .created(new URI("/api/post-feeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-feeds/:id} : Updates an existing postFeed.
     *
     * @param id the id of the postFeedDTO to save.
     * @param postFeedDTO the postFeedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postFeedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostFeedDTO> updatePostFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostFeedDTO postFeedDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostFeed : {}, {}", id, postFeedDTO);
        if (postFeedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postFeedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostFeedDTO result = postFeedService.update(postFeedDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postFeedDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-feeds/:id} : Partial updates given fields of an existing postFeed, field will ignore if it is null
     *
     * @param id the id of the postFeedDTO to save.
     * @param postFeedDTO the postFeedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postFeedDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postFeedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostFeedDTO> partialUpdatePostFeed(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostFeedDTO postFeedDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostFeed partially : {}, {}", id, postFeedDTO);
        if (postFeedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postFeedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostFeedDTO> result = postFeedService.partialUpdate(postFeedDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postFeedDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-feeds} : get all the postFeeds.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postFeeds in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostFeedDTO>> getAllPostFeeds(
        @ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of PostFeeds");
        Page<PostFeedDTO> page;
        if (eagerload) {
            page = postFeedService.findAllWithEagerRelationships(pageable);
        } else {
            page = postFeedService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /post-feeds/:id} : get the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFeedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostFeedDTO> getPostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to get PostFeed : {}", id);
        Optional<PostFeedDTO> postFeedDTO = postFeedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postFeedDTO);
    }

    /**
     * {@code DELETE  /post-feeds/:id} : delete the "id" postFeed.
     *
     * @param id the id of the postFeedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostFeed(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostFeed : {}", id);
        postFeedService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

}
