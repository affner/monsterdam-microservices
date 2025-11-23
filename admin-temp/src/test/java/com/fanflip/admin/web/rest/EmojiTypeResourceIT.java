package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.EmojiType;
import com.fanflip.admin.repository.EmojiTypeRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.EmojiTypeSearchRepository;
import com.fanflip.admin.service.dto.EmojiTypeDTO;
import com.fanflip.admin.service.mapper.EmojiTypeMapper;
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
 * Integration tests for the {@link EmojiTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class EmojiTypeResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/emoji-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/emoji-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmojiTypeRepository emojiTypeRepository;

    @Autowired
    private EmojiTypeMapper emojiTypeMapper;

    @Autowired
    private EmojiTypeSearchRepository emojiTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private EmojiType emojiType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmojiType createEntity(EntityManager em) {
        EmojiType emojiType = new EmojiType()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return emojiType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmojiType createUpdatedEntity(EntityManager em) {
        EmojiType emojiType = new EmojiType()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return emojiType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(EmojiType.class).block();
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
        emojiTypeSearchRepository.deleteAll().block();
        assertThat(emojiTypeSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        emojiType = createEntity(em);
    }

    @Test
    void createEmojiType() throws Exception {
        int databaseSizeBeforeCreate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createEmojiTypeWithExistingId() throws Exception {
        // Create the EmojiType with an existing ID
        emojiType.setId(1L);
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        int databaseSizeBeforeCreate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        // set the field null
        emojiType.setDescription(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        // set the field null
        emojiType.setCreatedDate(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        // set the field null
        emojiType.setIsDeleted(null);

        // Create the EmojiType, which fails.
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllEmojiTypes() {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();

        // Get all the emojiTypeList
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
            .value(hasItem(emojiType.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
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
    void getEmojiType() {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();

        // Get the emojiType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, emojiType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(emojiType.getId().intValue()))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
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
    void getNonExistingEmojiType() {
        // Get the emojiType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingEmojiType() throws Exception {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        emojiTypeSearchRepository.save(emojiType).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());

        // Update the emojiType
        EmojiType updatedEmojiType = emojiTypeRepository.findById(emojiType.getId()).block();
        updatedEmojiType
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(updatedEmojiType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, emojiTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<EmojiType> emojiTypeSearchList = IterableUtils.toList(emojiTypeSearchRepository.findAll().collectList().block());
                EmojiType testEmojiTypeSearch = emojiTypeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testEmojiTypeSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testEmojiTypeSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testEmojiTypeSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testEmojiTypeSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testEmojiTypeSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testEmojiTypeSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testEmojiTypeSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testEmojiTypeSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, emojiTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateEmojiTypeWithPatch() throws Exception {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();

        // Update the emojiType using partial update
        EmojiType partialUpdatedEmojiType = new EmojiType();
        partialUpdatedEmojiType.setId(emojiType.getId());

        partialUpdatedEmojiType.createdBy(UPDATED_CREATED_BY).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmojiType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmojiType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateEmojiTypeWithPatch() throws Exception {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();

        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();

        // Update the emojiType using partial update
        EmojiType partialUpdatedEmojiType = new EmojiType();
        partialUpdatedEmojiType.setId(emojiType.getId());

        partialUpdatedEmojiType
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedEmojiType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedEmojiType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        EmojiType testEmojiType = emojiTypeList.get(emojiTypeList.size() - 1);
        assertThat(testEmojiType.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testEmojiType.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testEmojiType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmojiType.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testEmojiType.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testEmojiType.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmojiType.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testEmojiType.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, emojiTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamEmojiType() throws Exception {
        int databaseSizeBeforeUpdate = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        emojiType.setId(longCount.incrementAndGet());

        // Create the EmojiType
        EmojiTypeDTO emojiTypeDTO = emojiTypeMapper.toDto(emojiType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(emojiTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the EmojiType in the database
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteEmojiType() {
        // Initialize the database
        emojiTypeRepository.save(emojiType).block();
        emojiTypeRepository.save(emojiType).block();
        emojiTypeSearchRepository.save(emojiType).block();

        int databaseSizeBeforeDelete = emojiTypeRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the emojiType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, emojiType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<EmojiType> emojiTypeList = emojiTypeRepository.findAll().collectList().block();
        assertThat(emojiTypeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(emojiTypeSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchEmojiType() {
        // Initialize the database
        emojiType = emojiTypeRepository.save(emojiType).block();
        emojiTypeSearchRepository.save(emojiType).block();

        // Search the emojiType
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + emojiType.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(emojiType.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
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
