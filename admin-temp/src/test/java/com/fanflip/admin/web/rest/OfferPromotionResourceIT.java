package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.OfferPromotion;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.domain.enumeration.OfferPromotionType;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.OfferPromotionRepository;
import com.fanflip.admin.repository.search.OfferPromotionSearchRepository;
import com.fanflip.admin.service.dto.OfferPromotionDTO;
import com.fanflip.admin.service.mapper.OfferPromotionMapper;
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
 * Integration tests for the {@link OfferPromotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OfferPromotionResourceIT {

    private static final Duration DEFAULT_FREE_DAYS_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_FREE_DAYS_DURATION = Duration.ofHours(12);

    private static final Float DEFAULT_DISCOUNT_PERCENTAGE = 0F;
    private static final Float UPDATED_DISCOUNT_PERCENTAGE = 1F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SUBSCRIPTIONS_LIMIT = 1;
    private static final Integer UPDATED_SUBSCRIPTIONS_LIMIT = 2;

    private static final String DEFAULT_LINK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINK_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FINISHED = false;
    private static final Boolean UPDATED_IS_FINISHED = true;

    private static final OfferPromotionType DEFAULT_PROMOTION_TYPE = OfferPromotionType.SPECIAL;
    private static final OfferPromotionType UPDATED_PROMOTION_TYPE = OfferPromotionType.TRIAL_LINK;

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

    private static final String ENTITY_API_URL = "/api/offer-promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/offer-promotions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfferPromotionRepository offerPromotionRepository;

    @Autowired
    private OfferPromotionMapper offerPromotionMapper;

    @Autowired
    private OfferPromotionSearchRepository offerPromotionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private OfferPromotion offerPromotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferPromotion createEntity(EntityManager em) {
        OfferPromotion offerPromotion = new OfferPromotion()
            .freeDaysDuration(DEFAULT_FREE_DAYS_DURATION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .subscriptionsLimit(DEFAULT_SUBSCRIPTIONS_LIMIT)
            .linkCode(DEFAULT_LINK_CODE)
            .isFinished(DEFAULT_IS_FINISHED)
            .promotionType(DEFAULT_PROMOTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        offerPromotion.setCreator(userProfile);
        return offerPromotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferPromotion createUpdatedEntity(EntityManager em) {
        OfferPromotion offerPromotion = new OfferPromotion()
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        offerPromotion.setCreator(userProfile);
        return offerPromotion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(OfferPromotion.class).block();
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
        offerPromotionSearchRepository.deleteAll().block();
        assertThat(offerPromotionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        offerPromotion = createEntity(em);
    }

    @Test
    void createOfferPromotion() throws Exception {
        int databaseSizeBeforeCreate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(DEFAULT_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(DEFAULT_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(DEFAULT_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(DEFAULT_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(DEFAULT_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createOfferPromotionWithExistingId() throws Exception {
        // Create the OfferPromotion with an existing ID
        offerPromotion.setId(1L);
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        int databaseSizeBeforeCreate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setStartDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setEndDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkLinkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setLinkCode(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsFinishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setIsFinished(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPromotionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setPromotionType(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setCreatedDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        // set the field null
        offerPromotion.setIsDeleted(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllOfferPromotions() {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();

        // Get all the offerPromotionList
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
            .value(hasItem(offerPromotion.getId().intValue()))
            .jsonPath("$.[*].freeDaysDuration")
            .value(hasItem(DEFAULT_FREE_DAYS_DURATION.toString()))
            .jsonPath("$.[*].discountPercentage")
            .value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].subscriptionsLimit")
            .value(hasItem(DEFAULT_SUBSCRIPTIONS_LIMIT))
            .jsonPath("$.[*].linkCode")
            .value(hasItem(DEFAULT_LINK_CODE))
            .jsonPath("$.[*].isFinished")
            .value(hasItem(DEFAULT_IS_FINISHED.booleanValue()))
            .jsonPath("$.[*].promotionType")
            .value(hasItem(DEFAULT_PROMOTION_TYPE.toString()))
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
    void getOfferPromotion() {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();

        // Get the offerPromotion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, offerPromotion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(offerPromotion.getId().intValue()))
            .jsonPath("$.freeDaysDuration")
            .value(is(DEFAULT_FREE_DAYS_DURATION.toString()))
            .jsonPath("$.discountPercentage")
            .value(is(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.subscriptionsLimit")
            .value(is(DEFAULT_SUBSCRIPTIONS_LIMIT))
            .jsonPath("$.linkCode")
            .value(is(DEFAULT_LINK_CODE))
            .jsonPath("$.isFinished")
            .value(is(DEFAULT_IS_FINISHED.booleanValue()))
            .jsonPath("$.promotionType")
            .value(is(DEFAULT_PROMOTION_TYPE.toString()))
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
    void getNonExistingOfferPromotion() {
        // Get the offerPromotion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOfferPromotion() throws Exception {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        offerPromotionSearchRepository.save(offerPromotion).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());

        // Update the offerPromotion
        OfferPromotion updatedOfferPromotion = offerPromotionRepository.findById(offerPromotion.getId()).block();
        updatedOfferPromotion
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(updatedOfferPromotion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, offerPromotionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(UPDATED_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(UPDATED_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OfferPromotion> offerPromotionSearchList = IterableUtils.toList(
                    offerPromotionSearchRepository.findAll().collectList().block()
                );
                OfferPromotion testOfferPromotionSearch = offerPromotionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testOfferPromotionSearch.getFreeDaysDuration()).isEqualTo(UPDATED_FREE_DAYS_DURATION);
                assertThat(testOfferPromotionSearch.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
                assertThat(testOfferPromotionSearch.getStartDate()).isEqualTo(UPDATED_START_DATE);
                assertThat(testOfferPromotionSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testOfferPromotionSearch.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
                assertThat(testOfferPromotionSearch.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
                assertThat(testOfferPromotionSearch.getIsFinished()).isEqualTo(UPDATED_IS_FINISHED);
                assertThat(testOfferPromotionSearch.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
                assertThat(testOfferPromotionSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testOfferPromotionSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testOfferPromotionSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testOfferPromotionSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testOfferPromotionSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, offerPromotionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateOfferPromotionWithPatch() throws Exception {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();

        // Update the offerPromotion using partial update
        OfferPromotion partialUpdatedOfferPromotion = new OfferPromotion();
        partialUpdatedOfferPromotion.setId(offerPromotion.getId());

        partialUpdatedOfferPromotion
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .linkCode(UPDATED_LINK_CODE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOfferPromotion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferPromotion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(DEFAULT_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(DEFAULT_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(DEFAULT_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(DEFAULT_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateOfferPromotionWithPatch() throws Exception {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();

        // Update the offerPromotion using partial update
        OfferPromotion partialUpdatedOfferPromotion = new OfferPromotion();
        partialUpdatedOfferPromotion.setId(offerPromotion.getId());

        partialUpdatedOfferPromotion
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOfferPromotion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferPromotion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(UPDATED_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(UPDATED_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, offerPromotionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteOfferPromotion() {
        // Initialize the database
        offerPromotionRepository.save(offerPromotion).block();
        offerPromotionRepository.save(offerPromotion).block();
        offerPromotionSearchRepository.save(offerPromotion).block();

        int databaseSizeBeforeDelete = offerPromotionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the offerPromotion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, offerPromotion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll().collectList().block();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(offerPromotionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchOfferPromotion() {
        // Initialize the database
        offerPromotion = offerPromotionRepository.save(offerPromotion).block();
        offerPromotionSearchRepository.save(offerPromotion).block();

        // Search the offerPromotion
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + offerPromotion.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(offerPromotion.getId().intValue()))
            .jsonPath("$.[*].freeDaysDuration")
            .value(hasItem(DEFAULT_FREE_DAYS_DURATION.toString()))
            .jsonPath("$.[*].discountPercentage")
            .value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].subscriptionsLimit")
            .value(hasItem(DEFAULT_SUBSCRIPTIONS_LIMIT))
            .jsonPath("$.[*].linkCode")
            .value(hasItem(DEFAULT_LINK_CODE))
            .jsonPath("$.[*].isFinished")
            .value(hasItem(DEFAULT_IS_FINISHED.booleanValue()))
            .jsonPath("$.[*].promotionType")
            .value(hasItem(DEFAULT_PROMOTION_TYPE.toString()))
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
