package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.ContentPackage;
import com.monsterdam.admin.domain.SingleAudio;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.SingleAudioRepository;
import com.monsterdam.admin.repository.search.SingleAudioSearchRepository;
import com.monsterdam.admin.service.dto.SingleAudioDTO;
import com.monsterdam.admin.service.mapper.SingleAudioMapper;
import java.time.Duration;
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
 * Integration tests for the {@link SingleAudioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SingleAudioResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CONTENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_S_3_KEY = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final String ENTITY_API_URL = "/api/single-audios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/single-audios/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleAudioRepository singleAudioRepository;

    @Autowired
    private SingleAudioMapper singleAudioMapper;

    @Autowired
    private SingleAudioSearchRepository singleAudioSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SingleAudio singleAudio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createEntity(EntityManager em) {
        SingleAudio singleAudio = new SingleAudio()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        ContentPackage contentPackage;
        contentPackage = em.insert(ContentPackageResourceIT.createEntity(em)).block();
        singleAudio.setContentPackage(contentPackage);
        return singleAudio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleAudio createUpdatedEntity(EntityManager em) {
        SingleAudio singleAudio = new SingleAudio()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        ContentPackage contentPackage;
        contentPackage = em.insert(ContentPackageResourceIT.createUpdatedEntity(em)).block();
        singleAudio.setContentPackage(contentPackage);
        return singleAudio;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SingleAudio.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        ContentPackageResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        singleAudioSearchRepository.deleteAll().block();
        assertThat(singleAudioSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        singleAudio = createEntity(em);
    }

    @Test
    void createSingleAudio() throws Exception {
        int databaseSizeBeforeCreate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSingleAudioWithExistingId() throws Exception {
        // Create the SingleAudio with an existing ID
        singleAudio.setId(1L);
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        int databaseSizeBeforeCreate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        // set the field null
        singleAudio.setThumbnailS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        // set the field null
        singleAudio.setContentS3Key(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        // set the field null
        singleAudio.setCreatedDate(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        // set the field null
        singleAudio.setIsDeleted(null);

        // Create the SingleAudio, which fails.
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSingleAudios() {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();

        // Get all the singleAudioList
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
            .value(hasItem(singleAudio.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].contentContentType")
            .value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].content")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .jsonPath("$.[*].contentS3Key")
            .value(hasItem(DEFAULT_CONTENT_S_3_KEY))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION.toString()))
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
    void getSingleAudio() {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();

        // Get the singleAudio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singleAudio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singleAudio.getId().intValue()))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.thumbnailS3Key")
            .value(is(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.contentContentType")
            .value(is(DEFAULT_CONTENT_CONTENT_TYPE))
            .jsonPath("$.content")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .jsonPath("$.contentS3Key")
            .value(is(DEFAULT_CONTENT_S_3_KEY))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION.toString()))
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
    void getNonExistingSingleAudio() {
        // Get the singleAudio
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSingleAudio() throws Exception {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        singleAudioSearchRepository.save(singleAudio).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());

        // Update the singleAudio
        SingleAudio updatedSingleAudio = singleAudioRepository.findById(singleAudio.getId()).block();
        updatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(updatedSingleAudio);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleAudioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SingleAudio> singleAudioSearchList = IterableUtils.toList(singleAudioSearchRepository.findAll().collectList().block());
                SingleAudio testSingleAudioSearch = singleAudioSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSingleAudioSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSingleAudioSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSingleAudioSearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testSingleAudioSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testSingleAudioSearch.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
                assertThat(testSingleAudioSearch.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
                assertThat(testSingleAudioSearch.getDuration()).isEqualTo(UPDATED_DURATION);
                assertThat(testSingleAudioSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSingleAudioSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSingleAudioSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSingleAudioSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSingleAudioSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleAudioDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleAudio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateSingleAudioWithPatch() throws Exception {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();

        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();

        // Update the singleAudio using partial update
        SingleAudio partialUpdatedSingleAudio = new SingleAudio();
        partialUpdatedSingleAudio.setId(singleAudio.getId());

        partialUpdatedSingleAudio
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleAudio.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleAudio))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        SingleAudio testSingleAudio = singleAudioList.get(singleAudioList.size() - 1);
        assertThat(testSingleAudio.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleAudio.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleAudio.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleAudio.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSingleAudio.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleAudio.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleAudio.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleAudio.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleAudio.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleAudio.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleAudio.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleAudio.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singleAudioDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSingleAudio() throws Exception {
        int databaseSizeBeforeUpdate = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        singleAudio.setId(longCount.incrementAndGet());

        // Create the SingleAudio
        SingleAudioDTO singleAudioDTO = singleAudioMapper.toDto(singleAudio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleAudioDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleAudio in the database
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSingleAudio() {
        // Initialize the database
        singleAudioRepository.save(singleAudio).block();
        singleAudioRepository.save(singleAudio).block();
        singleAudioSearchRepository.save(singleAudio).block();

        int databaseSizeBeforeDelete = singleAudioRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the singleAudio
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singleAudio.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SingleAudio> singleAudioList = singleAudioRepository.findAll().collectList().block();
        assertThat(singleAudioList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleAudioSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSingleAudio() {
        // Initialize the database
        singleAudio = singleAudioRepository.save(singleAudio).block();
        singleAudioSearchRepository.save(singleAudio).block();

        // Search the singleAudio
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + singleAudio.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(singleAudio.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].contentContentType")
            .value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE))
            .jsonPath("$.[*].content")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .jsonPath("$.[*].contentS3Key")
            .value(hasItem(DEFAULT_CONTENT_S_3_KEY))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION.toString()))
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
