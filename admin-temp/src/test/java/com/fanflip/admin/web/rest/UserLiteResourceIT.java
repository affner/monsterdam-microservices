package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.UserLite;
import com.fanflip.admin.domain.enumeration.ContentPreference;
import com.fanflip.admin.domain.enumeration.UserGender;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.UserLiteRepository;
import com.fanflip.admin.repository.search.UserLiteSearchRepository;
import com.fanflip.admin.service.dto.UserLiteDTO;
import com.fanflip.admin.service.mapper.UserLiteMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link UserLiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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

    private static final String DEFAULT_NICK_NAME = "afy";
    private static final String UPDATED_NICK_NAME = "jzs2zuwav";

    private static final String DEFAULT_FULL_NAME = "yjg548";
    private static final String UPDATED_FULL_NAME = "x-tq6f";

    private static final ContentPreference DEFAULT_CONTENT_PREFERENCE = ContentPreference.ALL;
    private static final ContentPreference UPDATED_CONTENT_PREFERENCE = ContentPreference.STRAIGHT;

    private static final String ENTITY_API_URL = "/api/user-lites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-lites/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserLiteRepository userLiteRepository;

    @Autowired
    private UserLiteMapper userLiteMapper;

    @Autowired
    private UserLiteSearchRepository userLiteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

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
            .contentPreference(DEFAULT_CONTENT_PREFERENCE);
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
            .contentPreference(UPDATED_CONTENT_PREFERENCE);
        return userLite;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserLite.class).block();
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
        userLiteSearchRepository.deleteAll().block();
        assertThat(userLiteSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userLite = createEntity(em);
    }

    @Test
    void createUserLite() throws Exception {
        int databaseSizeBeforeCreate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
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
    }

    @Test
    void createUserLiteWithExistingId() throws Exception {
        // Create the UserLite with an existing ID
        userLite.setId(1L);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        int databaseSizeBeforeCreate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setBirthDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setGender(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setCreatedDate(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setIsDeleted(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNickNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setNickName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setFullName(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentPreferenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        // set the field null
        userLite.setContentPreference(null);

        // Create the UserLite, which fails.
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserLites() {
        // Initialize the database
        userLiteRepository.save(userLite).block();

        // Get all the userLiteList
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
            .value(hasItem(userLite.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].birthDate")
            .value(hasItem(DEFAULT_BIRTH_DATE.toString()))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
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
            .jsonPath("$.[*].nickName")
            .value(hasItem(DEFAULT_NICK_NAME))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].contentPreference")
            .value(hasItem(DEFAULT_CONTENT_PREFERENCE.toString()));
    }

    @Test
    void getUserLite() {
        // Initialize the database
        userLiteRepository.save(userLite).block();

        // Get the userLite
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userLite.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userLite.getId().intValue()))
            .jsonPath("$.thumbnailContentType")
            .value(is(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.thumbnail")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.thumbnailS3Key")
            .value(is(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.birthDate")
            .value(is(DEFAULT_BIRTH_DATE.toString()))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
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
            .jsonPath("$.nickName")
            .value(is(DEFAULT_NICK_NAME))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME))
            .jsonPath("$.contentPreference")
            .value(is(DEFAULT_CONTENT_PREFERENCE.toString()));
    }

    @Test
    void getNonExistingUserLite() {
        // Get the userLite
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserLite() throws Exception {
        // Initialize the database
        userLiteRepository.save(userLite).block();

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        userLiteSearchRepository.save(userLite).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());

        // Update the userLite
        UserLite updatedUserLite = userLiteRepository.findById(userLite.getId()).block();
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
            .contentPreference(UPDATED_CONTENT_PREFERENCE);
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(updatedUserLite);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userLiteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
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
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserLite> userLiteSearchList = IterableUtils.toList(userLiteSearchRepository.findAll().collectList().block());
                UserLite testUserLiteSearch = userLiteSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserLiteSearch.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
                assertThat(testUserLiteSearch.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
                assertThat(testUserLiteSearch.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
                assertThat(testUserLiteSearch.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
                assertThat(testUserLiteSearch.getGender()).isEqualTo(UPDATED_GENDER);
                assertThat(testUserLiteSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserLiteSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserLiteSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserLiteSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserLiteSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testUserLiteSearch.getNickName()).isEqualTo(UPDATED_NICK_NAME);
                assertThat(testUserLiteSearch.getFullName()).isEqualTo(UPDATED_FULL_NAME);
                assertThat(testUserLiteSearch.getContentPreference()).isEqualTo(UPDATED_CONTENT_PREFERENCE);
            });
    }

    @Test
    void putNonExistingUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userLiteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        userLiteRepository.save(userLite).block();

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();

        // Update the userLite using partial update
        UserLite partialUpdatedUserLite = new UserLite();
        partialUpdatedUserLite.setId(userLite.getId());

        partialUpdatedUserLite
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .gender(UPDATED_GENDER)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        UserLite testUserLite = userLiteList.get(userLiteList.size() - 1);
        assertThat(testUserLite.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testUserLite.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testUserLite.getThumbnailS3Key()).isEqualTo(UPDATED_THUMBNAIL_S_3_KEY);
        assertThat(testUserLite.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testUserLite.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserLite.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserLite.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserLite.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserLite.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserLite.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testUserLite.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testUserLite.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testUserLite.getContentPreference()).isEqualTo(DEFAULT_CONTENT_PREFERENCE);
    }

    @Test
    void fullUpdateUserLiteWithPatch() throws Exception {
        // Initialize the database
        userLiteRepository.save(userLite).block();

        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();

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
            .contentPreference(UPDATED_CONTENT_PREFERENCE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserLite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserLite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
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
    }

    @Test
    void patchNonExistingUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userLiteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserLite() throws Exception {
        int databaseSizeBeforeUpdate = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        userLite.setId(longCount.incrementAndGet());

        // Create the UserLite
        UserLiteDTO userLiteDTO = userLiteMapper.toDto(userLite);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userLiteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserLite in the database
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserLite() {
        // Initialize the database
        userLiteRepository.save(userLite).block();
        userLiteRepository.save(userLite).block();
        userLiteSearchRepository.save(userLite).block();

        int databaseSizeBeforeDelete = userLiteRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userLite
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userLite.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserLite> userLiteList = userLiteRepository.findAll().collectList().block();
        assertThat(userLiteList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userLiteSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserLite() {
        // Initialize the database
        userLite = userLiteRepository.save(userLite).block();
        userLiteSearchRepository.save(userLite).block();

        // Search the userLite
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userLite.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userLite.getId().intValue()))
            .jsonPath("$.[*].thumbnailContentType")
            .value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .jsonPath("$.[*].thumbnail")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .jsonPath("$.[*].thumbnailS3Key")
            .value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY))
            .jsonPath("$.[*].birthDate")
            .value(hasItem(DEFAULT_BIRTH_DATE.toString()))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
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
            .jsonPath("$.[*].nickName")
            .value(hasItem(DEFAULT_NICK_NAME))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].contentPreference")
            .value(hasItem(DEFAULT_CONTENT_PREFERENCE.toString()));
    }
}
