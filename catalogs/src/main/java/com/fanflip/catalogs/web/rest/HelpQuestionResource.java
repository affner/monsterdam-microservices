package com.fanflip.catalogs.web.rest;

import com.fanflip.catalogs.repository.HelpQuestionRepository;
import com.fanflip.catalogs.service.HelpQuestionService;
import com.fanflip.catalogs.service.dto.HelpQuestionDTO;
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
 * REST controller for managing {@link com.fanflip.catalogs.domain.HelpQuestion}.
 */
@RestController
@RequestMapping("/api/help-questions")
public class HelpQuestionResource {

    private final Logger log = LoggerFactory.getLogger(HelpQuestionResource.class);

    private static final String ENTITY_NAME = "catalogsHelpQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HelpQuestionService helpQuestionService;

    private final HelpQuestionRepository helpQuestionRepository;

    public HelpQuestionResource(HelpQuestionService helpQuestionService, HelpQuestionRepository helpQuestionRepository) {
        this.helpQuestionService = helpQuestionService;
        this.helpQuestionRepository = helpQuestionRepository;
    }

    /**
     * {@code POST  /help-questions} : Create a new helpQuestion.
     *
     * @param helpQuestionDTO the helpQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new helpQuestionDTO, or with status {@code 400 (Bad Request)} if the helpQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HelpQuestionDTO> createHelpQuestion(@Valid @RequestBody HelpQuestionDTO helpQuestionDTO)
        throws URISyntaxException {
        log.debug("REST request to save HelpQuestion : {}", helpQuestionDTO);
        if (helpQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new helpQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HelpQuestionDTO result = helpQuestionService.save(helpQuestionDTO);
        return ResponseEntity
            .created(new URI("/api/help-questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /help-questions/:id} : Updates an existing helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to save.
     * @param helpQuestionDTO the helpQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the helpQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the helpQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HelpQuestionDTO> updateHelpQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HelpQuestionDTO helpQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HelpQuestion : {}, {}", id, helpQuestionDTO);
        if (helpQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HelpQuestionDTO result = helpQuestionService.update(helpQuestionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpQuestionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /help-questions/:id} : Partial updates given fields of an existing helpQuestion, field will ignore if it is null
     *
     * @param id the id of the helpQuestionDTO to save.
     * @param helpQuestionDTO the helpQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated helpQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the helpQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the helpQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the helpQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HelpQuestionDTO> partialUpdateHelpQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HelpQuestionDTO helpQuestionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HelpQuestion partially : {}, {}", id, helpQuestionDTO);
        if (helpQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, helpQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!helpQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HelpQuestionDTO> result = helpQuestionService.partialUpdate(helpQuestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, helpQuestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /help-questions} : get all the helpQuestions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of helpQuestions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HelpQuestionDTO>> getAllHelpQuestions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of HelpQuestions");
        Page<HelpQuestionDTO> page;
        if (eagerload) {
            page = helpQuestionService.findAllWithEagerRelationships(pageable);
        } else {
            page = helpQuestionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /help-questions/:id} : get the "id" helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the helpQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HelpQuestionDTO> getHelpQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get HelpQuestion : {}", id);
        Optional<HelpQuestionDTO> helpQuestionDTO = helpQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(helpQuestionDTO);
    }

    /**
     * {@code DELETE  /help-questions/:id} : delete the "id" helpQuestion.
     *
     * @param id the id of the helpQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHelpQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete HelpQuestion : {}", id);
        helpQuestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
