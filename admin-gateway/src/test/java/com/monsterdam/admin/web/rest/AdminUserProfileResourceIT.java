package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.AdminUserProfile;
import com.monsterdam.admin.domain.enumeration.AdminGender;
import com.monsterdam.admin.repository.AdminUserProfileRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.AdminUserProfileSearchRepository;
import com.monsterdam.admin.service.dto.AdminUserProfileDTO;
import com.monsterdam.admin.service.mapper.AdminUserProfileMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AdminUserProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AdminUserProfileResourceIT {

    private static final String DEFAULT_FULL_NAME = "imvri";
    private static final String UPDATED_FULL_NAME = "qmj";

    private static final String DEFAULT_EMAIL_ADDRESS = "YinRs@(o.N/(l~_";
    private static final String UPDATED_EMAIL_ADDRESS = "YeBF$@/.m";

    private static final String DEFAULT_NICK_NAME = "nrt2ie8peh1bwp";
    private static final String UPDATED_NICK_NAME = "k2z4823ews1l5";

    private static final AdminGender DEFAULT_GENDER = AdminGender.MALE;
    private static final AdminGender UPDATED_GENDER = AdminGender.FEMALE;

    private static final String DEFAULT_MOBILE_PHONE = "+5411653015821";
    private static final String UPDATED_MOBILE_PHONE = "+0367253677929";

    private static final Instant DEFAULT_LAST_LOGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_LOGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/admin-user-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/admin-user-profiles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminUserProfileRepository adminUserProfileRepository;

    @Autowired
    private AdminUserProfileMapper adminUserProfileMapper;

    @Autowired
    private AdminUserProfileSearchRepository adminUserProfileSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AdminUserProfile adminUserProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminUserProfile createEntity(EntityManager em) {
        AdminUserProfile adminUserProfile = new AdminUserProfile()
            .fullName(DEFAULT_FULL_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .nickName(DEFAULT_NICK_NAME)
            .gender(DEFAULT_GENDER)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .lastLoginDate(DEFAULT_LAST_LOGIN_DATE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return adminUserProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminUserProfile createUpdatedEntity(EntityManager em) {
        AdminUserProfile adminUserProfile = new AdminUserProfile()
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return adminUserProfile;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AdminUserProfile.class).block();
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
        adminUserProfileSearchRepository.deleteAll().block();
        assertThat(adminUserProfileSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        adminUserProfile = createEntity(em);
    }

    @Test
    void createAdminUserProfile() throws Exception {
        int databaseSizeBeforeCreate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AdminUserProfile testAdminUserProfile = adminUserProfileList.get(adminUserProfileList.size() - 1);
        assertThat(testAdminUserProfile.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testAdminUserProfile.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testAdminUserProfile.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testAdminUserProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAdminUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testAdminUserProfile.getLastLoginDate()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testAdminUserProfile.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAdminUserProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminUserProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminUserProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminUserProfile.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createAdminUserProfileWithExistingId() throws Exception {
        // Create the AdminUserProfile with an existing ID
        adminUserProfile.setId(1L);
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        int databaseSizeBeforeCreate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setFullName(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setEmailAddress(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNickNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setNickName(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setGender(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLastLoginDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setLastLoginDate(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setBirthDate(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setCreatedDate(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        // set the field null
        adminUserProfile.setIsDeleted(null);

        // Create the AdminUserProfile, which fails.
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAdminUserProfilesAsStream() {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        List<AdminUserProfile> adminUserProfileList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AdminUserProfileDTO.class)
            .getResponseBody()
            .map(adminUserProfileMapper::toEntity)
            .filter(adminUserProfile::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(adminUserProfileList).isNotNull();
        assertThat(adminUserProfileList).hasSize(1);
        AdminUserProfile testAdminUserProfile = adminUserProfileList.get(0);
        assertThat(testAdminUserProfile.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testAdminUserProfile.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testAdminUserProfile.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testAdminUserProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAdminUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testAdminUserProfile.getLastLoginDate()).isEqualTo(DEFAULT_LAST_LOGIN_DATE);
        assertThat(testAdminUserProfile.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAdminUserProfile.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testAdminUserProfile.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testAdminUserProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminUserProfile.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void getAllAdminUserProfiles() {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        // Get all the adminUserProfileList
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
            .value(hasItem(adminUserProfile.getId().intValue()))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].emailAddress")
            .value(hasItem(DEFAULT_EMAIL_ADDRESS))
            .jsonPath("$.[*].nickName")
            .value(hasItem(DEFAULT_NICK_NAME))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].mobilePhone")
            .value(hasItem(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.[*].lastLoginDate")
            .value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.[*].birthDate")
            .value(hasItem(DEFAULT_BIRTH_DATE.toString()))
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

    @Test
    void getAdminUserProfile() {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        // Get the adminUserProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, adminUserProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(adminUserProfile.getId().intValue()))
            .jsonPath("$.fullName")
            .value(is(DEFAULT_FULL_NAME))
            .jsonPath("$.emailAddress")
            .value(is(DEFAULT_EMAIL_ADDRESS))
            .jsonPath("$.nickName")
            .value(is(DEFAULT_NICK_NAME))
            .jsonPath("$.gender")
            .value(is(DEFAULT_GENDER.toString()))
            .jsonPath("$.mobilePhone")
            .value(is(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.lastLoginDate")
            .value(is(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.birthDate")
            .value(is(DEFAULT_BIRTH_DATE.toString()))
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
    void getNonExistingAdminUserProfile() {
        // Get the adminUserProfile
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAdminUserProfile() throws Exception {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        adminUserProfileSearchRepository.save(adminUserProfile).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());

        // Update the adminUserProfile
        AdminUserProfile updatedAdminUserProfile = adminUserProfileRepository.findById(adminUserProfile.getId()).block();
        updatedAdminUserProfile
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(updatedAdminUserProfile);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminUserProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        AdminUserProfile testAdminUserProfile = adminUserProfileList.get(adminUserProfileList.size() - 1);
        assertThat(testAdminUserProfile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAdminUserProfile.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testAdminUserProfile.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testAdminUserProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAdminUserProfile.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testAdminUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAdminUserProfile.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAdminUserProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminUserProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminUserProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminUserProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AdminUserProfile> adminUserProfileSearchList = IterableUtils.toList(
                    adminUserProfileSearchRepository.findAll().collectList().block()
                );
                AdminUserProfile testAdminUserProfileSearch = adminUserProfileSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAdminUserProfileSearch.getFullName()).isEqualTo(UPDATED_FULL_NAME);
                assertThat(testAdminUserProfileSearch.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
                assertThat(testAdminUserProfileSearch.getNickName()).isEqualTo(UPDATED_NICK_NAME);
                assertThat(testAdminUserProfileSearch.getGender()).isEqualTo(UPDATED_GENDER);
                assertThat(testAdminUserProfileSearch.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
                assertThat(testAdminUserProfileSearch.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
                assertThat(testAdminUserProfileSearch.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
                assertThat(testAdminUserProfileSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testAdminUserProfileSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testAdminUserProfileSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testAdminUserProfileSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testAdminUserProfileSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, adminUserProfileDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAdminUserProfileWithPatch() throws Exception {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();

        // Update the adminUserProfile using partial update
        AdminUserProfile partialUpdatedAdminUserProfile = new AdminUserProfile();
        partialUpdatedAdminUserProfile.setId(adminUserProfile.getId());

        partialUpdatedAdminUserProfile
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        AdminUserProfile testAdminUserProfile = adminUserProfileList.get(adminUserProfileList.size() - 1);
        assertThat(testAdminUserProfile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAdminUserProfile.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testAdminUserProfile.getNickName()).isEqualTo(DEFAULT_NICK_NAME);
        assertThat(testAdminUserProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAdminUserProfile.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testAdminUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAdminUserProfile.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testAdminUserProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminUserProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminUserProfile.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testAdminUserProfile.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testAdminUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateAdminUserProfileWithPatch() throws Exception {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();

        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();

        // Update the adminUserProfile using partial update
        AdminUserProfile partialUpdatedAdminUserProfile = new AdminUserProfile();
        partialUpdatedAdminUserProfile.setId(adminUserProfile.getId());

        partialUpdatedAdminUserProfile
            .fullName(UPDATED_FULL_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .nickName(UPDATED_NICK_NAME)
            .gender(UPDATED_GENDER)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .lastLoginDate(UPDATED_LAST_LOGIN_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAdminUserProfile.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminUserProfile))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        AdminUserProfile testAdminUserProfile = adminUserProfileList.get(adminUserProfileList.size() - 1);
        assertThat(testAdminUserProfile.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAdminUserProfile.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testAdminUserProfile.getNickName()).isEqualTo(UPDATED_NICK_NAME);
        assertThat(testAdminUserProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAdminUserProfile.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testAdminUserProfile.getLastLoginDate()).isEqualTo(UPDATED_LAST_LOGIN_DATE);
        assertThat(testAdminUserProfile.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testAdminUserProfile.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testAdminUserProfile.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testAdminUserProfile.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testAdminUserProfile.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testAdminUserProfile.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, adminUserProfileDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAdminUserProfile() throws Exception {
        int databaseSizeBeforeUpdate = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        adminUserProfile.setId(longCount.incrementAndGet());

        // Create the AdminUserProfile
        AdminUserProfileDTO adminUserProfileDTO = adminUserProfileMapper.toDto(adminUserProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(adminUserProfileDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AdminUserProfile in the database
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAdminUserProfile() {
        // Initialize the database
        adminUserProfileRepository.save(adminUserProfile).block();
        adminUserProfileRepository.save(adminUserProfile).block();
        adminUserProfileSearchRepository.save(adminUserProfile).block();

        int databaseSizeBeforeDelete = adminUserProfileRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the adminUserProfile
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, adminUserProfile.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AdminUserProfile> adminUserProfileList = adminUserProfileRepository.findAll().collectList().block();
        assertThat(adminUserProfileList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(adminUserProfileSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAdminUserProfile() {
        // Initialize the database
        adminUserProfile = adminUserProfileRepository.save(adminUserProfile).block();
        adminUserProfileSearchRepository.save(adminUserProfile).block();

        // Search the adminUserProfile
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + adminUserProfile.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(adminUserProfile.getId().intValue()))
            .jsonPath("$.[*].fullName")
            .value(hasItem(DEFAULT_FULL_NAME))
            .jsonPath("$.[*].emailAddress")
            .value(hasItem(DEFAULT_EMAIL_ADDRESS))
            .jsonPath("$.[*].nickName")
            .value(hasItem(DEFAULT_NICK_NAME))
            .jsonPath("$.[*].gender")
            .value(hasItem(DEFAULT_GENDER.toString()))
            .jsonPath("$.[*].mobilePhone")
            .value(hasItem(DEFAULT_MOBILE_PHONE))
            .jsonPath("$.[*].lastLoginDate")
            .value(hasItem(DEFAULT_LAST_LOGIN_DATE.toString()))
            .jsonPath("$.[*].birthDate")
            .value(hasItem(DEFAULT_BIRTH_DATE.toString()))
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
