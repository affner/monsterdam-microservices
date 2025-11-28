package com.monsterdam.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.catalogs.IntegrationTest;
import com.monsterdam.catalogs.domain.SpecialTitle;
import com.monsterdam.catalogs.repository.SpecialTitleRepository;
import com.monsterdam.catalogs.service.dto.SpecialTitleDTO;
import com.monsterdam.catalogs.service.mapper.SpecialTitleMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpecialTitleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialTitleResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/special-titles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialTitleRepository specialTitleRepository;

    @Autowired
    private SpecialTitleMapper specialTitleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialTitleMockMvc;

    private SpecialTitle specialTitle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialTitle createEntity(EntityManager em) {
        SpecialTitle specialTitle = new SpecialTitle()
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return specialTitle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialTitle createUpdatedEntity(EntityManager em) {
        SpecialTitle specialTitle = new SpecialTitle()
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return specialTitle;
    }

    @BeforeEach
    public void initTest() {
        specialTitle = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialTitle() throws Exception {
        int databaseSizeBeforeCreate = specialTitleRepository.findAll().size();
        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);
        restSpecialTitleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createSpecialTitleWithExistingId() throws Exception {
        // Create the SpecialTitle with an existing ID
        specialTitle.setId(1L);
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        int databaseSizeBeforeCreate = specialTitleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialTitleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialTitleRepository.findAll().size();
        // set the field null
        specialTitle.setCreatedDate(null);

        // Create the SpecialTitle, which fails.
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        restSpecialTitleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialTitleRepository.findAll().size();
        // set the field null
        specialTitle.setIsDeleted(null);

        // Create the SpecialTitle, which fails.
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        restSpecialTitleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialTitles() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        // Get all the specialTitleList
        restSpecialTitleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialTitle.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getSpecialTitle() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        // Get the specialTitle
        restSpecialTitleMockMvc
            .perform(get(ENTITY_API_URL_ID, specialTitle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialTitle.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialTitle() throws Exception {
        // Get the specialTitle
        restSpecialTitleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialTitle() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();

        // Update the specialTitle
        SpecialTitle updatedSpecialTitle = specialTitleRepository.findById(specialTitle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialTitle are not directly saved in db
        em.detach(updatedSpecialTitle);
        updatedSpecialTitle
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(updatedSpecialTitle);

        restSpecialTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialTitleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialTitleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialTitleWithPatch() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();

        // Update the specialTitle using partial update
        SpecialTitle partialUpdatedSpecialTitle = new SpecialTitle();
        partialUpdatedSpecialTitle.setId(specialTitle.getId());

        partialUpdatedSpecialTitle.lastModifiedDate(UPDATED_LAST_MODIFIED_DATE).isDeleted(UPDATED_IS_DELETED);

        restSpecialTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialTitle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialTitle))
            )
            .andExpect(status().isOk());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateSpecialTitleWithPatch() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();

        // Update the specialTitle using partial update
        SpecialTitle partialUpdatedSpecialTitle = new SpecialTitle();
        partialUpdatedSpecialTitle.setId(specialTitle.getId());

        partialUpdatedSpecialTitle
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restSpecialTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialTitle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialTitle))
            )
            .andExpect(status().isOk());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialTitleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().size();
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialTitleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialTitle() throws Exception {
        // Initialize the database
        specialTitleRepository.saveAndFlush(specialTitle);

        int databaseSizeBeforeDelete = specialTitleRepository.findAll().size();

        // Delete the specialTitle
        restSpecialTitleMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialTitle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
