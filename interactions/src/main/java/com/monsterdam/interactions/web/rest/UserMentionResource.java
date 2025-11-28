package com.monsterdam.interactions.web.rest;

import com.monsterdam.interactions.repository.UserMentionRepository;
import com.monsterdam.interactions.service.UserMentionService;
import com.monsterdam.interactions.service.dto.UserMentionDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.monsterdam.interactions.domain.UserMention}.
 */
@RestController
@RequestMapping("/api/user-mentions")
public class UserMentionResource {

    private final Logger log = LoggerFactory.getLogger(UserMentionResource.class);

    private static final String ENTITY_NAME = "interactionsUserMention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMentionService userMentionService;

    private final UserMentionRepository userMentionRepository;

    public UserMentionResource(UserMentionService userMentionService, UserMentionRepository userMentionRepository) {
        this.userMentionService = userMentionService;
        this.userMentionRepository = userMentionRepository;
    }

    /**
     * {@code POST  /user-mentions} : Create a new userMention.
     *
     * @param userMentionDTO the userMentionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMentionDTO, or with status {@code 400 (Bad Request)} if the userMention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserMentionDTO> createUserMention(@Valid @RequestBody UserMentionDTO userMentionDTO) throws URISyntaxException {
        log.debug("REST request to save UserMention : {}", userMentionDTO);
        if (userMentionDTO.getId() != null) {
            throw new BadRequestAlertException("A new userMention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMentionDTO result = userMentionService.save(userMentionDTO);
        return ResponseEntity
            .created(new URI("/api/user-mentions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-mentions/:id} : Updates an existing userMention.
     *
     * @param id the id of the userMentionDTO to save.
     * @param userMentionDTO the userMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMentionDTO,
     * or with status {@code 400 (Bad Request)} if the userMentionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserMentionDTO> updateUserMention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserMentionDTO userMentionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserMention : {}, {}", id, userMentionDTO);
        if (userMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserMentionDTO result = userMentionService.update(userMentionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMentionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-mentions/:id} : Partial updates given fields of an existing userMention, field will ignore if it is null
     *
     * @param id the id of the userMentionDTO to save.
     * @param userMentionDTO the userMentionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMentionDTO,
     * or with status {@code 400 (Bad Request)} if the userMentionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userMentionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMentionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserMentionDTO> partialUpdateUserMention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserMentionDTO userMentionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserMention partially : {}, {}", id, userMentionDTO);
        if (userMentionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMentionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMentionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserMentionDTO> result = userMentionService.partialUpdate(userMentionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMentionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-mentions} : get all the userMentions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMentions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserMentionDTO>> getAllUserMentions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserMentions");
        Page<UserMentionDTO> page;
        if (eagerload) {
            page = userMentionService.findAllWithEagerRelationships(pageable);
        } else {
            page = userMentionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-mentions/:id} : get the "id" userMention.
     *
     * @param id the id of the userMentionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMentionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserMentionDTO> getUserMention(@PathVariable("id") Long id) {
        log.debug("REST request to get UserMention : {}", id);
        Optional<UserMentionDTO> userMentionDTO = userMentionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMentionDTO);
    }

    /**
     * {@code DELETE  /user-mentions/:id} : delete the "id" userMention.
     *
     * @param id the id of the userMentionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserMention(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserMention : {}", id);
        userMentionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
