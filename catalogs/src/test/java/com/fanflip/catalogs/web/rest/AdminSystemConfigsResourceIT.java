package com.fanflip.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.catalogs.IntegrationTest;
import com.fanflip.catalogs.domain.AdminSystemConfigs;
import com.fanflip.catalogs.domain.enumeration.ConfigurationCategory;
import com.fanflip.catalogs.domain.enumeration.ConfigurationValueType;
import com.fanflip.catalogs.repository.AdminSystemConfigsRepository;
import com.fanflip.catalogs.service.dto.AdminSystemConfigsDTO;
import com.fanflip.catalogs.service.mapper.AdminSystemConfigsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link AdminSystemConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminSystemConfigsResourceIT {

    private static final String DEFAULT_CONFIG_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ConfigurationValueType DEFAULT_CONFIG_VALUE_TYPE = ConfigurationValueType.STRING;
    private static final ConfigurationValueType UPDATED_CONFIG_VALUE_TYPE = ConfigurationValueType.INTEGER;

    private static final ConfigurationCategory DEFAULT_CONFIG_CATEGORY = ConfigurationCategory.GENERAL;
    private static final ConfigurationCategory UPDATED_CONFIG_CATEGORY = ConfigurationCategory.SECURITY;

    private static final byte[] DEFAULT_CONFIG_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONFIG_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONFIG_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONFIG_FILE_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/admin-system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminSystemConfigsRepository adminSystemConfigsRepository;

    @Autowired
    private AdminSystemConfigsMapper adminSystemConfigsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminSystemConfigsMockMvc;

    private AdminSystemConfigs adminSystemConfigs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminSystemConfigs createEntity(EntityManager em) {
        AdminSystemConfigs adminSystemConfigs = new AdminSystemConfigs()
            .configKey(DEFAULT_CONFIG_KEY)
            .configValue(DEFAULT_CONFIG_VALUE)
            .description(DEFAULT_DESCRIPTION)
            .configValueType(DEFAULT_CONFIG_VALUE_TYPE)
            .configCategory(DEFAULT_CONFIG_CATEGORY)
            .configFile(DEFAULT_CONFIG_FILE)
            .configFileContentType(DEFAULT_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isActive(DEFAULT_IS_ACTIVE);
        return adminSystemConfigs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminSystemConfigs createUpdatedEntity(EntityManager em) {
        AdminSystemConfigs adminSystemConfigs = new AdminSystemConfigs()
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        return adminSystemConfigs;
    }

    @BeforeEach
    public void initTest() {
        adminSystemConfigs = createEntity(em);
    }

    @Test
    @Transactional
    void createAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeCreate = adminSystemConfigsRepository.findAll().size();
        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);
        restAdminSystemConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeCreate + 1);
        AdminSystemConfigs testAdminSystemConfigs = adminSystemConfigsList.get(adminSystemConfigsList.size() - 1);
        assertThat(testAdminSystemConfigs.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testAdminSystemConfigs.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testAdminSystemConfigs.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAdminSystemConfigs.getConfigValueType()).isEqualTo(DEFAULT_CONFIG_VALUE_TYPE);
        assertThat(testAdminSystemConfigs.getConfigCategory()).isEqualTo(DEFAULT_CONFIG_CATEGORY);
        assertThat(testAdminSystemConfigs.getConfigFile()).isEqualTo(DEFAULT_CONFIG_FILE);
        assertThat(testAdminSystemConfigs.getConfigFileContentType()).isEqualTo(DEFAULT_CONFIG_FILE_CONTENT_TYPE);
        assertThat(testAdminSystemConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminSystemConfigs.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminSystemConfigs.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminSystemConfigs.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminSystemConfigs.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void createAdminSystemConfigsWithExistingId() throws Exception {
        // Create the AdminSystemConfigs with an existing ID
        adminSystemConfigs.setId(1L);
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        int databaseSizeBeforeCreate = adminSystemConfigsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminSystemConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConfigKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().size();
        // set the field null
        adminSystemConfigs.setConfigKey(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfigValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().size();
        // set the field null
        adminSystemConfigs.setConfigValue(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().size();
        // set the field null
        adminSystemConfigs.setCreatedDate(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdminSystemConfigs() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        // Get all the adminSystemConfigsList
        restAdminSystemConfigsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminSystemConfigs.getId().intValue())))
            .andExpect(jsonPath("$.[*].configKey").value(hasItem(DEFAULT_CONFIG_KEY)))
            .andExpect(jsonPath("$.[*].configValue").value(hasItem(DEFAULT_CONFIG_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].configValueType").value(hasItem(DEFAULT_CONFIG_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].configCategory").value(hasItem(DEFAULT_CONFIG_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].configFileContentType").value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getAdminSystemConfigs() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        // Get the adminSystemConfigs
        restAdminSystemConfigsMockMvc
            .perform(get(ENTITY_API_URL_ID, adminSystemConfigs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminSystemConfigs.getId().intValue()))
            .andExpect(jsonPath("$.configKey").value(DEFAULT_CONFIG_KEY))
            .andExpect(jsonPath("$.configValue").value(DEFAULT_CONFIG_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.configValueType").value(DEFAULT_CONFIG_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.configCategory").value(DEFAULT_CONFIG_CATEGORY.toString()))
            .andExpect(jsonPath("$.configFileContentType").value(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.configFile").value(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAdminSystemConfigs() throws Exception {
        // Get the adminSystemConfigs
        restAdminSystemConfigsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdminSystemConfigs() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();

        // Update the adminSystemConfigs
        AdminSystemConfigs updatedAdminSystemConfigs = adminSystemConfigsRepository.findById(adminSystemConfigs.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdminSystemConfigs are not directly saved in db
        em.detach(updatedAdminSystemConfigs);
        updatedAdminSystemConfigs
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(updatedAdminSystemConfigs);

        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminSystemConfigs testAdminSystemConfigs = adminSystemConfigsList.get(adminSystemConfigsList.size() - 1);
        assertThat(testAdminSystemConfigs.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testAdminSystemConfigs.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testAdminSystemConfigs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAdminSystemConfigs.getConfigValueType()).isEqualTo(UPDATED_CONFIG_VALUE_TYPE);
        assertThat(testAdminSystemConfigs.getConfigCategory()).isEqualTo(UPDATED_CONFIG_CATEGORY);
        assertThat(testAdminSystemConfigs.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testAdminSystemConfigs.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
        assertThat(testAdminSystemConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminSystemConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminSystemConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminSystemConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminSystemConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();

        // Update the adminSystemConfigs using partial update
        AdminSystemConfigs partialUpdatedAdminSystemConfigs = new AdminSystemConfigs();
        partialUpdatedAdminSystemConfigs.setId(adminSystemConfigs.getId());

        partialUpdatedAdminSystemConfigs
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminSystemConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminSystemConfigs testAdminSystemConfigs = adminSystemConfigsList.get(adminSystemConfigsList.size() - 1);
        assertThat(testAdminSystemConfigs.getConfigKey()).isEqualTo(DEFAULT_CONFIG_KEY);
        assertThat(testAdminSystemConfigs.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testAdminSystemConfigs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAdminSystemConfigs.getConfigValueType()).isEqualTo(UPDATED_CONFIG_VALUE_TYPE);
        assertThat(testAdminSystemConfigs.getConfigCategory()).isEqualTo(DEFAULT_CONFIG_CATEGORY);
        assertThat(testAdminSystemConfigs.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testAdminSystemConfigs.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
        assertThat(testAdminSystemConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminSystemConfigs.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminSystemConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminSystemConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminSystemConfigs.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();

        // Update the adminSystemConfigs using partial update
        AdminSystemConfigs partialUpdatedAdminSystemConfigs = new AdminSystemConfigs();
        partialUpdatedAdminSystemConfigs.setId(adminSystemConfigs.getId());

        partialUpdatedAdminSystemConfigs
            .configKey(UPDATED_CONFIG_KEY)
            .configValue(UPDATED_CONFIG_VALUE)
            .description(UPDATED_DESCRIPTION)
            .configValueType(UPDATED_CONFIG_VALUE_TYPE)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .configFile(UPDATED_CONFIG_FILE)
            .configFileContentType(UPDATED_CONFIG_FILE_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminSystemConfigs))
            )
            .andExpect(status().isOk());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminSystemConfigs testAdminSystemConfigs = adminSystemConfigsList.get(adminSystemConfigsList.size() - 1);
        assertThat(testAdminSystemConfigs.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testAdminSystemConfigs.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
        assertThat(testAdminSystemConfigs.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAdminSystemConfigs.getConfigValueType()).isEqualTo(UPDATED_CONFIG_VALUE_TYPE);
        assertThat(testAdminSystemConfigs.getConfigCategory()).isEqualTo(UPDATED_CONFIG_CATEGORY);
        assertThat(testAdminSystemConfigs.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testAdminSystemConfigs.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
        assertThat(testAdminSystemConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminSystemConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminSystemConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminSystemConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminSystemConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().size();
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminSystemConfigsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminSystemConfigs() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.saveAndFlush(adminSystemConfigs);

        int databaseSizeBeforeDelete = adminSystemConfigsRepository.findAll().size();

        // Delete the adminSystemConfigs
        restAdminSystemConfigsMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminSystemConfigs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
