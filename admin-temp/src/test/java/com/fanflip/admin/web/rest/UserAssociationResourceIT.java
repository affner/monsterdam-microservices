package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.UserAssociation;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.enumeration.AssociationStatus;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.UserAssociationRepository;
import com.monsterdam.admin.repository.search.UserAssociationSearchRepository;
import com.monsterdam.admin.service.dto.UserAssociationDTO;
import com.monsterdam.admin.service.mapper.UserAssociationMapper;
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
 * Integration tests for the {@link UserAssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserAssociationResourceIT {

    private static final Instant DEFAULT_REQUESTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AssociationStatus DEFAULT_STATUS = AssociationStatus.REQUESTED;
    private static final AssociationStatus UPDATED_STATUS = AssociationStatus.APPROVED;

    private static final String DEFAULT_ASSOCIATION_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATION_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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

    private static final String ENTITY_API_URL = "/api/user-associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-associations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserAssociationRepository userAssociationRepository;

    @Autowired
    private UserAssociationMapper userAssociationMapper;

    @Autowired
    private UserAssociationSearchRepository userAssociationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserAssociation userAssociation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createEntity(EntityManager em) {
        UserAssociation userAssociation = new UserAssociation()
            .requestedDate(DEFAULT_REQUESTED_DATE)
            .status(DEFAULT_STATUS)
            .associationToken(DEFAULT_ASSOCIATION_TOKEN)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        userAssociation.setOwner(userProfile);
        return userAssociation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAssociation createUpdatedEntity(EntityManager em) {
        UserAssociation userAssociation = new UserAssociation()
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        userAssociation.setOwner(userProfile);
        return userAssociation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserAssociation.class).block();
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
        userAssociationSearchRepository.deleteAll().block();
        assertThat(userAssociationSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userAssociation = createEntity(em);
    }

    @Test
    void createUserAssociation() throws Exception {
        int databaseSizeBeforeCreate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(DEFAULT_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createUserAssociationWithExistingId() throws Exception {
        // Create the UserAssociation with an existing ID
        userAssociation.setId(1L);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        int databaseSizeBeforeCreate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkRequestedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // set the field null
        userAssociation.setRequestedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAssociationTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // set the field null
        userAssociation.setAssociationToken(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkExpiryDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // set the field null
        userAssociation.setExpiryDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // set the field null
        userAssociation.setCreatedDate(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        // set the field null
        userAssociation.setIsDeleted(null);

        // Create the UserAssociation, which fails.
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserAssociations() {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();

        // Get all the userAssociationList
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
            .value(hasItem(userAssociation.getId().intValue()))
            .jsonPath("$.[*].requestedDate")
            .value(hasItem(DEFAULT_REQUESTED_DATE.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].associationToken")
            .value(hasItem(DEFAULT_ASSOCIATION_TOKEN))
            .jsonPath("$.[*].expiryDate")
            .value(hasItem(DEFAULT_EXPIRY_DATE.toString()))
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
    void getUserAssociation() {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();

        // Get the userAssociation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userAssociation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userAssociation.getId().intValue()))
            .jsonPath("$.requestedDate")
            .value(is(DEFAULT_REQUESTED_DATE.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.associationToken")
            .value(is(DEFAULT_ASSOCIATION_TOKEN))
            .jsonPath("$.expiryDate")
            .value(is(DEFAULT_EXPIRY_DATE.toString()))
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
    void getNonExistingUserAssociation() {
        // Get the userAssociation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserAssociation() throws Exception {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        userAssociationSearchRepository.save(userAssociation).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());

        // Update the userAssociation
        UserAssociation updatedUserAssociation = userAssociationRepository.findById(userAssociation.getId()).block();
        updatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(updatedUserAssociation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAssociationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserAssociation> userAssociationSearchList = IterableUtils.toList(
                    userAssociationSearchRepository.findAll().collectList().block()
                );
                UserAssociation testUserAssociationSearch = userAssociationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserAssociationSearch.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
                assertThat(testUserAssociationSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testUserAssociationSearch.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
                assertThat(testUserAssociationSearch.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
                assertThat(testUserAssociationSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserAssociationSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserAssociationSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserAssociationSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserAssociationSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userAssociationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(DEFAULT_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateUserAssociationWithPatch() throws Exception {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();

        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();

        // Update the userAssociation using partial update
        UserAssociation partialUpdatedUserAssociation = new UserAssociation();
        partialUpdatedUserAssociation.setId(userAssociation.getId());

        partialUpdatedUserAssociation
            .requestedDate(UPDATED_REQUESTED_DATE)
            .status(UPDATED_STATUS)
            .associationToken(UPDATED_ASSOCIATION_TOKEN)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserAssociation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserAssociation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        UserAssociation testUserAssociation = userAssociationList.get(userAssociationList.size() - 1);
        assertThat(testUserAssociation.getRequestedDate()).isEqualTo(UPDATED_REQUESTED_DATE);
        assertThat(testUserAssociation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserAssociation.getAssociationToken()).isEqualTo(UPDATED_ASSOCIATION_TOKEN);
        assertThat(testUserAssociation.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testUserAssociation.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserAssociation.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserAssociation.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserAssociation.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserAssociation.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userAssociationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserAssociation() throws Exception {
        int databaseSizeBeforeUpdate = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        userAssociation.setId(longCount.incrementAndGet());

        // Create the UserAssociation
        UserAssociationDTO userAssociationDTO = userAssociationMapper.toDto(userAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userAssociationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserAssociation in the database
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserAssociation() {
        // Initialize the database
        userAssociationRepository.save(userAssociation).block();
        userAssociationRepository.save(userAssociation).block();
        userAssociationSearchRepository.save(userAssociation).block();

        int databaseSizeBeforeDelete = userAssociationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userAssociation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userAssociation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserAssociation> userAssociationList = userAssociationRepository.findAll().collectList().block();
        assertThat(userAssociationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userAssociationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserAssociation() {
        // Initialize the database
        userAssociation = userAssociationRepository.save(userAssociation).block();
        userAssociationSearchRepository.save(userAssociation).block();

        // Search the userAssociation
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userAssociation.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userAssociation.getId().intValue()))
            .jsonPath("$.[*].requestedDate")
            .value(hasItem(DEFAULT_REQUESTED_DATE.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].associationToken")
            .value(hasItem(DEFAULT_ASSOCIATION_TOKEN))
            .jsonPath("$.[*].expiryDate")
            .value(hasItem(DEFAULT_EXPIRY_DATE.toString()))
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
