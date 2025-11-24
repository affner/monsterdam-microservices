package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.AdminAnnouncement;
import com.monsterdam.admin.domain.AdminUserProfile;
import com.monsterdam.admin.domain.enumeration.AdminAnnouncementType;
import com.monsterdam.admin.repository.AdminAnnouncementRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.AdminAnnouncementSearchRepository;
import com.monsterdam.admin.service.dto.AdminAnnouncementDTO;
import com.monsterdam.admin.service.mapper.AdminAnnouncementMapper;
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
 * Integration tests for the {@link AdminAnnouncementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AdminAnnouncementResourceIT {

    private static final AdminAnnouncementType DEFAULT_ANNOUNCEMENT_TYPE = AdminAnnouncementType.BANNER;
    private static final AdminAnnouncementType UPDATED_ANNOUNCEMENT_TYPE = AdminAnnouncementType.SYSTEM_UPDATE;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/admin-announcements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/admin-announcements/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminAnnouncementRepository adminAnnouncementRepository;

    @Autowired
    private AdminAnnouncementMapper adminAnnouncementMapper;

    @Autowired
    private AdminAnnouncementSearchRepository adminAnnouncementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AdminAnnouncement adminAnnouncement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAnnouncement createEntity(EntityManager em) {
        AdminAnnouncement adminAnnouncement = new AdminAnnouncement()
            .announcementType(DEFAULT_ANNOUNCEMENT_TYPE)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        // Add required entity
        AdminUserProfile adminUserProfile;
        adminUserProfile = em.insert(AdminUserProfileResourceIT.createEntity(em)).block();
        adminAnnouncement.setAdmin(adminUserProfile);
        return adminAnnouncement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminAnnouncement createUpdatedEntity(EntityManager em) {
        AdminAnnouncement adminAnnouncement = new AdminAnnouncement()
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        // Add required entity
        AdminUserProfile adminUserProfile;
        adminUserProfile = em.insert(AdminUserProfileResourceIT.createUpdatedEntity(em)).block();
        adminAnnouncement.setAdmin(adminUserProfile);
        return adminAnnouncement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AdminAnnouncement.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        AdminUserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        adminAnnouncementSearchRepository.deleteAll().block();
        assertThat(adminAnnouncementSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        adminAnnouncement = createEntity(em);
    }

    @Test
    void createAdminAnnouncement() throws Exception {
        int databaseSizeBeforeCreate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AdminAnnouncement testAdminAnnouncement = adminAnnouncementList.get(adminAnnouncementList.size() - 1);
        assertThat(testAdminAnnouncement.getAnnouncementType()).isEqualTo(DEFAULT_ANNOUNCEMENT_TYPE);
        assertThat(testAdminAnnouncement.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAdminAnnouncement.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminAnnouncement.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminAnnouncement.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminAnnouncement.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminAnnouncement.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void createAdminAnnouncementWithExistingId() throws Exception {
        // Create the AdminAnnouncement with an existing ID
        adminAnnouncement.setId(1L);
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        int databaseSizeBeforeCreate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAnnouncementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        // set the field null
        adminAnnouncement.setAnnouncementType(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        // set the field null
        adminAnnouncement.setTitle(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        // set the field null
        adminAnnouncement.setContent(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        // set the field null
        adminAnnouncement.setCreatedDate(null);

        // Create the AdminAnnouncement, which fails.
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAdminAnnouncements() {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();

        // Get all the adminAnnouncementList
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
            .value(hasItem(adminAnnouncement.getId().intValue()))
            .jsonPath("$.[*].announcementType")
            .value(hasItem(DEFAULT_ANNOUNCEMENT_TYPE.toString()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getAdminAnnouncement() {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();

        // Get the adminAnnouncement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, adminAnnouncement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(adminAnnouncement.getId().intValue()))
            .jsonPath("$.announcementType")
            .value(is(DEFAULT_ANNOUNCEMENT_TYPE.toString()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getNonExistingAdminAnnouncement() {
        // Get the adminAnnouncement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAdminAnnouncement() throws Exception {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();

        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        adminAnnouncementSearchRepository.save(adminAnnouncement).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());

        // Update the adminAnnouncement
        AdminAnnouncement updatedAdminAnnouncement = adminAnnouncementRepository.findById(adminAnnouncement.getId()).block();
        updatedAdminAnnouncement
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(updatedAdminAnnouncement);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        AdminAnnouncement testAdminAnnouncement = adminAnnouncementList.get(adminAnnouncementList.size() - 1);
        assertThat(testAdminAnnouncement.getAnnouncementType()).isEqualTo(UPDATED_ANNOUNCEMENT_TYPE);
        assertThat(testAdminAnnouncement.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminAnnouncement.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminAnnouncement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminAnnouncement.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminAnnouncement.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminAnnouncement.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AdminAnnouncement> adminAnnouncementSearchList = IterableUtils.toList(
                    adminAnnouncementSearchRepository.findAll().collectList().block()
                );
                AdminAnnouncement testAdminAnnouncementSearch = adminAnnouncementSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAdminAnnouncementSearch.getAnnouncementType()).isEqualTo(UPDATED_ANNOUNCEMENT_TYPE);
                assertThat(testAdminAnnouncementSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testAdminAnnouncementSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testAdminAnnouncementSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testAdminAnnouncementSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testAdminAnnouncementSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testAdminAnnouncementSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
            });
    }

    @Test
    void putNonExistingAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAdminAnnouncementWithPatch() throws Exception {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();

        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();

        // Update the adminAnnouncement using partial update
        AdminAnnouncement partialUpdatedAdminAnnouncement = new AdminAnnouncement();
        partialUpdatedAdminAnnouncement.setId(adminAnnouncement.getId());

        partialUpdatedAdminAnnouncement.createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminAnnouncement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminAnnouncement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        AdminAnnouncement testAdminAnnouncement = adminAnnouncementList.get(adminAnnouncementList.size() - 1);
        assertThat(testAdminAnnouncement.getAnnouncementType()).isEqualTo(DEFAULT_ANNOUNCEMENT_TYPE);
        assertThat(testAdminAnnouncement.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAdminAnnouncement.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAdminAnnouncement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminAnnouncement.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminAnnouncement.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminAnnouncement.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void fullUpdateAdminAnnouncementWithPatch() throws Exception {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();

        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();

        // Update the adminAnnouncement using partial update
        AdminAnnouncement partialUpdatedAdminAnnouncement = new AdminAnnouncement();
        partialUpdatedAdminAnnouncement.setId(adminAnnouncement.getId());

        partialUpdatedAdminAnnouncement
            .announcementType(UPDATED_ANNOUNCEMENT_TYPE)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminAnnouncement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminAnnouncement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        AdminAnnouncement testAdminAnnouncement = adminAnnouncementList.get(adminAnnouncementList.size() - 1);
        assertThat(testAdminAnnouncement.getAnnouncementType()).isEqualTo(UPDATED_ANNOUNCEMENT_TYPE);
        assertThat(testAdminAnnouncement.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdminAnnouncement.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAdminAnnouncement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminAnnouncement.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminAnnouncement.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminAnnouncement.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void patchNonExistingAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, adminAnnouncementDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAdminAnnouncement() throws Exception {
        int databaseSizeBeforeUpdate = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        adminAnnouncement.setId(longCount.incrementAndGet());

        // Create the AdminAnnouncement
        AdminAnnouncementDTO adminAnnouncementDTO = adminAnnouncementMapper.toDto(adminAnnouncement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminAnnouncementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminAnnouncement in the database
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAdminAnnouncement() {
        // Initialize the database
        adminAnnouncementRepository.save(adminAnnouncement).block();
        adminAnnouncementRepository.save(adminAnnouncement).block();
        adminAnnouncementSearchRepository.save(adminAnnouncement).block();

        int databaseSizeBeforeDelete = adminAnnouncementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the adminAnnouncement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, adminAnnouncement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AdminAnnouncement> adminAnnouncementList = adminAnnouncementRepository.findAll().collectList().block();
        assertThat(adminAnnouncementList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminAnnouncementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAdminAnnouncement() {
        // Initialize the database
        adminAnnouncement = adminAnnouncementRepository.save(adminAnnouncement).block();
        adminAnnouncementSearchRepository.save(adminAnnouncement).block();

        // Search the adminAnnouncement
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + adminAnnouncement.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(adminAnnouncement.getId().intValue()))
            .jsonPath("$.[*].announcementType")
            .value(hasItem(DEFAULT_ANNOUNCEMENT_TYPE.toString()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }
}
