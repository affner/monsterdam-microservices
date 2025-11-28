package com.monsterdam.catalogs.web.rest;

import com.monsterdam.catalogs.repository.SocialNetworkRepository;
import com.monsterdam.catalogs.service.SocialNetworkService;
import com.monsterdam.catalogs.service.dto.SocialNetworkDTO;
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
 * REST controller for managing {@link com.monsterdam.catalogs.domain.SocialNetwork}.
 */
@RestController
@RequestMapping("/api/social-networks")
public class SocialNetworkResource {

    private final Logger log = LoggerFactory.getLogger(SocialNetworkResource.class);

    private static final String ENTITY_NAME = "catalogsSocialNetwork";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialNetworkService socialNetworkService;

    private final SocialNetworkRepository socialNetworkRepository;

    public SocialNetworkResource(SocialNetworkService socialNetworkService, SocialNetworkRepository socialNetworkRepository) {
        this.socialNetworkService = socialNetworkService;
        this.socialNetworkRepository = socialNetworkRepository;
    }

    /**
     * {@code POST  /social-networks} : Create a new socialNetwork.
     *
     * @param socialNetworkDTO the socialNetworkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialNetworkDTO, or with status {@code 400 (Bad Request)} if the socialNetwork has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SocialNetworkDTO> createSocialNetwork(@Valid @RequestBody SocialNetworkDTO socialNetworkDTO)
        throws URISyntaxException {
        log.debug("REST request to save SocialNetwork : {}", socialNetworkDTO);
        if (socialNetworkDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialNetwork cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialNetworkDTO result = socialNetworkService.save(socialNetworkDTO);
        return ResponseEntity
            .created(new URI("/api/social-networks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-networks/:id} : Updates an existing socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to save.
     * @param socialNetworkDTO the socialNetworkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetworkDTO,
     * or with status {@code 400 (Bad Request)} if the socialNetworkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialNetworkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialNetworkDTO> updateSocialNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialNetworkDTO socialNetworkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SocialNetwork : {}, {}", id, socialNetworkDTO);
        if (socialNetworkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetworkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialNetworkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialNetworkDTO result = socialNetworkService.update(socialNetworkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialNetworkDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-networks/:id} : Partial updates given fields of an existing socialNetwork, field will ignore if it is null
     *
     * @param id the id of the socialNetworkDTO to save.
     * @param socialNetworkDTO the socialNetworkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialNetworkDTO,
     * or with status {@code 400 (Bad Request)} if the socialNetworkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialNetworkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialNetworkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialNetworkDTO> partialUpdateSocialNetwork(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialNetworkDTO socialNetworkDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialNetwork partially : {}, {}", id, socialNetworkDTO);
        if (socialNetworkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialNetworkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialNetworkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialNetworkDTO> result = socialNetworkService.partialUpdate(socialNetworkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialNetworkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /social-networks} : get all the socialNetworks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialNetworks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SocialNetworkDTO>> getAllSocialNetworks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SocialNetworks");
        Page<SocialNetworkDTO> page = socialNetworkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /social-networks/:id} : get the "id" socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialNetworkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialNetworkDTO> getSocialNetwork(@PathVariable("id") Long id) {
        log.debug("REST request to get SocialNetwork : {}", id);
        Optional<SocialNetworkDTO> socialNetworkDTO = socialNetworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialNetworkDTO);
    }

    /**
     * {@code DELETE  /social-networks/:id} : delete the "id" socialNetwork.
     *
     * @param id the id of the socialNetworkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialNetwork(@PathVariable("id") Long id) {
        log.debug("REST request to delete SocialNetwork : {}", id);
        socialNetworkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
