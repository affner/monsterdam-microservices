package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SingleVideo;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SingleVideoRepository;
import com.fanflip.admin.repository.search.SingleVideoSearchRepository;
import com.fanflip.admin.service.SingleVideoService;
import com.fanflip.admin.service.dto.SingleVideoDTO;
import com.fanflip.admin.service.mapper.SingleVideoMapper;
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
 * Integration tests for the {@link SingleVideoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SingleVideoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/single-videos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/single-videos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleVideoRepository singleVideoRepository;

    @Mock
    private SingleVideoRepository singleVideoRepositoryMock;

    @Autowired
    private SingleVideoMapper singleVideoMapper;

    @Mock
    private SingleVideoService singleVideoServiceMock;

    @Autowired
    private SingleVideoSearchRepository singleVideoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SingleVideo singleVideo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createEntity(EntityManager em) {
        SingleVideo singleVideo = new SingleVideo()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .duration(DEFAULT_DURATION)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return singleVideo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleVideo createUpdatedEntity(EntityManager em) {
        SingleVideo singleVideo = new SingleVideo()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return singleVideo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SingleVideo.class).block();
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
        singleVideoSearchRepository.deleteAll().block();
        assertThat(singleVideoSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        singleVideo = createEntity(em);
    }

    @Test
    void createSingleVideo() throws Exception {
        int databaseSizeBeforeCreate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSingleVideoWithExistingId() throws Exception {
        // Create the SingleVideo with an existing ID
        singleVideo.setId(1L);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        int databaseSizeBeforeCreate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        // set the field null
        singleVideo.setThumbnailS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        // set the field null
        singleVideo.setContentS3Key(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        // set the field null
        singleVideo.setCreatedDate(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        // set the field null
        singleVideo.setIsDeleted(null);

        // Create the SingleVideo, which fails.
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSingleVideos() {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();

        // Get all the singleVideoList
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
            .value(hasItem(singleVideo.getId().intValue()))
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

    @SuppressWarnings({ "unchecked" })
    void getAllSingleVideosWithEagerRelationshipsIsEnabled() {
        when(singleVideoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(singleVideoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSingleVideosWithEagerRelationshipsIsNotEnabled() {
        when(singleVideoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(singleVideoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSingleVideo() {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();

        // Get the singleVideo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singleVideo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singleVideo.getId().intValue()))
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
    void getNonExistingSingleVideo() {
        // Get the singleVideo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSingleVideo() throws Exception {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        singleVideoSearchRepository.save(singleVideo).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());

        // Update the singleVideo
        SingleVideo updatedSingleVideo = singleVideoRepository.findById(singleVideo.getId()).block();
        updatedSingleVideo
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(updatedSingleVideo);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleVideoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SingleVideo> singleVideoSearchList = IterableUtils.toList(singleVideoSearchRepository.findAll().collectList().block());
                SingleVideo testSingleVideoSearch = singleVideoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSingleVideoSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSingleVideoSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSingleVideoSearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testSingleVideoSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testSingleVideoSearch.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
                assertThat(testSingleVideoSearch.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
                assertThat(testSingleVideoSearch.getDuration()).isEqualTo(UPDATED_DURATION);
                assertThat(testSingleVideoSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testSingleVideoSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSingleVideoSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSingleVideoSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSingleVideoSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSingleVideoSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleVideoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleVideo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateSingleVideoWithPatch() throws Exception {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();

        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();

        // Update the singleVideo using partial update
        SingleVideo partialUpdatedSingleVideo = new SingleVideo();
        partialUpdatedSingleVideo.setId(singleVideo.getId());

        partialUpdatedSingleVideo
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .duration(UPDATED_DURATION)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleVideo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleVideo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        SingleVideo testSingleVideo = singleVideoList.get(singleVideoList.size() - 1);
        assertThat(testSingleVideo.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSingleVideo.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSingleVideo.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSingleVideo.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSingleVideo.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSingleVideo.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSingleVideo.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSingleVideo.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSingleVideo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleVideo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleVideo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleVideo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleVideo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singleVideoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSingleVideo() throws Exception {
        int databaseSizeBeforeUpdate = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        singleVideo.setId(longCount.incrementAndGet());

        // Create the SingleVideo
        SingleVideoDTO singleVideoDTO = singleVideoMapper.toDto(singleVideo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleVideoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleVideo in the database
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSingleVideo() {
        // Initialize the database
        singleVideoRepository.save(singleVideo).block();
        singleVideoRepository.save(singleVideo).block();
        singleVideoSearchRepository.save(singleVideo).block();

        int databaseSizeBeforeDelete = singleVideoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the singleVideo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singleVideo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SingleVideo> singleVideoList = singleVideoRepository.findAll().collectList().block();
        assertThat(singleVideoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleVideoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSingleVideo() {
        // Initialize the database
        singleVideo = singleVideoRepository.save(singleVideo).block();
        singleVideoSearchRepository.save(singleVideo).block();

        // Search the singleVideo
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + singleVideo.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(singleVideo.getId().intValue()))
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
