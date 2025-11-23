package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SinglePhoto;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SinglePhotoRepository;
import com.fanflip.admin.repository.search.SinglePhotoSearchRepository;
import com.fanflip.admin.service.SinglePhotoService;
import com.fanflip.admin.service.dto.SinglePhotoDTO;
import com.fanflip.admin.service.mapper.SinglePhotoMapper;
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
 * Integration tests for the {@link SinglePhotoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SinglePhotoResourceIT {

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

    private static final String ENTITY_API_URL = "/api/single-photos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/single-photos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SinglePhotoRepository singlePhotoRepository;

    @Mock
    private SinglePhotoRepository singlePhotoRepositoryMock;

    @Autowired
    private SinglePhotoMapper singlePhotoMapper;

    @Mock
    private SinglePhotoService singlePhotoServiceMock;

    @Autowired
    private SinglePhotoSearchRepository singlePhotoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SinglePhoto singlePhoto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createEntity(EntityManager em) {
        SinglePhoto singlePhoto = new SinglePhoto()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .contentS3Key(DEFAULT_CONTENT_S_3_KEY)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return singlePhoto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SinglePhoto createUpdatedEntity(EntityManager em) {
        SinglePhoto singlePhoto = new SinglePhoto()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return singlePhoto;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SinglePhoto.class).block();
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
        singlePhotoSearchRepository.deleteAll().block();
        assertThat(singlePhotoSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        singlePhoto = createEntity(em);
    }

    @Test
    void createSinglePhoto() throws Exception {
        int databaseSizeBeforeCreate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSinglePhotoWithExistingId() throws Exception {
        // Create the SinglePhoto with an existing ID
        singlePhoto.setId(1L);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        int databaseSizeBeforeCreate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkThumbnailS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        // set the field null
        singlePhoto.setThumbnailS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        // set the field null
        singlePhoto.setContentS3Key(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        // set the field null
        singlePhoto.setCreatedDate(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        // set the field null
        singlePhoto.setIsDeleted(null);

        // Create the SinglePhoto, which fails.
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSinglePhotos() {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();

        // Get all the singlePhotoList
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
            .value(hasItem(singlePhoto.getId().intValue()))
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
    void getAllSinglePhotosWithEagerRelationshipsIsEnabled() {
        when(singlePhotoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(singlePhotoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSinglePhotosWithEagerRelationshipsIsNotEnabled() {
        when(singlePhotoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(singlePhotoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSinglePhoto() {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();

        // Get the singlePhoto
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singlePhoto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singlePhoto.getId().intValue()))
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
    void getNonExistingSinglePhoto() {
        // Get the singlePhoto
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSinglePhoto() throws Exception {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        singlePhotoSearchRepository.save(singlePhoto).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());

        // Update the singlePhoto
        SinglePhoto updatedSinglePhoto = singlePhotoRepository.findById(singlePhoto.getId()).block();
        updatedSinglePhoto
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(updatedSinglePhoto);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singlePhotoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SinglePhoto> singlePhotoSearchList = IterableUtils.toList(singlePhotoSearchRepository.findAll().collectList().block());
                SinglePhoto testSinglePhotoSearch = singlePhotoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSinglePhotoSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testSinglePhotoSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testSinglePhotoSearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testSinglePhotoSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testSinglePhotoSearch.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
                assertThat(testSinglePhotoSearch.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
                assertThat(testSinglePhotoSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testSinglePhotoSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSinglePhotoSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSinglePhotoSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSinglePhotoSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSinglePhotoSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singlePhotoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSinglePhoto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(DEFAULT_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateSinglePhotoWithPatch() throws Exception {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();

        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();

        // Update the singlePhoto using partial update
        SinglePhoto partialUpdatedSinglePhoto = new SinglePhoto();
        partialUpdatedSinglePhoto.setId(singlePhoto.getId());

        partialUpdatedSinglePhoto
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .contentS3Key(UPDATED_CONTENT_S_3_KEY)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSinglePhoto.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSinglePhoto))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        SinglePhoto testSinglePhoto = singlePhotoList.get(singlePhotoList.size() - 1);
        assertThat(testSinglePhoto.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testSinglePhoto.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testSinglePhoto.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testSinglePhoto.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testSinglePhoto.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testSinglePhoto.getContentS3Key()).isEqualTo(UPDATED_CONTENT_S_3_KEY);
        assertThat(testSinglePhoto.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testSinglePhoto.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSinglePhoto.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSinglePhoto.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSinglePhoto.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSinglePhoto.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singlePhotoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSinglePhoto() throws Exception {
        int databaseSizeBeforeUpdate = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        singlePhoto.setId(longCount.incrementAndGet());

        // Create the SinglePhoto
        SinglePhotoDTO singlePhotoDTO = singlePhotoMapper.toDto(singlePhoto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singlePhotoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SinglePhoto in the database
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSinglePhoto() {
        // Initialize the database
        singlePhotoRepository.save(singlePhoto).block();
        singlePhotoRepository.save(singlePhoto).block();
        singlePhotoSearchRepository.save(singlePhoto).block();

        int databaseSizeBeforeDelete = singlePhotoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the singlePhoto
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singlePhoto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SinglePhoto> singlePhotoList = singlePhotoRepository.findAll().collectList().block();
        assertThat(singlePhotoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singlePhotoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSinglePhoto() {
        // Initialize the database
        singlePhoto = singlePhotoRepository.save(singlePhoto).block();
        singlePhotoSearchRepository.save(singlePhoto).block();

        // Search the singlePhoto
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + singlePhoto.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(singlePhoto.getId().intValue()))
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
