package com.fanflip.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.profile.IntegrationTest;
import com.fanflip.profile.domain.UserLite;
import com.fanflip.profile.domain.enumeration.ContentPreference;
import com.fanflip.profile.domain.enumeration.UserGender;
import com.fanflip.profile.repository.UserLiteRepository;
import com.fanflip.profile.service.dto.UserLiteDTO;
import com.fanflip.profile.service.mapper.UserLiteMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link UserLiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserLiteResourceIT {

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final UserGender DEFAULT_GENDER = UserGender.MALE;
    private static final UserGender UPDATED_GENDER = UserGender.FEMALE;

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

    private static final String DEFAULT_NICK_NAME = "somvl0bit-a";
    private static final String UPDATED_NICK_NAME = "6g5nxzo";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final ContentPreference DEFAULT_CONTENT_PREFERENCE = ContentPreference.ALL;
    private static final ContentPreference UPDATED_CONTENT_PREFERENCE = ContentPreference.STRAIGHT;

    private static final Long DEFAULT_COUNTRY_OF_BIRTH_ID = 1L;
    private static final Long UPDATED_COUNTRY_OF_BIRTH_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-lites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserLiteRepository userLiteRepository;

    @Autowired
    private UserLiteMapper userLiteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserLiteMockMvc;

    private UserLite userLite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLite createEntity(EntityManager em) {
        UserLite userLite = new UserLite()
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .nickName(DEFAULT_NICK_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .contentPreference(DEFAULT_CONTENT_PREFERENCE)
            .countryOfBirthId(DEFAULT_COUNTRY_OF_BIRTH_ID);
        return userLite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserLite createUpdatedEntity(EntityManager em) {
        UserLite userLite = new UserLite()
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME)
            .contentPreference(UPDATED_CONTENT_PREFERENCE)
            .countryOfBirthId(UPDATED_COUNTRY_OF_BIRTH_ID);
        return userLite;
    }

    @BeforeEach
    public void initTest() {
        userLite = createEntity(em);
    }

    @Test
    @Transactional
    void createUserLite() throws Exception {
        int databaseSizeBeforeCreate = userLiteRepository.findAll().size();
        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);
        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isCreated());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeCreate + 1);
        UserLite testUserLite = userLiteList.get(userLiteList.size() - 1);
        assertThat(testUserLite.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testUserLite.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testUserLite.getThumbnailS3Key()).isEqualTo(DEFAULT_THUMBNAIL_S_3_KEY);
        assertThat(testUserLite.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUserLite.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserLite.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserLite.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserLite.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserLite.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserLite.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserLite.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testUserLite.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUserLite.getContentPreference()).isEqualTo(DEFAULT_CONTENT_PREFERENCE);
        assertThat(testUserLite.getCountryOfBirthId()).isEqualTo(DEFAULT_COUNTRY_OF_BIRTH_ID);
    }

    @Test
    @Transactional
    void createUserLiteWithExistingId() throws Exception {
        // Create the UserLite with an existing ID
        userLite.setId(1L);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        int databaseSizeBeforeCreate = userLiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setBirthDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setGender(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setCreatedDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setIsDeleted(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNickNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setNickName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setFullName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContentPreferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().size();
        // set the field null
        userLite.setContentPreference(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        restUserLiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isBadRequest());

        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserLites() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        // Get all the userLiteList
        restUserLiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userLite.getId().intValue())))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].nickName").value(hasItem(DEFAULT_NICK_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].contentPreference").value(hasItem(DEFAULT_CONTENT_PREFERENCE.toString())))
            .andExpect(jsonPath("$.[*].countryOfBirthId").value(hasItem(DEFAULT_COUNTRY_OF_BIRTH_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserLite() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        // Get the userLite
        restUserLiteMockMvc
            .perform(get(ENTITY_API_URL_ID, userLite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userLite.getId().intValue()))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.nickName").value(DEFAULT_NICK_NAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.contentPreference").value(DEFAULT_CONTENT_PREFERENCE.toString()))
            .andExpect(jsonPath("$.countryOfBirthId").value(DEFAULT_COUNTRY_OF_BIRTH_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserLite() throws Exception {
        // Get the userLite
        restUserLiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserLite() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();

        // Update the userLite
        UserLite updatedUserLite = userLiteRepository.findById(userLite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserLite are not directly saved in db
        em.detach(updatedUserLite);
        updatedUserLite
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME)
            .contentPreference(UPDATED_CONTENT_PREFERENCE)
            .countryOfBirthId(UPDATED_COUNTRY_OF_BIRTH_ID);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(updatedUserLite);

        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        UserLite testUserLite = userLiteList.get(userLiteList.size() - 1);
        assertThat(testUserLite.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testUserLite.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testUserLite.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testUserLite.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserLite.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserLite.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserLite.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserLite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserLite.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserLite.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserLite.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testUserLite.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUserLite.getContentPreference()).isEqualTo(UPDATED_CONTENT_PREFERENCE);
        assertThat(testUserLite.getCountryOfBirthId()).isEqualTo(UPDATED_COUNTRY_OF_BIRTH_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userLiteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();

        // Update the userLite using partial update
        UserLite partialUpdatedUserLite = new UserLite();
        partialUpdatedUserLite.setId(userLite.getId());

        partialUpdatedUserLite
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .isDeleted(UPDATED_IS_DELETED)
            .nickName(UPDATED_NICK_NAME)
            .contentPreference(UPDATED_CONTENT_PREFERENCE)
            .countryOfBirthId(UPDATED_COUNTRY_OF_BIRTH_ID);

        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLite))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        UserLite testUserLite = userLiteList.get(userLiteList.size() - 1);
        assertThat(testUserLite.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testUserLite.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testUserLite.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testUserLite.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserLite.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserLite.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserLite.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserLite.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserLite.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserLite.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserLite.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testUserLite.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUserLite.getContentPreference()).isEqualTo(UPDATED_CONTENT_PREFERENCE);
        assertThat(testUserLite.getCountryOfBirthId()).isEqualTo(UPDATED_COUNTRY_OF_BIRTH_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();

        // Update the userLite using partial update
        UserLite partialUpdatedUserLite = new UserLite();
        partialUpdatedUserLite.setId(userLite.getId());

        partialUpdatedUserLite
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .nickName(UPDATED_NICK_NAME)
            .fullName(UPDATED_FULL_NAME)
            .contentPreference(UPDATED_CONTENT_PREFERENCE)
            .countryOfBirthId(UPDATED_COUNTRY_OF_BIRTH_ID);

        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLite))
            )
            .andExpect(status().isOk());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        UserLite testUserLite = userLiteList.get(userLiteList.size() - 1);
        assertThat(testUserLite.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testUserLite.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testUserLite.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testUserLite.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testUserLite.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserLite.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserLite.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserLite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserLite.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserLite.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserLite.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testUserLite.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testUserLite.getContentPreference()).isEqualTo(UPDATED_CONTENT_PREFERENCE);
        assertThat(testUserLite.getCountryOfBirthId()).isEqualTo(UPDATED_COUNTRY_OF_BIRTH_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userLiteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().size();
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserLiteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserLite() throws Exception {
        // Initialize the database
        userLiteRepository.saveAndFlush(userLite);

        int databaseSizeBeforeDelete = userLiteRepository.findAll().size();

        // Delete the userLite
        restUserLiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, userLite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserLite> userLiteList = userLiteRepository.findAll();
        assertThat(userLiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
