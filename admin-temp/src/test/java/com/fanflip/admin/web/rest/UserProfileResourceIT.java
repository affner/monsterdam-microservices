package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.UserLite;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.UserSettings;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.UserProfileRepository;
import com.monsterdam.admin.repository.search.UserProfileSearchRepository;
import com.monsterdam.admin.service.UserProfileService;
import com.monsterdam.admin.service.dto.UserProfileDTO;
import com.monsterdam.admin.service.mapper.UserProfileMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_EMAIL_CONTACT = "0hsa4o";
    private static final String UPDATED_EMAIL_CONTACT = "l2n";

    private static final byte[] DEFAULT_PROFILE_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PHOTO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_COVER_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COVER_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_COVER_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COVER_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PROFILE_PHOTO_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PHOTO_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_PHOTO_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_COVER_PHOTO_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_MAIN_CONTENT_URL = "AAAAAAAAAA";
    private static final String UPDATED_MAIN_CONTENT_URL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE = "37121651959";
    private static final String UPDATED_MOBILE_PHONE = "48069949865527";

    private static final String DEFAULT_WEBSITE_URL = "eur@cx_J71.z?";
    private static final String UPDATED_WEBSITE_URL = "Tp @zLB~.1";

    private static final String DEFAULT_AMAZON_WISHLIST_URL = "Cm&@].nN5`3/";
    private static final String UPDATED_AMAZON_WISHLIST_URL = ":0v'@VLt.;;Rj`";

    private static final Instant DEFAULT_LAST_LOGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIOGRAPHY = "AAAAAAAAAA";
    private static final String UPDATED_BIOGRAPHY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FREE = false;
    private static final Boolean UPDATED_IS_FREE = true;

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

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-profiles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepositoryMock;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Mock
    private UserProfileService userProfileServiceMock;

    @Autowired
    private UserProfileSearchRepository userProfileSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserProfile userProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .emailContact(DEFAULT_EMAIL_CONTACT)
            .profilePhoto(DEFAULT_PROFILE_PHOTO)
            .profilePhotoContentType(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhoto(DEFAULT_COVER_PHOTO)
            .coverPhotoContentType(DEFAULT_COVER_PHOTO_CONTENT_TYPE)
            .profilePhotoS3Key(DEFAULT_PROFILE_PHOTO_S_3_KEY)
            .coverPhotoS3Key(DEFAULT_COVER_PHOTO_S_3_KEY)
            .mainContentUrl(DEFAULT_MAIN_CONTENT_URL)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .websiteUrl(DEFAULT_WEBSITE_URL)
            .amazonWishlistUrl(DEFAULT_AMAZON_WISHLIST_URL)
            .lastLoginDate(DEFAULT_LAST_LOGIN_DATE)
            .biography(DEFAULT_BIOGRAPHY)
            .isFree(DEFAULT_IS_FREE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserLite userLite;
        userLite = em.insert(UserLiteResourceIT.createEntity(em)).block();
        userProfile.setUserLite(userLite);
        // Add required entity
        UserSettings userSettings;
        userSettings = em.insert(UserSettingsResourceIT.createEntity(em)).block();
        userProfile.setSettings(userSettings);
        return userProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserProfile createUpdatedEntity(EntityManager em) {
        UserProfile userProfile = new UserProfile()
            .emailContact(UPDATED_EMAIL_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .coverPhotoContentType(UPDATED_COVER_PHOTO_CONTENT_TYPE)
            .profilePhotoS3Key(UPDATED_PROFILE_PHOTO_S_3_KEY)
            .coverPhotoS3Key(UPDATED_COVER_PHOTO_S_3_KEY)
            .mainContentUrl(UPDATED_MAIN_CONTENT_URL)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .amazonWishlistUrl(UPDATED_AMAZON_WISHLIST_URL)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .biography(UPDATED_BIOGRAPHY)
            .isFree(UPDATED_IS_FREE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserLite userLite;
        userLite = em.insert(UserLiteResourceIT.createUpdatedEntity(em)).block();
        userProfile.setUserLite(userLite);
        // Add required entity
        UserSettings userSettings;
        userSettings = em.insert(UserSettingsResourceIT.createUpdatedEntity(em)).block();
        userProfile.setSettings(userSettings);
        return userProfile;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_user_profile__followed").block();
            em.deleteAll("rel_user_profile__blocked_list").block();
            em.deleteAll("rel_user_profile__loya_lists").block();
            em.deleteAll("rel_user_profile__subscribed").block();
            em.deleteAll("rel_user_profile__joined_events").block();
            em.deleteAll("rel_user_profile__blocked_ubications").block();
            em.deleteAll("rel_user_profile__hash_tags").block();
            em.deleteAll(UserProfile.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserLiteResourceIT.deleteEntities(em);
        UserSettingsResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        userProfileSearchRepository.deleteAll().block();
        assertThat(userProfileSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userProfile = createEntity(em);
    }

    @Test
    void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getEmailContact()).isEqualTo(DEFAULT_EMAIL_CONTACT);
        assertThat(testUserProfile.getProfilePhoto()).isEqualTo(DEFAULT_PROFILE_PHOTO);
        assertThat(testUserProfile.getProfilePhotoContentType()).isEqualTo(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getCoverPhoto()).isEqualTo(DEFAULT_COVER_PHOTO);
        assertThat(testUserProfile.getCoverPhotoContentType()).isEqualTo(DEFAULT_COVER_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getProfilePhotoS3Key()).isEqualTo(DEFAULT_PROFILE_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getCoverPhotoS3Key()).isEqualTo(DEFAULT_COVER_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getMainContentUrl()).isEqualTo(DEFAULT_MAIN_CONTENT_URL);
        assertThat(testUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testUserProfile.getWebsiteUrl()).isEqualTo(DEFAULT_WEBSITE_URL);
        assertThat(testUserProfile.getAmazonWishlistUrl()).isEqualTo(DEFAULT_AMAZON_WISHLIST_URL);
        assertThat(testUserProfile.getLastLoginDate()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testUserProfile.getBiography()).isEqualTo(DEFAULT_BIOGRAPHY);
        assertThat(testUserProfile.getIsFree()).isEqualTo(DEFAULT_IS_FREE);
        assertThat(testUserProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserProfile.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        int databaseSizeBeforeCreate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEmailContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setEmailContact(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLastLoginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setLastLoginDate(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setCreatedDate(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        // set the field null
        userProfile.setIsDeleted(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserProfiles() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        // Get all the userProfileList
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
            .value(hasItem(userProfile.getId().intValue()))
            .jsonPath("$.[*].emailContact")
            .value(hasItem(DEFAULT_EMAIL_CONTACT))
            .jsonPath("$.[*].profilePhotoContentType")
            .value(hasItem(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].profilePhoto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)))
            .jsonPath("$.[*].coverPhotoContentType")
            .value(hasItem(DEFAULT_COVER_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].coverPhoto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_COVER_PHOTO)))
            .jsonPath("$.[*].profilePhotoS3Key")
            .value(hasItem(DEFAULT_PROFILE_PHOTO_S_3_KEY))
            .jsonPath("$.[*].coverPhotoS3Key")
            .value(hasItem(DEFAULT_COVER_PHOTO_S_3_KEY))
            .jsonPath("$.[*].mainContentUrl")
            .value(hasItem(DEFAULT_MAIN_CONTENT_URL))
            .jsonPath("$.[*].mobilePhone")
            .value(hasItem(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.[*].websiteUrl")
            .value(hasItem(DEFAULT_WEBSITE_URL))
            .jsonPath("$.[*].amazonWishlistUrl")
            .value(hasItem(DEFAULT_AMAZON_WISHLIST_URL))
            .jsonPath("$.[*].lastLoginDate")
            .value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.[*].biography")
            .value(hasItem(DEFAULT_BIOGRAPHY.toString()))
            .jsonPath("$.[*].isFree")
            .value(hasItem(DEFAULT_IS_FREE.booleanValue()))
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
    void getAllUserProfilesWithEagerRelationshipsIsEnabled() {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(userProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getUserProfile() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userProfile.getId().intValue()))
            .jsonPath("$.emailContact")
            .value(is(DEFAULT_EMAIL_CONTACT))
            .jsonPath("$.profilePhotoContentType")
            .value(is(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .jsonPath("$.profilePhoto")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)))
            .jsonPath("$.coverPhotoContentType")
            .value(is(DEFAULT_COVER_PHOTO_CONTENT_TYPE))
            .jsonPath("$.coverPhoto")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_COVER_PHOTO)))
            .jsonPath("$.profilePhotoS3Key")
            .value(is(DEFAULT_PROFILE_PHOTO_S_3_KEY))
            .jsonPath("$.coverPhotoS3Key")
            .value(is(DEFAULT_COVER_PHOTO_S_3_KEY))
            .jsonPath("$.mainContentUrl")
            .value(is(DEFAULT_MAIN_CONTENT_URL))
            .jsonPath("$.mobilePhone")
            .value(is(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.websiteUrl")
            .value(is(DEFAULT_WEBSITE_URL))
            .jsonPath("$.amazonWishlistUrl")
            .value(is(DEFAULT_AMAZON_WISHLIST_URL))
            .jsonPath("$.lastLoginDate")
            .value(is(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.biography")
            .value(is(DEFAULT_BIOGRAPHY.toString()))
            .jsonPath("$.isFree")
            .value(is(DEFAULT_IS_FREE.booleanValue()))
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
    void getNonExistingUserProfile() {
        // Get the userProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        userProfileSearchRepository.save(userProfile).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).block();
        updatedUserProfile
            .emailContact(UPDATED_EMAIL_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .coverPhotoContentType(UPDATED_COVER_PHOTO_CONTENT_TYPE)
            .profilePhotoS3Key(UPDATED_PROFILE_PHOTO_S_3_KEY)
            .coverPhotoS3Key(UPDATED_COVER_PHOTO_S_3_KEY)
            .mainContentUrl(UPDATED_MAIN_CONTENT_URL)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .amazonWishlistUrl(UPDATED_AMAZON_WISHLIST_URL)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .biography(UPDATED_BIOGRAPHY)
            .isFree(UPDATED_IS_FREE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getEmailContact()).isEqualTo(UPDATED_EMAIL_CONTACT);
        assertThat(testUserProfile.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testUserProfile.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testUserProfile.getCoverPhotoContentType()).isEqualTo(UPDATED_COVER_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getProfilePhotoS3Key()).isEqualTo(UPDATED_PROFILE_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getCoverPhotoS3Key()).isEqualTo(UPDATED_COVER_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getMainContentUrl()).isEqualTo(UPDATED_MAIN_CONTENT_URL);
        assertThat(testUserProfile.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testUserProfile.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
        assertThat(testUserProfile.getAmazonWishlistUrl()).isEqualTo(UPDATED_AMAZON_WISHLIST_URL);
        assertThat(testUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testUserProfile.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserProfile.getIsFree()).isEqualTo(UPDATED_IS_FREE);
        assertThat(testUserProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserProfile> userProfileSearchList = IterableUtils.toList(userProfileSearchRepository.findAll().collectList().block());
                UserProfile testUserProfileSearch = userProfileSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserProfileSearch.getEmailContact()).isEqualTo(UPDATED_EMAIL_CONTACT);
                assertThat(testUserProfileSearch.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
                assertThat(testUserProfileSearch.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
                assertThat(testUserProfileSearch.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
                assertThat(testUserProfileSearch.getCoverPhotoContentType()).isEqualTo(UPDATED_COVER_PHOTO_CONTENT_TYPE);
                assertThat(testUserProfileSearch.getProfilePhotoS3Key()).isEqualTo(UPDATED_PROFILE_PHOTO_S_3_KEY);
                assertThat(testUserProfileSearch.getCoverPhotoS3Key()).isEqualTo(UPDATED_COVER_PHOTO_S_3_KEY);
                assertThat(testUserProfileSearch.getMainContentUrl()).isEqualTo(UPDATED_MAIN_CONTENT_URL);
                assertThat(testUserProfileSearch.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
                assertThat(testUserProfileSearch.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
                assertThat(testUserProfileSearch.getAmazonWishlistUrl()).isEqualTo(UPDATED_AMAZON_WISHLIST_URL);
                assertThat(testUserProfileSearch.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
                assertThat(testUserProfileSearch.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
                assertThat(testUserProfileSearch.getIsFree()).isEqualTo(UPDATED_IS_FREE);
                assertThat(testUserProfileSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserProfileSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserProfileSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserProfileSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserProfileSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .emailContact(UPDATED_EMAIL_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .coverPhotoContentType(UPDATED_COVER_PHOTO_CONTENT_TYPE)
            .coverPhotoS3Key(UPDATED_COVER_PHOTO_S_3_KEY)
            .mainContentUrl(UPDATED_MAIN_CONTENT_URL)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .biography(UPDATED_BIOGRAPHY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getEmailContact()).isEqualTo(UPDATED_EMAIL_CONTACT);
        assertThat(testUserProfile.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testUserProfile.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testUserProfile.getCoverPhotoContentType()).isEqualTo(UPDATED_COVER_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getProfilePhotoS3Key()).isEqualTo(DEFAULT_PROFILE_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getCoverPhotoS3Key()).isEqualTo(UPDATED_COVER_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getMainContentUrl()).isEqualTo(UPDATED_MAIN_CONTENT_URL);
        assertThat(testUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testUserProfile.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
        assertThat(testUserProfile.getAmazonWishlistUrl()).isEqualTo(DEFAULT_AMAZON_WISHLIST_URL);
        assertThat(testUserProfile.getLastLoginDate()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testUserProfile.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserProfile.getIsFree()).isEqualTo(DEFAULT_IS_FREE);
        assertThat(testUserProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserProfile.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.save(userProfile).block();

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .emailContact(UPDATED_EMAIL_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhoto(UPDATED_COVER_PHOTO)
            .coverPhotoContentType(UPDATED_COVER_PHOTO_CONTENT_TYPE)
            .profilePhotoS3Key(UPDATED_PROFILE_PHOTO_S_3_KEY)
            .coverPhotoS3Key(UPDATED_COVER_PHOTO_S_3_KEY)
            .mainContentUrl(UPDATED_MAIN_CONTENT_URL)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .websiteUrl(UPDATED_WEBSITE_URL)
            .amazonWishlistUrl(UPDATED_AMAZON_WISHLIST_URL)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .biography(UPDATED_BIOGRAPHY)
            .isFree(UPDATED_IS_FREE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getEmailContact()).isEqualTo(UPDATED_EMAIL_CONTACT);
        assertThat(testUserProfile.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testUserProfile.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getCoverPhoto()).isEqualTo(UPDATED_COVER_PHOTO);
        assertThat(testUserProfile.getCoverPhotoContentType()).isEqualTo(UPDATED_COVER_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getProfilePhotoS3Key()).isEqualTo(UPDATED_PROFILE_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getCoverPhotoS3Key()).isEqualTo(UPDATED_COVER_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getMainContentUrl()).isEqualTo(UPDATED_MAIN_CONTENT_URL);
        assertThat(testUserProfile.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testUserProfile.getWebsiteUrl()).isEqualTo(UPDATED_WEBSITE_URL);
        assertThat(testUserProfile.getAmazonWishlistUrl()).isEqualTo(UPDATED_AMAZON_WISHLIST_URL);
        assertThat(testUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testUserProfile.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserProfile.getIsFree()).isEqualTo(UPDATED_IS_FREE);
        assertThat(testUserProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userProfileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserProfile() {
        // Initialize the database
        userProfileRepository.save(userProfile).block();
        userProfileRepository.save(userProfile).block();
        userProfileSearchRepository.save(userProfile).block();

        int databaseSizeBeforeDelete = userProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userProfile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll().collectList().block();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserProfile() {
        // Initialize the database
        userProfile = userProfileRepository.save(userProfile).block();
        userProfileSearchRepository.save(userProfile).block();

        // Search the userProfile
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userProfile.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userProfile.getId().intValue()))
            .jsonPath("$.[*].emailContact")
            .value(hasItem(DEFAULT_EMAIL_CONTACT))
            .jsonPath("$.[*].profilePhotoContentType")
            .value(hasItem(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].profilePhoto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)))
            .jsonPath("$.[*].coverPhotoContentType")
            .value(hasItem(DEFAULT_COVER_PHOTO_CONTENT_TYPE))
            .jsonPath("$.[*].coverPhoto")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_COVER_PHOTO)))
            .jsonPath("$.[*].profilePhotoS3Key")
            .value(hasItem(DEFAULT_PROFILE_PHOTO_S_3_KEY))
            .jsonPath("$.[*].coverPhotoS3Key")
            .value(hasItem(DEFAULT_COVER_PHOTO_S_3_KEY))
            .jsonPath("$.[*].mainContentUrl")
            .value(hasItem(DEFAULT_MAIN_CONTENT_URL))
            .jsonPath("$.[*].mobilePhone")
            .value(hasItem(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.[*].websiteUrl")
            .value(hasItem(DEFAULT_WEBSITE_URL))
            .jsonPath("$.[*].amazonWishlistUrl")
            .value(hasItem(DEFAULT_AMAZON_WISHLIST_URL))
            .jsonPath("$.[*].lastLoginDate")
            .value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.[*].biography")
            .value(hasItem(DEFAULT_BIOGRAPHY.toString()))
            .jsonPath("$.[*].isFree")
            .value(hasItem(DEFAULT_IS_FREE.booleanValue()))
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
