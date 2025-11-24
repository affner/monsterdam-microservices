package com.monsterdam.profile.web.rest;

import com.monsterdam.profile.repository.UserAssociationRepository;
import com.monsterdam.profile.service.UserAssociationService;
import com.monsterdam.profile.service.dto.UserAssociationDTO;
import com.monsterdam.profile.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.monsterdam.profile.domain.UserAssociation}.
 */
@RestController
@RequestMapping("/api/user-associations")
public class UserAssociationResource {

    private final Logger log = LoggerFactory.getLogger(UserAssociationResource.class);

    private static final String ENTITY_NAME = "profileUserAssociation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAssociationService userAssociationService;

    private final UserAssociationRepository userAssociationRepository;

    public UserAssociationResource(UserAssociationService userAssociationService, UserAssociationRepository userAssociationRepository) {
        this.userAssociationService = userAssociationService;
        this.userAssociationRepository = userAssociationRepository;
    }

    /**
     * {@code POST  /user-associations} : Create a new userAssociation.
     *
     * @param userAssociationDTO the userAssociationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAssociationDTO, or with status {@code 400 (Bad Request)} if the userAssociation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAssociationDTO> createUserAssociation(@Valid @RequestBody UserAssociationDTO userAssociationDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserAssociation : {}", userAssociationDTO);
        if (userAssociationDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAssociation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserAssociationDTO result = userAssociationService.save(userAssociationDTO);
        return ResponseEntity
            .created(new URI("/api/user-associations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-associations/:id} : Updates an existing userAssociation.
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAssociationDTO> updateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAssociation : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserAssociationDTO result = userAssociationService.update(userAssociationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAssociationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-associations/:id} : Partial updates given fields of an existing userAssociation, field will ignore if it is null
     *
     * @param id the id of the userAssociationDTO to save.
     * @param userAssociationDTO the userAssociationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAssociationDTO,
     * or with status {@code 400 (Bad Request)} if the userAssociationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAssociationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAssociationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAssociationDTO> partialUpdateUserAssociation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAssociationDTO userAssociationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAssociation partially : {}, {}", id, userAssociationDTO);
        if (userAssociationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAssociationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAssociationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAssociationDTO> result = userAssociationService.partialUpdate(userAssociationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAssociationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-associations} : get all the userAssociations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAssociations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserAssociationDTO>> getAllUserAssociations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UserAssociations");
        Page<UserAssociationDTO> page = userAssociationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-associations/:id} : get the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAssociationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAssociationDTO> getUserAssociation(@PathVariable("id") Long id) {
        log.debug("REST request to get UserAssociation : {}", id);
        Optional<UserAssociationDTO> userAssociationDTO = userAssociationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAssociationDTO);
    }

    /**
     * {@code DELETE  /user-associations/:id} : delete the "id" userAssociation.
     *
     * @param id the id of the userAssociationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAssociation(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserAssociation : {}", id);
        userAssociationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
