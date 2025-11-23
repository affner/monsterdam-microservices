package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.CreatorEarning;
import com.fanflip.admin.domain.PurchasedSubscription;
import com.fanflip.admin.domain.SubscriptionBundle;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.enumeration.PurchasedSubscriptionStatus;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PurchasedSubscriptionRepository;
import com.fanflip.admin.repository.search.PurchasedSubscriptionSearchRepository;
import com.fanflip.admin.service.PurchasedSubscriptionService;
import com.fanflip.admin.service.dto.PurchasedSubscriptionDTO;
import com.fanflip.admin.service.mapper.PurchasedSubscriptionMapper;
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
 * Integration tests for the {@link PurchasedSubscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PurchasedSubscriptionResourceIT {

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

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final PurchasedSubscriptionStatus DEFAULT_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.PURCHASED;
    private static final PurchasedSubscriptionStatus UPDATED_SUBSCRIPTION_STATUS = PurchasedSubscriptionStatus.PENDING;

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/purchased-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/purchased-subscriptions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedSubscriptionRepository purchasedSubscriptionRepository;

    @Mock
    private PurchasedSubscriptionRepository purchasedSubscriptionRepositoryMock;

    @Autowired
    private PurchasedSubscriptionMapper purchasedSubscriptionMapper;

    @Mock
    private PurchasedSubscriptionService purchasedSubscriptionServiceMock;

    @Autowired
    private PurchasedSubscriptionSearchRepository purchasedSubscriptionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PurchasedSubscription purchasedSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createEntity(EntityManager em) {
        PurchasedSubscription purchasedSubscription = new PurchasedSubscription()
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .endDate(DEFAULT_END_DATE)
            .subscriptionStatus(DEFAULT_SUBSCRIPTION_STATUS)
            .viewerId(DEFAULT_VIEWER_ID)
            .creatorId(DEFAULT_CREATOR_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createEntity(em)).block();
        purchasedSubscription.setCreatorEarning(creatorEarning);
        // Add required entity
        SubscriptionBundle subscriptionBundle;
        subscriptionBundle = em.insert(SubscriptionBundleResourceIT.createEntity(em)).block();
        purchasedSubscription.setSubscriptionBundle(subscriptionBundle);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        purchasedSubscription.setViewer(userProfile);
        return purchasedSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedSubscription createUpdatedEntity(EntityManager em) {
        PurchasedSubscription purchasedSubscription = new PurchasedSubscription()
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createUpdatedEntity(em)).block();
        purchasedSubscription.setCreatorEarning(creatorEarning);
        // Add required entity
        SubscriptionBundle subscriptionBundle;
        subscriptionBundle = em.insert(SubscriptionBundleResourceIT.createUpdatedEntity(em)).block();
        purchasedSubscription.setSubscriptionBundle(subscriptionBundle);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        purchasedSubscription.setViewer(userProfile);
        return purchasedSubscription;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PurchasedSubscription.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CreatorEarningResourceIT.deleteEntities(em);
        SubscriptionBundleResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        purchasedSubscriptionSearchRepository.deleteAll().block();
        assertThat(purchasedSubscriptionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        purchasedSubscription = createEntity(em);
    }

    @Test
    void createPurchasedSubscription() throws Exception {
        int databaseSizeBeforeCreate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(DEFAULT_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    void createPurchasedSubscriptionWithExistingId() throws Exception {
        // Create the PurchasedSubscription with an existing ID
        purchasedSubscription.setId(1L);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        int databaseSizeBeforeCreate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setCreatedDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setIsDeleted(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setEndDate(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSubscriptionStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setSubscriptionStatus(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setViewerId(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedSubscription.setCreatorId(null);

        // Create the PurchasedSubscription, which fails.
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPurchasedSubscriptions() {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();

        // Get all the purchasedSubscriptionList
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
            .value(hasItem(purchasedSubscription.getId().intValue()))
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
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].subscriptionStatus")
            .value(hasItem(DEFAULT_SUBSCRIPTION_STATUS.toString()))
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].creatorId")
            .value(hasItem(DEFAULT_CREATOR_ID.intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedSubscriptionsWithEagerRelationshipsIsEnabled() {
        when(purchasedSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(purchasedSubscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedSubscriptionsWithEagerRelationshipsIsNotEnabled() {
        when(purchasedSubscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(purchasedSubscriptionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPurchasedSubscription() {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();

        // Get the purchasedSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, purchasedSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(purchasedSubscription.getId().intValue()))
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
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.subscriptionStatus")
            .value(is(DEFAULT_SUBSCRIPTION_STATUS.toString()))
            .jsonPath("$.viewerId")
            .value(is(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.creatorId")
            .value(is(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    void getNonExistingPurchasedSubscription() {
        // Get the purchasedSubscription
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPurchasedSubscription() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        purchasedSubscriptionSearchRepository.save(purchasedSubscription).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());

        // Update the purchasedSubscription
        PurchasedSubscription updatedPurchasedSubscription = purchasedSubscriptionRepository
            .findById(purchasedSubscription.getId())
            .block();
        updatedPurchasedSubscription
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(updatedPurchasedSubscription);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PurchasedSubscription> purchasedSubscriptionSearchList = IterableUtils.toList(
                    purchasedSubscriptionSearchRepository.findAll().collectList().block()
                );
                PurchasedSubscription testPurchasedSubscriptionSearch = purchasedSubscriptionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPurchasedSubscriptionSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPurchasedSubscriptionSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPurchasedSubscriptionSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPurchasedSubscriptionSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPurchasedSubscriptionSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testPurchasedSubscriptionSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testPurchasedSubscriptionSearch.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
                assertThat(testPurchasedSubscriptionSearch.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
                assertThat(testPurchasedSubscriptionSearch.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
            });
    }

    @Test
    void putNonExistingPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    void fullUpdatePurchasedSubscriptionWithPatch() throws Exception {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();

        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();

        // Update the purchasedSubscription using partial update
        PurchasedSubscription partialUpdatedPurchasedSubscription = new PurchasedSubscription();
        partialUpdatedPurchasedSubscription.setId(purchasedSubscription.getId());

        partialUpdatedPurchasedSubscription
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .endDate(UPDATED_END_DATE)
            .subscriptionStatus(UPDATED_SUBSCRIPTION_STATUS)
            .viewerId(UPDATED_VIEWER_ID)
            .creatorId(UPDATED_CREATOR_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedSubscription.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedSubscription))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        PurchasedSubscription testPurchasedSubscription = purchasedSubscriptionList.get(purchasedSubscriptionList.size() - 1);
        assertThat(testPurchasedSubscription.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedSubscription.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedSubscription.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedSubscription.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedSubscription.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testPurchasedSubscription.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPurchasedSubscription.getSubscriptionStatus()).isEqualTo(UPDATED_SUBSCRIPTION_STATUS);
        assertThat(testPurchasedSubscription.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testPurchasedSubscription.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    void patchNonExistingPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, purchasedSubscriptionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPurchasedSubscription() throws Exception {
        int databaseSizeBeforeUpdate = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        purchasedSubscription.setId(longCount.incrementAndGet());

        // Create the PurchasedSubscription
        PurchasedSubscriptionDTO purchasedSubscriptionDTO = purchasedSubscriptionMapper.toDto(purchasedSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedSubscriptionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedSubscription in the database
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePurchasedSubscription() {
        // Initialize the database
        purchasedSubscriptionRepository.save(purchasedSubscription).block();
        purchasedSubscriptionRepository.save(purchasedSubscription).block();
        purchasedSubscriptionSearchRepository.save(purchasedSubscription).block();

        int databaseSizeBeforeDelete = purchasedSubscriptionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the purchasedSubscription
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, purchasedSubscription.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PurchasedSubscription> purchasedSubscriptionList = purchasedSubscriptionRepository.findAll().collectList().block();
        assertThat(purchasedSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedSubscriptionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPurchasedSubscription() {
        // Initialize the database
        purchasedSubscription = purchasedSubscriptionRepository.save(purchasedSubscription).block();
        purchasedSubscriptionSearchRepository.save(purchasedSubscription).block();

        // Search the purchasedSubscription
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + purchasedSubscription.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(purchasedSubscription.getId().intValue()))
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
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].subscriptionStatus")
            .value(hasItem(DEFAULT_SUBSCRIPTION_STATUS.toString()))
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].creatorId")
            .value(hasItem(DEFAULT_CREATOR_ID.intValue()));
    }
}
