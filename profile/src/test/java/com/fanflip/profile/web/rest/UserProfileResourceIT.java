package com.fanflip.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.profile.IntegrationTest;
import com.fanflip.profile.domain.UserProfile;
import com.fanflip.profile.domain.UserSettings;
import com.fanflip.profile.repository.UserProfileRepository;
import com.fanflip.profile.service.UserProfileService;
import com.fanflip.profile.service.dto.UserProfileDTO;
import com.fanflip.profile.service.mapper.UserProfileMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link UserProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserProfileResourceIT {

    private static final String DEFAULT_EMAIL_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_CONTACT = "BBBBBBBBBB";

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

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_AMAZON_WISHLIST_URL = "AAAAAAAAAA";
    private static final String UPDATED_AMAZON_WISHLIST_URL = "BBBBBBBBBB";

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

    private static final Long DEFAULT_STATE_OF_RESIDENCE_ID = 1L;
    private static final Long UPDATED_STATE_OF_RESIDENCE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

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
    private EntityManager em;

    @Autowired
    private MockMvc restUserProfileMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .stateOfResidenceId(DEFAULT_STATE_OF_RESIDENCE_ID);
        // Add required entity
        UserSettings userSettings;
        if (TestUtil.findAll(em, UserSettings.class).isEmpty()) {
            userSettings = UserSettingsResourceIT.createEntity(em);
            em.persist(userSettings);
            em.flush();
        } else {
            userSettings = TestUtil.findAll(em, UserSettings.class).get(0);
        }
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
            .isDeleted(UPDATED_IS_DELETED)
            .stateOfResidenceId(UPDATED_STATE_OF_RESIDENCE_ID);
        // Add required entity
        UserSettings userSettings;
        if (TestUtil.findAll(em, UserSettings.class).isEmpty()) {
            userSettings = UserSettingsResourceIT.createUpdatedEntity(em);
            em.persist(userSettings);
            em.flush();
        } else {
            userSettings = TestUtil.findAll(em, UserSettings.class).get(0);
        }
        userProfile.setSettings(userSettings);
        return userProfile;
    }

    @BeforeEach
    public void initTest() {
        userProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();
        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate + 1);
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
        assertThat(testUserProfile.getStateOfResidenceId()).isEqualTo(DEFAULT_STATE_OF_RESIDENCE_ID);
    }

    @Test
    @Transactional
    void createUserProfileWithExistingId() throws Exception {
        // Create the UserProfile with an existing ID
        userProfile.setId(1L);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setEmailContact(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastLoginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setLastLoginDate(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setCreatedDate(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userProfileRepository.findAll().size();
        // set the field null
        userProfile.setIsDeleted(null);

        // Create the UserProfile, which fails.
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        restUserProfileMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfileList
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailContact").value(hasItem(DEFAULT_EMAIL_CONTACT)))
            .andExpect(jsonPath("$.[*].profilePhotoContentType").value(hasItem(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePhoto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO))))
            .andExpect(jsonPath("$.[*].coverPhotoContentType").value(hasItem(DEFAULT_COVER_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].coverPhoto").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_COVER_PHOTO))))
            .andExpect(jsonPath("$.[*].profilePhotoS3Key").value(hasItem(DEFAULT_PROFILE_PHOTO_S_3_KEY)))
            .andExpect(jsonPath("$.[*].coverPhotoS3Key").value(hasItem(DEFAULT_COVER_PHOTO_S_3_KEY)))
            .andExpect(jsonPath("$.[*].mainContentUrl").value(hasItem(DEFAULT_MAIN_CONTENT_URL)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].websiteUrl").value(hasItem(DEFAULT_WEBSITE_URL)))
            .andExpect(jsonPath("$.[*].amazonWishlistUrl").value(hasItem(DEFAULT_AMAZON_WISHLIST_URL)))
            .andExpect(jsonPath("$.[*].lastLoginDate").value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].biography").value(hasItem(DEFAULT_BIOGRAPHY.toString())))
            .andExpect(jsonPath("$.[*].isFree").value(hasItem(DEFAULT_IS_FREE.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].stateOfResidenceId").value(hasItem(DEFAULT_STATE_OF_RESIDENCE_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userProfileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userProfileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.emailContact").value(DEFAULT_EMAIL_CONTACT))
            .andExpect(jsonPath("$.profilePhotoContentType").value(DEFAULT_PROFILE_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePhoto").value(Base64.getEncoder().encodeToString(DEFAULT_PROFILE_PHOTO)))
            .andExpect(jsonPath("$.coverPhotoContentType").value(DEFAULT_COVER_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.coverPhoto").value(Base64.getEncoder().encodeToString(DEFAULT_COVER_PHOTO)))
            .andExpect(jsonPath("$.profilePhotoS3Key").value(DEFAULT_PROFILE_PHOTO_S_3_KEY))
            .andExpect(jsonPath("$.coverPhotoS3Key").value(DEFAULT_COVER_PHOTO_S_3_KEY))
            .andExpect(jsonPath("$.mainContentUrl").value(DEFAULT_MAIN_CONTENT_URL))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.websiteUrl").value(DEFAULT_WEBSITE_URL))
            .andExpect(jsonPath("$.amazonWishlistUrl").value(DEFAULT_AMAZON_WISHLIST_URL))
            .andExpect(jsonPath("$.lastLoginDate").value(DEFAULT_LAST_LOGIN_DATE.toString()))
            .andExpect(jsonPath("$.biography").value(DEFAULT_BIOGRAPHY.toString()))
            .andExpect(jsonPath("$.isFree").value(DEFAULT_IS_FREE.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.stateOfResidenceId").value(DEFAULT_STATE_OF_RESIDENCE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        UserProfile updatedUserProfile = userProfileRepository.findById(userProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserProfile are not directly saved in db
        em.detach(updatedUserProfile);
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
            .isDeleted(UPDATED_IS_DELETED)
            .stateOfResidenceId(UPDATED_STATE_OF_RESIDENCE_ID);
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(updatedUserProfile);

        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
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
        assertThat(testUserProfile.getStateOfResidenceId()).isEqualTo(UPDATED_STATE_OF_RESIDENCE_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile using partial update
        UserProfile partialUpdatedUserProfile = new UserProfile();
        partialUpdatedUserProfile.setId(userProfile.getId());

        partialUpdatedUserProfile
            .emailContact(UPDATED_EMAIL_CONTACT)
            .profilePhoto(UPDATED_PROFILE_PHOTO)
            .profilePhotoContentType(UPDATED_PROFILE_PHOTO_CONTENT_TYPE)
            .coverPhotoS3Key(UPDATED_COVER_PHOTO_S_3_KEY)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .biography(UPDATED_BIOGRAPHY)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .stateOfResidenceId(UPDATED_STATE_OF_RESIDENCE_ID);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfileList.get(userProfileList.size() - 1);
        assertThat(testUserProfile.getEmailContact()).isEqualTo(UPDATED_EMAIL_CONTACT);
        assertThat(testUserProfile.getProfilePhoto()).isEqualTo(UPDATED_PROFILE_PHOTO);
        assertThat(testUserProfile.getProfilePhotoContentType()).isEqualTo(UPDATED_PROFILE_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getCoverPhoto()).isEqualTo(DEFAULT_COVER_PHOTO);
        assertThat(testUserProfile.getCoverPhotoContentType()).isEqualTo(DEFAULT_COVER_PHOTO_CONTENT_TYPE);
        assertThat(testUserProfile.getProfilePhotoS3Key()).isEqualTo(DEFAULT_PROFILE_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getCoverPhotoS3Key()).isEqualTo(UPDATED_COVER_PHOTO_S_3_KEY);
        assertThat(testUserProfile.getMainContentUrl()).isEqualTo(DEFAULT_MAIN_CONTENT_URL);
        assertThat(testUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testUserProfile.getWebsiteUrl()).isEqualTo(DEFAULT_WEBSITE_URL);
        assertThat(testUserProfile.getAmazonWishlistUrl()).isEqualTo(DEFAULT_AMAZON_WISHLIST_URL);
        assertThat(testUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testUserProfile.getBiography()).isEqualTo(UPDATED_BIOGRAPHY);
        assertThat(testUserProfile.getIsFree()).isEqualTo(DEFAULT_IS_FREE);
        assertThat(testUserProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserProfile.getStateOfResidenceId()).isEqualTo(UPDATED_STATE_OF_RESIDENCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserProfileWithPatch() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .stateOfResidenceId(UPDATED_STATE_OF_RESIDENCE_ID);

        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserProfile))
            )
            .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
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
        assertThat(testUserProfile.getStateOfResidenceId()).isEqualTo(UPDATED_STATE_OF_RESIDENCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();
        userProfile.setId(longCount.incrementAndGet());

        // Create the UserProfile
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserProfile in the database
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Delete the userProfile
        restUserProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, userProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserProfile> userProfileList = userProfileRepository.findAll();
        assertThat(userProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
