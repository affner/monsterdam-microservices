package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.AdminEmailConfigs;
import com.monsterdam.admin.domain.enumeration.EmailTemplateType;
import com.monsterdam.admin.repository.AdminEmailConfigsRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.AdminEmailConfigsSearchRepository;
import com.monsterdam.admin.service.dto.AdminEmailConfigsDTO;
import com.monsterdam.admin.service.mapper.AdminEmailConfigsMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AdminEmailConfigsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AdminEmailConfigsResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final EmailTemplateType DEFAULT_MAIL_TEMPLATE_TYPE = EmailTemplateType.NOTIFICATION;
    private static final EmailTemplateType UPDATED_MAIL_TEMPLATE_TYPE = EmailTemplateType.NEWSLETTER;

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

    private static final String ENTITY_API_URL = "/api/admin-email-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/admin-email-configs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminEmailConfigsRepository adminEmailConfigsRepository;

    @Autowired
    private AdminEmailConfigsMapper adminEmailConfigsMapper;

    @Autowired
    private AdminEmailConfigsSearchRepository adminEmailConfigsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AdminEmailConfigs adminEmailConfigs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createEntity(EntityManager em) {
        AdminEmailConfigs adminEmailConfigs = new AdminEmailConfigs()
            .title(DEFAULT_TITLE)
            .subject(DEFAULT_SUBJECT)
            .content(DEFAULT_CONTENT)
            .mailTemplateType(DEFAULT_MAIL_TEMPLATE_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isActive(DEFAULT_IS_ACTIVE);
        return adminEmailConfigs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminEmailConfigs createUpdatedEntity(EntityManager em) {
        AdminEmailConfigs adminEmailConfigs = new AdminEmailConfigs()
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        return adminEmailConfigs;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AdminEmailConfigs.class).block();
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
        adminEmailConfigsSearchRepository.deleteAll().block();
        assertThat(adminEmailConfigsSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        adminEmailConfigs = createEntity(em);
    }

    @Test
    void createAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeCreate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(DEFAULT_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    void createAdminEmailConfigsWithExistingId() throws Exception {
        // Create the AdminEmailConfigs with an existing ID
        adminEmailConfigs.setId(1L);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        int databaseSizeBeforeCreate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setTitle(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setSubject(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setContent(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMailTemplateTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setMailTemplateType(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setCreatedDate(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        // set the field null
        adminEmailConfigs.setIsActive(null);

        // Create the AdminEmailConfigs, which fails.
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAdminEmailConfigs() {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();

        // Get all the adminEmailConfigsList
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
            .value(hasItem(adminEmailConfigs.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].subject")
            .value(hasItem(DEFAULT_SUBJECT))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].mailTemplateType")
            .value(hasItem(DEFAULT_MAIL_TEMPLATE_TYPE.toString()))
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
    void getAdminEmailConfigs() {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();

        // Get the adminEmailConfigs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, adminEmailConfigs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(adminEmailConfigs.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.subject")
            .value(is(DEFAULT_SUBJECT))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.mailTemplateType")
            .value(is(DEFAULT_MAIL_TEMPLATE_TYPE.toString()))
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
    void getNonExistingAdminEmailConfigs() {
        // Get the adminEmailConfigs
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAdminEmailConfigs() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        adminEmailConfigsSearchRepository.save(adminEmailConfigs).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());

        // Update the adminEmailConfigs
        AdminEmailConfigs updatedAdminEmailConfigs = adminEmailConfigsRepository.findById(adminEmailConfigs.getId()).block();
        updatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(updatedAdminEmailConfigs);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AdminEmailConfigs> adminEmailConfigsSearchList = IterableUtils.toList(
                    adminEmailConfigsSearchRepository.findAll().collectList().block()
                );
                AdminEmailConfigs testAdminEmailConfigsSearch = adminEmailConfigsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAdminEmailConfigsSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testAdminEmailConfigsSearch.getSubject()).isEqualTo(UPDATED_SUBJECT);
                assertThat(testAdminEmailConfigsSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testAdminEmailConfigsSearch.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
                assertThat(testAdminEmailConfigsSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testAdminEmailConfigsSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testAdminEmailConfigsSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testAdminEmailConfigsSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testAdminEmailConfigsSearch.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
            });
    }

    @Test
    void putNonExistingAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminEmailConfigs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void fullUpdateAdminEmailConfigsWithPatch() throws Exception {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();

        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();

        // Update the adminEmailConfigs using partial update
        AdminEmailConfigs partialUpdatedAdminEmailConfigs = new AdminEmailConfigs();
        partialUpdatedAdminEmailConfigs.setId(adminEmailConfigs.getId());

        partialUpdatedAdminEmailConfigs
            .title(UPDATED_TITLE)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .mailTemplateType(UPDATED_MAIL_TEMPLATE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isActive(UPDATED_IS_ACTIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminEmailConfigs.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminEmailConfigs))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        AdminEmailConfigs testAdminEmailConfigs = adminEmailConfigsList.get(adminEmailConfigsList.size() - 1);
        assertThat(testAdminEmailConfigs.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminEmailConfigs.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testAdminEmailConfigs.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminEmailConfigs.getMailTemplateType()).isEqualTo(UPDATED_MAIL_TEMPLATE_TYPE);
        assertThat(testAdminEmailConfigs.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminEmailConfigs.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminEmailConfigs.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminEmailConfigs.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminEmailConfigs.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    void patchNonExistingAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, adminEmailConfigsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAdminEmailConfigs() throws Exception {
        int databaseSizeBeforeUpdate = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        adminEmailConfigs.setId(longCount.incrementAndGet());

        // Create the AdminEmailConfigs
        AdminEmailConfigsDTO adminEmailConfigsDTO = adminEmailConfigsMapper.toDto(adminEmailConfigs);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminEmailConfigsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminEmailConfigs in the database
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAdminEmailConfigs() {
        // Initialize the database
        adminEmailConfigsRepository.save(adminEmailConfigs).block();
        adminEmailConfigsRepository.save(adminEmailConfigs).block();
        adminEmailConfigsSearchRepository.save(adminEmailConfigs).block();

        int databaseSizeBeforeDelete = adminEmailConfigsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the adminEmailConfigs
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, adminEmailConfigs.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AdminEmailConfigs> adminEmailConfigsList = adminEmailConfigsRepository.findAll().collectList().block();
        assertThat(adminEmailConfigsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminEmailConfigsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAdminEmailConfigs() {
        // Initialize the database
        adminEmailConfigs = adminEmailConfigsRepository.save(adminEmailConfigs).block();
        adminEmailConfigsSearchRepository.save(adminEmailConfigs).block();

        // Search the adminEmailConfigs
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + adminEmailConfigs.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(adminEmailConfigs.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].subject")
            .value(hasItem(DEFAULT_SUBJECT))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].mailTemplateType")
            .value(hasItem(DEFAULT_MAIL_TEMPLATE_TYPE.toString()))
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
