package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.HashTag;
import com.fanflip.admin.domain.enumeration.HashtagType;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.HashTagRepository;
import com.fanflip.admin.repository.search.HashTagSearchRepository;
import com.fanflip.admin.service.dto.HashTagDTO;
import com.fanflip.admin.service.mapper.HashTagMapper;
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
 * Integration tests for the {@link HashTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HashTagResourceIT {

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

    private static final HashtagType DEFAULT_HASHTAG_TYPE = HashtagType.USER;
    private static final HashtagType UPDATED_HASHTAG_TYPE = HashtagType.POST;

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

    private static final String ENTITY_API_URL = "/api/hash-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/hash-tags/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HashTagRepository hashTagRepository;

    @Autowired
    private HashTagMapper hashTagMapper;

    @Autowired
    private HashTagSearchRepository hashTagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HashTag hashTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createEntity(EntityManager em) {
        HashTag hashTag = new HashTag()
            .tagName(DEFAULT_TAG_NAME)
            .hashtagType(DEFAULT_HASHTAG_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return hashTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HashTag createUpdatedEntity(EntityManager em) {
        HashTag hashTag = new HashTag()
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return hashTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HashTag.class).block();
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
        hashTagSearchRepository.deleteAll().block();
        assertThat(hashTagSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        hashTag = createEntity(em);
    }

    @Test
    void createHashTag() throws Exception {
        int databaseSizeBeforeCreate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testHashTag.getHashtagType()).isEqualTo(DEFAULT_HASHTAG_TYPE);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createHashTagWithExistingId() throws Exception {
        // Create the HashTag with an existing ID
        hashTag.setId(1L);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        int databaseSizeBeforeCreate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTagNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        // set the field null
        hashTag.setTagName(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkHashtagTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        // set the field null
        hashTag.setHashtagType(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        // set the field null
        hashTag.setCreatedDate(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        // set the field null
        hashTag.setIsDeleted(null);

        // Create the HashTag, which fails.
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHashTags() {
        // Initialize the database
        hashTagRepository.save(hashTag).block();

        // Get all the hashTagList
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
            .value(hasItem(hashTag.getId().intValue()))
            .jsonPath("$.[*].tagName")
            .value(hasItem(DEFAULT_TAG_NAME))
            .jsonPath("$.[*].hashtagType")
            .value(hasItem(DEFAULT_HASHTAG_TYPE.toString()))
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
    void getHashTag() {
        // Initialize the database
        hashTagRepository.save(hashTag).block();

        // Get the hashTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, hashTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(hashTag.getId().intValue()))
            .jsonPath("$.tagName")
            .value(is(DEFAULT_TAG_NAME))
            .jsonPath("$.hashtagType")
            .value(is(DEFAULT_HASHTAG_TYPE.toString()))
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
    void getNonExistingHashTag() {
        // Get the hashTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHashTag() throws Exception {
        // Initialize the database
        hashTagRepository.save(hashTag).block();

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        hashTagSearchRepository.save(hashTag).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());

        // Update the hashTag
        HashTag updatedHashTag = hashTagRepository.findById(hashTag.getId()).block();
        updatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        HashTagDTO hashTagDTO = hashTagMapper.toDto(updatedHashTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, hashTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testHashTag.getHashtagType()).isEqualTo(UPDATED_HASHTAG_TYPE);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HashTag> hashTagSearchList = IterableUtils.toList(hashTagSearchRepository.findAll().collectList().block());
                HashTag testHashTagSearch = hashTagSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHashTagSearch.getTagName()).isEqualTo(UPDATED_TAG_NAME);
                assertThat(testHashTagSearch.getHashtagType()).isEqualTo(UPDATED_HASHTAG_TYPE);
                assertThat(testHashTagSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testHashTagSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testHashTagSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testHashTagSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testHashTagSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, hashTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        hashTagRepository.save(hashTag).block();

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHashTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(DEFAULT_TAG_NAME);
        assertThat(testHashTag.getHashtagType()).isEqualTo(UPDATED_HASHTAG_TYPE);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateHashTagWithPatch() throws Exception {
        // Initialize the database
        hashTagRepository.save(hashTag).block();

        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();

        // Update the hashTag using partial update
        HashTag partialUpdatedHashTag = new HashTag();
        partialUpdatedHashTag.setId(hashTag.getId());

        partialUpdatedHashTag
            .tagName(UPDATED_TAG_NAME)
            .hashtagType(UPDATED_HASHTAG_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHashTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHashTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        HashTag testHashTag = hashTagList.get(hashTagList.size() - 1);
        assertThat(testHashTag.getTagName()).isEqualTo(UPDATED_TAG_NAME);
        assertThat(testHashTag.getHashtagType()).isEqualTo(UPDATED_HASHTAG_TYPE);
        assertThat(testHashTag.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHashTag.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testHashTag.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testHashTag.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testHashTag.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, hashTagDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHashTag() throws Exception {
        int databaseSizeBeforeUpdate = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        hashTag.setId(longCount.incrementAndGet());

        // Create the HashTag
        HashTagDTO hashTagDTO = hashTagMapper.toDto(hashTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(hashTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HashTag in the database
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHashTag() {
        // Initialize the database
        hashTagRepository.save(hashTag).block();
        hashTagRepository.save(hashTag).block();
        hashTagSearchRepository.save(hashTag).block();

        int databaseSizeBeforeDelete = hashTagRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the hashTag
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, hashTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HashTag> hashTagList = hashTagRepository.findAll().collectList().block();
        assertThat(hashTagList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(hashTagSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHashTag() {
        // Initialize the database
        hashTag = hashTagRepository.save(hashTag).block();
        hashTagSearchRepository.save(hashTag).block();

        // Search the hashTag
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + hashTag.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(hashTag.getId().intValue()))
            .jsonPath("$.[*].tagName")
            .value(hasItem(DEFAULT_TAG_NAME))
            .jsonPath("$.[*].hashtagType")
            .value(hasItem(DEFAULT_HASHTAG_TYPE.toString()))
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
