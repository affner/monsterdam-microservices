package com.fanflip.multimedia.web.rest;

import com.fanflip.multimedia.repository.SingleDocumentRepository;
import com.fanflip.multimedia.service.SingleDocumentService;
import com.fanflip.multimedia.service.dto.SingleDocumentDTO;
import com.fanflip.multimedia.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.multimedia.domain.SingleDocument}.
 */
@RestController
@RequestMapping("/api/single-documents")
public class SingleDocumentResource {

    private final Logger log = LoggerFactory.getLogger(SingleDocumentResource.class);

    private static final String ENTITY_NAME = "multimediaSingleDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleDocumentService singleDocumentService;

    private final SingleDocumentRepository singleDocumentRepository;

    public SingleDocumentResource(SingleDocumentService singleDocumentService, SingleDocumentRepository singleDocumentRepository) {
        this.singleDocumentService = singleDocumentService;
        this.singleDocumentRepository = singleDocumentRepository;
    }

    /**
     * {@code POST  /single-documents} : Create a new singleDocument.
     *
     * @param singleDocumentDTO the singleDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleDocumentDTO, or with status {@code 400 (Bad Request)} if the singleDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SingleDocumentDTO> createSingleDocument(@Valid @RequestBody SingleDocumentDTO singleDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleDocument : {}", singleDocumentDTO);
        if (singleDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SingleDocumentDTO result = singleDocumentService.save(singleDocumentDTO);
        return ResponseEntity
            .created(new URI("/api/single-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /single-documents/:id} : Updates an existing singleDocument.
     *
     * @param id the id of the singleDocumentDTO to save.
     * @param singleDocumentDTO the singleDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the singleDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SingleDocumentDTO> updateSingleDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleDocumentDTO singleDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleDocument : {}, {}", id, singleDocumentDTO);
        if (singleDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SingleDocumentDTO result = singleDocumentService.update(singleDocumentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /single-documents/:id} : Partial updates given fields of an existing singleDocument, field will ignore if it is null
     *
     * @param id the id of the singleDocumentDTO to save.
     * @param singleDocumentDTO the singleDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the singleDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SingleDocumentDTO> partialUpdateSingleDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleDocumentDTO singleDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleDocument partially : {}, {}", id, singleDocumentDTO);
        if (singleDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SingleDocumentDTO> result = singleDocumentService.partialUpdate(singleDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /single-documents} : get all the singleDocuments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleDocuments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SingleDocumentDTO>> getAllSingleDocuments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SingleDocuments");
        Page<SingleDocumentDTO> page = singleDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /single-documents/:id} : get the "id" singleDocument.
     *
     * @param id the id of the singleDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SingleDocumentDTO> getSingleDocument(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleDocument : {}", id);
        Optional<SingleDocumentDTO> singleDocumentDTO = singleDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleDocumentDTO);
    }

    /**
     * {@code DELETE  /single-documents/:id} : delete the "id" singleDocument.
     *
     * @param id the id of the singleDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSingleDocument(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleDocument : {}", id);
        singleDocumentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
