package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.PostPollRepository;
import com.monsterdam.interactions.service.PostPollService;
import com.monsterdam.interactions.service.dto.PostPollDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.interactions.domain.PostPoll}.
 */
@RestController
@RequestMapping("/api/post-polls")
public class PostPollResource {

    private final Logger log = LoggerFactory.getLogger(PostPollResource.class);

    private static final String ENTITY_NAME = "interactionsPostPoll";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostPollService postPollService;

    private final PostPollRepository postPollRepository;

    public PostPollResource(PostPollService postPollService, PostPollRepository postPollRepository) {
        this.postPollService = postPollService;
        this.postPollRepository = postPollRepository;
    }

    /**
     * {@code POST  /post-polls} : Create a new postPoll.
     *
     * @param postPollDTO the postPollDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postPollDTO, or with status {@code 400 (Bad Request)} if the postPoll has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostPollDTO> createPostPoll(@Valid @RequestBody PostPollDTO postPollDTO) throws URISyntaxException {
        log.debug("REST request to save PostPoll : {}", postPollDTO);
        if (postPollDTO.getId() != null) {
            throw new BadRequestAlertException("A new postPoll cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PostPollDTO result = postPollService.save(postPollDTO);
        return ResponseEntity
            .created(new URI("/api/post-polls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /post-polls/:id} : Updates an existing postPoll.
     *
     * @param id the id of the postPollDTO to save.
     * @param postPollDTO the postPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPollDTO,
     * or with status {@code 400 (Bad Request)} if the postPollDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostPollDTO> updatePostPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostPollDTO postPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PostPoll : {}, {}", id, postPollDTO);
        if (postPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postPollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PostPollDTO result = postPollService.update(postPollDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postPollDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /post-polls/:id} : Partial updates given fields of an existing postPoll, field will ignore if it is null
     *
     * @param id the id of the postPollDTO to save.
     * @param postPollDTO the postPollDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postPollDTO,
     * or with status {@code 400 (Bad Request)} if the postPollDTO is not valid,
     * or with status {@code 404 (Not Found)} if the postPollDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the postPollDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostPollDTO> partialUpdatePostPoll(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PostPollDTO postPollDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PostPoll partially : {}, {}", id, postPollDTO);
        if (postPollDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postPollDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postPollRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostPollDTO> result = postPollService.partialUpdate(postPollDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postPollDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /post-polls} : get all the postPolls.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postPolls in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PostPollDTO>> getAllPostPolls(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("post-is-null".equals(filter)) {
            log.debug("REST request to get all PostPolls where post is null");
            return new ResponseEntity<>(postPollService.findAllWherePostIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of PostPolls");
        Page<PostPollDTO> page = postPollService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /post-polls/:id} : get the "id" postPoll.
     *
     * @param id the id of the postPollDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postPollDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostPollDTO> getPostPoll(@PathVariable("id") Long id) {
        log.debug("REST request to get PostPoll : {}", id);
        Optional<PostPollDTO> postPollDTO = postPollService.findOne(id);
        return ResponseUtil.wrapOrNotFound(postPollDTO);
    }

    /**
     * {@code DELETE  /post-polls/:id} : delete the "id" postPoll.
     *
     * @param id the id of the postPollDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostPoll(@PathVariable("id") Long id) {
        log.debug("REST request to delete PostPoll : {}", id);
        postPollService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
