package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.HelpSubcategory;
import com.fanflip.catalogs.repository.HelpSubcategoryRepository;
import com.fanflip.catalogs.service.dto.HelpSubcategoryDTO;
import com.fanflip.catalogs.service.mapper.HelpSubcategoryMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HelpSubcategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpSubcategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-subcategories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpSubcategoryRepository helpSubcategoryRepository;

    @Autowired
    private HelpSubcategoryMapper helpSubcategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpSubcategoryMockMvc;

    private HelpSubcategory helpSubcategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createEntity(EntityManager em) {
        HelpSubcategory helpSubcategory = new HelpSubcategory().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return helpSubcategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createUpdatedEntity(EntityManager em) {
        HelpSubcategory helpSubcategory = new HelpSubcategory().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return helpSubcategory;
    }

    @BeforeEach
    public void initTest() {
        helpSubcategory = createEntity(em);
    }

    @Test
    @Transactional
    void createHelpSubcategory() throws Exception {
        int databaseSizeBeforeCreate = helpSubcategoryRepository.findAll().size();
        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);
        restHelpSubcategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeCreate + 1);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createHelpSubcategoryWithExistingId() throws Exception {
        // Create the HelpSubcategory with an existing ID
        helpSubcategory.setId(1L);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        int databaseSizeBeforeCreate = helpSubcategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpSubcategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpSubcategoryRepository.findAll().size();
        // set the field null
        helpSubcategory.setName(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpSubcategoryRepository.findAll().size();
        // set the field null
        helpSubcategory.setIsDeleted(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpSubcategories() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        // Get all the helpSubcategoryList
        restHelpSubcategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpSubcategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHelpSubcategory() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        // Get the helpSubcategory
        restHelpSubcategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, helpSubcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpSubcategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHelpSubcategory() throws Exception {
        // Get the helpSubcategory
        restHelpSubcategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpSubcategory() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();

        // Update the helpSubcategory
        HelpSubcategory updatedHelpSubcategory = helpSubcategoryRepository.findById(helpSubcategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpSubcategory are not directly saved in db
        em.detach(updatedHelpSubcategory);
        updatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(updatedHelpSubcategory);

        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpSubcategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpSubcategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().size();
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpSubcategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpSubcategory() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.saveAndFlush(helpSubcategory);

        int databaseSizeBeforeDelete = helpSubcategoryRepository.findAll().size();

        // Delete the helpSubcategory
        restHelpSubcategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpSubcategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
