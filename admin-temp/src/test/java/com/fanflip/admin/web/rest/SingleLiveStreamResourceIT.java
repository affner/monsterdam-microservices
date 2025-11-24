package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.SingleLiveStream;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.SingleLiveStreamRepository;
import com.monsterdam.admin.repository.search.SingleLiveStreamSearchRepository;
import com.monsterdam.admin.service.dto.SingleLiveStreamDTO;
import com.monsterdam.admin.service.mapper.SingleLiveStreamMapper;
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
 * Integration tests for the {@link SingleLiveStreamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SingleLiveStreamResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_LIVE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LIVE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LIVE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LIVE_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LIVE_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_LIVE_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_RECORDED = false;
    private static final Boolean UPDATED_IS_RECORDED = true;

    private static final Long DEFAULT_LIKE_COUNT = 1L;
    private static final Long UPDATED_LIKE_COUNT = 2L;

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

    private static final String ENTITY_API_URL = "/api/single-live-streams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/single-live-streams/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleLiveStreamRepository singleLiveStreamRepository;

    @Autowired
    private SingleLiveStreamMapper singleLiveStreamMapper;

    @Autowired
    private SingleLiveStreamSearchRepository singleLiveStreamSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SingleLiveStream singleLiveStream;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleLiveStream createEntity(EntityManager em) {
        SingleLiveStream singleLiveStream = new SingleLiveStream()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .liveContent(DEFAULT_LIVE_CONTENT)
            .liveContentContentType(DEFAULT_LIVE_CONTENT_CONTENT_TYPE)
            .liveContentS3Key(DEFAULT_LIVE_CONTENT_S_3_KEY)
            .isRecorded(DEFAULT_IS_RECORDED)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return singleLiveStream;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleLiveStream createUpdatedEntity(EntityManager em) {
        SingleLiveStream singleLiveStream = new SingleLiveStream()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .liveContent(UPDATED_LIVE_CONTENT)
            .liveContentContentType(UPDATED_LIVE_CONTENT_CONTENT_TYPE)
            .liveContentS3Key(UPDATED_LIVE_CONTENT_S_3_KEY)
            .isRecorded(UPDATED_IS_RECORDED)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return singleLiveStream;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SingleLiveStream.class).block();
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
        singleLiveStreamSearchRepository.deleteAll().block();
        assertThat(singleLiveStreamSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        singleLiveStream = createEntity(em);
    }

    @Test
    void createSingleLiveStream() throws Exception {
        int databaseSizeBeforeCreate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSingleLiveStream.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSingleLiveStream.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSingleLiveStream.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testSingleLiveStream.getLiveContent()).isEqualTo(DEFAULT_LIVE_CONTENT);
        assertThat(testSingleLiveStream.getLiveContentContentType()).isEqualTo(DEFAULT_LIVE_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getLiveContentS3Key()).isEqualTo(DEFAULT_LIVE_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getIsRecorded()).isEqualTo(DEFAULT_IS_RECORDED);
        assertThat(testSingleLiveStream.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSingleLiveStreamWithExistingId() throws Exception {
        // Create the SingleLiveStream with an existing ID
        singleLiveStream.setId(1L);
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        int databaseSizeBeforeCreate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        // set the field null
        singleLiveStream.setStartTime(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsRecordedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        // set the field null
        singleLiveStream.setIsRecorded(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        // set the field null
        singleLiveStream.setCreatedDate(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        // set the field null
        singleLiveStream.setIsDeleted(null);

        // Create the SingleLiveStream, which fails.
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSingleLiveStreams() {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();

        // Get all the singleLiveStreamList
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
            .value(hasItem(singleLiveStream.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].startTime")
            .value(hasItem(DEFAULT_START_TIME.toString()))
            .jsonPath("$.[*].endTime")
            .value(hasItem(DEFAULT_END_TIME.toString()))
            .jsonPath("$.[*].liveContentContentType")
            .value(hasItem(DEFAULT_LIVE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].liveContent")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LIVE_CONTENT)))
            .jsonPath("$.[*].liveContentS3Key")
            .value(hasItem(DEFAULT_LIVE_CONTENT_S_3_KEY))
            .jsonPath("$.[*].isRecorded")
            .value(hasItem(DEFAULT_IS_RECORDED.booleanValue()))
            .jsonPath("$.[*].likeCount")
            .value(hasItem(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getSingleLiveStream() {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();

        // Get the singleLiveStream
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singleLiveStream.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singleLiveStream.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.thumbnailS3Key")
            .value(is(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.startTime")
            .value(is(DEFAULT_START_TIME.toString()))
            .jsonPath("$.endTime")
            .value(is(DEFAULT_END_TIME.toString()))
            .jsonPath("$.liveContentContentType")
            .value(is(DEFAULT_LIVE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.liveContent")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_LIVE_CONTENT)))
            .jsonPath("$.liveContentS3Key")
            .value(is(DEFAULT_LIVE_CONTENT_S_3_KEY))
            .jsonPath("$.isRecorded")
            .value(is(DEFAULT_IS_RECORDED.booleanValue()))
            .jsonPath("$.likeCount")
            .value(is(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingSingleLiveStream() {
        // Get the singleLiveStream
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSingleLiveStream() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        singleLiveStreamSearchRepository.save(singleLiveStream).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());

        // Update the singleLiveStream
        SingleLiveStream updatedSingleLiveStream = singleLiveStreamRepository.findById(singleLiveStream.getId()).block();
        updatedSingleLiveStream
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .liveContent(UPDATED_LIVE_CONTENT)
            .liveContentContentType(UPDATED_LIVE_CONTENT_CONTENT_TYPE)
            .liveContentS3Key(UPDATED_LIVE_CONTENT_S_3_KEY)
            .isRecorded(UPDATED_IS_RECORDED)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(updatedSingleLiveStream);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSingleLiveStream.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleLiveStream.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSingleLiveStream.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSingleLiveStream.getLiveContent()).isEqualTo(UPDATED_LIVE_CONTENT);
        assertThat(testSingleLiveStream.getLiveContentContentType()).isEqualTo(UPDATED_LIVE_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getLiveContentS3Key()).isEqualTo(UPDATED_LIVE_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getIsRecorded()).isEqualTo(UPDATED_IS_RECORDED);
        assertThat(testSingleLiveStream.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SingleLiveStream> singleLiveStreamSearchList = IterableUtils.toList(
                    singleLiveStreamSearchRepository.findAll().collectList().block()
                );
                SingleLiveStream testSingleLiveStreamSearch = singleLiveStreamSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSingleLiveStreamSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testSingleLiveStreamSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSingleLiveStreamSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSingleLiveStreamSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSingleLiveStreamSearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testSingleLiveStreamSearch.getStartTime()).isEqualTo(UPDATED_START_TIME);
                assertThat(testSingleLiveStreamSearch.getEndTime()).isEqualTo(UPDATED_END_TIME);
                assertThat(testSingleLiveStreamSearch.getLiveContent()).isEqualTo(UPDATED_LIVE_CONTENT);
                assertThat(testSingleLiveStreamSearch.getLiveContentContentType()).isEqualTo(UPDATED_LIVE_CONTENT_CONTENT_TYPE);
                assertThat(testSingleLiveStreamSearch.getLiveContentS3Key()).isEqualTo(UPDATED_LIVE_CONTENT_S_3_KEY);
                assertThat(testSingleLiveStreamSearch.getIsRecorded()).isEqualTo(UPDATED_IS_RECORDED);
                assertThat(testSingleLiveStreamSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testSingleLiveStreamSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSingleLiveStreamSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSingleLiveStreamSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSingleLiveStreamSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSingleLiveStreamSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSingleLiveStreamWithPatch() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();

        // Update the singleLiveStream using partial update
        SingleLiveStream partialUpdatedSingleLiveStream = new SingleLiveStream();
        partialUpdatedSingleLiveStream.setId(singleLiveStream.getId());

        partialUpdatedSingleLiveStream
            .description(UPDATED_DESCRIPTION)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .endTime(UPDATED_END_TIME)
            .isRecorded(UPDATED_IS_RECORDED)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleLiveStream.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleLiveStream))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSingleLiveStream.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleLiveStream.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testSingleLiveStream.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSingleLiveStream.getLiveContent()).isEqualTo(DEFAULT_LIVE_CONTENT);
        assertThat(testSingleLiveStream.getLiveContentContentType()).isEqualTo(DEFAULT_LIVE_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getLiveContentS3Key()).isEqualTo(DEFAULT_LIVE_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getIsRecorded()).isEqualTo(UPDATED_IS_RECORDED);
        assertThat(testSingleLiveStream.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateSingleLiveStreamWithPatch() throws Exception {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();

        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();

        // Update the singleLiveStream using partial update
        SingleLiveStream partialUpdatedSingleLiveStream = new SingleLiveStream();
        partialUpdatedSingleLiveStream.setId(singleLiveStream.getId());

        partialUpdatedSingleLiveStream
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .liveContent(UPDATED_LIVE_CONTENT)
            .liveContentContentType(UPDATED_LIVE_CONTENT_CONTENT_TYPE)
            .liveContentS3Key(UPDATED_LIVE_CONTENT_S_3_KEY)
            .isRecorded(UPDATED_IS_RECORDED)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleLiveStream.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleLiveStream))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        SingleLiveStream testSingleLiveStream = singleLiveStreamList.get(singleLiveStreamList.size() - 1);
        assertThat(testSingleLiveStream.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSingleLiveStream.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleLiveStream.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleLiveStream.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleLiveStream.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testSingleLiveStream.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testSingleLiveStream.getLiveContent()).isEqualTo(UPDATED_LIVE_CONTENT);
        assertThat(testSingleLiveStream.getLiveContentContentType()).isEqualTo(UPDATED_LIVE_CONTENT_CONTENT_TYPE);
        assertThat(testSingleLiveStream.getLiveContentS3Key()).isEqualTo(UPDATED_LIVE_CONTENT_S_3_KEY);
        assertThat(testSingleLiveStream.getIsRecorded()).isEqualTo(UPDATED_IS_RECORDED);
        assertThat(testSingleLiveStream.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleLiveStream.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleLiveStream.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleLiveStream.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleLiveStream.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleLiveStream.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singleLiveStreamDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSingleLiveStream() throws Exception {
        int databaseSizeBeforeUpdate = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        singleLiveStream.setId(longCount.incrementAndGet());

        // Create the SingleLiveStream
        SingleLiveStreamDTO singleLiveStreamDTO = singleLiveStreamMapper.toDto(singleLiveStream);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleLiveStreamDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleLiveStream in the database
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSingleLiveStream() {
        // Initialize the database
        singleLiveStreamRepository.save(singleLiveStream).block();
        singleLiveStreamRepository.save(singleLiveStream).block();
        singleLiveStreamSearchRepository.save(singleLiveStream).block();

        int databaseSizeBeforeDelete = singleLiveStreamRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the singleLiveStream
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singleLiveStream.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SingleLiveStream> singleLiveStreamList = singleLiveStreamRepository.findAll().collectList().block();
        assertThat(singleLiveStreamList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleLiveStreamSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSingleLiveStream() {
        // Initialize the database
        singleLiveStream = singleLiveStreamRepository.save(singleLiveStream).block();
        singleLiveStreamSearchRepository.save(singleLiveStream).block();

        // Search the singleLiveStream
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + singleLiveStream.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(singleLiveStream.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].startTime")
            .value(hasItem(DEFAULT_START_TIME.toString()))
            .jsonPath("$.[*].endTime")
            .value(hasItem(DEFAULT_END_TIME.toString()))
            .jsonPath("$.[*].liveContentContentType")
            .value(hasItem(DEFAULT_LIVE_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].liveContent")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LIVE_CONTENT)))
            .jsonPath("$.[*].liveContentS3Key")
            .value(hasItem(DEFAULT_LIVE_CONTENT_S_3_KEY))
            .jsonPath("$.[*].isRecorded")
            .value(hasItem(DEFAULT_IS_RECORDED.booleanValue()))
            .jsonPath("$.[*].likeCount")
            .value(hasItem(DEFAULT_LIKE_COUNT.intValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
