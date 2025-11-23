package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SpecialAward;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SpecialAwardRepository;
import com.fanflip.admin.repository.search.SpecialAwardSearchRepository;
import com.fanflip.admin.service.dto.SpecialAwardDTO;
import com.fanflip.admin.service.mapper.SpecialAwardMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SpecialAwardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SpecialAwardResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_SPECIAL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ALT_SPECIAL_TITLE = "BBBBBBBBBB";

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

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final Long DEFAULT_SPECIAL_TITLE_ID = 1L;
    private static final Long UPDATED_SPECIAL_TITLE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/special-awards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/special-awards/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialAwardRepository specialAwardRepository;

    @Autowired
    private SpecialAwardMapper specialAwardMapper;

    @Autowired
    private SpecialAwardSearchRepository specialAwardSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SpecialAward specialAward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createEntity(EntityManager em) {
        SpecialAward specialAward = new SpecialAward()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reason(DEFAULT_REASON)
            .altSpecialTitle(DEFAULT_ALT_SPECIAL_TITLE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .viewerId(DEFAULT_VIEWER_ID)
            .creatorId(DEFAULT_CREATOR_ID)
            .specialTitleId(DEFAULT_SPECIAL_TITLE_ID);
        return specialAward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createUpdatedEntity(EntityManager em) {
        SpecialAward specialAward = new SpecialAward()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
        return specialAward;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SpecialAward.class).block();
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
        specialAwardSearchRepository.deleteAll().block();
        assertThat(specialAwardSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        specialAward = createEntity(em);
    }

    @Test
    void createSpecialAward() throws Exception {
        int databaseSizeBeforeCreate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(DEFAULT_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSpecialAward.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testSpecialAward.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(DEFAULT_SPECIAL_TITLE_ID);
    }

    @Test
    void createSpecialAwardWithExistingId() throws Exception {
        // Create the SpecialAward with an existing ID
        specialAward.setId(1L);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        int databaseSizeBeforeCreate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setStartDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setEndDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setCreatedDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setIsDeleted(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setViewerId(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setCreatorId(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSpecialTitleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        // set the field null
        specialAward.setSpecialTitleId(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSpecialAwards() {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();

        // Get all the specialAwardList
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
            .value(hasItem(specialAward.getId().intValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].altSpecialTitle")
            .value(hasItem(DEFAULT_ALT_SPECIAL_TITLE))
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
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].creatorId")
            .value(hasItem(DEFAULT_CREATOR_ID.intValue()))
            .jsonPath("$.[*].specialTitleId")
            .value(hasItem(DEFAULT_SPECIAL_TITLE_ID.intValue()));
    }

    @Test
    void getSpecialAward() {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();

        // Get the specialAward
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, specialAward.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(specialAward.getId().intValue()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.reason")
            .value(is(DEFAULT_REASON))
            .jsonPath("$.altSpecialTitle")
            .value(is(DEFAULT_ALT_SPECIAL_TITLE))
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
            .jsonPath("$.viewerId")
            .value(is(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.creatorId")
            .value(is(DEFAULT_CREATOR_ID.intValue()))
            .jsonPath("$.specialTitleId")
            .value(is(DEFAULT_SPECIAL_TITLE_ID.intValue()));
    }

    @Test
    void getNonExistingSpecialAward() {
        // Get the specialAward
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSpecialAward() throws Exception {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        specialAwardSearchRepository.save(specialAward).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());

        // Update the specialAward
        SpecialAward updatedSpecialAward = specialAwardRepository.findById(specialAward.getId()).block();
        updatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(updatedSpecialAward);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialAwardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialAward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialAward.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SpecialAward> specialAwardSearchList = IterableUtils.toList(
                    specialAwardSearchRepository.findAll().collectList().block()
                );
                SpecialAward testSpecialAwardSearch = specialAwardSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSpecialAwardSearch.getStartDate()).isEqualTo(UPDATED_START_DATE);
                assertThat(testSpecialAwardSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testSpecialAwardSearch.getReason()).isEqualTo(UPDATED_REASON);
                assertThat(testSpecialAwardSearch.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
                assertThat(testSpecialAwardSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSpecialAwardSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSpecialAwardSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSpecialAwardSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSpecialAwardSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testSpecialAwardSearch.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
                assertThat(testSpecialAwardSearch.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
                assertThat(testSpecialAwardSearch.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
            });
    }

    @Test
    void putNonExistingSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialAwardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .endDate(UPDATED_END_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .creatorId(UPDATED_CREATOR_ID)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialAward))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(DEFAULT_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSpecialAward.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testSpecialAward.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
    }

    @Test
    void fullUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialAward))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialAward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialAward.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
    }

    @Test
    void patchNonExistingSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, specialAwardDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSpecialAward() {
        // Initialize the database
        specialAwardRepository.save(specialAward).block();
        specialAwardRepository.save(specialAward).block();
        specialAwardSearchRepository.save(specialAward).block();

        int databaseSizeBeforeDelete = specialAwardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the specialAward
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, specialAward.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll().collectList().block();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialAwardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSpecialAward() {
        // Initialize the database
        specialAward = specialAwardRepository.save(specialAward).block();
        specialAwardSearchRepository.save(specialAward).block();

        // Search the specialAward
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + specialAward.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(specialAward.getId().intValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].reason")
            .value(hasItem(DEFAULT_REASON))
            .jsonPath("$.[*].altSpecialTitle")
            .value(hasItem(DEFAULT_ALT_SPECIAL_TITLE))
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
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].creatorId")
            .value(hasItem(DEFAULT_CREATOR_ID.intValue()))
            .jsonPath("$.[*].specialTitleId")
            .value(hasItem(DEFAULT_SPECIAL_TITLE_ID.intValue()));
    }
}
