package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.UserSettings;
import com.monsterdam.profile.domain.enumeration.UserLanguage;
import com.monsterdam.profile.repository.UserSettingsRepository;
import com.monsterdam.profile.service.dto.UserSettingsDTO;
import com.monsterdam.profile.service.mapper.UserSettingsMapper;
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
 * Integration tests for the {@link UserSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private UserSettingsMapper userSettingsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSettingsMockMvc;

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

    @BeforeEach
    public void initTest() {
        userSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createUserSettings() throws Exception {
        int databaseSizeBeforeCreate = userSettingsRepository.findAll().size();
        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);
        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate + 1);
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
    @Transactional
    void createUserSettingsWithExistingId() throws Exception {
        // Create the UserSettings with an existing ID
        userSettings.setId(1L);
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        int databaseSizeBeforeCreate = userSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDarkModeIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setDarkMode(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setLanguage(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentFilterIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setContentFilter(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityStatusVisibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setActivityStatusVisibility(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTwoFactorAuthenticationIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setTwoFactorAuthentication(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setEmailNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportantSubscriptionNotificationsIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setImportantSubscriptionNotifications(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewMessagesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setNewMessages(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostRepliesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setPostReplies(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setPostLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNewFollowersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setNewFollowers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSmsNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSmsNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setToastNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setToastNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToastNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setToastNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSiteNewComment(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewLikesIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSiteNewLikes(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteDiscountsFromFollowedUsersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSiteDiscountsFromFollowedUsers(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteNewStreamIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSiteNewStream(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSiteUpcomingStreamRemindersIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setSiteUpcomingStreamReminders(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setCreatedDate(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSettingsRepository.findAll().size();
        // set the field null
        userSettings.setIsDeleted(null);

        // Create the UserSettings, which fails.
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        restUserSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        // Get all the userSettingsList
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].darkMode").value(hasItem(DEFAULT_DARK_MODE.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].contentFilter").value(hasItem(DEFAULT_CONTENT_FILTER.booleanValue())))
            .andExpect(jsonPath("$.[*].messageBlurIntensity").value(hasItem(DEFAULT_MESSAGE_BLUR_INTENSITY)))
            .andExpect(jsonPath("$.[*].activityStatusVisibility").value(hasItem(DEFAULT_ACTIVITY_STATUS_VISIBILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].twoFactorAuthentication").value(hasItem(DEFAULT_TWO_FACTOR_AUTHENTICATION.booleanValue())))
            .andExpect(jsonPath("$.[*].sessionsActiveCount").value(hasItem(DEFAULT_SESSIONS_ACTIVE_COUNT)))
            .andExpect(jsonPath("$.[*].emailNotifications").value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue())))
            .andExpect(
                jsonPath("$.[*].importantSubscriptionNotifications")
                    .value(hasItem(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].newMessages").value(hasItem(DEFAULT_NEW_MESSAGES.booleanValue())))
            .andExpect(jsonPath("$.[*].postReplies").value(hasItem(DEFAULT_POST_REPLIES.booleanValue())))
            .andExpect(jsonPath("$.[*].postLikes").value(hasItem(DEFAULT_POST_LIKES.booleanValue())))
            .andExpect(jsonPath("$.[*].newFollowers").value(hasItem(DEFAULT_NEW_FOLLOWERS.booleanValue())))
            .andExpect(jsonPath("$.[*].smsNewStream").value(hasItem(DEFAULT_SMS_NEW_STREAM.booleanValue())))
            .andExpect(jsonPath("$.[*].toastNewComment").value(hasItem(DEFAULT_TOAST_NEW_COMMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].toastNewLikes").value(hasItem(DEFAULT_TOAST_NEW_LIKES.booleanValue())))
            .andExpect(jsonPath("$.[*].toastNewStream").value(hasItem(DEFAULT_TOAST_NEW_STREAM.booleanValue())))
            .andExpect(jsonPath("$.[*].siteNewComment").value(hasItem(DEFAULT_SITE_NEW_COMMENT.booleanValue())))
            .andExpect(jsonPath("$.[*].siteNewLikes").value(hasItem(DEFAULT_SITE_NEW_LIKES.booleanValue())))
            .andExpect(
                jsonPath("$.[*].siteDiscountsFromFollowedUsers").value(hasItem(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS.booleanValue()))
            )
            .andExpect(jsonPath("$.[*].siteNewStream").value(hasItem(DEFAULT_SITE_NEW_STREAM.booleanValue())))
            .andExpect(jsonPath("$.[*].siteUpcomingStreamReminders").value(hasItem(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        // Get the userSettings
        restUserSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, userSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSettings.getId().intValue()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.darkMode").value(DEFAULT_DARK_MODE.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.contentFilter").value(DEFAULT_CONTENT_FILTER.booleanValue()))
            .andExpect(jsonPath("$.messageBlurIntensity").value(DEFAULT_MESSAGE_BLUR_INTENSITY))
            .andExpect(jsonPath("$.activityStatusVisibility").value(DEFAULT_ACTIVITY_STATUS_VISIBILITY.booleanValue()))
            .andExpect(jsonPath("$.twoFactorAuthentication").value(DEFAULT_TWO_FACTOR_AUTHENTICATION.booleanValue()))
            .andExpect(jsonPath("$.sessionsActiveCount").value(DEFAULT_SESSIONS_ACTIVE_COUNT))
            .andExpect(jsonPath("$.emailNotifications").value(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.importantSubscriptionNotifications").value(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.newMessages").value(DEFAULT_NEW_MESSAGES.booleanValue()))
            .andExpect(jsonPath("$.postReplies").value(DEFAULT_POST_REPLIES.booleanValue()))
            .andExpect(jsonPath("$.postLikes").value(DEFAULT_POST_LIKES.booleanValue()))
            .andExpect(jsonPath("$.newFollowers").value(DEFAULT_NEW_FOLLOWERS.booleanValue()))
            .andExpect(jsonPath("$.smsNewStream").value(DEFAULT_SMS_NEW_STREAM.booleanValue()))
            .andExpect(jsonPath("$.toastNewComment").value(DEFAULT_TOAST_NEW_COMMENT.booleanValue()))
            .andExpect(jsonPath("$.toastNewLikes").value(DEFAULT_TOAST_NEW_LIKES.booleanValue()))
            .andExpect(jsonPath("$.toastNewStream").value(DEFAULT_TOAST_NEW_STREAM.booleanValue()))
            .andExpect(jsonPath("$.siteNewComment").value(DEFAULT_SITE_NEW_COMMENT.booleanValue()))
            .andExpect(jsonPath("$.siteNewLikes").value(DEFAULT_SITE_NEW_LIKES.booleanValue()))
            .andExpect(jsonPath("$.siteDiscountsFromFollowedUsers").value(DEFAULT_SITE_DISCOUNTS_FROM_FOLLOWED_USERS.booleanValue()))
            .andExpect(jsonPath("$.siteNewStream").value(DEFAULT_SITE_NEW_STREAM.booleanValue()))
            .andExpect(jsonPath("$.siteUpcomingStreamReminders").value(DEFAULT_SITE_UPCOMING_STREAM_REMINDERS.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserSettings() throws Exception {
        // Get the userSettings
        restUserSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

        // Update the userSettings
        UserSettings updatedUserSettings = userSettingsRepository.findById(userSettings.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSettings are not directly saved in db
        em.detach(updatedUserSettings);
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

        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
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
    @Transactional
    void putNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

        // Update the userSettings using partial update
        UserSettings partialUpdatedUserSettings = new UserSettings();
        partialUpdatedUserSettings.setId(userSettings.getId());

        partialUpdatedUserSettings
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .darkMode(UPDATED_DARK_MODE)
            .messageBlurIntensity(UPDATED_MESSAGE_BLUR_INTENSITY)
            .twoFactorAuthentication(UPDATED_TWO_FACTOR_AUTHENTICATION)
            .sessionsActiveCount(UPDATED_SESSIONS_ACTIVE_COUNT)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .newMessages(UPDATED_NEW_MESSAGES)
            .postReplies(UPDATED_POST_REPLIES)
            .postLikes(UPDATED_POST_LIKES)
            .newFollowers(UPDATED_NEW_FOLLOWERS)
            .toastNewComment(UPDATED_TOAST_NEW_COMMENT)
            .toastNewLikes(UPDATED_TOAST_NEW_LIKES)
            .toastNewStream(UPDATED_TOAST_NEW_STREAM)
            .siteNewComment(UPDATED_SITE_NEW_COMMENT)
            .siteNewLikes(UPDATED_SITE_NEW_LIKES)
            .siteDiscountsFromFollowedUsers(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS)
            .siteUpcomingStreamReminders(UPDATED_SITE_UPCOMING_STREAM_REMINDERS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
        UserSettings testUserSettings = userSettingsList.get(userSettingsList.size() - 1);
        assertThat(testUserSettings.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserSettings.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
        assertThat(testUserSettings.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testUserSettings.getContentFilter()).isEqualTo(DEFAULT_CONTENT_FILTER);
        assertThat(testUserSettings.getMessageBlurIntensity()).isEqualTo(UPDATED_MESSAGE_BLUR_INTENSITY);
        assertThat(testUserSettings.getActivityStatusVisibility()).isEqualTo(DEFAULT_ACTIVITY_STATUS_VISIBILITY);
        assertThat(testUserSettings.getTwoFactorAuthentication()).isEqualTo(UPDATED_TWO_FACTOR_AUTHENTICATION);
        assertThat(testUserSettings.getSessionsActiveCount()).isEqualTo(UPDATED_SESSIONS_ACTIVE_COUNT);
        assertThat(testUserSettings.getEmailNotifications()).isEqualTo(UPDATED_EMAIL_NOTIFICATIONS);
        assertThat(testUserSettings.getImportantSubscriptionNotifications()).isEqualTo(DEFAULT_IMPORTANT_SUBSCRIPTION_NOTIFICATIONS);
        assertThat(testUserSettings.getNewMessages()).isEqualTo(UPDATED_NEW_MESSAGES);
        assertThat(testUserSettings.getPostReplies()).isEqualTo(UPDATED_POST_REPLIES);
        assertThat(testUserSettings.getPostLikes()).isEqualTo(UPDATED_POST_LIKES);
        assertThat(testUserSettings.getNewFollowers()).isEqualTo(UPDATED_NEW_FOLLOWERS);
        assertThat(testUserSettings.getSmsNewStream()).isEqualTo(DEFAULT_SMS_NEW_STREAM);
        assertThat(testUserSettings.getToastNewComment()).isEqualTo(UPDATED_TOAST_NEW_COMMENT);
        assertThat(testUserSettings.getToastNewLikes()).isEqualTo(UPDATED_TOAST_NEW_LIKES);
        assertThat(testUserSettings.getToastNewStream()).isEqualTo(UPDATED_TOAST_NEW_STREAM);
        assertThat(testUserSettings.getSiteNewComment()).isEqualTo(UPDATED_SITE_NEW_COMMENT);
        assertThat(testUserSettings.getSiteNewLikes()).isEqualTo(UPDATED_SITE_NEW_LIKES);
        assertThat(testUserSettings.getSiteDiscountsFromFollowedUsers()).isEqualTo(UPDATED_SITE_DISCOUNTS_FROM_FOLLOWED_USERS);
        assertThat(testUserSettings.getSiteNewStream()).isEqualTo(DEFAULT_SITE_NEW_STREAM);
        assertThat(testUserSettings.getSiteUpcomingStreamReminders()).isEqualTo(UPDATED_SITE_UPCOMING_STREAM_REMINDERS);
        assertThat(testUserSettings.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserSettings.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserSettings.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserSettings.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateUserSettingsWithPatch() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();

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

        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSettings))
            )
            .andExpect(status().isOk());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
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
    @Transactional
    void patchNonExistingUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSettingsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSettings() throws Exception {
        int databaseSizeBeforeUpdate = userSettingsRepository.findAll().size();
        userSettings.setId(longCount.incrementAndGet());

        // Create the UserSettings
        UserSettingsDTO userSettingsDTO = userSettingsMapper.toDto(userSettings);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSettingsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSettings in the database
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSettings() throws Exception {
        // Initialize the database
        userSettingsRepository.saveAndFlush(userSettings);

        int databaseSizeBeforeDelete = userSettingsRepository.findAll().size();

        // Delete the userSettings
        restUserSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserSettings> userSettingsList = userSettingsRepository.findAll();
        assertThat(userSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
