package com.fanflip.profile.web.rest;

import com.fanflip.profile.repository.UserEventRepository;
import com.fanflip.profile.service.UserEventService;
import com.fanflip.profile.service.dto.UserEventDTO;
import com.fanflip.profile.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.fanflip.profile.domain.UserEvent}.
 */
@RestController
@RequestMapping("/api/user-events")
public class UserEventResource {

    private final Logger log = LoggerFactory.getLogger(UserEventResource.class);

    private static final String ENTITY_NAME = "profileUserEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEventService userEventService;

    private final UserEventRepository userEventRepository;

    public UserEventResource(UserEventService userEventService, UserEventRepository userEventRepository) {
        this.userEventService = userEventService;
        this.userEventRepository = userEventRepository;
    }

    /**
     * {@code POST  /user-events} : Create a new userEvent.
     *
     * @param userEventDTO the userEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEventDTO, or with status {@code 400 (Bad Request)} if the userEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserEventDTO> createUserEvent(@Valid @RequestBody UserEventDTO userEventDTO) throws URISyntaxException {
        log.debug("REST request to save UserEvent : {}", userEventDTO);
        if (userEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new userEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserEventDTO result = userEventService.save(userEventDTO);
        return ResponseEntity
            .created(new URI("/api/user-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-events/:id} : Updates an existing userEvent.
     *
     * @param id the id of the userEventDTO to save.
     * @param userEventDTO the userEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventDTO,
     * or with status {@code 400 (Bad Request)} if the userEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserEventDTO> updateUserEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserEventDTO userEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserEvent : {}, {}", id, userEventDTO);
        if (userEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserEventDTO result = userEventService.update(userEventDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-events/:id} : Partial updates given fields of an existing userEvent, field will ignore if it is null
     *
     * @param id the id of the userEventDTO to save.
     * @param userEventDTO the userEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventDTO,
     * or with status {@code 400 (Bad Request)} if the userEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserEventDTO> partialUpdateUserEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserEventDTO userEventDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserEvent partially : {}, {}", id, userEventDTO);
        if (userEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserEventDTO> result = userEventService.partialUpdate(userEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-events} : get all the userEvents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserEventDTO>> getAllUserEvents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserEvents");
        Page<UserEventDTO> page = userEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-events/:id} : get the "id" userEvent.
     *
     * @param id the id of the userEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEventDTO> getUserEvent(@PathVariable("id") Long id) {
        log.debug("REST request to get UserEvent : {}", id);
        Optional<UserEventDTO> userEventDTO = userEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEventDTO);
    }

    /**
     * {@code DELETE  /user-events/:id} : delete the "id" userEvent.
     *
     * @param id the id of the userEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserEvent(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserEvent : {}", id);
        userEventService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
