package com.monsterdam.catalogs.web.rest;

import com.monsterdam.catalogs.repository.EmojiTypeRepository;
import com.monsterdam.catalogs.service.EmojiTypeService;
import com.monsterdam.catalogs.service.dto.EmojiTypeDTO;
import com.monsterdam.catalogs.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.catalogs.domain.EmojiType}.
 */
@RestController
@RequestMapping("/api/emoji-types")
public class EmojiTypeResource {

    private final Logger log = LoggerFactory.getLogger(EmojiTypeResource.class);

    private static final String ENTITY_NAME = "catalogsEmojiType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmojiTypeService emojiTypeService;

    private final EmojiTypeRepository emojiTypeRepository;

    public EmojiTypeResource(EmojiTypeService emojiTypeService, EmojiTypeRepository emojiTypeRepository) {
        this.emojiTypeService = emojiTypeService;
        this.emojiTypeRepository = emojiTypeRepository;
    }

    /**
     * {@code POST  /emoji-types} : Create a new emojiType.
     *
     * @param emojiTypeDTO the emojiTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emojiTypeDTO, or with status {@code 400 (Bad Request)} if the emojiType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmojiTypeDTO> createEmojiType(@Valid @RequestBody EmojiTypeDTO emojiTypeDTO) throws URISyntaxException {
        log.debug("REST request to save EmojiType : {}", emojiTypeDTO);
        if (emojiTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new emojiType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmojiTypeDTO result = emojiTypeService.save(emojiTypeDTO);
        return ResponseEntity
            .created(new URI("/api/emoji-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emoji-types/:id} : Updates an existing emojiType.
     *
     * @param id the id of the emojiTypeDTO to save.
     * @param emojiTypeDTO the emojiTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emojiTypeDTO,
     * or with status {@code 400 (Bad Request)} if the emojiTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emojiTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmojiTypeDTO> updateEmojiType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmojiTypeDTO emojiTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmojiType : {}, {}", id, emojiTypeDTO);
        if (emojiTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emojiTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emojiTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmojiTypeDTO result = emojiTypeService.update(emojiTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emojiTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emoji-types/:id} : Partial updates given fields of an existing emojiType, field will ignore if it is null
     *
     * @param id the id of the emojiTypeDTO to save.
     * @param emojiTypeDTO the emojiTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emojiTypeDTO,
     * or with status {@code 400 (Bad Request)} if the emojiTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emojiTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emojiTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmojiTypeDTO> partialUpdateEmojiType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmojiTypeDTO emojiTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmojiType partially : {}, {}", id, emojiTypeDTO);
        if (emojiTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emojiTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emojiTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmojiTypeDTO> result = emojiTypeService.partialUpdate(emojiTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emojiTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /emoji-types} : get all the emojiTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emojiTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmojiTypeDTO>> getAllEmojiTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of EmojiTypes");
        Page<EmojiTypeDTO> page = emojiTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /emoji-types/:id} : get the "id" emojiType.
     *
     * @param id the id of the emojiTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emojiTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmojiTypeDTO> getEmojiType(@PathVariable("id") Long id) {
        log.debug("REST request to get EmojiType : {}", id);
        Optional<EmojiTypeDTO> emojiTypeDTO = emojiTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emojiTypeDTO);
    }

    /**
     * {@code DELETE  /emoji-types/:id} : delete the "id" emojiType.
     *
     * @param id the id of the emojiTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmojiType(@PathVariable("id") Long id) {
        log.debug("REST request to delete EmojiType : {}", id);
        emojiTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
