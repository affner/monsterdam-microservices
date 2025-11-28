package com.monsterdam.multimedia.web.rest;

import com.monsterdam.multimedia.repository.SingleLiveStreamRepository;
import com.monsterdam.multimedia.service.SingleLiveStreamService;
import com.monsterdam.multimedia.service.dto.SingleLiveStreamDTO;
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
 * REST controller for managing {@link com.monsterdam.multimedia.domain.SingleLiveStream}.
 */
@RestController
@RequestMapping("/api/single-live-streams")
public class SingleLiveStreamResource {

    private final Logger log = LoggerFactory.getLogger(SingleLiveStreamResource.class);

    private static final String ENTITY_NAME = "multimediaSingleLiveStream";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SingleLiveStreamService singleLiveStreamService;

    private final SingleLiveStreamRepository singleLiveStreamRepository;

    public SingleLiveStreamResource(
        SingleLiveStreamService singleLiveStreamService,
        SingleLiveStreamRepository singleLiveStreamRepository
    ) {
        this.singleLiveStreamService = singleLiveStreamService;
        this.singleLiveStreamRepository = singleLiveStreamRepository;
    }

    /**
     * {@code POST  /single-live-streams} : Create a new singleLiveStream.
     *
     * @param singleLiveStreamDTO the singleLiveStreamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new singleLiveStreamDTO, or with status {@code 400 (Bad Request)} if the singleLiveStream has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SingleLiveStreamDTO> createSingleLiveStream(@Valid @RequestBody SingleLiveStreamDTO singleLiveStreamDTO)
        throws URISyntaxException {
        log.debug("REST request to save SingleLiveStream : {}", singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() != null) {
            throw new BadRequestAlertException("A new singleLiveStream cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SingleLiveStreamDTO result = singleLiveStreamService.save(singleLiveStreamDTO);
        return ResponseEntity
            .created(new URI("/api/single-live-streams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /single-live-streams/:id} : Updates an existing singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to save.
     * @param singleLiveStreamDTO the singleLiveStreamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleLiveStreamDTO,
     * or with status {@code 400 (Bad Request)} if the singleLiveStreamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the singleLiveStreamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SingleLiveStreamDTO> updateSingleLiveStream(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SingleLiveStreamDTO singleLiveStreamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SingleLiveStream : {}, {}", id, singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleLiveStreamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleLiveStreamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SingleLiveStreamDTO result = singleLiveStreamService.update(singleLiveStreamDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleLiveStreamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /single-live-streams/:id} : Partial updates given fields of an existing singleLiveStream, field will ignore if it is null
     *
     * @param id the id of the singleLiveStreamDTO to save.
     * @param singleLiveStreamDTO the singleLiveStreamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated singleLiveStreamDTO,
     * or with status {@code 400 (Bad Request)} if the singleLiveStreamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the singleLiveStreamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the singleLiveStreamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SingleLiveStreamDTO> partialUpdateSingleLiveStream(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SingleLiveStreamDTO singleLiveStreamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SingleLiveStream partially : {}, {}", id, singleLiveStreamDTO);
        if (singleLiveStreamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, singleLiveStreamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!singleLiveStreamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SingleLiveStreamDTO> result = singleLiveStreamService.partialUpdate(singleLiveStreamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, singleLiveStreamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /single-live-streams} : get all the singleLiveStreams.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of singleLiveStreams in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SingleLiveStreamDTO>> getAllSingleLiveStreams(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of SingleLiveStreams");
        Page<SingleLiveStreamDTO> page = singleLiveStreamService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /single-live-streams/:id} : get the "id" singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the singleLiveStreamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SingleLiveStreamDTO> getSingleLiveStream(@PathVariable("id") Long id) {
        log.debug("REST request to get SingleLiveStream : {}", id);
        Optional<SingleLiveStreamDTO> singleLiveStreamDTO = singleLiveStreamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(singleLiveStreamDTO);
    }

    /**
     * {@code DELETE  /single-live-streams/:id} : delete the "id" singleLiveStream.
     *
     * @param id the id of the singleLiveStreamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSingleLiveStream(@PathVariable("id") Long id) {
        log.debug("REST request to delete SingleLiveStream : {}", id);
        singleLiveStreamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
