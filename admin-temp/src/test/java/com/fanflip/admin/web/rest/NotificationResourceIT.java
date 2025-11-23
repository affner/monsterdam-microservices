package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.Notification;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.NotificationRepository;
import com.fanflip.admin.repository.search.NotificationSearchRepository;
import com.fanflip.admin.service.dto.NotificationDTO;
import com.fanflip.admin.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NotificationResourceIT {

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

    private static final Long DEFAULT_USER_MENTION_ID = 1L;
    private static final Long UPDATED_USER_MENTION_ID = 2L;

    private static final Long DEFAULT_LIKE_MARK_ID = 1L;
    private static final Long UPDATED_LIKE_MARK_ID = 2L;

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/notifications/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationSearchRepository notificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .readDate(DEFAULT_READ_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .postCommentId(DEFAULT_POST_COMMENT_ID)
            .postFeedId(DEFAULT_POST_FEED_ID)
            .directMessageId(DEFAULT_DIRECT_MESSAGE_ID)
            .userMentionId(DEFAULT_USER_MENTION_ID)
            .likeMarkId(DEFAULT_LIKE_MARK_ID);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        notification.setCommentedUser(userProfile);
        // Add required entity
        notification.setMessagedUser(userProfile);
        // Add required entity
        notification.setMentionerUserInPost(userProfile);
        // Add required entity
        notification.setMentionerUserInComment(userProfile);
        return notification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .userMentionId(UPDATED_USER_MENTION_ID)
            .likeMarkId(UPDATED_LIKE_MARK_ID);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        notification.setCommentedUser(userProfile);
        // Add required entity
        notification.setMessagedUser(userProfile);
        // Add required entity
        notification.setMentionerUserInPost(userProfile);
        // Add required entity
        notification.setMentionerUserInComment(userProfile);
        return notification;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Notification.class).block();
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
        notificationSearchRepository.deleteAll().block();
        assertThat(notificationSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        notification = createEntity(em);
    }

    @Test
    void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testNotification.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testNotification.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testNotification.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testNotification.getPostCommentId()).isEqualTo(DEFAULT_POST_COMMENT_ID);
        assertThat(testNotification.getPostFeedId()).isEqualTo(DEFAULT_POST_FEED_ID);
        assertThat(testNotification.getDirectMessageId()).isEqualTo(DEFAULT_DIRECT_MESSAGE_ID);
        assertThat(testNotification.getUserMentionId()).isEqualTo(DEFAULT_USER_MENTION_ID);
        assertThat(testNotification.getLikeMarkId()).isEqualTo(DEFAULT_LIKE_MARK_ID);
    }

    @Test
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        int databaseSizeBeforeCreate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        // set the field null
        notification.setCreatedDate(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        // set the field null
        notification.setIsDeleted(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllNotifications() {
        // Initialize the database
        notificationRepository.save(notification).block();

        // Get all the notificationList
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
            .value(hasItem(notification.getId().intValue()))
            .jsonPath("$.[*].readDate")
            .value(hasItem(DEFAULT_READ_DATE.toString()))
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
            .jsonPath("$.[*].postCommentId")
            .value(hasItem(DEFAULT_POST_COMMENT_ID.intValue()))
            .jsonPath("$.[*].postFeedId")
            .value(hasItem(DEFAULT_POST_FEED_ID.intValue()))
            .jsonPath("$.[*].directMessageId")
            .value(hasItem(DEFAULT_DIRECT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].userMentionId")
            .value(hasItem(DEFAULT_USER_MENTION_ID.intValue()))
            .jsonPath("$.[*].likeMarkId")
            .value(hasItem(DEFAULT_LIKE_MARK_ID.intValue()));
    }

    @Test
    void getNotification() {
        // Initialize the database
        notificationRepository.save(notification).block();

        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, notification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(notification.getId().intValue()))
            .jsonPath("$.readDate")
            .value(is(DEFAULT_READ_DATE.toString()))
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
            .jsonPath("$.postCommentId")
            .value(is(DEFAULT_POST_COMMENT_ID.intValue()))
            .jsonPath("$.postFeedId")
            .value(is(DEFAULT_POST_FEED_ID.intValue()))
            .jsonPath("$.directMessageId")
            .value(is(DEFAULT_DIRECT_MESSAGE_ID.intValue()))
            .jsonPath("$.userMentionId")
            .value(is(DEFAULT_USER_MENTION_ID.intValue()))
            .jsonPath("$.likeMarkId")
            .value(is(DEFAULT_LIKE_MARK_ID.intValue()));
    }

    @Test
    void getNonExistingNotification() {
        // Get the notification
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingNotification() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        notificationSearchRepository.save(notification).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).block();
        updatedNotification
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .userMentionId(UPDATED_USER_MENTION_ID)
            .likeMarkId(UPDATED_LIKE_MARK_ID);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testNotification.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNotification.getPostCommentId()).isEqualTo(UPDATED_POST_COMMENT_ID);
        assertThat(testNotification.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
        assertThat(testNotification.getDirectMessageId()).isEqualTo(UPDATED_DIRECT_MESSAGE_ID);
        assertThat(testNotification.getUserMentionId()).isEqualTo(UPDATED_USER_MENTION_ID);
        assertThat(testNotification.getLikeMarkId()).isEqualTo(UPDATED_LIKE_MARK_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Notification> notificationSearchList = IterableUtils.toList(
                    notificationSearchRepository.findAll().collectList().block()
                );
                Notification testNotificationSearch = notificationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testNotificationSearch.getReadDate()).isEqualTo(UPDATED_READ_DATE);
                assertThat(testNotificationSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testNotificationSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testNotificationSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testNotificationSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testNotificationSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testNotificationSearch.getPostCommentId()).isEqualTo(UPDATED_POST_COMMENT_ID);
                assertThat(testNotificationSearch.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
                assertThat(testNotificationSearch.getDirectMessageId()).isEqualTo(UPDATED_DIRECT_MESSAGE_ID);
                assertThat(testNotificationSearch.getUserMentionId()).isEqualTo(UPDATED_USER_MENTION_ID);
                assertThat(testNotificationSearch.getLikeMarkId()).isEqualTo(UPDATED_LIKE_MARK_ID);
            });
    }

    @Test
    void putNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .userMentionId(UPDATED_USER_MENTION_ID)
            .likeMarkId(UPDATED_LIKE_MARK_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getReadDate()).isEqualTo(DEFAULT_READ_DATE);
        assertThat(testNotification.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testNotification.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNotification.getPostCommentId()).isEqualTo(DEFAULT_POST_COMMENT_ID);
        assertThat(testNotification.getPostFeedId()).isEqualTo(DEFAULT_POST_FEED_ID);
        assertThat(testNotification.getDirectMessageId()).isEqualTo(DEFAULT_DIRECT_MESSAGE_ID);
        assertThat(testNotification.getUserMentionId()).isEqualTo(UPDATED_USER_MENTION_ID);
        assertThat(testNotification.getLikeMarkId()).isEqualTo(UPDATED_LIKE_MARK_ID);
    }

    @Test
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        notificationRepository.save(notification).block();

        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .readDate(UPDATED_READ_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .postCommentId(UPDATED_POST_COMMENT_ID)
            .postFeedId(UPDATED_POST_FEED_ID)
            .directMessageId(UPDATED_DIRECT_MESSAGE_ID)
            .userMentionId(UPDATED_USER_MENTION_ID)
            .likeMarkId(UPDATED_LIKE_MARK_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNotification))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getReadDate()).isEqualTo(UPDATED_READ_DATE);
        assertThat(testNotification.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testNotification.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testNotification.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testNotification.getPostCommentId()).isEqualTo(UPDATED_POST_COMMENT_ID);
        assertThat(testNotification.getPostFeedId()).isEqualTo(UPDATED_POST_FEED_ID);
        assertThat(testNotification.getDirectMessageId()).isEqualTo(UPDATED_DIRECT_MESSAGE_ID);
        assertThat(testNotification.getUserMentionId()).isEqualTo(UPDATED_USER_MENTION_ID);
        assertThat(testNotification.getLikeMarkId()).isEqualTo(UPDATED_LIKE_MARK_ID);
    }

    @Test
    void patchNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, notificationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(notificationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteNotification() {
        // Initialize the database
        notificationRepository.save(notification).block();
        notificationRepository.save(notification).block();
        notificationSearchRepository.save(notification).block();

        int databaseSizeBeforeDelete = notificationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the notification
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, notification.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll().collectList().block();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(notificationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchNotification() {
        // Initialize the database
        notification = notificationRepository.save(notification).block();
        notificationSearchRepository.save(notification).block();

        // Search the notification
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + notification.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(notification.getId().intValue()))
            .jsonPath("$.[*].readDate")
            .value(hasItem(DEFAULT_READ_DATE.toString()))
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
            .jsonPath("$.[*].postCommentId")
            .value(hasItem(DEFAULT_POST_COMMENT_ID.intValue()))
            .jsonPath("$.[*].postFeedId")
            .value(hasItem(DEFAULT_POST_FEED_ID.intValue()))
            .jsonPath("$.[*].directMessageId")
            .value(hasItem(DEFAULT_DIRECT_MESSAGE_ID.intValue()))
            .jsonPath("$.[*].userMentionId")
            .value(hasItem(DEFAULT_USER_MENTION_ID.intValue()))
            .jsonPath("$.[*].likeMarkId")
            .value(hasItem(DEFAULT_LIKE_MARK_ID.intValue()));
    }
}
