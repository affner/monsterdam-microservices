package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.UserSettings;
import com.fanflip.admin.domain.enumeration.UserLanguage;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.UserSettingsRepository;
import com.fanflip.admin.repository.search.UserSettingsSearchRepository;
import com.fanflip.admin.service.dto.UserSettingsDTO;
import com.fanflip.admin.service.mapper.UserSettingsMapper;
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
 * Integration tests for the {@link UserSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserSettingsResourceIT {

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DARK_MODE = false;
    private static final Boolean UPDATED_DARK_MODE = true;

    private static final UserLanguage DEFAULT_LANGUAGE = UserLanguage.ES;
    private static final UserLanguage UPDATED_LANGUAGE = UserLanguage.EN;

    private static final Boolean DEFAULT_CONTENT_FILTER = false;
    private static final Boolean UPDATED_CONTENT_FILTER = true;

    private static final Integer DEFAULT_MESSAGE_BLUR_INTENSITY = 1;
    private static final Integer UPDATED_MESSAGE_BLUR_INTENSITY = 2;

    private static final Boolean DEFAULT_ACTIVITY_STATUS_VISIBILITY = false;
    private static final Boolean UPDATED_ACTIVITY_STATUS_VISIBILITY = true;

    private static final Boolean DEFAULT_TWO_FACTOR_AUTHENTICATION = false;
    private static final Boolean UPDATED_TWO_FACTOR_AUTHENTICATION = true;

    private static final Integer DEFAULT_SESSIONS_ACTIVE_COUNT = 1;
    private static final Integer UPDATED_SESSIONS_ACTIVE_COUNT = 2;

    private static final Boolean DEFAULT_EMAIL_NOTIFICATIONS = false;
    private static final Boolean UPDATED_EMAIL_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS = false;
    private static final Boolean UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_NEW_MESSAGES = false;
    private static final Boolean UPDATED_NEW_MESSAGES = true;

    private static final Boolean DEFAULT_POST_REPLIES = false;
    private static final Boolean UPDATED_POST_REPLIES = true;

    private static final Boolean DEFAULT_POST_LIKES = false;
    private static final Boolean UPDATED_POST_LIKES = true;

    private static final Boolean DEFAULT_NEW_FOLLOWERS = false;
    private static final Boolean UPDATED_NEW_FOLLOWERS = true;

    private static final Boolean DEFAULT_SMS_NEW_STREAM = false;
    private static final Boolean UPDATED_SMS_NEW_STREAM = true;

    private static final Boolean DEFAULT_TOAST_NEW_COMMENT = false;
    private static final Boolean UPDATED_TOAST_NEW_COMMENT = true;

    private static final Boolean DEFAULT_TOAST_NEW_LIKES = false;
    private static final Boolean UPDATED_TOAST_NEW_LIKES = true;

    private static final Boolean DEFAULT_TOAST_NEW_STREAM = false;
    private static final Boolean UPDATED_TOAST_NEW_STREAM = true;

    private static final Boolean DEFAULT_SITE_NEW_COMMENT = false;
    private static final Boolean UPDATED_SITE_NEW_COMMENT = true;

    private static final Boolean DEFAULT_SITE_NEW_LIKES = false;
    private static final Boolean UPDATED_SITE_NEW_LIKES = true;

    private static final Boolean DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS = false;
    private static final Boolean UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS = true;

    private static final Boolean DEFAULT_SITE_NEW_STREAM = false;
    private static final Boolean UPDATED_SITE_NEW_STREAM = true;

    private static final Boolean DEFAULT_SITE_UPCOMING_STREAM_REMINDERS = false;
    private static final Boolean UPDATED_SITE_UPCOMING_STREAM_REMINDERS = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/user-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-settings/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private UserSettingsSearchRepository userSettingsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserSettings userSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createEntity(EntityManager em) {
        UserSettings userSettings = new UserSettings()
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .darkMode(DEFAULT_DARK_MODE)
            .language(DEFAULT_LANGUAGE)
            .contentFilter(DEFAULT_CONTENT_FILTER)
            .messageBlurIntensity(DEFAULT_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(DEFAULT_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(DEFAULT_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(DEFAULT_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(DEFAULT_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(DEFAULT_NEW_MESSAGES)
            .postReplies(DEFAULT_POST_REPLIES)
            .postLikes(DEFAULT_POST_LIKES)
            .newFollowers(DEFAULT_NEW_FOLLOWERS)
            .smsNewStream(DEFAULT_SMS_NEW_STREAM)
            .toastNewComment(DEFAULT_TOAST_NEW_COMMENT)
            .toastNewLikes(DEFAULT_TOAST_NEW_LIKES)
            .toastNewStream(DEFAULT_TOAST_NEW_STREAM)
            .siteNewComment(DEFAULT_SITE_NEW_COMMENT)
            .siteNewLikes(DEFAULT_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(DEFAULT_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return userSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSettings createUpdatedEntity(EntityManager em) {
        UserSettings userSettings = new UserSettings()
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return userSettings;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserSettings.class).block();
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
        userSettingsSearchRepository.deleteAll().block();
        assertThat(userSettingsSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userSettings = createEntity(em);
    }

    @Test
    void createUserSettings() throws Exception {
        int databaseSizeBeforeCreate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserSettings.getDarkMode()).isEqualTo(DEFAULT_DARK_MODE);
        assertThat(testUserSettings.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testUserSettings.getContentFilter()).isEqualTo(DEFAULT_CONTENT_FILTER);
        assertThat(testUserSettings.getMessageBlurIntensity()).isEqualTo(DEFAULT_MESSAGE_BLUR_INTENSITY);
        assertThat(testUserSettings.getActivityStatusVisibility()).isEqualTo(DEFAULT_ACTIVITY_STATUS_VISIBILITY);
        assertThat(testUserSettings.getTwoFactorAuthentication()).isEqualTo(DEFAULT_TWO_FACTOR_AUTHENTICATION);
        assertThat(testUserSettings.getSessionsActiveCount()).isEqualTo(DEFAULT_SESSIONS_ACTIVE_COUNT);
        assertThat(testUserSettings.getEmailNotifications()).isEqualTo(DEFAULT_EMAIL_NOTIFICATIONS);
        assertThat(testUserSettings.getImportantSubscriptionNotifications()).isEqualTo(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
        assertThat(testUserSettings.getNewMessages()).isEqualTo(DEFAULT_NEW_MESSAGES);
        assertThat(testUserSettings.getPostReplies()).isEqualTo(DEFAULT_POST_REPLIES);
        assertThat(testUserSettings.getPostLikes()).isEqualTo(DEFAULT_POST_LIKES);
        assertThat(testUserSettings.getNewFollowers()).isEqualTo(DEFAULT_NEW_FOLLOWERS);
        assertThat(testUserSettings.getSmsNewStream()).isEqualTo(DEFAULT_SMS_NEW_STREAM);
        assertThat(testUserSettings.getToastNewComment()).isEqualTo(DEFAULT_TOAST_NEW_COMMENT);
        assertThat(testUserSettings.getToastNewLikes()).isEqualTo(DEFAULT_TOAST_NEW_LIKES);
        assertThat(testUserSettings.getToastNewStream()).isEqualTo(DEFAULT_TOAST_NEW_STREAM);
        assertThat(testUserSettings.getSiteNewComment()).isEqualTo(DEFAULT_SITE_NEW_COMMENT);
        assertThat(testUserSettings.getSiteNewLikes()).isEqualTo(DEFAULT_SITE_NEW_LIKES);
        assertThat(testUserSettings.getSiteDiscountsFromFollowedUsers()).isEqualTo(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
        assertThat(testUserSettings.getSiteNewStream()).isEqualTo(DEFAULT_SITE_NEW_STREAM);
        assertThat(testUserSettings.getSiteUpcomingStreamReminders()).isEqualTo(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createUserSettingsWithExistingId() throws Exception {
        // Create the UserSettings with an existing ID
        userSettings.setId(1L);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        int databaseSizeBeforeCreate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDarkModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setDarkMode(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setLanguage(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentFilterIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setContentFilter(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkActivityStatusVisibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setActivityStatusVisibility(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTwoFactorAuthenticationIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setTwoFactorAuthentication(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEmailNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setEmailNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkImportantSubscriptionNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setImportantSubscriptionNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNewMessagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setNewMessages(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPostRepliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setPostReplies(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPostLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setPostLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNewFollowersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setNewFollowers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSmsNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSmsNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkToastNewCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setToastNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkToastNewLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setToastNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkToastNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setToastNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSiteNewCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSiteNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSiteNewLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSiteNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSiteDiscountsFromFollowedUsersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSiteDiscountsFromFollowedUsers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSiteNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSiteNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSiteUpcomingStreamRemindersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setSiteUpcomingStreamReminders(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setCreatedDate(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        // set the field null
        userSettings.setIsDeleted(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserSettings() {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();

        // Get all the userSettingsList
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
            .value(hasItem(userSettings.getId().intValue()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].darkMode")
            .value(hasItem(DEFAULT_DARK_MODE.booleanValue()))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE.toString()))
            .jsonPath("$.[*].contentFilter")
            .value(hasItem(DEFAULT_CONTENT_FILTER.booleanValue()))
            .jsonPath("$.[*].messageBlurIntensity")
            .value(hasItem(DEFAULT_MESSAGE_BLUR_INTENSITY))
            .jsonPath("$.[*].activityStatusVisibility")
            .value(hasItem(DEFAULT_ACTIVITY_STATUS_VISIBILITY.booleanValue()))
            .jsonPath("$.[*].twoFactorAuthentication")
            .value(hasItem(DEFAULT_TWO_FACTOR_AUTHENTICATION.booleanValue()))
            .jsonPath("$.[*].sessionsActiveCount")
            .value(hasItem(DEFAULT_SESSIONS_ACTIVE_COUNT))
            .jsonPath("$.[*].emailNotifications")
            .value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.[*].importantSubscriptionNotifications")
            .value(hasItem(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.[*].newMessages")
            .value(hasItem(DEFAULT_NEW_MESSAGES.booleanValue()))
            .jsonPath("$.[*].postReplies")
            .value(hasItem(DEFAULT_POST_REPLIES.booleanValue()))
            .jsonPath("$.[*].postLikes")
            .value(hasItem(DEFAULT_POST_LIKES.booleanValue()))
            .jsonPath("$.[*].newFollowers")
            .value(hasItem(DEFAULT_NEW_FOLLOWERS.booleanValue()))
            .jsonPath("$.[*].smsNewStream")
            .value(hasItem(DEFAULT_SMS_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].toastNewComment")
            .value(hasItem(DEFAULT_TOAST_NEW_COMMENT.booleanValue()))
            .jsonPath("$.[*].toastNewLikes")
            .value(hasItem(DEFAULT_TOAST_NEW_LIKES.booleanValue()))
            .jsonPath("$.[*].toastNewStream")
            .value(hasItem(DEFAULT_TOAST_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].siteNewComment")
            .value(hasItem(DEFAULT_SITE_NEW_COMMENT.booleanValue()))
            .jsonPath("$.[*].siteNewLikes")
            .value(hasItem(DEFAULT_SITE_NEW_LIKES.booleanValue()))
            .jsonPath("$.[*].siteDiscountsFromFollowedUsers")
            .value(hasItem(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS.booleanValue()))
            .jsonPath("$.[*].siteNewStream")
            .value(hasItem(DEFAULT_SITE_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].siteUpcomingStreamReminders")
            .value(hasItem(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS.booleanValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getUserSettings() {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();

        // Get the userSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userSettings.getId().intValue()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.darkMode")
            .value(is(DEFAULT_DARK_MODE.booleanValue()))
            .jsonPath("$.language")
            .value(is(DEFAULT_LANGUAGE.toString()))
            .jsonPath("$.contentFilter")
            .value(is(DEFAULT_CONTENT_FILTER.booleanValue()))
            .jsonPath("$.messageBlurIntensity")
            .value(is(DEFAULT_MESSAGE_BLUR_INTENSITY))
            .jsonPath("$.activityStatusVisibility")
            .value(is(DEFAULT_ACTIVITY_STATUS_VISIBILITY.booleanValue()))
            .jsonPath("$.twoFactorAuthentication")
            .value(is(DEFAULT_TWO_FACTOR_AUTHENTICATION.booleanValue()))
            .jsonPath("$.sessionsActiveCount")
            .value(is(DEFAULT_SESSIONS_ACTIVE_COUNT))
            .jsonPath("$.emailNotifications")
            .value(is(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.importantSubscriptionNotifications")
            .value(is(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.newMessages")
            .value(is(DEFAULT_NEW_MESSAGES.booleanValue()))
            .jsonPath("$.postReplies")
            .value(is(DEFAULT_POST_REPLIES.booleanValue()))
            .jsonPath("$.postLikes")
            .value(is(DEFAULT_POST_LIKES.booleanValue()))
            .jsonPath("$.newFollowers")
            .value(is(DEFAULT_NEW_FOLLOWERS.booleanValue()))
            .jsonPath("$.smsNewStream")
            .value(is(DEFAULT_SMS_NEW_STREAM.booleanValue()))
            .jsonPath("$.toastNewComment")
            .value(is(DEFAULT_TOAST_NEW_COMMENT.booleanValue()))
            .jsonPath("$.toastNewLikes")
            .value(is(DEFAULT_TOAST_NEW_LIKES.booleanValue()))
            .jsonPath("$.toastNewStream")
            .value(is(DEFAULT_TOAST_NEW_STREAM.booleanValue()))
            .jsonPath("$.siteNewComment")
            .value(is(DEFAULT_SITE_NEW_COMMENT.booleanValue()))
            .jsonPath("$.siteNewLikes")
            .value(is(DEFAULT_SITE_NEW_LIKES.booleanValue()))
            .jsonPath("$.siteDiscountsFromFollowedUsers")
            .value(is(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS.booleanValue()))
            .jsonPath("$.siteNewStream")
            .value(is(DEFAULT_SITE_NEW_STREAM.booleanValue()))
            .jsonPath("$.siteUpcomingStreamReminders")
            .value(is(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS.booleanValue()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingUserSettings() {
        // Get the userSettings
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        userSettingsSearchRepository.save(userSettings).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());

        // Update the userSettings
        UserSettings updatedUserSettings = userSettingsRepository.findById(userSettings.getId()).block();
        updatedUserSettings
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(updatedUserSettings);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSettingsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserSettings.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
        assertThat(testUserSettings.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testUserSettings.getContentFilter()).isEqualTo(UPDATED_CONTENT_FILTER);
        assertThat(testUserSettings.getMessageBlurIntensity()).isEqualTo(UPDATED_MESSAGE_BLUR_INTENSITY);
        assertThat(testUserSettings.getActivityStatusVisibility()).isEqualTo(UPDATED_ACTIVITY_STATUS_VISIBILITY);
        assertThat(testUserSettings.getTwoFactorAuthentication()).isEqualTo(UPDATED_TWO_FACTOR_AUTHENTICATION);
        assertThat(testUserSettings.getSessionsActiveCount()).isEqualTo(UPDATED_SESSIONS_ACTIVE_COUNT);
        assertThat(testUserSettings.getEmailNotifications()).isEqualTo(UPDATED_EMAIL_NOTIFICATIONS);
        assertThat(testUserSettings.getImportantSubscriptionNotifications()).isEqualTo(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
        assertThat(testUserSettings.getNewMessages()).isEqualTo(UPDATED_NEW_MESSAGES);
        assertThat(testUserSettings.getPostReplies()).isEqualTo(UPDATED_POST_REPLIES);
        assertThat(testUserSettings.getPostLikes()).isEqualTo(UPDATED_POST_LIKES);
        assertThat(testUserSettings.getNewFollowers()).isEqualTo(UPDATED_NEW_FOLLOWERS);
        assertThat(testUserSettings.getSmsNewStream()).isEqualTo(UPDATED_SMS_NEW_STREAM);
        assertThat(testUserSettings.getToastNewComment()).isEqualTo(UPDATED_TOAST_NEW_COMMENT);
        assertThat(testUserSettings.getToastNewLikes()).isEqualTo(UPDATED_TOAST_NEW_LIKES);
        assertThat(testUserSettings.getToastNewStream()).isEqualTo(UPDATED_TOAST_NEW_STREAM);
        assertThat(testUserSettings.getSiteNewComment()).isEqualTo(UPDATED_SITE_NEW_COMMENT);
        assertThat(testUserSettings.getSiteNewLikes()).isEqualTo(UPDATED_SITE_NEW_LIKES);
        assertThat(testUserSettings.getSiteDiscountsFromFollowedUsers()).isEqualTo(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
        assertThat(testUserSettings.getSiteNewStream()).isEqualTo(UPDATED_SITE_NEW_STREAM);
        assertThat(testUserSettings.getSiteUpcomingStreamReminders()).isEqualTo(UPDATED_SITE_UPCOMING_STREAM_REMINDERS);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserSettings> userSettingsSearchList = IterableUtils.toList(
                    userSettingsSearchRepository.findAll().collectList().block()
                );
                UserSettings testUserSettingsSearch = userSettingsSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserSettingsSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserSettingsSearch.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
                assertThat(testUserSettingsSearch.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
                assertThat(testUserSettingsSearch.getContentFilter()).isEqualTo(UPDATED_CONTENT_FILTER);
                assertThat(testUserSettingsSearch.getMessageBlurIntensity()).isEqualTo(UPDATED_MESSAGE_BLUR_INTENSITY);
                assertThat(testUserSettingsSearch.getActivityStatusVisibility()).isEqualTo(UPDATED_ACTIVITY_STATUS_VISIBILITY);
                assertThat(testUserSettingsSearch.getTwoFactorAuthentication()).isEqualTo(UPDATED_TWO_FACTOR_AUTHENTICATION);
                assertThat(testUserSettingsSearch.getSessionsActiveCount()).isEqualTo(UPDATED_SESSIONS_ACTIVE_COUNT);
                assertThat(testUserSettingsSearch.getEmailNotifications()).isEqualTo(UPDATED_EMAIL_NOTIFICATIONS);
                assertThat(testUserSettingsSearch.getImportantSubscriptionNotifications())
                    .isEqualTo(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
                assertThat(testUserSettingsSearch.getNewMessages()).isEqualTo(UPDATED_NEW_MESSAGES);
                assertThat(testUserSettingsSearch.getPostReplies()).isEqualTo(UPDATED_POST_REPLIES);
                assertThat(testUserSettingsSearch.getPostLikes()).isEqualTo(UPDATED_POST_LIKES);
                assertThat(testUserSettingsSearch.getNewFollowers()).isEqualTo(UPDATED_NEW_FOLLOWERS);
                assertThat(testUserSettingsSearch.getSmsNewStream()).isEqualTo(UPDATED_SMS_NEW_STREAM);
                assertThat(testUserSettingsSearch.getToastNewComment()).isEqualTo(UPDATED_TOAST_NEW_COMMENT);
                assertThat(testUserSettingsSearch.getToastNewLikes()).isEqualTo(UPDATED_TOAST_NEW_LIKES);
                assertThat(testUserSettingsSearch.getToastNewStream()).isEqualTo(UPDATED_TOAST_NEW_STREAM);
                assertThat(testUserSettingsSearch.getSiteNewComment()).isEqualTo(UPDATED_SITE_NEW_COMMENT);
                assertThat(testUserSettingsSearch.getSiteNewLikes()).isEqualTo(UPDATED_SITE_NEW_LIKES);
                assertThat(testUserSettingsSearch.getSiteDiscountsFromFollowedUsers())
                    .isEqualTo(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
                assertThat(testUserSettingsSearch.getSiteNewStream()).isEqualTo(UPDATED_SITE_NEW_STREAM);
                assertThat(testUserSettingsSearch.getSiteUpcomingStreamReminders()).isEqualTo(UPDATED_SITE_UPCOMING_STREAM_REMINDERS);
                assertThat(testUserSettingsSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserSettingsSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserSettingsSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserSettingsSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userSettingsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .darkMode(UPDATED_DARK_MODE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .newMessages(UPDATED_NEW_MESSAGES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserSettings.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
        assertThat(testUserSettings.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testUserSettings.getContentFilter()).isEqualTo(UPDATED_CONTENT_FILTER);
        assertThat(testUserSettings.getMessageBlurIntensity()).isEqualTo(DEFAULT_MESSAGE_BLUR_INTENSITY);
        assertThat(testUserSettings.getActivityStatusVisibility()).isEqualTo(UPDATED_ACTIVITY_STATUS_VISIBILITY);
        assertThat(testUserSettings.getTwoFactorAuthentication()).isEqualTo(DEFAULT_TWO_FACTOR_AUTHENTICATION);
        assertThat(testUserSettings.getSessionsActiveCount()).isEqualTo(UPDATED_SESSIONS_ACTIVE_COUNT);
        assertThat(testUserSettings.getEmailNotifications()).isEqualTo(DEFAULT_EMAIL_NOTIFICATIONS);
        assertThat(testUserSettings.getImportantSubscriptionNotifications()).isEqualTo(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
        assertThat(testUserSettings.getNewMessages()).isEqualTo(UPDATED_NEW_MESSAGES);
        assertThat(testUserSettings.getPostReplies()).isEqualTo(DEFAULT_POST_REPLIES);
        assertThat(testUserSettings.getPostLikes()).isEqualTo(DEFAULT_POST_LIKES);
        assertThat(testUserSettings.getNewFollowers()).isEqualTo(UPDATED_NEW_FOLLOWERS);
        assertThat(testUserSettings.getSmsNewStream()).isEqualTo(UPDATED_SMS_NEW_STREAM);
        assertThat(testUserSettings.getToastNewComment()).isEqualTo(UPDATED_TOAST_NEW_COMMENT);
        assertThat(testUserSettings.getToastNewLikes()).isEqualTo(UPDATED_TOAST_NEW_LIKES);
        assertThat(testUserSettings.getToastNewStream()).isEqualTo(DEFAULT_TOAST_NEW_STREAM);
        assertThat(testUserSettings.getSiteNewComment()).isEqualTo(DEFAULT_SITE_NEW_COMMENT);
        assertThat(testUserSettings.getSiteNewLikes()).isEqualTo(UPDATED_SITE_NEW_LIKES);
        assertThat(testUserSettings.getSiteDiscountsFromFollowedUsers()).isEqualTo(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
        assertThat(testUserSettings.getSiteNewStream()).isEqualTo(DEFAULT_SITE_NEW_STREAM);
        assertThat(testUserSettings.getSiteUpcomingStreamReminders()).isEqualTo(UPDATED_SITE_UPCOMING_STREAM_REMINDERS);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .darkMode(UPDATED_DARK_MODE)
            .language(UPDATED_LANGUAGE)
            .contentFilter(UPDATED_CONTENT_FILTER)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .activityStatusVisibility(UPDATED_ACTIVITY_STATUS_VISIBILITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .importantSubscriptionNotifications(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .smsNewStream(UPDATED_SMS_NEW_STREAM)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteNewStream(UPDATED_SITE_NEW_STREAM)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserSettings.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
        assertThat(testUserSettings.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testUserSettings.getContentFilter()).isEqualTo(UPDATED_CONTENT_FILTER);
        assertThat(testUserSettings.getMessageBlurIntensity()).isEqualTo(UPDATED_MESSAGE_BLUR_INTENSITY);
        assertThat(testUserSettings.getActivityStatusVisibility()).isEqualTo(UPDATED_ACTIVITY_STATUS_VISIBILITY);
        assertThat(testUserSettings.getTwoFactorAuthentication()).isEqualTo(UPDATED_TWO_FACTOR_AUTHENTICATION);
        assertThat(testUserSettings.getSessionsActiveCount()).isEqualTo(UPDATED_SESSIONS_ACTIVE_COUNT);
        assertThat(testUserSettings.getEmailNotifications()).isEqualTo(UPDATED_EMAIL_NOTIFICATIONS);
        assertThat(testUserSettings.getImportantSubscriptionNotifications()).isEqualTo(UPDATED_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
        assertThat(testUserSettings.getNewMessages()).isEqualTo(UPDATED_NEW_MESSAGES);
        assertThat(testUserSettings.getPostReplies()).isEqualTo(UPDATED_POST_REPLIES);
        assertThat(testUserSettings.getPostLikes()).isEqualTo(UPDATED_POST_LIKES);
        assertThat(testUserSettings.getNewFollowers()).isEqualTo(UPDATED_NEW_FOLLOWERS);
        assertThat(testUserSettings.getSmsNewStream()).isEqualTo(UPDATED_SMS_NEW_STREAM);
        assertThat(testUserSettings.getToastNewComment()).isEqualTo(UPDATED_TOAST_NEW_COMMENT);
        assertThat(testUserSettings.getToastNewLikes()).isEqualTo(UPDATED_TOAST_NEW_LIKES);
        assertThat(testUserSettings.getToastNewStream()).isEqualTo(UPDATED_TOAST_NEW_STREAM);
        assertThat(testUserSettings.getSiteNewComment()).isEqualTo(UPDATED_SITE_NEW_COMMENT);
        assertThat(testUserSettings.getSiteNewLikes()).isEqualTo(UPDATED_SITE_NEW_LIKES);
        assertThat(testUserSettings.getSiteDiscountsFromFollowedUsers()).isEqualTo(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
        assertThat(testUserSettings.getSiteNewStream()).isEqualTo(UPDATED_SITE_NEW_STREAM);
        assertThat(testUserSettings.getSiteUpcomingStreamReminders()).isEqualTo(UPDATED_SITE_UPCOMING_STREAM_REMINDERS);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userSettingsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserSettings() {
        // Initialize the database
        userSettingsRepository.save(userSettings).block();
        userSettingsRepository.save(userSettings).block();
        userSettingsSearchRepository.save(userSettings).block();

        int databaseSizeBeforeDelete = userSettingsRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userSettings
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userSettings.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserSettings> userSettingsList = userSettingsRepository.findAll().collectList().block();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userSettingsSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserSettings() {
        // Initialize the database
        userSettings = userSettingsRepository.save(userSettings).block();
        userSettingsSearchRepository.save(userSettings).block();

        // Search the userSettings
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userSettings.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userSettings.getId().intValue()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].darkMode")
            .value(hasItem(DEFAULT_DARK_MODE.booleanValue()))
            .jsonPath("$.[*].language")
            .value(hasItem(DEFAULT_LANGUAGE.toString()))
            .jsonPath("$.[*].contentFilter")
            .value(hasItem(DEFAULT_CONTENT_FILTER.booleanValue()))
            .jsonPath("$.[*].messageBlurIntensity")
            .value(hasItem(DEFAULT_MESSAGE_BLUR_INTENSITY))
            .jsonPath("$.[*].activityStatusVisibility")
            .value(hasItem(DEFAULT_ACTIVITY_STATUS_VISIBILITY.booleanValue()))
            .jsonPath("$.[*].twoFactorAuthentication")
            .value(hasItem(DEFAULT_TWO_FACTOR_AUTHENTICATION.booleanValue()))
            .jsonPath("$.[*].sessionsActiveCount")
            .value(hasItem(DEFAULT_SESSIONS_ACTIVE_COUNT))
            .jsonPath("$.[*].emailNotifications")
            .value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.[*].importantSubscriptionNotifications")
            .value(hasItem(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS.booleanValue()))
            .jsonPath("$.[*].newMessages")
            .value(hasItem(DEFAULT_NEW_MESSAGES.booleanValue()))
            .jsonPath("$.[*].postReplies")
            .value(hasItem(DEFAULT_POST_REPLIES.booleanValue()))
            .jsonPath("$.[*].postLikes")
            .value(hasItem(DEFAULT_POST_LIKES.booleanValue()))
            .jsonPath("$.[*].newFollowers")
            .value(hasItem(DEFAULT_NEW_FOLLOWERS.booleanValue()))
            .jsonPath("$.[*].smsNewStream")
            .value(hasItem(DEFAULT_SMS_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].toastNewComment")
            .value(hasItem(DEFAULT_TOAST_NEW_COMMENT.booleanValue()))
            .jsonPath("$.[*].toastNewLikes")
            .value(hasItem(DEFAULT_TOAST_NEW_LIKES.booleanValue()))
            .jsonPath("$.[*].toastNewStream")
            .value(hasItem(DEFAULT_TOAST_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].siteNewComment")
            .value(hasItem(DEFAULT_SITE_NEW_COMMENT.booleanValue()))
            .jsonPath("$.[*].siteNewLikes")
            .value(hasItem(DEFAULT_SITE_NEW_LIKES.booleanValue()))
            .jsonPath("$.[*].siteDiscountsFromFollowedUsers")
            .value(hasItem(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS.booleanValue()))
            .jsonPath("$.[*].siteNewStream")
            .value(hasItem(DEFAULT_SITE_NEW_STREAM.booleanValue()))
            .jsonPath("$.[*].siteUpcomingStreamReminders")
            .value(hasItem(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS.booleanValue()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
