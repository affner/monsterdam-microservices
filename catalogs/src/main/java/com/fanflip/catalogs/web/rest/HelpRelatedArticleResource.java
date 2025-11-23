package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.HelpRelatedArticleRepository;
import com.fanflip.catalogs.service.HelpRelatedArticleService;
import com.fanflip.catalogs.service.dto.HelpRelatedArticleDTO;
import com.fanflip.catalogs.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.HelpRelatedArticle}.
 */
@RestController
@RequestMapping("/api/help-related-articles")
public class HelpRelatedArticleResource {

    private final Logger log = LoggerFactory.getLogger(HelpRelatedArticleResource.class);

    private static final String ENTITY_NAME = "catalogsHelpRelatedArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpRelatedArticleService helpRelatedArticleService;

    private final HelpRelatedArticleRepository helpRelatedArticleRepository;

    public HelpRelatedArticleResource(
        HelpRelatedArticleService helpRelatedArticleService,
        HelpRelatedArticleRepository helpRelatedArticleRepository
    ) {
        this.helpRelatedArticleService = helpRelatedArticleService;
        this.helpRelatedArticleRepository = helpRelatedArticleRepository;
    }

    /**
     * {@code POST  /help-related-articles} : Create a new helpRelatedArticle.
     *
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpRelatedArticleDTO, or with status {@code 400 (Bad Request)} if the helpRelatedArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HelpRelatedArticleDTO> createHelpRelatedArticle(@Valid @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpRelatedArticle : {}", helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpRelatedArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HelpRelatedArticleDTO result = helpRelatedArticleService.save(helpRelatedArticleDTO);
        return ResponseEntity
            .created(new URI("/api/help-related-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /help-related-articles/:id} : Updates an existing helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to save.
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpRelatedArticleDTO,
     * or with status {@code 400 (Bad Request)} if the helpRelatedArticleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpRelatedArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HelpRelatedArticleDTO> updateHelpRelatedArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpRelatedArticle : {}, {}", id, helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpRelatedArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpRelatedArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HelpRelatedArticleDTO result = helpRelatedArticleService.update(helpRelatedArticleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpRelatedArticleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /help-related-articles/:id} : Partial updates given fields of an existing helpRelatedArticle, field will ignore if it is null
     *
     * @param id the id of the helpRelatedArticleDTO to save.
     * @param helpRelatedArticleDTO the helpRelatedArticleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpRelatedArticleDTO,
     * or with status {@code 400 (Bad Request)} if the helpRelatedArticleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpRelatedArticleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpRelatedArticleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HelpRelatedArticleDTO> partialUpdateHelpRelatedArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpRelatedArticleDTO helpRelatedArticleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpRelatedArticle partially : {}, {}", id, helpRelatedArticleDTO);
        if (helpRelatedArticleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpRelatedArticleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpRelatedArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HelpRelatedArticleDTO> result = helpRelatedArticleService.partialUpdate(helpRelatedArticleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpRelatedArticleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /help-related-articles} : get all the helpRelatedArticles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpRelatedArticles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HelpRelatedArticleDTO>> getAllHelpRelatedArticles(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of HelpRelatedArticles");
        Page<HelpRelatedArticleDTO> page = helpRelatedArticleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /help-related-articles/:id} : get the "id" helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpRelatedArticleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HelpRelatedArticleDTO> getHelpRelatedArticle(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpRelatedArticle : {}", id);
        Optional<HelpRelatedArticleDTO> helpRelatedArticleDTO = helpRelatedArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpRelatedArticleDTO);
    }

    /**
     * {@code DELETE  /help-related-articles/:id} : delete the "id" helpRelatedArticle.
     *
     * @param id the id of the helpRelatedArticleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelpRelatedArticle(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpRelatedArticle : {}", id);
        helpRelatedArticleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
