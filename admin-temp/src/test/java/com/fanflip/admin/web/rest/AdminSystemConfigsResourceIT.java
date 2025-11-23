package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.AdminSystemConfigs;
import com.fanflip.admin.domain.enumeration.ConfigurationCategory;
import com.fanflip.admin.domain.enumeration.ConfigurationValueType;
import com.fanflip.admin.repository.AdminSystemConfigsRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.AdminSystemConfigsSearchRepository;
import com.fanflip.admin.service.dto.AdminSystemConfigsDTO;
import com.fanflip.admin.service.mapper.AdminSystemConfigsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AdminSystemConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private static final String ENTITY_SEARCH_API_URL = "/api/admin-system-configs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminSystemConfigsRepository adminSystemConfigsRepository;

    @Autowired
    private AdminSystemConfigsMapper adminSystemConfigsMapper;

    @Autowired
    private AdminSystemConfigsSearchRepository adminSystemConfigsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AdminSystemConfigs.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        adminSystemConfigsSearchRepository.deleteAll().block();
        assertThat(adminSystemConfigsSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        adminSystemConfigs = createEntity(em);
    }

    @Test
    void createAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeCreate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
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
    void createAdminSystemConfigsWithExistingId() throws Exception {
        // Create the AdminSystemConfigs with an existing ID
        adminSystemConfigs.setId(1L);
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        int databaseSizeBeforeCreate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkConfigKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminSystemConfigs.setConfigKey(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkConfigValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminSystemConfigs.setConfigValue(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminSystemConfigs.setCreatedDate(null);

        // Create the AdminSystemConfigs, which fails.
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAdminSystemConfigs() {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();

        // Get all the adminSystemConfigsList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(adminSystemConfigs.getId().intValue()))
            .jsonPath("$.[*].configKey")
            .value(hasItem(DEFAULT_CONFIG_KEY))
            .jsonPath("$.[*].configValue")
            .value(hasItem(DEFAULT_CONFIG_VALUE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].configValueType")
            .value(hasItem(DEFAULT_CONFIG_VALUE_TYPE.toString()))
            .jsonPath("$.[*].configCategory")
            .value(hasItem(DEFAULT_CONFIG_CATEGORY.toString()))
            .jsonPath("$.[*].configFileContentType")
            .value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].configFile")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE)))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getAdminSystemConfigs() {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();

        // Get the adminSystemConfigs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, adminSystemConfigs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(adminSystemConfigs.getId().intValue()))
            .jsonPath("$.configKey")
            .value(is(DEFAULT_CONFIG_KEY))
            .jsonPath("$.configValue")
            .value(is(DEFAULT_CONFIG_VALUE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.configValueType")
            .value(is(DEFAULT_CONFIG_VALUE_TYPE.toString()))
            .jsonPath("$.configCategory")
            .value(is(DEFAULT_CONFIG_CATEGORY.toString()))
            .jsonPath("$.configFileContentType")
            .value(is(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .jsonPath("$.configFile")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE)))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isActive")
            .value(is(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingAdminSystemConfigs() {
        // Get the adminSystemConfigs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAdminSystemConfigs() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        adminSystemConfigsSearchRepository.save(adminSystemConfigs).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());

        // Update the adminSystemConfigs
        AdminSystemConfigs updatedAdminSystemConfigs = adminSystemConfigsRepository.findById(adminSystemConfigs.getId()).block();
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

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
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
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AdminSystemConfigs> adminSystemConfigsSearchList = IterableUtils.toList(
                    adminSystemConfigsSearchRepository.findAll().collectList().block()
                );
                AdminSystemConfigs testAdminSystemConfigsSearch = adminSystemConfigsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAdminSystemConfigsSearch.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
                assertThat(testAdminSystemConfigsSearch.getConfigValue()).isEqualTo(UPDATED_CONFIG_VALUE);
                assertThat(testAdminSystemConfigsSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testAdminSystemConfigsSearch.getConfigValueType()).isEqualTo(UPDATED_CONFIG_VALUE_TYPE);
                assertThat(testAdminSystemConfigsSearch.getConfigCategory()).isEqualTo(UPDATED_CONFIG_CATEGORY);
                assertThat(testAdminSystemConfigsSearch.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
                assertThat(testAdminSystemConfigsSearch.getConfigFileContentType()).isEqualTo(UPDATED_CONFIG_FILE_CONTENT_TYPE);
                assertThat(testAdminSystemConfigsSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testAdminSystemConfigsSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testAdminSystemConfigsSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testAdminSystemConfigsSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testAdminSystemConfigsSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    void putNonExistingAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();

        // Update the adminSystemConfigs using partial update
        AdminSystemConfigs partialUpdatedAdminSystemConfigs = new AdminSystemConfigs();
        partialUpdatedAdminSystemConfigs.setId(adminSystemConfigs.getId());

        partialUpdatedAdminSystemConfigs
            .configKey(UPDATED_CONFIG_KEY)
            .configCategory(UPDATED_CONFIG_CATEGORY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminSystemConfigs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminSystemConfigs testAdminSystemConfigs = adminSystemConfigsList.get(adminSystemConfigsList.size() - 1);
        assertThat(testAdminSystemConfigs.getConfigKey()).isEqualTo(UPDATED_CONFIG_KEY);
        assertThat(testAdminSystemConfigs.getConfigValue()).isEqualTo(DEFAULT_CONFIG_VALUE);
        assertThat(testAdminSystemConfigs.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAdminSystemConfigs.getConfigValueType()).isEqualTo(DEFAULT_CONFIG_VALUE_TYPE);
        assertThat(testAdminSystemConfigs.getConfigCategory()).isEqualTo(UPDATED_CONFIG_CATEGORY);
        assertThat(testAdminSystemConfigs.getConfigFile()).isEqualTo(DEFAULT_CONFIG_FILE);
        assertThat(testAdminSystemConfigs.getConfigFileContentType()).isEqualTo(DEFAULT_CONFIG_FILE_CONTENT_TYPE);
        assertThat(testAdminSystemConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminSystemConfigs.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminSystemConfigs.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminSystemConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminSystemConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void fullUpdateAdminSystemConfigsWithPatch() throws Exception {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();

        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminSystemConfigs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminSystemConfigs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
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
    void patchNonExistingAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, adminSystemConfigsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAdminSystemConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        adminSystemConfigs.setId(longCount.incrementAndGet());

        // Create the AdminSystemConfigs
        AdminSystemConfigsDTO adminSystemConfigsDTO = adminSystemConfigsMapper.toDto(adminSystemConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminSystemConfigsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminSystemConfigs in the database
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAdminSystemConfigs() {
        // Initialize the database
        adminSystemConfigsRepository.save(adminSystemConfigs).block();
        adminSystemConfigsRepository.save(adminSystemConfigs).block();
        adminSystemConfigsSearchRepository.save(adminSystemConfigs).block();

        int databaseSizeBeforeDelete = adminSystemConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the adminSystemConfigs
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, adminSystemConfigs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AdminSystemConfigs> adminSystemConfigsList = adminSystemConfigsRepository.findAll().collectList().block();
        assertThat(adminSystemConfigsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminSystemConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAdminSystemConfigs() {
        // Initialize the database
        adminSystemConfigs = adminSystemConfigsRepository.save(adminSystemConfigs).block();
        adminSystemConfigsSearchRepository.save(adminSystemConfigs).block();

        // Search the adminSystemConfigs
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + adminSystemConfigs.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(adminSystemConfigs.getId().intValue()))
            .jsonPath("$.[*].configKey")
            .value(hasItem(DEFAULT_CONFIG_KEY))
            .jsonPath("$.[*].configValue")
            .value(hasItem(DEFAULT_CONFIG_VALUE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].configValueType")
            .value(hasItem(DEFAULT_CONFIG_VALUE_TYPE.toString()))
            .jsonPath("$.[*].configCategory")
            .value(hasItem(DEFAULT_CONFIG_CATEGORY.toString()))
            .jsonPath("$.[*].configFileContentType")
            .value(hasItem(DEFAULT_CONFIG_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].configFile")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONFIG_FILE)))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isActive")
            .value(hasItem(DEFAULT_IS_ACTIVE.booleanValue()));
    }
}
