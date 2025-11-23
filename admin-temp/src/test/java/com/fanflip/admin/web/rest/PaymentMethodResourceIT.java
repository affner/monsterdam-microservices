package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.PaymentMethod;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PaymentMethodRepository;
import com.fanflip.admin.repository.search.PaymentMethodSearchRepository;
import com.fanflip.admin.service.dto.PaymentMethodDTO;
import com.fanflip.admin.service.mapper.PaymentMethodMapper;
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
 * Integration tests for the {@link PaymentMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentMethodResourceIT {

    private static final String DEFAULT_METHOD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METHOD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_TEXT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/payment-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payment-methods/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Autowired
    private PaymentMethodSearchRepository paymentMethodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentMethod paymentMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .methodName(DEFAULT_METHOD_NAME)
            .tokenText(DEFAULT_TOKEN_TEXT)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return paymentMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentMethod createUpdatedEntity(EntityManager em) {
        PaymentMethod paymentMethod = new PaymentMethod()
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return paymentMethod;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PaymentMethod.class).block();
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
        paymentMethodSearchRepository.deleteAll().block();
        assertThat(paymentMethodSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        paymentMethod = createEntity(em);
    }

    @Test
    void createPaymentMethod() throws Exception {
        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testPaymentMethod.getTokenText()).isEqualTo(DEFAULT_TOKEN_TEXT);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPaymentMethod.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPaymentMethod.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPaymentMethod.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPaymentMethod.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPaymentMethodWithExistingId() throws Exception {
        // Create the PaymentMethod with an existing ID
        paymentMethod.setId(1L);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        int databaseSizeBeforeCreate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        // set the field null
        paymentMethod.setMethodName(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTokenTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        // set the field null
        paymentMethod.setTokenText(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        // set the field null
        paymentMethod.setCreatedDate(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        // set the field null
        paymentMethod.setIsDeleted(null);

        // Create the PaymentMethod, which fails.
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPaymentMethods() {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();

        // Get all the paymentMethodList
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
            .value(hasItem(paymentMethod.getId().intValue()))
            .jsonPath("$.[*].methodName")
            .value(hasItem(DEFAULT_METHOD_NAME))
            .jsonPath("$.[*].tokenText")
            .value(hasItem(DEFAULT_TOKEN_TEXT))
            .jsonPath("$.[*].expirationDate")
            .value(hasItem(DEFAULT_EXPIRATION_DATE.toString()))
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
    void getPaymentMethod() {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();

        // Get the paymentMethod
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentMethod.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentMethod.getId().intValue()))
            .jsonPath("$.methodName")
            .value(is(DEFAULT_METHOD_NAME))
            .jsonPath("$.tokenText")
            .value(is(DEFAULT_TOKEN_TEXT))
            .jsonPath("$.expirationDate")
            .value(is(DEFAULT_EXPIRATION_DATE.toString()))
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
    void getNonExistingPaymentMethod() {
        // Get the paymentMethod
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentMethod() throws Exception {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        paymentMethodSearchRepository.save(paymentMethod).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());

        // Update the paymentMethod
        PaymentMethod updatedPaymentMethod = paymentMethodRepository.findById(paymentMethod.getId()).block();
        updatedPaymentMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(updatedPaymentMethod);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentMethodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPaymentMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PaymentMethod> paymentMethodSearchList = IterableUtils.toList(
                    paymentMethodSearchRepository.findAll().collectList().block()
                );
                PaymentMethod testPaymentMethodSearch = paymentMethodSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPaymentMethodSearch.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
                assertThat(testPaymentMethodSearch.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
                assertThat(testPaymentMethodSearch.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
                assertThat(testPaymentMethodSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPaymentMethodSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPaymentMethodSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPaymentMethodSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPaymentMethodSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentMethodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentMethod))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testPaymentMethod.getTokenText()).isEqualTo(DEFAULT_TOKEN_TEXT);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdatePaymentMethodWithPatch() throws Exception {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();

        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();

        // Update the paymentMethod using partial update
        PaymentMethod partialUpdatedPaymentMethod = new PaymentMethod();
        partialUpdatedPaymentMethod.setId(paymentMethod.getId());

        partialUpdatedPaymentMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentMethod.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentMethod))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        PaymentMethod testPaymentMethod = paymentMethodList.get(paymentMethodList.size() - 1);
        assertThat(testPaymentMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPaymentMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPaymentMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPaymentMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPaymentMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPaymentMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPaymentMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPaymentMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentMethodDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPaymentMethod() throws Exception {
        int databaseSizeBeforeUpdate = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        paymentMethod.setId(longCount.incrementAndGet());

        // Create the PaymentMethod
        PaymentMethodDTO paymentMethodDTO = paymentMethodMapper.toDto(paymentMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentMethodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentMethod in the database
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePaymentMethod() {
        // Initialize the database
        paymentMethodRepository.save(paymentMethod).block();
        paymentMethodRepository.save(paymentMethod).block();
        paymentMethodSearchRepository.save(paymentMethod).block();

        int databaseSizeBeforeDelete = paymentMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the paymentMethod
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentMethod.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll().collectList().block();
        assertThat(paymentMethodList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPaymentMethod() {
        // Initialize the database
        paymentMethod = paymentMethodRepository.save(paymentMethod).block();
        paymentMethodSearchRepository.save(paymentMethod).block();

        // Search the paymentMethod
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + paymentMethod.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(paymentMethod.getId().intValue()))
            .jsonPath("$.[*].methodName")
            .value(hasItem(DEFAULT_METHOD_NAME))
            .jsonPath("$.[*].tokenText")
            .value(hasItem(DEFAULT_TOKEN_TEXT))
            .jsonPath("$.[*].expirationDate")
            .value(hasItem(DEFAULT_EXPIRATION_DATE.toString()))
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
