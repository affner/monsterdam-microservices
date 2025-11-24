package com.monsterdam.profile.web.rest;

import com.monsterdam.profile.repository.HashTagRepository;
import com.monsterdam.profile.service.HashTagService;
import com.monsterdam.profile.service.dto.HashTagDTO;
import com.monsterdam.profile.web.rest.errors.BadRequestAlertException;
import com.monsterdam.profile.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link com.monsterdam.profile.domain.HashTag}.
 */
@RestController
@RequestMapping("/api/hash-tags")
public class HashTagResource {

    private final Logger log = LoggerFactory.getLogger(HashTagResource.class);

    private static final String ENTITY_NAME = "profileHashTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashTagService hashTagService;

    private final HashTagRepository hashTagRepository;

    public HashTagResource(HashTagService hashTagService, HashTagRepository hashTagRepository) {
        this.hashTagService = hashTagService;
        this.hashTagRepository = hashTagRepository;
    }

    /**
     * {@code POST  /hash-tags} : Create a new hashTag.
     *
     * @param hashTagDTO the hashTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashTagDTO, or with status {@code 400 (Bad Request)} if the hashTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HashTagDTO> createHashTag(@Valid @RequestBody HashTagDTO hashTagDTO) throws URISyntaxException {
        log.debug("REST request to save HashTag : {}", hashTagDTO);
        if (hashTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new hashTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HashTagDTO result = hashTagService.save(hashTagDTO);
        return ResponseEntity
            .created(new URI("/api/hash-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hash-tags/:id} : Updates an existing hashTag.
     *
     * @param id the id of the hashTagDTO to save.
     * @param hashTagDTO the hashTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashTagDTO,
     * or with status {@code 400 (Bad Request)} if the hashTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HashTagDTO> updateHashTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HashTagDTO hashTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HashTag : {}, {}", id, hashTagDTO);
        if (hashTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HashTagDTO result = hashTagService.update(hashTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hash-tags/:id} : Partial updates given fields of an existing hashTag, field will ignore if it is null
     *
     * @param id the id of the hashTagDTO to save.
     * @param hashTagDTO the hashTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashTagDTO,
     * or with status {@code 400 (Bad Request)} if the hashTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hashTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hashTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HashTagDTO> partialUpdateHashTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HashTagDTO hashTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HashTag partially : {}, {}", id, hashTagDTO);
        if (hashTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HashTagDTO> result = hashTagService.partialUpdate(hashTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hash-tags} : get all the hashTags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashTags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HashTagDTO>> getAllHashTags(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of HashTags");
        Page<HashTagDTO> page = hashTagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hash-tags/:id} : get the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HashTagDTO> getHashTag(@PathVariable("id") Long id) {
        log.debug("REST request to get HashTag : {}", id);
        Optional<HashTagDTO> hashTagDTO = hashTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashTagDTO);
    }

    /**
     * {@code DELETE  /hash-tags/:id} : delete the "id" hashTag.
     *
     * @param id the id of the hashTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHashTag(@PathVariable("id") Long id) {
        log.debug("REST request to delete HashTag : {}", id);
        hashTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /hash-tags/_search?query=:query} : search for the hashTag corresponding
     * to the query.
     *
     * @param query the query of the hashTag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<HashTagDTO>> searchHashTags(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of HashTags for query {}", query);
        try {
            Page<HashTagDTO> page = hashTagService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
