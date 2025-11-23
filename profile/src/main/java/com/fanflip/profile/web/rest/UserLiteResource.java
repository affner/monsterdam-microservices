package com.fanflip.profile.web.rest;

import com.fanflip.profile.repository.UserLiteRepository;
import com.fanflip.profile.service.UserLiteService;
import com.fanflip.profile.service.dto.UserLiteDTO;
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
 * REST controller for managing {@link com.fanflip.profile.domain.UserLite}.
 */
@RestController
@RequestMapping("/api/user-lites")
public class UserLiteResource {

    private final Logger log = LoggerFactory.getLogger(UserLiteResource.class);

    private static final String ENTITY_NAME = "profileUserLite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserLiteService userLiteService;

    private final UserLiteRepository userLiteRepository;

    public UserLiteResource(UserLiteService userLiteService, UserLiteRepository userLiteRepository) {
        this.userLiteService = userLiteService;
        this.userLiteRepository = userLiteRepository;
    }

    /**
     * {@code POST  /user-lites} : Create a new userLite.
     *
     * @param userLiteDTO the userLiteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userLiteDTO, or with status {@code 400 (Bad Request)} if the userLite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserLiteDTO> createUserLite(@Valid @RequestBody UserLiteDTO userLiteDTO) throws URISyntaxException {
        log.debug("REST request to save UserLite : {}", userLiteDTO);
        if (userLiteDTO.getId() != null) {
            throw new BadRequestAlertException("A new userLite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserLiteDTO result = userLiteService.save(userLiteDTO);
        return ResponseEntity
            .created(new URI("/api/user-lites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-lites/:id} : Updates an existing userLite.
     *
     * @param id the id of the userLiteDTO to save.
     * @param userLiteDTO the userLiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLiteDTO,
     * or with status {@code 400 (Bad Request)} if the userLiteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userLiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserLiteDTO> updateUserLite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserLiteDTO userLiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserLite : {}, {}", id, userLiteDTO);
        if (userLiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userLiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserLiteDTO result = userLiteService.update(userLiteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userLiteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-lites/:id} : Partial updates given fields of an existing userLite, field will ignore if it is null
     *
     * @param id the id of the userLiteDTO to save.
     * @param userLiteDTO the userLiteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userLiteDTO,
     * or with status {@code 400 (Bad Request)} if the userLiteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userLiteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userLiteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserLiteDTO> partialUpdateUserLite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserLiteDTO userLiteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserLite partially : {}, {}", id, userLiteDTO);
        if (userLiteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userLiteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userLiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserLiteDTO> result = userLiteService.partialUpdate(userLiteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userLiteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-lites} : get all the userLites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userLites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserLiteDTO>> getAllUserLites(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserLites");
        Page<UserLiteDTO> page = userLiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-lites/:id} : get the "id" userLite.
     *
     * @param id the id of the userLiteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userLiteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserLiteDTO> getUserLite(@PathVariable("id") Long id) {
        log.debug("REST request to get UserLite : {}", id);
        Optional<UserLiteDTO> userLiteDTO = userLiteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userLiteDTO);
    }

    /**
     * {@code DELETE  /user-lites/:id} : delete the "id" userLite.
     *
     * @param id the id of the userLiteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserLite(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserLite : {}", id);
        userLiteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
