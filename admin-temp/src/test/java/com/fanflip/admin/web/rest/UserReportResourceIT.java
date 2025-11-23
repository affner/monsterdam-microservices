package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.UserReport;
import com.fanflip.admin.domain.enumeration.ReportCategory;
import com.fanflip.admin.domain.enumeration.ReportStatus;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.UserReportRepository;
import com.fanflip.admin.repository.search.UserReportSearchRepository;
import com.fanflip.admin.service.UserReportService;
import com.fanflip.admin.service.dto.UserReportDTO;
import com.fanflip.admin.service.mapper.UserReportMapper;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

/**
 * Integration tests for the {@link UserReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
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

    private static final String ENTITY_API_URL = "/api/user-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-reports/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserReportRepository userReportRepository;

    @Mock
    private UserReportRepository userReportRepositoryMock;

    @Autowired
    private UserReportMapper userReportMapper;

    @Mock
    private UserReportService userReportServiceMock;

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
            .reportCategory(DEFAULT_REPORT_CATEGORY);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createEntity(em)).block();
        userReport.setTicket(assistanceTicket);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        userReport.setReporter(userProfile);
        // Add required entity
        userReport.setReported(userProfile);
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
            .reportCategory(UPDATED_REPORT_CATEGORY);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createUpdatedEntity(em)).block();
        userReport.setTicket(assistanceTicket);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        userReport.setReporter(userProfile);
        // Add required entity
        userReport.setReported(userProfile);
        return userReport;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserReport.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        AssistanceTicketResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
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
            .value(hasItem(DEFAULT_REPORT_CATEGORY.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserReportsWithEagerRelationshipsIsEnabled() {
        when(userReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserReportsWithEagerRelationshipsIsNotEnabled() {
        when(userReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userReportRepositoryMock, times(1)).findAllWithEagerRelationships(any());
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
            .value(is(DEFAULT_REPORT_CATEGORY.toString()));
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
            .reportCategory(UPDATED_REPORT_CATEGORY);
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
            .reportCategory(UPDATED_REPORT_CATEGORY);

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
            .reportCategory(UPDATED_REPORT_CATEGORY);

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
            .value(hasItem(DEFAULT_REPORT_CATEGORY.toString()));
    }
}
