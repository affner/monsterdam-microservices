package com.fanflip.notifications.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.notifications.IntegrationTest;
import com.fanflip.notifications.domain.AppNotification;
import com.fanflip.notifications.repository.AppNotificationRepository;
import com.fanflip.notifications.service.dto.AppNotificationDTO;
import com.fanflip.notifications.service.mapper.AppNotificationMapper;
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
 * Integration tests for the {@link AppNotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppNotificationResourceIT {

    private static final Instant DEFAULT_READ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final Long DEFAULT_POST_COMMENT_ID = 1L;
    private static final Long UPDATED_POST_COMMENT_ID = 2L;

    private static final Long DEFAULT_POST_FEED_ID = 1L;
    private static final Long UPDATED_POST_FEED_ID = 2L;

    private static final Long DEFAULT_DIRECT_MESSAGE_ID = 1L;
    private static final Long UPDATED_DIRECT_MESSAGE_ID = 2L;

    private static final Long DEFAULT_TARGET_USER_ID = 1L;
    private static final Long UPDATED_TARGET_USER_ID = 2L;

    private static final Long DEFAULT_LIKE_MARK = 1L;
    private static final Long UPDATED_LIKE_MARK = 2L;

    private static final String ENTITY_API_URL = "/api/app-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AppNotificationRepository appNotificationRepository;

    @Autowired
    private AppNotificationMapper appNotificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppNotificationMockMvc;

    private AppNotification appNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppNotification createEntity(EntityManager em) {
        AppNotification appNotification = new AppNotification()
            .readDate(DEFAULT_READ_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .postCommentId(DEFAULT_POST_COMMENT_ID)
            .postFeedId(DEFAULT_POST_FEED_ID)
            .directMessageId(DEFAULT_DIRECT_MESSAGE_ID)
            .targetUserId(DEFAULT_TARGET_USER_ID)
            .likeMark(DEFAULT_LIKE_MARK);
        return appNotification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppNotification createUpdatedEntity(EntityManager em) {
        AppNotification appNotification = new AppNotification()
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .targetUserId(UPDATED_TARGET_USER_ID)
            .likeMark(UPDATED_LIKE_MARK);
        return appNotification;
    }

    @BeforeEach
    public void initTest() {
        appNotification = createEntity(em);
    }

    @Test
    @Transactional
    void createAppNotification() throws Exception {
        int databaseSizeBeforeCreate = appNotificationRepository.findAll().size();
        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);
        restAppNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        AppNotification testAppNotification = appNotificationList.get(appNotificationList.size() - 1);
        assertThat(testAppNotification.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testAppNotification.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAppNotification.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAppNotification.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAppNotification.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAppNotification.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testAppNotification.getPostCommentId()).isEqualTo(DEFAULT_POST_COMMENT_ID);
        assertThat(testAppNotification.getPostFeedId()).isEqualTo(DEFAULT_POST_FEED_ID);
        assertThat(testAppNotification.getDirectMessageId()).isEqualTo(DEFAULT_DIRECT_MESSAGE_ID);
        assertThat(testAppNotification.getTargetUserId()).isEqualTo(DEFAULT_TARGET_USER_ID);
        assertThat(testAppNotification.getLikeMark()).isEqualTo(DEFAULT_LIKE_MARK);
    }

    @Test
    @Transactional
    void createAppNotificationWithExistingId() throws Exception {
        // Create the AppNotification with an existing ID
        appNotification.setId(1L);
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        int databaseSizeBeforeCreate = appNotificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = appNotificationRepository.findAll().size();
        // set the field null
        appNotification.setCreatedDate(null);

        // Create the AppNotification, which fails.
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        restAppNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = appNotificationRepository.findAll().size();
        // set the field null
        appNotification.setIsDeleted(null);

        // Create the AppNotification, which fails.
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        restAppNotificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppNotifications() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        // Get all the appNotificationList
        restAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].postCommentId").value(hasItem(DEFAULT_POST_COMMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].postFeedId").value(hasItem(DEFAULT_POST_FEED_ID.intValue())))
            .andExpect(jsonPath("$.[*].directMessageId").value(hasItem(DEFAULT_DIRECT_MESSAGE_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetUserId").value(hasItem(DEFAULT_TARGET_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].likeMark").value(hasItem(DEFAULT_LIKE_MARK.intValue())));
    }

    @Test
    @Transactional
    void getAppNotification() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        // Get the appNotification
        restAppNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, appNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appNotification.getId().intValue()))
            .andExpect(jsonPath("$.readDate").value(DEFAULT_READ_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.postCommentId").value(DEFAULT_POST_COMMENT_ID.intValue()))
            .andExpect(jsonPath("$.postFeedId").value(DEFAULT_POST_FEED_ID.intValue()))
            .andExpect(jsonPath("$.directMessageId").value(DEFAULT_DIRECT_MESSAGE_ID.intValue()))
            .andExpect(jsonPath("$.targetUserId").value(DEFAULT_TARGET_USER_ID.intValue()))
            .andExpect(jsonPath("$.likeMark").value(DEFAULT_LIKE_MARK.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAppNotification() throws Exception {
        // Get the appNotification
        restAppNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppNotification() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();

        // Update the appNotification
        AppNotification updatedAppNotification = appNotificationRepository.findById(appNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppNotification are not directly saved in db
        em.detach(updatedAppNotification);
        updatedAppNotification
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .targetUserId(UPDATED_TARGET_USER_ID)
            .likeMark(UPDATED_LIKE_MARK);
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(updatedAppNotification);

        restAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
        AppNotification testAppNotification = appNotificationList.get(appNotificationList.size() - 1);
        assertThat(testAppNotification.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testAppNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppNotification.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAppNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAppNotification.getPostCommentId()).isEqualTo(UPDATED_POST_COMMENT_ID);
        assertThat(testAppNotification.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
        assertThat(testAppNotification.getDirectMessageId()).isEqualTo(UPDATED_DIRECT_MESSAGE_ID);
        assertThat(testAppNotification.getTargetUserId()).isEqualTo(UPDATED_TARGET_USER_ID);
        assertThat(testAppNotification.getLikeMark()).isEqualTo(UPDATED_LIKE_MARK);
    }

    @Test
    @Transactional
    void putNonExistingAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppNotificationWithPatch() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();

        // Update the appNotification using partial update
        AppNotification partialUpdatedAppNotification = new AppNotification();
        partialUpdatedAppNotification.setId(appNotification.getId());

        partialUpdatedAppNotification
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .targetUserId(UPDATED_TARGET_USER_ID);

        restAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppNotification))
            )
            .andExpect(status().isOk());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
        AppNotification testAppNotification = appNotificationList.get(appNotificationList.size() - 1);
        assertThat(testAppNotification.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testAppNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppNotification.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAppNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAppNotification.getPostCommentId()).isEqualTo(DEFAULT_POST_COMMENT_ID);
        assertThat(testAppNotification.getPostFeedId()).isEqualTo(DEFAULT_POST_FEED_ID);
        assertThat(testAppNotification.getDirectMessageId()).isEqualTo(DEFAULT_DIRECT_MESSAGE_ID);
        assertThat(testAppNotification.getTargetUserId()).isEqualTo(UPDATED_TARGET_USER_ID);
        assertThat(testAppNotification.getLikeMark()).isEqualTo(DEFAULT_LIKE_MARK);
    }

    @Test
    @Transactional
    void fullUpdateAppNotificationWithPatch() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();

        // Update the appNotification using partial update
        AppNotification partialUpdatedAppNotification = new AppNotification();
        partialUpdatedAppNotification.setId(appNotification.getId());

        partialUpdatedAppNotification
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .targetUserId(UPDATED_TARGET_USER_ID)
            .likeMark(UPDATED_LIKE_MARK);

        restAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAppNotification))
            )
            .andExpect(status().isOk());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
        AppNotification testAppNotification = appNotificationList.get(appNotificationList.size() - 1);
        assertThat(testAppNotification.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testAppNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAppNotification.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAppNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAppNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAppNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testAppNotification.getPostCommentId()).isEqualTo(UPDATED_POST_COMMENT_ID);
        assertThat(testAppNotification.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
        assertThat(testAppNotification.getDirectMessageId()).isEqualTo(UPDATED_DIRECT_MESSAGE_ID);
        assertThat(testAppNotification.getTargetUserId()).isEqualTo(UPDATED_TARGET_USER_ID);
        assertThat(testAppNotification.getLikeMark()).isEqualTo(UPDATED_LIKE_MARK);
    }

    @Test
    @Transactional
    void patchNonExistingAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppNotification() throws Exception {
        int databaseSizeBeforeUpdate = appNotificationRepository.findAll().size();
        appNotification.setId(longCount.incrementAndGet());

        // Create the AppNotification
        AppNotificationDTO appNotificationDTO = appNotificationMapper.toDto(appNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(appNotificationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppNotification in the database
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppNotification() throws Exception {
        // Initialize the database
        appNotificationRepository.saveAndFlush(appNotification);

        int databaseSizeBeforeDelete = appNotificationRepository.findAll().size();

        // Delete the appNotification
        restAppNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, appNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppNotification> appNotificationList = appNotificationRepository.findAll();
        assertThat(appNotificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
