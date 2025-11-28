package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.ContentPackageRepository;
import com.monsterdam.multimedia.service.ContentPackageService;
import com.monsterdam.multimedia.service.dto.ContentPackageDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.multimedia.domain.ContentPackage}.
 */
@RestController
@RequestMapping("/api/content-packages")
public class ContentPackageResource {

    private final Logger log = LoggerFactory.getLogger(ContentPackageResource.class);

    private static final String ENTITY_NAME = "multimediaContentPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContentPackageService contentPackageService;

    private final ContentPackageRepository contentPackageRepository;

    public ContentPackageResource(ContentPackageService contentPackageService, ContentPackageRepository contentPackageRepository) {
        this.contentPackageService = contentPackageService;
        this.contentPackageRepository = contentPackageRepository;
    }

    /**
     * {@code POST  /content-packages} : Create a new contentPackage.
     *
     * @param contentPackageDTO the contentPackageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contentPackageDTO, or with status {@code 400 (Bad Request)} if the contentPackage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContentPackageDTO> createContentPackage(@Valid @RequestBody ContentPackageDTO contentPackageDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContentPackage : {}", contentPackageDTO);
        if (contentPackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentPackage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContentPackageDTO result = contentPackageService.save(contentPackageDTO);
        return ResponseEntity
            .created(new URI("/api/content-packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /content-packages/:id} : Updates an existing contentPackage.
     *
     * @param id the id of the contentPackageDTO to save.
     * @param contentPackageDTO the contentPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPackageDTO,
     * or with status {@code 400 (Bad Request)} if the contentPackageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contentPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContentPackageDTO> updateContentPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentPackageDTO contentPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContentPackage : {}, {}", id, contentPackageDTO);
        if (contentPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContentPackageDTO result = contentPackageService.update(contentPackageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentPackageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /content-packages/:id} : Partial updates given fields of an existing contentPackage, field will ignore if it is null
     *
     * @param id the id of the contentPackageDTO to save.
     * @param contentPackageDTO the contentPackageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contentPackageDTO,
     * or with status {@code 400 (Bad Request)} if the contentPackageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contentPackageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contentPackageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContentPackageDTO> partialUpdateContentPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContentPackageDTO contentPackageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContentPackage partially : {}, {}", id, contentPackageDTO);
        if (contentPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contentPackageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContentPackageDTO> result = contentPackageService.partialUpdate(contentPackageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contentPackageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /content-packages} : get all the contentPackages.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contentPackages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContentPackageDTO>> getAllContentPackages(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("specialreward-is-null".equals(filter)) {
            log.debug("REST request to get all ContentPackages where specialReward is null");
            return new ResponseEntity<>(contentPackageService.findAllWhereSpecialRewardIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of ContentPackages");
        Page<ContentPackageDTO> page = contentPackageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /content-packages/:id} : get the "id" contentPackage.
     *
     * @param id the id of the contentPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContentPackageDTO> getContentPackage(@PathVariable("id") Long id) {
        log.debug("REST request to get ContentPackage : {}", id);
        Optional<ContentPackageDTO> contentPackageDTO = contentPackageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contentPackageDTO);
    }

    /**
     * {@code GET  /content-packages/by-post-id/:id} : get the "id" contentPackage.
     *
     * @param postId the id of the contentPackageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contentPackageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/content-packages/by-post-id/{postId}")
    public ResponseEntity<ContentPackageDTO> getContentPackageByPostFeedId(@PathVariable("postId") Long postId) {
        log.debug("REST request to get ContentPackage : {}", postId);
        Optional<ContentPackageDTO> contentPackageDTO = contentPackageService.findOneByPostFeedId(postId);
        return ResponseUtil.wrapOrNotFound(contentPackageDTO);
    }


    /**
     * {@code DELETE  /content-packages/:id} : delete the "id" contentPackage.
     *
     * @param id the id of the contentPackageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContentPackage(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContentPackage : {}", id);
        contentPackageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
