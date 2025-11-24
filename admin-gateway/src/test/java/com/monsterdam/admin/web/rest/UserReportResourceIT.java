package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.AssistanceTicket;
import com.monsterdam.admin.domain.UserReport;
import com.monsterdam.admin.domain.enumeration.ReportCategory;
import com.monsterdam.admin.domain.enumeration.ReportStatus;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.UserReportRepository;
import com.monsterdam.admin.repository.search.UserReportSearchRepository;
import com.monsterdam.admin.service.dto.UserReportDTO;
import com.monsterdam.admin.service.mapper.UserReportMapper;
import java.time.Duration;
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
 * Integration tests for the {@link UserReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserReportResourceIT {

    private static final String DEFAULT_REPORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_DESCRIPTION = "BBBBBBBBBB";

    private static final ReportStatus DEFAULT_STATUS = ReportStatus.PENDING;
    private static final ReportStatus UPDATED_STATUS = ReportStatus.REVIEWED;

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

    private static final ReportCategory DEFAULT_REPORT_CATEGORY = ReportCategory.POST_REPORT;
    private static final ReportCategory UPDATED_REPORT_CATEGORY = ReportCategory.COMMENT_REPORT;

    private static final Long DEFAULT_REPORTER_ID = 1L;
    private static final Long UPDATED_REPORTER_ID = 2L;

    private static final Long DEFAULT_REPORTED_ID = 1L;
    private static final Long UPDATED_REPORTED_ID = 2L;

    private static final Long DEFAULT_MULTIMEDIA_ID = 1L;
    private static final Long UPDATED_MULTIMEDIA_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_COMMENT_ID = 1L;
    private static final Long UPDATED_COMMENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-reports/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserReportRepository userReportRepository;

    @Autowired
    private UserReportMapper userReportMapper;

    @Autowired
    private UserReportSearchRepository userReportSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserReport userReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createEntity(EntityManager em) {
        UserReport userReport = new UserReport()
            .reportDescription(DEFAULT_REPORT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .reportCategory(DEFAULT_REPORT_CATEGORY)
            .reporterId(DEFAULT_REPORTER_ID)
            .reportedId(DEFAULT_REPORTED_ID)
            .multimediaId(DEFAULT_MULTIMEDIA_ID)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID)
            .commentId(DEFAULT_COMMENT_ID);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createEntity(em)).block();
        userReport.setTicket(assistanceTicket);
        return userReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserReport createUpdatedEntity(EntityManager em) {
        UserReport userReport = new UserReport()
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createUpdatedEntity(em)).block();
        userReport.setTicket(assistanceTicket);
        return userReport;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserReport.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        AssistanceTicketResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        userReportSearchRepository.deleteAll().block();
        assertThat(userReportSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userReport = createEntity(em);
    }

    @Test
    void createUserReport() throws Exception {
        int databaseSizeBeforeCreate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getReportDescription()).isEqualTo(DEFAULT_REPORT_DESCRIPTION);
        assertThat(testUserReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserReport.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserReport.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserReport.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserReport.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserReport.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserReport.getReportCategory()).isEqualTo(DEFAULT_REPORT_CATEGORY);
        assertThat(testUserReport.getReporterId()).isEqualTo(DEFAULT_REPORTER_ID);
        assertThat(testUserReport.getReportedId()).isEqualTo(DEFAULT_REPORTED_ID);
        assertThat(testUserReport.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testUserReport.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testUserReport.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testUserReport.getCommentId()).isEqualTo(DEFAULT_COMMENT_ID);
    }

    @Test
    void createUserReportWithExistingId() throws Exception {
        // Create the UserReport with an existing ID
        userReport.setId(1L);
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        int databaseSizeBeforeCreate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setStatus(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setCreatedDate(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setIsDeleted(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkReportCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setReportCategory(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkReporterIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setReporterId(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkReportedIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        // set the field null
        userReport.setReportedId(null);

        // Create the UserReport, which fails.
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserReportsAsStream() {
        // Initialize the database
        userReportRepository.save(userReport).block();

        List<UserReport> userReportList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(UserReportDTO.class)
            .getResponseBody()
            .map(userReportMapper::toEntity)
            .filter(userReport::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(userReportList).isNotNull();
        assertThat(userReportList).hasSize(1);
        UserReport testUserReport = userReportList.get(0);
        assertThat(testUserReport.getReportDescription()).isEqualTo(DEFAULT_REPORT_DESCRIPTION);
        assertThat(testUserReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserReport.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserReport.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserReport.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserReport.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserReport.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserReport.getReportCategory()).isEqualTo(DEFAULT_REPORT_CATEGORY);
        assertThat(testUserReport.getReporterId()).isEqualTo(DEFAULT_REPORTER_ID);
        assertThat(testUserReport.getReportedId()).isEqualTo(DEFAULT_REPORTED_ID);
        assertThat(testUserReport.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testUserReport.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testUserReport.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testUserReport.getCommentId()).isEqualTo(DEFAULT_COMMENT_ID);
    }

    @Test
    void getAllUserReports() {
        // Initialize the database
        userReportRepository.save(userReport).block();

        // Get all the userReportList
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
            .value(hasItem(userReport.getId().intValue()))
            .jsonPath("$.[*].reportDescription")
            .value(hasItem(DEFAULT_REPORT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.[*].reportCategory")
            .value(hasItem(DEFAULT_REPORT_CATEGORY.toString()))
            .jsonPath("$.[*].reporterId")
            .value(hasItem(DEFAULT_REPORTER_ID.intValue()))
            .jsonPath("$.[*].reportedId")
            .value(hasItem(DEFAULT_REPORTED_ID.intValue()))
            .jsonPath("$.[*].multimediaId")
            .value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.[*].messageId")
            .value(hasItem(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].postId")
            .value(hasItem(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.[*].commentId")
            .value(hasItem(DEFAULT_COMMENT_ID.intValue()));
    }

    @Test
    void getUserReport() {
        // Initialize the database
        userReportRepository.save(userReport).block();

        // Get the userReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userReport.getId().intValue()))
            .jsonPath("$.reportDescription")
            .value(is(DEFAULT_REPORT_DESCRIPTION))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.reportCategory")
            .value(is(DEFAULT_REPORT_CATEGORY.toString()))
            .jsonPath("$.reporterId")
            .value(is(DEFAULT_REPORTER_ID.intValue()))
            .jsonPath("$.reportedId")
            .value(is(DEFAULT_REPORTED_ID.intValue()))
            .jsonPath("$.multimediaId")
            .value(is(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.messageId")
            .value(is(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.postId")
            .value(is(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.commentId")
            .value(is(DEFAULT_COMMENT_ID.intValue()));
    }

    @Test
    void getNonExistingUserReport() {
        // Get the userReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserReport() throws Exception {
        // Initialize the database
        userReportRepository.save(userReport).block();

        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        userReportSearchRepository.save(userReport).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());

        // Update the userReport
        UserReport updatedUserReport = userReportRepository.findById(userReport.getId()).block();
        updatedUserReport
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);
        UserReportDTO userReportDTO = userReportMapper.toDto(updatedUserReport);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userReportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getReportDescription()).isEqualTo(UPDATED_REPORT_DESCRIPTION);
        assertThat(testUserReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserReport.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserReport.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserReport.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserReport.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserReport.getReportCategory()).isEqualTo(UPDATED_REPORT_CATEGORY);
        assertThat(testUserReport.getReporterId()).isEqualTo(UPDATED_REPORTER_ID);
        assertThat(testUserReport.getReportedId()).isEqualTo(UPDATED_REPORTED_ID);
        assertThat(testUserReport.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
        assertThat(testUserReport.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testUserReport.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testUserReport.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserReport> userReportSearchList = IterableUtils.toList(userReportSearchRepository.findAll().collectList().block());
                UserReport testUserReportSearch = userReportSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserReportSearch.getReportDescription()).isEqualTo(UPDATED_REPORT_DESCRIPTION);
                assertThat(testUserReportSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testUserReportSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserReportSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserReportSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserReportSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserReportSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testUserReportSearch.getReportCategory()).isEqualTo(UPDATED_REPORT_CATEGORY);
                assertThat(testUserReportSearch.getReporterId()).isEqualTo(UPDATED_REPORTER_ID);
                assertThat(testUserReportSearch.getReportedId()).isEqualTo(UPDATED_REPORTED_ID);
                assertThat(testUserReportSearch.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
                assertThat(testUserReportSearch.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
                assertThat(testUserReportSearch.getPostId()).isEqualTo(UPDATED_POST_ID);
                assertThat(testUserReportSearch.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
            });
    }

    @Test
    void putNonExistingUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userReportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        userReportRepository.save(userReport).block();

        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reportedId(UPDATED_REPORTED_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getReportDescription()).isEqualTo(DEFAULT_REPORT_DESCRIPTION);
        assertThat(testUserReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserReport.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserReport.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserReport.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserReport.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserReport.getReportCategory()).isEqualTo(UPDATED_REPORT_CATEGORY);
        assertThat(testUserReport.getReporterId()).isEqualTo(DEFAULT_REPORTER_ID);
        assertThat(testUserReport.getReportedId()).isEqualTo(UPDATED_REPORTED_ID);
        assertThat(testUserReport.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testUserReport.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testUserReport.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testUserReport.getCommentId()).isEqualTo(DEFAULT_COMMENT_ID);
    }

    @Test
    void fullUpdateUserReportWithPatch() throws Exception {
        // Initialize the database
        userReportRepository.save(userReport).block();

        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();

        // Update the userReport using partial update
        UserReport partialUpdatedUserReport = new UserReport();
        partialUpdatedUserReport.setId(userReport.getId());

        partialUpdatedUserReport
            .reportDescription(UPDATED_REPORT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .reportCategory(UPDATED_REPORT_CATEGORY)
            .reporterId(UPDATED_REPORTER_ID)
            .reportedId(UPDATED_REPORTED_ID)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        UserReport testUserReport = userReportList.get(userReportList.size() - 1);
        assertThat(testUserReport.getReportDescription()).isEqualTo(UPDATED_REPORT_DESCRIPTION);
        assertThat(testUserReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserReport.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserReport.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserReport.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserReport.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserReport.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserReport.getReportCategory()).isEqualTo(UPDATED_REPORT_CATEGORY);
        assertThat(testUserReport.getReporterId()).isEqualTo(UPDATED_REPORTER_ID);
        assertThat(testUserReport.getReportedId()).isEqualTo(UPDATED_REPORTED_ID);
        assertThat(testUserReport.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
        assertThat(testUserReport.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testUserReport.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testUserReport.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
    }

    @Test
    void patchNonExistingUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userReportDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserReport() throws Exception {
        int databaseSizeBeforeUpdate = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        userReport.setId(longCount.incrementAndGet());

        // Create the UserReport
        UserReportDTO userReportDTO = userReportMapper.toDto(userReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userReportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserReport in the database
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserReport() {
        // Initialize the database
        userReportRepository.save(userReport).block();
        userReportRepository.save(userReport).block();
        userReportSearchRepository.save(userReport).block();

        int databaseSizeBeforeDelete = userReportRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userReport
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserReport> userReportList = userReportRepository.findAll().collectList().block();
        assertThat(userReportList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userReportSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserReport() {
        // Initialize the database
        userReport = userReportRepository.save(userReport).block();
        userReportSearchRepository.save(userReport).block();

        // Search the userReport
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userReport.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userReport.getId().intValue()))
            .jsonPath("$.[*].reportDescription")
            .value(hasItem(DEFAULT_REPORT_DESCRIPTION))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.[*].reportCategory")
            .value(hasItem(DEFAULT_REPORT_CATEGORY.toString()))
            .jsonPath("$.[*].reporterId")
            .value(hasItem(DEFAULT_REPORTER_ID.intValue()))
            .jsonPath("$.[*].reportedId")
            .value(hasItem(DEFAULT_REPORTED_ID.intValue()))
            .jsonPath("$.[*].multimediaId")
            .value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.[*].messageId")
            .value(hasItem(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].postId")
            .value(hasItem(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.[*].commentId")
            .value(hasItem(DEFAULT_COMMENT_ID.intValue()));
    }
}
