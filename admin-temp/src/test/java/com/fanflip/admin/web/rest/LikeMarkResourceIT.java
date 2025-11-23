package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.LikeMark;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.LikeMarkRepository;
import com.fanflip.admin.repository.search.LikeMarkSearchRepository;
import com.fanflip.admin.service.dto.LikeMarkDTO;
import com.fanflip.admin.service.mapper.LikeMarkMapper;
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
 * Integration tests for the {@link LikeMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LikeMarkResourceIT {

    private static final Long DEFAULT_EMOJI_TYPE_ID = 1L;
    private static final Long UPDATED_EMOJI_TYPE_ID = 2L;

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

    private static final Long DEFAULT_MULTIMEDIA_ID = 1L;
    private static final Long UPDATED_MULTIMEDIA_ID = 2L;

    private static final Long DEFAULT_MESSAGE_ID = 1L;
    private static final Long UPDATED_MESSAGE_ID = 2L;

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Long DEFAULT_COMMENT_ID = 1L;
    private static final Long UPDATED_COMMENT_ID = 2L;

    private static final Long DEFAULT_LIKER_USER_ID = 1L;
    private static final Long UPDATED_LIKER_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/like-marks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/like-marks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeMarkRepository likeMarkRepository;

    @Autowired
    private LikeMarkMapper likeMarkMapper;

    @Autowired
    private LikeMarkSearchRepository likeMarkSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private LikeMark likeMark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMark createEntity(EntityManager em) {
        LikeMark likeMark = new LikeMark()
            .emojiTypeId(DEFAULT_EMOJI_TYPE_ID)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .multimediaId(DEFAULT_MULTIMEDIA_ID)
            .messageId(DEFAULT_MESSAGE_ID)
            .postId(DEFAULT_POST_ID)
            .commentId(DEFAULT_COMMENT_ID)
            .likerUserId(DEFAULT_LIKER_USER_ID);
        return likeMark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMark createUpdatedEntity(EntityManager em) {
        LikeMark likeMark = new LikeMark()
            .emojiTypeId(UPDATED_EMOJI_TYPE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerUserId(UPDATED_LIKER_USER_ID);
        return likeMark;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(LikeMark.class).block();
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
        likeMarkSearchRepository.deleteAll().block();
        assertThat(likeMarkSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        likeMark = createEntity(em);
    }

    @Test
    void createLikeMark() throws Exception {
        int databaseSizeBeforeCreate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        LikeMark testLikeMark = likeMarkList.get(likeMarkList.size() - 1);
        assertThat(testLikeMark.getEmojiTypeId()).isEqualTo(DEFAULT_EMOJI_TYPE_ID);
        assertThat(testLikeMark.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testLikeMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testLikeMark.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLikeMark.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testLikeMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testLikeMark.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testLikeMark.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testLikeMark.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testLikeMark.getCommentId()).isEqualTo(DEFAULT_COMMENT_ID);
        assertThat(testLikeMark.getLikerUserId()).isEqualTo(DEFAULT_LIKER_USER_ID);
    }

    @Test
    void createLikeMarkWithExistingId() throws Exception {
        // Create the LikeMark with an existing ID
        likeMark.setId(1L);
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        int databaseSizeBeforeCreate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEmojiTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        // set the field null
        likeMark.setEmojiTypeId(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        // set the field null
        likeMark.setCreatedDate(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        // set the field null
        likeMark.setIsDeleted(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllLikeMarks() {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();

        // Get all the likeMarkList
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
            .value(hasItem(likeMark.getId().intValue()))
            .jsonPath("$.[*].emojiTypeId")
            .value(hasItem(DEFAULT_EMOJI_TYPE_ID.intValue()))
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
            .jsonPath("$.[*].multimediaId")
            .value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.[*].messageId")
            .value(hasItem(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].postId")
            .value(hasItem(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.[*].commentId")
            .value(hasItem(DEFAULT_COMMENT_ID.intValue()))
            .jsonPath("$.[*].likerUserId")
            .value(hasItem(DEFAULT_LIKER_USER_ID.intValue()));
    }

    @Test
    void getLikeMark() {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();

        // Get the likeMark
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, likeMark.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(likeMark.getId().intValue()))
            .jsonPath("$.emojiTypeId")
            .value(is(DEFAULT_EMOJI_TYPE_ID.intValue()))
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
            .jsonPath("$.multimediaId")
            .value(is(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.messageId")
            .value(is(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.postId")
            .value(is(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.commentId")
            .value(is(DEFAULT_COMMENT_ID.intValue()))
            .jsonPath("$.likerUserId")
            .value(is(DEFAULT_LIKER_USER_ID.intValue()));
    }

    @Test
    void getNonExistingLikeMark() {
        // Get the likeMark
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLikeMark() throws Exception {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        likeMarkSearchRepository.save(likeMark).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());

        // Update the likeMark
        LikeMark updatedLikeMark = likeMarkRepository.findById(likeMark.getId()).block();
        updatedLikeMark
            .emojiTypeId(UPDATED_EMOJI_TYPE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerUserId(UPDATED_LIKER_USER_ID);
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(updatedLikeMark);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, likeMarkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        LikeMark testLikeMark = likeMarkList.get(likeMarkList.size() - 1);
        assertThat(testLikeMark.getEmojiTypeId()).isEqualTo(UPDATED_EMOJI_TYPE_ID);
        assertThat(testLikeMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLikeMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testLikeMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikeMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLikeMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testLikeMark.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
        assertThat(testLikeMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testLikeMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testLikeMark.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
        assertThat(testLikeMark.getLikerUserId()).isEqualTo(UPDATED_LIKER_USER_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<LikeMark> likeMarkSearchList = IterableUtils.toList(likeMarkSearchRepository.findAll().collectList().block());
                LikeMark testLikeMarkSearch = likeMarkSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testLikeMarkSearch.getEmojiTypeId()).isEqualTo(UPDATED_EMOJI_TYPE_ID);
                assertThat(testLikeMarkSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testLikeMarkSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testLikeMarkSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testLikeMarkSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testLikeMarkSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testLikeMarkSearch.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
                assertThat(testLikeMarkSearch.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
                assertThat(testLikeMarkSearch.getPostId()).isEqualTo(UPDATED_POST_ID);
                assertThat(testLikeMarkSearch.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
                assertThat(testLikeMarkSearch.getLikerUserId()).isEqualTo(UPDATED_LIKER_USER_ID);
            });
    }

    @Test
    void putNonExistingLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, likeMarkDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();

        // Update the likeMark using partial update
        LikeMark partialUpdatedLikeMark = new LikeMark();
        partialUpdatedLikeMark.setId(likeMark.getId());

        partialUpdatedLikeMark
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMark))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        LikeMark testLikeMark = likeMarkList.get(likeMarkList.size() - 1);
        assertThat(testLikeMark.getEmojiTypeId()).isEqualTo(DEFAULT_EMOJI_TYPE_ID);
        assertThat(testLikeMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLikeMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testLikeMark.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testLikeMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLikeMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testLikeMark.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testLikeMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testLikeMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testLikeMark.getCommentId()).isEqualTo(DEFAULT_COMMENT_ID);
        assertThat(testLikeMark.getLikerUserId()).isEqualTo(DEFAULT_LIKER_USER_ID);
    }

    @Test
    void fullUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();

        // Update the likeMark using partial update
        LikeMark partialUpdatedLikeMark = new LikeMark();
        partialUpdatedLikeMark.setId(likeMark.getId());

        partialUpdatedLikeMark
            .emojiTypeId(UPDATED_EMOJI_TYPE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .multimediaId(UPDATED_MULTIMEDIA_ID)
            .messageId(UPDATED_MESSAGE_ID)
            .postId(UPDATED_POST_ID)
            .commentId(UPDATED_COMMENT_ID)
            .likerUserId(UPDATED_LIKER_USER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMark))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        LikeMark testLikeMark = likeMarkList.get(likeMarkList.size() - 1);
        assertThat(testLikeMark.getEmojiTypeId()).isEqualTo(UPDATED_EMOJI_TYPE_ID);
        assertThat(testLikeMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLikeMark.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testLikeMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikeMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLikeMark.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testLikeMark.getMultimediaId()).isEqualTo(UPDATED_MULTIMEDIA_ID);
        assertThat(testLikeMark.getMessageId()).isEqualTo(UPDATED_MESSAGE_ID);
        assertThat(testLikeMark.getPostId()).isEqualTo(UPDATED_POST_ID);
        assertThat(testLikeMark.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
        assertThat(testLikeMark.getLikerUserId()).isEqualTo(UPDATED_LIKER_USER_ID);
    }

    @Test
    void patchNonExistingLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, likeMarkDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteLikeMark() {
        // Initialize the database
        likeMarkRepository.save(likeMark).block();
        likeMarkRepository.save(likeMark).block();
        likeMarkSearchRepository.save(likeMark).block();

        int databaseSizeBeforeDelete = likeMarkRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the likeMark
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, likeMark.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<LikeMark> likeMarkList = likeMarkRepository.findAll().collectList().block();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(likeMarkSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchLikeMark() {
        // Initialize the database
        likeMark = likeMarkRepository.save(likeMark).block();
        likeMarkSearchRepository.save(likeMark).block();

        // Search the likeMark
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + likeMark.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(likeMark.getId().intValue()))
            .jsonPath("$.[*].emojiTypeId")
            .value(hasItem(DEFAULT_EMOJI_TYPE_ID.intValue()))
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
            .jsonPath("$.[*].multimediaId")
            .value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue()))
            .jsonPath("$.[*].messageId")
            .value(hasItem(DEFAULT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].postId")
            .value(hasItem(DEFAULT_POST_ID.intValue()))
            .jsonPath("$.[*].commentId")
            .value(hasItem(DEFAULT_COMMENT_ID.intValue()))
            .jsonPath("$.[*].likerUserId")
            .value(hasItem(DEFAULT_LIKER_USER_ID.intValue()));
    }
}
