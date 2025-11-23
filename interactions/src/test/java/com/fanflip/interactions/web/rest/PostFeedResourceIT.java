package com.fanflip.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.interactions.IntegrationTest;
import com.fanflip.interactions.domain.PostFeed;
import com.fanflip.interactions.repository.PostFeedRepository;
import com.fanflip.interactions.repository.search.PostFeedSearchRepository;
import com.fanflip.interactions.service.PostFeedService;
import com.fanflip.interactions.service.dto.PostFeedDTO;
import com.fanflip.interactions.service.mapper.PostFeedMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PostFeedResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PostFeedResourceIT {

    private static final String DEFAULT_POST_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_POST_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_HIDDEN = false;
    private static final Boolean UPDATED_IS_HIDDEN = true;

    private static final Boolean DEFAULT_PINNED_POST = false;
    private static final Boolean UPDATED_PINNED_POST = true;

    private static final Integer DEFAULT_LIKE_COUNT = 1;
    private static final Integer UPDATED_LIKE_COUNT = 2;

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

    private static final Long DEFAULT_CREATOR_USER_ID = 1L;
    private static final Long UPDATED_CREATOR_USER_ID = 2L;

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
    private MockMvc restPostFeedMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorUserId(DEFAULT_CREATOR_USER_ID);
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
            .isDeleted(UPDATED_IS_DELETED)
            .creatorUserId(UPDATED_CREATOR_USER_ID);
        return postFeed;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        postFeedSearchRepository.deleteAll();
        assertThat(postFeedSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        postFeed = createEntity(em);
    }

    @Test
    @Transactional
    void createPostFeed() throws Exception {
        int databaseSizeBeforeCreate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);
        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isCreated());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
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
        assertThat(testPostFeed.getCreatorUserId()).isEqualTo(DEFAULT_CREATOR_USER_ID);
    }

    @Test
    @Transactional
    void createPostFeedWithExistingId() throws Exception {
        // Create the PostFeed with an existing ID
        postFeed.setId(1L);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        int databaseSizeBeforeCreate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        // set the field null
        postFeed.setCreatedDate(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        // set the field null
        postFeed.setIsDeleted(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatorUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        // set the field null
        postFeed.setCreatorUserId(null);

        // Create the PostFeed, which fails.
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        restPostFeedMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isBadRequest());

        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllPostFeeds() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);

        // Get all the postFeedList
        restPostFeedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].postContent").value(hasItem(DEFAULT_POST_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].pinnedPost").value(hasItem(DEFAULT_PINNED_POST.booleanValue())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorUserId").value(hasItem(DEFAULT_CREATOR_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostFeedsWithEagerRelationshipsIsEnabled() throws Exception {
        when(postFeedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostFeedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(postFeedServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostFeedsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(postFeedServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostFeedMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(postFeedRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPostFeed() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);

        // Get the postFeed
        restPostFeedMockMvc
            .perform(get(ENTITY_API_URL_ID, postFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postFeed.getId().intValue()))
            .andExpect(jsonPath("$.postContent").value(DEFAULT_POST_CONTENT.toString()))
            .andExpect(jsonPath("$.isHidden").value(DEFAULT_IS_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.pinnedPost").value(DEFAULT_PINNED_POST.booleanValue()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorUserId").value(DEFAULT_CREATOR_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostFeed() throws Exception {
        // Get the postFeed
        restPostFeedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostFeed() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        postFeedSearchRepository.save(postFeed);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());

        // Update the postFeed
        PostFeed updatedPostFeed = postFeedRepository.findById(postFeed.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostFeed are not directly saved in db
        em.detach(updatedPostFeed);
        updatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorUserId(UPDATED_CREATOR_USER_ID);
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(updatedPostFeed);

        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
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
        assertThat(testPostFeed.getCreatorUserId()).isEqualTo(UPDATED_CREATOR_USER_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PostFeed> postFeedSearchList = IterableUtils.toList(postFeedSearchRepository.findAll());
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
                assertThat(testPostFeedSearch.getCreatorUserId()).isEqualTo(UPDATED_CREATOR_USER_ID);
            });
    }

    @Test
    @Transactional
    void putNonExistingPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postFeedDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();

        // Update the postFeed using partial update
        PostFeed partialUpdatedPostFeed = new PostFeed();
        partialUpdatedPostFeed.setId(postFeed.getId());

        partialUpdatedPostFeed
            .postContent(UPDATED_POST_CONTENT)
            .isHidden(UPDATED_IS_HIDDEN)
            .pinnedPost(UPDATED_PINNED_POST)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .creatorUserId(UPDATED_CREATOR_USER_ID);

        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeed))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        PostFeed testPostFeed = postFeedList.get(postFeedList.size() - 1);
        assertThat(testPostFeed.getPostContent()).isEqualTo(UPDATED_POST_CONTENT);
        assertThat(testPostFeed.getIsHidden()).isEqualTo(UPDATED_IS_HIDDEN);
        assertThat(testPostFeed.getPinnedPost()).isEqualTo(UPDATED_PINNED_POST);
        assertThat(testPostFeed.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostFeed.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostFeed.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostFeed.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostFeed.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostFeed.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPostFeed.getCreatorUserId()).isEqualTo(UPDATED_CREATOR_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdatePostFeedWithPatch() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);

        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .creatorUserId(UPDATED_CREATOR_USER_ID);

        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostFeed.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostFeed))
            )
            .andExpect(status().isOk());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
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
        assertThat(testPostFeed.getCreatorUserId()).isEqualTo(UPDATED_CREATOR_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postFeedDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostFeed() throws Exception {
        int databaseSizeBeforeUpdate = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        postFeed.setId(longCount.incrementAndGet());

        // Create the PostFeed
        PostFeedDTO postFeedDTO = postFeedMapper.toDto(postFeed);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostFeedMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postFeedDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostFeed in the database
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deletePostFeed() throws Exception {
        // Initialize the database
        postFeedRepository.saveAndFlush(postFeed);
        postFeedRepository.save(postFeed);
        postFeedSearchRepository.save(postFeed);

        int databaseSizeBeforeDelete = postFeedRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the postFeed
        restPostFeedMockMvc
            .perform(delete(ENTITY_API_URL_ID, postFeed.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostFeed> postFeedList = postFeedRepository.findAll();
        assertThat(postFeedList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postFeedSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchPostFeed() throws Exception {
        // Initialize the database
        postFeed = postFeedRepository.saveAndFlush(postFeed);
        postFeedSearchRepository.save(postFeed);

        // Search the postFeed
        restPostFeedMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + postFeed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postFeed.getId().intValue())))
            .andExpect(jsonPath("$.[*].postContent").value(hasItem(DEFAULT_POST_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].isHidden").value(hasItem(DEFAULT_IS_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].pinnedPost").value(hasItem(DEFAULT_PINNED_POST.booleanValue())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorUserId").value(hasItem(DEFAULT_CREATOR_USER_ID.intValue())));
    }
}
