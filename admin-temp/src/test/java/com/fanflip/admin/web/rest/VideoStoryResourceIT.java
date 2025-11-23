package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.VideoStory;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.VideoStoryRepository;
import com.fanflip.admin.repository.search.VideoStorySearchRepository;
import com.fanflip.admin.service.dto.VideoStoryDTO;
import com.fanflip.admin.service.mapper.VideoStoryMapper;
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
 * Integration tests for the {@link VideoStoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class VideoStoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/video-stories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/video-stories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideoStoryRepository videoStoryRepository;

    @Autowired
    private VideoStoryMapper videoStoryMapper;

    @Autowired
    private VideoStorySearchRepository videoStorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private VideoStory videoStory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createEntity(EntityManager em) {
        VideoStory videoStory = new VideoStory()
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
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        videoStory.setCreator(userProfile);
        return videoStory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoStory createUpdatedEntity(EntityManager em) {
        VideoStory videoStory = new VideoStory()
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
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        videoStory.setCreator(userProfile);
        return videoStory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(VideoStory.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        videoStorySearchRepository.deleteAll().block();
        assertThat(videoStorySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        videoStory = createEntity(em);
    }

    @Test
    void createVideoStory() throws Exception {
        int databaseSizeBeforeCreate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createVideoStoryWithExistingId() throws Exception {
        // Create the VideoStory with an existing ID
        videoStory.setId(1L);
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        int databaseSizeBeforeCreate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        // set the field null
        videoStory.setThumbnailS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        // set the field null
        videoStory.setContentS3Key(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        // set the field null
        videoStory.setCreatedDate(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        // set the field null
        videoStory.setIsDeleted(null);

        // Create the VideoStory, which fails.
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllVideoStories() {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();

        // Get all the videoStoryList
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
            .value(hasItem(videoStory.getId().intValue()))
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

    @Test
    void getVideoStory() {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();

        // Get the videoStory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, videoStory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(videoStory.getId().intValue()))
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
    void getNonExistingVideoStory() {
        // Get the videoStory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingVideoStory() throws Exception {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        videoStorySearchRepository.save(videoStory).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());

        // Update the videoStory
        VideoStory updatedVideoStory = videoStoryRepository.findById(videoStory.getId()).block();
        updatedVideoStory
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
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(updatedVideoStory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, videoStoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<VideoStory> videoStorySearchList = IterableUtils.toList(videoStorySearchRepository.findAll().collectList().block());
                VideoStory testVideoStorySearch = videoStorySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testVideoStorySearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testVideoStorySearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testVideoStorySearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testVideoStorySearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testVideoStorySearch.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
                assertThat(testVideoStorySearch.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
                assertThat(testVideoStorySearch.getDuration()).isEqualTo(UPDATED_DURATION);
                assertThat(testVideoStorySearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testVideoStorySearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testVideoStorySearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testVideoStorySearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testVideoStorySearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testVideoStorySearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, videoStoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoStory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateVideoStoryWithPatch() throws Exception {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();

        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();

        // Update the videoStory using partial update
        VideoStory partialUpdatedVideoStory = new VideoStory();
        partialUpdatedVideoStory.setId(videoStory.getId());

        partialUpdatedVideoStory
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
            .uri(ENTITY_API_URL_ID, partialUpdatedVideoStory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedVideoStory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        VideoStory testVideoStory = videoStoryList.get(videoStoryList.size() - 1);
        assertThat(testVideoStory.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testVideoStory.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testVideoStory.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testVideoStory.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testVideoStory.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testVideoStory.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testVideoStory.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoStory.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testVideoStory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testVideoStory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testVideoStory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testVideoStory.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testVideoStory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, videoStoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamVideoStory() throws Exception {
        int databaseSizeBeforeUpdate = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        videoStory.setId(longCount.incrementAndGet());

        // Create the VideoStory
        VideoStoryDTO videoStoryDTO = videoStoryMapper.toDto(videoStory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(videoStoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the VideoStory in the database
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteVideoStory() {
        // Initialize the database
        videoStoryRepository.save(videoStory).block();
        videoStoryRepository.save(videoStory).block();
        videoStorySearchRepository.save(videoStory).block();

        int databaseSizeBeforeDelete = videoStoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the videoStory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, videoStory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<VideoStory> videoStoryList = videoStoryRepository.findAll().collectList().block();
        assertThat(videoStoryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(videoStorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchVideoStory() {
        // Initialize the database
        videoStory = videoStoryRepository.save(videoStory).block();
        videoStorySearchRepository.save(videoStory).block();

        // Search the videoStory
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + videoStory.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(videoStory.getId().intValue()))
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
