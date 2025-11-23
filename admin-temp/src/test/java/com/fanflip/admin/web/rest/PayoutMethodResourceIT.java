package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.PayoutMethod;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PayoutMethodRepository;
import com.fanflip.admin.repository.search.PayoutMethodSearchRepository;
import com.fanflip.admin.service.dto.PayoutMethodDTO;
import com.fanflip.admin.service.mapper.PayoutMethodMapper;
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
 * Integration tests for the {@link PayoutMethodResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PayoutMethodResourceIT {

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

    private static final String ENTITY_API_URL = "/api/payout-methods";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payout-methods/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PayoutMethodRepository payoutMethodRepository;

    @Autowired
    private PayoutMethodMapper payoutMethodMapper;

    @Autowired
    private PayoutMethodSearchRepository payoutMethodSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PayoutMethod payoutMethod;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutMethod createEntity(EntityManager em) {
        PayoutMethod payoutMethod = new PayoutMethod()
            .methodName(DEFAULT_METHOD_NAME)
            .tokenText(DEFAULT_TOKEN_TEXT)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return payoutMethod;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PayoutMethod createUpdatedEntity(EntityManager em) {
        PayoutMethod payoutMethod = new PayoutMethod()
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return payoutMethod;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PayoutMethod.class).block();
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
        payoutMethodSearchRepository.deleteAll().block();
        assertThat(payoutMethodSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        payoutMethod = createEntity(em);
    }

    @Test
    void createPayoutMethod() throws Exception {
        int databaseSizeBeforeCreate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(DEFAULT_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPayoutMethodWithExistingId() throws Exception {
        // Create the PayoutMethod with an existing ID
        payoutMethod.setId(1L);
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        int databaseSizeBeforeCreate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        // set the field null
        payoutMethod.setMethodName(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTokenTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        // set the field null
        payoutMethod.setTokenText(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        // set the field null
        payoutMethod.setCreatedDate(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        // set the field null
        payoutMethod.setIsDeleted(null);

        // Create the PayoutMethod, which fails.
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPayoutMethods() {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();

        // Get all the payoutMethodList
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
            .value(hasItem(payoutMethod.getId().intValue()))
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
    void getPayoutMethod() {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();

        // Get the payoutMethod
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, payoutMethod.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(payoutMethod.getId().intValue()))
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
    void getNonExistingPayoutMethod() {
        // Get the payoutMethod
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPayoutMethod() throws Exception {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        payoutMethodSearchRepository.save(payoutMethod).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());

        // Update the payoutMethod
        PayoutMethod updatedPayoutMethod = payoutMethodRepository.findById(payoutMethod.getId()).block();
        updatedPayoutMethod
            .methodName(UPDATED_METHOD_NAME)
            .tokenText(UPDATED_TOKEN_TEXT)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(updatedPayoutMethod);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payoutMethodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PayoutMethod> payoutMethodSearchList = IterableUtils.toList(
                    payoutMethodSearchRepository.findAll().collectList().block()
                );
                PayoutMethod testPayoutMethodSearch = payoutMethodSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPayoutMethodSearch.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
                assertThat(testPayoutMethodSearch.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
                assertThat(testPayoutMethodSearch.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
                assertThat(testPayoutMethodSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPayoutMethodSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPayoutMethodSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPayoutMethodSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPayoutMethodSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payoutMethodDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePayoutMethodWithPatch() throws Exception {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();

        // Update the payoutMethod using partial update
        PayoutMethod partialUpdatedPayoutMethod = new PayoutMethod();
        partialUpdatedPayoutMethod.setId(payoutMethod.getId());

        partialUpdatedPayoutMethod
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayoutMethod.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayoutMethod))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(DEFAULT_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdatePayoutMethodWithPatch() throws Exception {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();

        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();

        // Update the payoutMethod using partial update
        PayoutMethod partialUpdatedPayoutMethod = new PayoutMethod();
        partialUpdatedPayoutMethod.setId(payoutMethod.getId());

        partialUpdatedPayoutMethod
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
            .uri(ENTITY_API_URL_ID, partialUpdatedPayoutMethod.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayoutMethod))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        PayoutMethod testPayoutMethod = payoutMethodList.get(payoutMethodList.size() - 1);
        assertThat(testPayoutMethod.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testPayoutMethod.getTokenText()).isEqualTo(UPDATED_TOKEN_TEXT);
        assertThat(testPayoutMethod.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPayoutMethod.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayoutMethod.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPayoutMethod.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayoutMethod.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPayoutMethod.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, payoutMethodDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPayoutMethod() throws Exception {
        int databaseSizeBeforeUpdate = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        payoutMethod.setId(longCount.incrementAndGet());

        // Create the PayoutMethod
        PayoutMethodDTO payoutMethodDTO = payoutMethodMapper.toDto(payoutMethod);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payoutMethodDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PayoutMethod in the database
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePayoutMethod() {
        // Initialize the database
        payoutMethodRepository.save(payoutMethod).block();
        payoutMethodRepository.save(payoutMethod).block();
        payoutMethodSearchRepository.save(payoutMethod).block();

        int databaseSizeBeforeDelete = payoutMethodRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the payoutMethod
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, payoutMethod.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PayoutMethod> payoutMethodList = payoutMethodRepository.findAll().collectList().block();
        assertThat(payoutMethodList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(payoutMethodSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPayoutMethod() {
        // Initialize the database
        payoutMethod = payoutMethodRepository.save(payoutMethod).block();
        payoutMethodSearchRepository.save(payoutMethod).block();

        // Search the payoutMethod
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + payoutMethod.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(payoutMethod.getId().intValue()))
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
