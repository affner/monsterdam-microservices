package com.monsterdam.interactions.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.interactions.IntegrationTest;
import com.monsterdam.interactions.domain.LikeMark;
import com.monsterdam.interactions.repository.LikeMarkRepository;
import com.monsterdam.interactions.service.dto.LikeMarkDTO;
import com.monsterdam.interactions.service.mapper.LikeMarkMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LikeMarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeMarkRepository likeMarkRepository;

    @Autowired
    private LikeMarkMapper likeMarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeMarkMockMvc;

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

    @BeforeEach
    public void initTest() {
        likeMark = createEntity(em);
    }

    @Test
    @Transactional
    void createLikeMark() throws Exception {
        int databaseSizeBeforeCreate = likeMarkRepository.findAll().size();
        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);
        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isCreated());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeCreate + 1);
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
    @Transactional
    void createLikeMarkWithExistingId() throws Exception {
        // Create the LikeMark with an existing ID
        likeMark.setId(1L);
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        int databaseSizeBeforeCreate = likeMarkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmojiTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().size();
        // set the field null
        likeMark.setEmojiTypeId(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().size();
        // set the field null
        likeMark.setCreatedDate(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeMarkRepository.findAll().size();
        // set the field null
        likeMark.setIsDeleted(null);

        // Create the LikeMark, which fails.
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        restLikeMarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isBadRequest());

        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikeMarks() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        // Get all the likeMarkList
        restLikeMarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeMark.getId().intValue())))
            .andExpect(jsonPath("$.[*].emojiTypeId").value(hasItem(DEFAULT_EMOJI_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].multimediaId").value(hasItem(DEFAULT_MULTIMEDIA_ID.intValue())))
            .andExpect(jsonPath("$.[*].messageId").value(hasItem(DEFAULT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].commentId").value(hasItem(DEFAULT_COMMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].likerUserId").value(hasItem(DEFAULT_LIKER_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getLikeMark() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        // Get the likeMark
        restLikeMarkMockMvc
            .perform(get(ENTITY_API_URL_ID, likeMark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeMark.getId().intValue()))
            .andExpect(jsonPath("$.emojiTypeId").value(DEFAULT_EMOJI_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.multimediaId").value(DEFAULT_MULTIMEDIA_ID.intValue()))
            .andExpect(jsonPath("$.messageId").value(DEFAULT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.commentId").value(DEFAULT_COMMENT_ID.intValue()))
            .andExpect(jsonPath("$.likerUserId").value(DEFAULT_LIKER_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikeMark() throws Exception {
        // Get the likeMark
        restLikeMarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikeMark() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();

        // Update the likeMark
        LikeMark updatedLikeMark = likeMarkRepository.findById(likeMark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLikeMark are not directly saved in db
        em.detach(updatedLikeMark);
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

        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
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
    @Transactional
    void putNonExistingLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();

        // Update the likeMark using partial update
        LikeMark partialUpdatedLikeMark = new LikeMark();
        partialUpdatedLikeMark.setId(likeMark.getId());

        partialUpdatedLikeMark
            .emojiTypeId(UPDATED_EMOJI_TYPE_ID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .commentId(UPDATED_COMMENT_ID);

        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMark))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
        LikeMark testLikeMark = likeMarkList.get(likeMarkList.size() - 1);
        assertThat(testLikeMark.getEmojiTypeId()).isEqualTo(UPDATED_EMOJI_TYPE_ID);
        assertThat(testLikeMark.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testLikeMark.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testLikeMark.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testLikeMark.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testLikeMark.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testLikeMark.getMultimediaId()).isEqualTo(DEFAULT_MULTIMEDIA_ID);
        assertThat(testLikeMark.getMessageId()).isEqualTo(DEFAULT_MESSAGE_ID);
        assertThat(testLikeMark.getPostId()).isEqualTo(DEFAULT_POST_ID);
        assertThat(testLikeMark.getCommentId()).isEqualTo(UPDATED_COMMENT_ID);
        assertThat(testLikeMark.getLikerUserId()).isEqualTo(DEFAULT_LIKER_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateLikeMarkWithPatch() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();

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

        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMark))
            )
            .andExpect(status().isOk());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
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
    @Transactional
    void patchNonExistingLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeMarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeMark() throws Exception {
        int databaseSizeBeforeUpdate = likeMarkRepository.findAll().size();
        likeMark.setId(longCount.incrementAndGet());

        // Create the LikeMark
        LikeMarkDTO likeMarkDTO = likeMarkMapper.toDto(likeMark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMarkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(likeMarkDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMark in the database
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeMark() throws Exception {
        // Initialize the database
        likeMarkRepository.saveAndFlush(likeMark);

        int databaseSizeBeforeDelete = likeMarkRepository.findAll().size();

        // Delete the likeMark
        restLikeMarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeMark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikeMark> likeMarkList = likeMarkRepository.findAll();
        assertThat(likeMarkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
