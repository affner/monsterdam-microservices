package com.fanflip.profile.web.rest;

import com.fanflip.profile.repository.PostFeedHashTagRelationRepository;
import com.fanflip.profile.service.PostFeedHashTagRelationService;
import com.fanflip.profile.service.dto.PostFeedHashTagRelationDTO;
import com.fanflip.profile.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.profile.domain.PostFeedHashTagRelation}.
 */
@RestController
@RequestMapping("/api/post-feed-hash-tag-relations")
public class PostFeedHashTagRelationResource {

    private final Logger log = LoggerFactory.getLogger(PostFeedHashTagRelationResource.class);

    private static final String ENTITY_NAME = "profilePostFeedHashTagRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostFeedHashTagRelationService postFeedHashTagRelationService;

    private final PostFeedHashTagRelationRepository postFeedHashTagRelationRepository;

    public PostFeedHashTagRelationResource(
        PostFeedHashTagRelationService postFeedHashTagRelationService,
        PostFeedHashTagRelationRepository postFeedHashTagRelationRepository
    ) {
        this.postFeedHashTagRelationService = postFeedHashTagRelationService;
        this.postFeedHashTagRelationRepository = postFeedHashTagRelationRepository;
    }

    /**
     * {@code POST  /post-feed-hash-tag-relations} : Create a new postFeedHashTagRelation.
     *
     * @param postFeedHashTagRelationDTO the postFeedHashTagRelationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postFeedHashTagRelationDTO, or with status {@code 400 (Bad Request)} if the postFeedHashTagRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostFeedHashTagRelationDTO> createPostFeedHashTagRelation(
        @Valid @RequestBody PostFeedHashTagRelationDTO postFeedHashTagRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PostFeedHashTagRelation : {}", postFeedHashTagRelationDTO);
        if (postFeedHashTagRelationDTO.getId() != null) {
            throw new BadRequestAlertException("A new postFeedHashTagRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostFeedHashTagRelationDTO result = postFeedHashTagRelationService.save(postFeedHashTagRelationDTO);
        return ResponseEntity
            .created(new URI("/api/post-feed-hash-tag-relations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-feed-hash-tag-relations/:id} : Updates an existing postFeedHashTagRelation.
     *
     * @param id the id of the postFeedHashTagRelationDTO to save.
     * @param postFeedHashTagRelationDTO the postFeedHashTagRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedHashTagRelationDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedHashTagRelationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postFeedHashTagRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostFeedHashTagRelationDTO> updatePostFeedHashTagRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostFeedHashTagRelationDTO postFeedHashTagRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostFeedHashTagRelation : {}, {}", id, postFeedHashTagRelationDTO);
        if (postFeedHashTagRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedHashTagRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postFeedHashTagRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostFeedHashTagRelationDTO result = postFeedHashTagRelationService.update(postFeedHashTagRelationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postFeedHashTagRelationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-feed-hash-tag-relations/:id} : Partial updates given fields of an existing postFeedHashTagRelation, field will ignore if it is null
     *
     * @param id the id of the postFeedHashTagRelationDTO to save.
     * @param postFeedHashTagRelationDTO the postFeedHashTagRelationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postFeedHashTagRelationDTO,
     * or with status {@code 400 (Bad Request)} if the postFeedHashTagRelationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postFeedHashTagRelationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postFeedHashTagRelationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostFeedHashTagRelationDTO> partialUpdatePostFeedHashTagRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostFeedHashTagRelationDTO postFeedHashTagRelationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostFeedHashTagRelation partially : {}, {}", id, postFeedHashTagRelationDTO);
        if (postFeedHashTagRelationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedHashTagRelationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postFeedHashTagRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostFeedHashTagRelationDTO> result = postFeedHashTagRelationService.partialUpdate(postFeedHashTagRelationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postFeedHashTagRelationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-feed-hash-tag-relations} : get all the postFeedHashTagRelations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postFeedHashTagRelations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostFeedHashTagRelationDTO>> getAllPostFeedHashTagRelations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of PostFeedHashTagRelations");
        Page<PostFeedHashTagRelationDTO> page = postFeedHashTagRelationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /post-feed-hash-tag-relations/:id} : get the "id" postFeedHashTagRelation.
     *
     * @param id the id of the postFeedHashTagRelationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postFeedHashTagRelationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostFeedHashTagRelationDTO> getPostFeedHashTagRelation(@PathVariable("id") Long id) {
        log.debug("REST request to get PostFeedHashTagRelation : {}", id);
        Optional<PostFeedHashTagRelationDTO> postFeedHashTagRelationDTO = postFeedHashTagRelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postFeedHashTagRelationDTO);
    }

    /**
     * {@code DELETE  /post-feed-hash-tag-relations/:id} : delete the "id" postFeedHashTagRelation.
     *
     * @param id the id of the postFeedHashTagRelationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostFeedHashTagRelation(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostFeedHashTagRelation : {}", id);
        postFeedHashTagRelationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
