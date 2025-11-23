package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.PostComment;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PostCommentRepository;
import com.fanflip.admin.repository.search.PostCommentSearchRepository;
import com.fanflip.admin.service.PostCommentService;
import com.fanflip.admin.service.dto.PostCommentDTO;
import com.fanflip.admin.service.mapper.PostCommentMapper;
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
 * Integration tests for the {@link PostCommentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PostCommentResourceIT {

    private static final String DEFAULT_COMMENT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_CONTENT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/post-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/post-comments/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Mock
    private PostCommentRepository postCommentRepositoryMock;

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Mock
    private PostCommentService postCommentServiceMock;

    @Autowired
    private PostCommentSearchRepository postCommentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PostComment postComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostComment createEntity(EntityManager em) {
        PostComment postComment = new PostComment()
            .commentContent(DEFAULT_COMMENT_CONTENT)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createEntity(em)).block();
        postComment.setPost(postFeed);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        postComment.setCommenter(userProfile);
        return postComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostComment createUpdatedEntity(EntityManager em) {
        PostComment postComment = new PostComment()
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createUpdatedEntity(em)).block();
        postComment.setPost(postFeed);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        postComment.setCommenter(userProfile);
        return postComment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PostComment.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PostFeedResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        postCommentSearchRepository.deleteAll().block();
        assertThat(postCommentSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        postComment = createEntity(em);
    }

    @Test
    void createPostComment() throws Exception {
        int databaseSizeBeforeCreate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(DEFAULT_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPostCommentWithExistingId() throws Exception {
        // Create the PostComment with an existing ID
        postComment.setId(1L);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        int databaseSizeBeforeCreate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        // set the field null
        postComment.setCreatedDate(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        // set the field null
        postComment.setIsDeleted(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPostComments() {
        // Initialize the database
        postCommentRepository.save(postComment).block();

        // Get all the postCommentList
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
            .value(hasItem(postComment.getId().intValue()))
            .jsonPath("$.[*].commentContent")
            .value(hasItem(DEFAULT_COMMENT_CONTENT.toString()))
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
    void getAllPostCommentsWithEagerRelationshipsIsEnabled() {
        when(postCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(postCommentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostCommentsWithEagerRelationshipsIsNotEnabled() {
        when(postCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(postCommentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPostComment() {
        // Initialize the database
        postCommentRepository.save(postComment).block();

        // Get the postComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, postComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(postComment.getId().intValue()))
            .jsonPath("$.commentContent")
            .value(is(DEFAULT_COMMENT_CONTENT.toString()))
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
    void getNonExistingPostComment() {
        // Get the postComment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPostComment() throws Exception {
        // Initialize the database
        postCommentRepository.save(postComment).block();

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        postCommentSearchRepository.save(postComment).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());

        // Update the postComment
        PostComment updatedPostComment = postCommentRepository.findById(postComment.getId()).block();
        updatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(updatedPostComment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postCommentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PostComment> postCommentSearchList = IterableUtils.toList(postCommentSearchRepository.findAll().collectList().block());
                PostComment testPostCommentSearch = postCommentSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPostCommentSearch.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
                assertThat(testPostCommentSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testPostCommentSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPostCommentSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPostCommentSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPostCommentSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPostCommentSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postCommentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        postCommentRepository.save(postComment).block();

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();

        // Update the postComment using partial update
        PostComment partialUpdatedPostComment = new PostComment();
        partialUpdatedPostComment.setId(postComment.getId());

        partialUpdatedPostComment.commentContent(UPDATED_COMMENT_CONTENT).likeCount(UPDATED_LIKE_COUNT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        postCommentRepository.save(postComment).block();

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();

        // Update the postComment using partial update
        PostComment partialUpdatedPostComment = new PostComment();
        partialUpdatedPostComment.setId(postComment.getId());

        partialUpdatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostComment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, postCommentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePostComment() {
        // Initialize the database
        postCommentRepository.save(postComment).block();
        postCommentRepository.save(postComment).block();
        postCommentSearchRepository.save(postComment).block();

        int databaseSizeBeforeDelete = postCommentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the postComment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, postComment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PostComment> postCommentList = postCommentRepository.findAll().collectList().block();
        assertThat(postCommentList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postCommentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPostComment() {
        // Initialize the database
        postComment = postCommentRepository.save(postComment).block();
        postCommentSearchRepository.save(postComment).block();

        // Search the postComment
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + postComment.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(postComment.getId().intValue()))
            .jsonPath("$.[*].commentContent")
            .value(hasItem(DEFAULT_COMMENT_CONTENT.toString()))
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
