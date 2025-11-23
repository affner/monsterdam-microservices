package com.fanflip.admin.web.rest;

import static com.fanflip.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SubscriptionBundle;
import com.fanflip.admin.domain.UserProfile;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SubscriptionBundleRepository;
import com.fanflip.admin.repository.search.SubscriptionBundleSearchRepository;
import com.fanflip.admin.service.dto.SubscriptionBundleDTO;
import com.fanflip.admin.service.mapper.SubscriptionBundleMapper;
import java.math.BigDecimal;
import java.time.Duration;
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
 * Integration tests for the {@link SubscriptionBundleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SubscriptionBundleResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(1);

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

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

    private static final String ENTITY_API_URL = "/api/subscription-bundles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/subscription-bundles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriptionBundleRepository subscriptionBundleRepository;

    @Autowired
    private SubscriptionBundleMapper subscriptionBundleMapper;

    @Autowired
    private SubscriptionBundleSearchRepository subscriptionBundleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SubscriptionBundle subscriptionBundle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionBundle createEntity(EntityManager em) {
        SubscriptionBundle subscriptionBundle = new SubscriptionBundle()
            .amount(DEFAULT_AMOUNT)
            .duration(DEFAULT_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        subscriptionBundle.setCreator(userProfile);
        return subscriptionBundle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubscriptionBundle createUpdatedEntity(EntityManager em) {
        SubscriptionBundle subscriptionBundle = new SubscriptionBundle()
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        subscriptionBundle.setCreator(userProfile);
        return subscriptionBundle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SubscriptionBundle.class).block();
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
        subscriptionBundleSearchRepository.deleteAll().block();
        assertThat(subscriptionBundleSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        subscriptionBundle = createEntity(em);
    }

    @Test
    void createSubscriptionBundle() throws Exception {
        int databaseSizeBeforeCreate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSubscriptionBundleWithExistingId() throws Exception {
        // Create the SubscriptionBundle with an existing ID
        subscriptionBundle.setId(1L);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        int databaseSizeBeforeCreate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        // set the field null
        subscriptionBundle.setAmount(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        // set the field null
        subscriptionBundle.setDuration(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        // set the field null
        subscriptionBundle.setCreatedDate(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        // set the field null
        subscriptionBundle.setIsDeleted(null);

        // Create the SubscriptionBundle, which fails.
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSubscriptionBundles() {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();

        // Get all the subscriptionBundleList
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
            .value(hasItem(subscriptionBundle.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION.toString()))
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
    void getSubscriptionBundle() {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();

        // Get the subscriptionBundle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, subscriptionBundle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(subscriptionBundle.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.duration")
            .value(is(DEFAULT_DURATION.toString()))
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
    void getNonExistingSubscriptionBundle() {
        // Get the subscriptionBundle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSubscriptionBundle() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        subscriptionBundleSearchRepository.save(subscriptionBundle).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());

        // Update the subscriptionBundle
        SubscriptionBundle updatedSubscriptionBundle = subscriptionBundleRepository.findById(subscriptionBundle.getId()).block();
        updatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(updatedSubscriptionBundle);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SubscriptionBundle> subscriptionBundleSearchList = IterableUtils.toList(
                    subscriptionBundleSearchRepository.findAll().collectList().block()
                );
                SubscriptionBundle testSubscriptionBundleSearch = subscriptionBundleSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSubscriptionBundleSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testSubscriptionBundleSearch.getDuration()).isEqualTo(UPDATED_DURATION);
                assertThat(testSubscriptionBundleSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSubscriptionBundleSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSubscriptionBundleSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSubscriptionBundleSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSubscriptionBundleSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionBundle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateSubscriptionBundleWithPatch() throws Exception {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();

        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();

        // Update the subscriptionBundle using partial update
        SubscriptionBundle partialUpdatedSubscriptionBundle = new SubscriptionBundle();
        partialUpdatedSubscriptionBundle.setId(subscriptionBundle.getId());

        partialUpdatedSubscriptionBundle
            .amount(UPDATED_AMOUNT)
            .duration(UPDATED_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSubscriptionBundle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriptionBundle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        SubscriptionBundle testSubscriptionBundle = subscriptionBundleList.get(subscriptionBundleList.size() - 1);
        assertThat(testSubscriptionBundle.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSubscriptionBundle.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSubscriptionBundle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSubscriptionBundle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSubscriptionBundle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSubscriptionBundle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSubscriptionBundle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, subscriptionBundleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSubscriptionBundle() throws Exception {
        int databaseSizeBeforeUpdate = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        subscriptionBundle.setId(longCount.incrementAndGet());

        // Create the SubscriptionBundle
        SubscriptionBundleDTO subscriptionBundleDTO = subscriptionBundleMapper.toDto(subscriptionBundle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(subscriptionBundleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SubscriptionBundle in the database
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSubscriptionBundle() {
        // Initialize the database
        subscriptionBundleRepository.save(subscriptionBundle).block();
        subscriptionBundleRepository.save(subscriptionBundle).block();
        subscriptionBundleSearchRepository.save(subscriptionBundle).block();

        int databaseSizeBeforeDelete = subscriptionBundleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the subscriptionBundle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, subscriptionBundle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SubscriptionBundle> subscriptionBundleList = subscriptionBundleRepository.findAll().collectList().block();
        assertThat(subscriptionBundleList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(subscriptionBundleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSubscriptionBundle() {
        // Initialize the database
        subscriptionBundle = subscriptionBundleRepository.save(subscriptionBundle).block();
        subscriptionBundleSearchRepository.save(subscriptionBundle).block();

        // Search the subscriptionBundle
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + subscriptionBundle.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(subscriptionBundle.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].duration")
            .value(hasItem(DEFAULT_DURATION.toString()))
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
