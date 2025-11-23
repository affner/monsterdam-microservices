package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.HelpCategory;
import com.fanflip.catalogs.repository.HelpCategoryRepository;
import com.fanflip.catalogs.service.dto.HelpCategoryDTO;
import com.fanflip.catalogs.service.mapper.HelpCategoryMapper;
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
 * Integration tests for the {@link HelpCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HelpCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpCategoryRepository helpCategoryRepository;

    @Autowired
    private HelpCategoryMapper helpCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHelpCategoryMockMvc;

    private HelpCategory helpCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpCategory createEntity(EntityManager em) {
        HelpCategory helpCategory = new HelpCategory().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return helpCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpCategory createUpdatedEntity(EntityManager em) {
        HelpCategory helpCategory = new HelpCategory().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return helpCategory;
    }

    @BeforeEach
    public void initTest() {
        helpCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createHelpCategory() throws Exception {
        int databaseSizeBeforeCreate = helpCategoryRepository.findAll().size();
        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);
        restHelpCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createHelpCategoryWithExistingId() throws Exception {
        // Create the HelpCategory with an existing ID
        helpCategory.setId(1L);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        int databaseSizeBeforeCreate = helpCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHelpCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpCategoryRepository.findAll().size();
        // set the field null
        helpCategory.setName(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        restHelpCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpCategoryRepository.findAll().size();
        // set the field null
        helpCategory.setIsDeleted(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        restHelpCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHelpCategories() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        // Get all the helpCategoryList
        restHelpCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(helpCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHelpCategory() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        // Get the helpCategory
        restHelpCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, helpCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(helpCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingHelpCategory() throws Exception {
        // Get the helpCategory
        restHelpCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHelpCategory() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();

        // Update the helpCategory
        HelpCategory updatedHelpCategory = helpCategoryRepository.findById(helpCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHelpCategory are not directly saved in db
        em.detach(updatedHelpCategory);
        updatedHelpCategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(updatedHelpCategory);

        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory.name(UPDATED_NAME);

        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpCategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpCategory))
            )
            .andExpect(status().isOk());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, helpCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().size();
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHelpCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHelpCategory() throws Exception {
        // Initialize the database
        helpCategoryRepository.saveAndFlush(helpCategory);

        int databaseSizeBeforeDelete = helpCategoryRepository.findAll().size();

        // Delete the helpCategory
        restHelpCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, helpCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
