package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.PaymentProvider;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PaymentProviderRepository;
import com.monsterdam.admin.repository.search.PaymentProviderSearchRepository;
import com.monsterdam.admin.service.dto.PaymentProviderDTO;
import com.monsterdam.admin.service.mapper.PaymentProviderMapper;
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
 * Integration tests for the {@link PaymentProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentProviderResourceIT {

    private static final String DEFAULT_PROVIDER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_API_SECRET_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_API_SECRET_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT_TEXT = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/payment-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payment-providers/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentProviderRepository paymentProviderRepository;

    @Autowired
    private PaymentProviderMapper paymentProviderMapper;

    @Autowired
    private PaymentProviderSearchRepository paymentProviderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentProvider paymentProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProvider createEntity(EntityManager em) {
        PaymentProvider paymentProvider = new PaymentProvider()
            .providerName(DEFAULT_PROVIDER_NAME)
            .description(DEFAULT_DESCRIPTION)
            .apiKeyText(DEFAULT_API_KEY_TEXT)
            .apiSecretText(DEFAULT_API_SECRET_TEXT)
            .endpointText(DEFAULT_ENDPOINT_TEXT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return paymentProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentProvider createUpdatedEntity(EntityManager em) {
        PaymentProvider paymentProvider = new PaymentProvider()
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return paymentProvider;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PaymentProvider.class).block();
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
        paymentProviderSearchRepository.deleteAll().block();
        assertThat(paymentProviderSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        paymentProvider = createEntity(em);
    }

    @Test
    void createPaymentProvider() throws Exception {
        int databaseSizeBeforeCreate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(DEFAULT_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(DEFAULT_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(DEFAULT_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(DEFAULT_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPaymentProviderWithExistingId() throws Exception {
        // Create the PaymentProvider with an existing ID
        paymentProvider.setId(1L);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        int databaseSizeBeforeCreate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkProviderNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setProviderName(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkApiKeyTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setApiKeyText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkApiSecretTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setApiSecretText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndpointTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setEndpointText(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setCreatedDate(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        // set the field null
        paymentProvider.setIsDeleted(null);

        // Create the PaymentProvider, which fails.
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPaymentProviders() {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();

        // Get all the paymentProviderList
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
            .value(hasItem(paymentProvider.getId().intValue()))
            .jsonPath("$.[*].providerName")
            .value(hasItem(DEFAULT_PROVIDER_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].apiKeyText")
            .value(hasItem(DEFAULT_API_KEY_TEXT))
            .jsonPath("$.[*].apiSecretText")
            .value(hasItem(DEFAULT_API_SECRET_TEXT))
            .jsonPath("$.[*].endpointText")
            .value(hasItem(DEFAULT_ENDPOINT_TEXT))
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
    void getPaymentProvider() {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();

        // Get the paymentProvider
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentProvider.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentProvider.getId().intValue()))
            .jsonPath("$.providerName")
            .value(is(DEFAULT_PROVIDER_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.apiKeyText")
            .value(is(DEFAULT_API_KEY_TEXT))
            .jsonPath("$.apiSecretText")
            .value(is(DEFAULT_API_SECRET_TEXT))
            .jsonPath("$.endpointText")
            .value(is(DEFAULT_ENDPOINT_TEXT))
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
    void getNonExistingPaymentProvider() {
        // Get the paymentProvider
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentProvider() throws Exception {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        paymentProviderSearchRepository.save(paymentProvider).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());

        // Update the paymentProvider
        PaymentProvider updatedPaymentProvider = paymentProviderRepository.findById(paymentProvider.getId()).block();
        updatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(updatedPaymentProvider);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentProviderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(UPDATED_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PaymentProvider> paymentProviderSearchList = IterableUtils.toList(
                    paymentProviderSearchRepository.findAll().collectList().block()
                );
                PaymentProvider testPaymentProviderSearch = paymentProviderSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPaymentProviderSearch.getProviderName()).isEqualTo(UPDATED_PROVIDER_NAME);
                assertThat(testPaymentProviderSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testPaymentProviderSearch.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
                assertThat(testPaymentProviderSearch.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
                assertThat(testPaymentProviderSearch.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
                assertThat(testPaymentProviderSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPaymentProviderSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPaymentProviderSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPaymentProviderSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPaymentProviderSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentProviderDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentProvider))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(DEFAULT_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(DEFAULT_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdatePaymentProviderWithPatch() throws Exception {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();

        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();

        // Update the paymentProvider using partial update
        PaymentProvider partialUpdatedPaymentProvider = new PaymentProvider();
        partialUpdatedPaymentProvider.setId(paymentProvider.getId());

        partialUpdatedPaymentProvider
            .providerName(UPDATED_PROVIDER_NAME)
            .description(UPDATED_DESCRIPTION)
            .apiKeyText(UPDATED_API_KEY_TEXT)
            .apiSecretText(UPDATED_API_SECRET_TEXT)
            .endpointText(UPDATED_ENDPOINT_TEXT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentProvider.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentProvider))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        PaymentProvider testPaymentProvider = paymentProviderList.get(paymentProviderList.size() - 1);
        assertThat(testPaymentProvider.getProviderName()).isEqualTo(UPDATED_PROVIDER_NAME);
        assertThat(testPaymentProvider.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPaymentProvider.getApiKeyText()).isEqualTo(UPDATED_API_KEY_TEXT);
        assertThat(testPaymentProvider.getApiSecretText()).isEqualTo(UPDATED_API_SECRET_TEXT);
        assertThat(testPaymentProvider.getEndpointText()).isEqualTo(UPDATED_ENDPOINT_TEXT);
        assertThat(testPaymentProvider.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentProvider.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentProvider.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentProvider.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentProvider.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentProviderDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPaymentProvider() throws Exception {
        int databaseSizeBeforeUpdate = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        paymentProvider.setId(longCount.incrementAndGet());

        // Create the PaymentProvider
        PaymentProviderDTO paymentProviderDTO = paymentProviderMapper.toDto(paymentProvider);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentProviderDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentProvider in the database
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePaymentProvider() {
        // Initialize the database
        paymentProviderRepository.save(paymentProvider).block();
        paymentProviderRepository.save(paymentProvider).block();
        paymentProviderSearchRepository.save(paymentProvider).block();

        int databaseSizeBeforeDelete = paymentProviderRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the paymentProvider
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentProvider.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentProvider> paymentProviderList = paymentProviderRepository.findAll().collectList().block();
        assertThat(paymentProviderList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentProviderSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPaymentProvider() {
        // Initialize the database
        paymentProvider = paymentProviderRepository.save(paymentProvider).block();
        paymentProviderSearchRepository.save(paymentProvider).block();

        // Search the paymentProvider
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + paymentProvider.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(paymentProvider.getId().intValue()))
            .jsonPath("$.[*].providerName")
            .value(hasItem(DEFAULT_PROVIDER_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].apiKeyText")
            .value(hasItem(DEFAULT_API_KEY_TEXT))
            .jsonPath("$.[*].apiSecretText")
            .value(hasItem(DEFAULT_API_SECRET_TEXT))
            .jsonPath("$.[*].endpointText")
            .value(hasItem(DEFAULT_ENDPOINT_TEXT))
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
