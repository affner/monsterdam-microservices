package com.monsterdam.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.interactions.IntegrationTest;
import com.monsterdam.interactions.domain.PostComment;
import com.monsterdam.interactions.domain.PostFeed;
import com.monsterdam.interactions.repository.PostCommentRepository;
import com.monsterdam.interactions.service.PostCommentService;
import com.monsterdam.interactions.service.dto.PostCommentDTO;
import com.monsterdam.interactions.service.mapper.PostCommentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link PostCommentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PostCommentResourceIT {

    private static final String DEFAULT_COMMENT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_CONTENT = "BBBBBBBBBB";

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

    private static final Long DEFAULT_COMMENTER_USER_ID = 1L;
    private static final Long UPDATED_COMMENTER_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/post-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
    private EntityManager em;

    @Autowired
    private MockMvc restPostCommentMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .commenterUserId(DEFAULT_COMMENTER_USER_ID);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        postComment.setPost(postFeed);
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
            .isDeleted(UPDATED_IS_DELETED)
            .commenterUserId(UPDATED_COMMENTER_USER_ID);
        // Add required entity
        PostFeed postFeed;
        if (TestUtil.findAll(em, PostFeed.class).isEmpty()) {
            postFeed = PostFeedResourceIT.createUpdatedEntity(em);
            em.persist(postFeed);
            em.flush();
        } else {
            postFeed = TestUtil.findAll(em, PostFeed.class).get(0);
        }
        postComment.setPost(postFeed);
        return postComment;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
    }

    @BeforeEach
    public void initTest() {
        postComment = createEntity(em);
    }

    @Test
    @Transactional
    void createPostComment() throws Exception {
        int databaseSizeBeforeCreate = postCommentRepository.findAll().size();
        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);
        restPostCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
            });
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(DEFAULT_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPostComment.getCommenterUserId()).isEqualTo(DEFAULT_COMMENTER_USER_ID);
    }

    @Test
    @Transactional
    void createPostCommentWithExistingId() throws Exception {
        // Create the PostComment with an existing ID
        postComment.setId(1L);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        int databaseSizeBeforeCreate = postCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postCommentRepository.findAll().size();
        // set the field null
        postComment.setCreatedDate(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        restPostCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postCommentRepository.findAll().size();
        // set the field null
        postComment.setIsDeleted(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        restPostCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommenterUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = postCommentRepository.findAll().size();
        // set the field null
        postComment.setCommenterUserId(null);

        // Create the PostComment, which fails.
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        restPostCommentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPostComments() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        // Get all the postCommentList
        restPostCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentContent").value(hasItem(DEFAULT_COMMENT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].likeCount").value(hasItem(DEFAULT_LIKE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].commenterUserId").value(hasItem(DEFAULT_COMMENTER_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostCommentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(postCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostCommentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(postCommentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPostCommentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(postCommentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPostCommentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(postCommentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        // Get the postComment
        restPostCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, postComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(postComment.getId().intValue()))
            .andExpect(jsonPath("$.commentContent").value(DEFAULT_COMMENT_CONTENT.toString()))
            .andExpect(jsonPath("$.likeCount").value(DEFAULT_LIKE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.commenterUserId").value(DEFAULT_COMMENTER_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPostComment() throws Exception {
        // Get the postComment
        restPostCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();

        // Update the postComment
        PostComment updatedPostComment = postCommentRepository.findById(postComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPostComment are not directly saved in db
        em.detach(updatedPostComment);
        updatedPostComment
            .commentContent(UPDATED_COMMENT_CONTENT)
            .likeCount(UPDATED_LIKE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .commenterUserId(UPDATED_COMMENTER_USER_ID);
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(updatedPostComment);

        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostComment.getCommenterUserId()).isEqualTo(UPDATED_COMMENTER_USER_ID);
//        await()
//            .atMost(5, TimeUnit.SECONDS)
//            .untilAsserted(() -> {
//                assertThat(testPostCommentSearch.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
//                assertThat(testPostCommentSearch.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
//                assertThat(testPostCommentSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
//                assertThat(testPostCommentSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
//                assertThat(testPostCommentSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
//                assertThat(testPostCommentSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
//                assertThat(testPostCommentSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
//                assertThat(testPostCommentSearch.getCommenterUserId()).isEqualTo(UPDATED_COMMENTER_USER_ID);
//            });
    }

    @Test
    @Transactional
    void putNonExistingPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(postCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();

        // Update the postComment using partial update
        PostComment partialUpdatedPostComment = new PostComment();
        partialUpdatedPostComment.setId(postComment.getId());

        partialUpdatedPostComment.createdBy(UPDATED_CREATED_BY).isDeleted(UPDATED_IS_DELETED).commenterUserId(UPDATED_COMMENTER_USER_ID);

        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostComment))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(DEFAULT_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(DEFAULT_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostComment.getCommenterUserId()).isEqualTo(UPDATED_COMMENTER_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdatePostCommentWithPatch() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);

        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .commenterUserId(UPDATED_COMMENTER_USER_ID);

        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPostComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPostComment))
            )
            .andExpect(status().isOk());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
        PostComment testPostComment = postCommentList.get(postCommentList.size() - 1);
        assertThat(testPostComment.getCommentContent()).isEqualTo(UPDATED_COMMENT_CONTENT);
        assertThat(testPostComment.getLikeCount()).isEqualTo(UPDATED_LIKE_COUNT);
        assertThat(testPostComment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostComment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostComment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostComment.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostComment.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPostComment.getCommenterUserId()).isEqualTo(UPDATED_COMMENTER_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, postCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPostComment() throws Exception {
        int databaseSizeBeforeUpdate = postCommentRepository.findAll().size();
        postComment.setId(longCount.incrementAndGet());

        // Create the PostComment
        PostCommentDTO postCommentDTO = postCommentMapper.toDto(postComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostCommentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(postCommentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PostComment in the database
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePostComment() throws Exception {
        // Initialize the database
        postCommentRepository.saveAndFlush(postComment);
        postCommentRepository.save(postComment);

        int databaseSizeBeforeDelete = postCommentRepository.findAll().size();

        // Delete the postComment
        restPostCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, postComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PostComment> postCommentList = postCommentRepository.findAll();
        assertThat(postCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }

}
