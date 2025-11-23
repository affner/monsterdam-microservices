package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PostFeedRepository;
import com.fanflip.admin.repository.search.PostFeedSearchRepository;
import com.fanflip.admin.service.PostFeedService;
import com.fanflip.admin.service.dto.PostFeedDTO;
import com.fanflip.admin.service.mapper.PostFeedMapper;
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
 * Integration tests for the {@link PostFeedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PostFeedResourceIT {

    private static final String DEFAULT_POST_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_POST_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    private static final Boolean DEFAULT_PINNED_POST = false;
    private static final Boolean UPDATED_PINNED_POST = true;

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

    private static final String ENTITY_API_URL = "/api/post-feeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/post-feeds/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostFeedRepository postFeedRepository;

    @Mock
    private PostFeedRepository postFeedRepositoryMock;

    @Autowired
    private PostFeedMapper postFeedMapper;

    @Mock
    private PostFeedService postFeedServiceMock;

    @Autowired
    private PostFeedSearchRepository postFeedSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PostFeed postFeed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeed createEntity(EntityManager em) {
        PostFeed postFeed = new PostFeed()
            .postContent(DEFAULT_POST_CONTENT)
            .isHidden(DEFAULT_IS_HIDDEN)
            .pinnedPost(DEFAULT_PINNED_POST)
            .likeCount(DEFAULT_LIKE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        postFeed.setCreator(userProfile);
        return postFeed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostFeed createUpdatedEntity(EntityManager em) {
        PostFeed postFeed = new PostFeed()
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        postFeed.setCreator(userProfile);
        return postFeed;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_post_feed__hash_tags").block();
            em.deleteAll(PostFeed.class).block();
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
        postFeedSearchRepository.deleteAll().block();
        assertThat(postFeedSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        postFeed = createEntity(em);
    }

    @Test
    void createPostFeed() throws Exception {
        int databaseSizeBeforeCreate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PostFeed testPostFeed = postFeedList.get(postFeedList.size() - 1);
        assertThat(testPostFeed.getPostContent()).isEqualTo(DEFAULT_POST_CONTENT);
        assertThat(testPostFeed.getIsHidden()).isEqualTo(DEFAULT_IS_HIDDEN);
        assertThat(testPostFeed.getPinnedPost()).isEqualTo(DEFAULT_PINNED_POST);
        assertThat(testPostFeed.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostFeed.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostFeed.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostFeed.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostFeed.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostFeed.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPostFeedWithExistingId() throws Exception {
        // Create the PostFeed with an existing ID
        postFeed.setId(1L);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        int databaseSizeBeforeCreate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        // set the field null
        postFeed.setCreatedDate(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        // set the field null
        postFeed.setIsDeleted(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPostFeeds() {
        // Initialize the database
        postFeedRepository.save(postFeed).block();

        // Get all the postFeedList
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
            .value(hasItem(postFeed.getId().intValue()))
            .jsonPath("$.[*].postContent")
            .value(hasItem(DEFAULT_POST_CONTENT.toString()))
            .jsonPath("$.[*].isHidden")
            .value(hasItem(DEFAULT_IS_HIDDEN.booleanValue()))
            .jsonPath("$.[*].pinnedPost")
            .value(hasItem(DEFAULT_PINNED_POST.booleanValue()))
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
    void getAllPostFeedsWithEagerRelationshipsIsEnabled() {
        when(postFeedServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(postFeedServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostFeedsWithEagerRelationshipsIsNotEnabled() {
        when(postFeedServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(postFeedRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPostFeed() {
        // Initialize the database
        postFeedRepository.save(postFeed).block();

        // Get the postFeed
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, postFeed.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(postFeed.getId().intValue()))
            .jsonPath("$.postContent")
            .value(is(DEFAULT_POST_CONTENT.toString()))
            .jsonPath("$.isHidden")
            .value(is(DEFAULT_IS_HIDDEN.booleanValue()))
            .jsonPath("$.pinnedPost")
            .value(is(DEFAULT_PINNED_POST.booleanValue()))
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
    void getNonExistingPostFeed() {
        // Get the postFeed
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPostFeed() throws Exception {
        // Initialize the database
        postFeedRepository.save(postFeed).block();

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        postFeedSearchRepository.save(postFeed).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());

        // Update the postFeed
        PostFeed updatedPostFeed = postFeedRepository.findById(postFeed.getId()).block();
        updatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(updatedPostFeed);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postFeedDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        PostFeed testPostFeed = postFeedList.get(postFeedList.size() - 1);
        assertThat(testPostFeed.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
        assertThat(testPostFeed.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testPostFeed.getPinnedPost()).isEqualTo(UPDATED_PINNED_POST);
        assertThat(testPostFeed.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostFeed.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeed.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostFeed.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostFeed.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeed.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PostFeed> postFeedSearchList = IterableUtils.toList(postFeedSearchRepository.findAll().collectList().block());
                PostFeed testPostFeedSearch = postFeedSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPostFeedSearch.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
                assertThat(testPostFeedSearch.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
                assertThat(testPostFeedSearch.getPinnedPost()).isEqualTo(UPDATED_PINNED_POST);
                assertThat(testPostFeedSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
                assertThat(testPostFeedSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPostFeedSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPostFeedSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPostFeedSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPostFeedSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postFeedDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        postFeedRepository.save(postFeed).block();

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();

        // Update the postFeed using partial update
        PostFeed partialUpdatedPostFeed = new PostFeed();
        partialUpdatedPostFeed.setId(postFeed.getId());

        partialUpdatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeed))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        PostFeed testPostFeed = postFeedList.get(postFeedList.size() - 1);
        assertThat(testPostFeed.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
        assertThat(testPostFeed.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testPostFeed.getPinnedPost()).isEqualTo(DEFAULT_PINNED_POST);
        assertThat(testPostFeed.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostFeed.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeed.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostFeed.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostFeed.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostFeed.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        postFeedRepository.save(postFeed).block();

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();

        // Update the postFeed using partial update
        PostFeed partialUpdatedPostFeed = new PostFeed();
        partialUpdatedPostFeed.setId(postFeed.getId());

        partialUpdatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeed))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        PostFeed testPostFeed = postFeedList.get(postFeedList.size() - 1);
        assertThat(testPostFeed.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
        assertThat(testPostFeed.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testPostFeed.getPinnedPost()).isEqualTo(UPDATED_PINNED_POST);
        assertThat(testPostFeed.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostFeed.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeed.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostFeed.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostFeed.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeed.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, postFeedDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePostFeed() {
        // Initialize the database
        postFeedRepository.save(postFeed).block();
        postFeedRepository.save(postFeed).block();
        postFeedSearchRepository.save(postFeed).block();

        int databaseSizeBeforeDelete = postFeedRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the postFeed
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, postFeed.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PostFeed> postFeedList = postFeedRepository.findAll().collectList().block();
        assertThat(postFeedList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPostFeed() {
        // Initialize the database
        postFeed = postFeedRepository.save(postFeed).block();
        postFeedSearchRepository.save(postFeed).block();

        // Search the postFeed
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + postFeed.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(postFeed.getId().intValue()))
            .jsonPath("$.[*].postContent")
            .value(hasItem(DEFAULT_POST_CONTENT.toString()))
            .jsonPath("$.[*].isHidden")
            .value(hasItem(DEFAULT_IS_HIDDEN.booleanValue()))
            .jsonPath("$.[*].pinnedPost")
            .value(hasItem(DEFAULT_PINNED_POST.booleanValue()))
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
