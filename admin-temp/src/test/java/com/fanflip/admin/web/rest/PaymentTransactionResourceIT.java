package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.PaymentTransaction;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.enumeration.GenericStatus;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PaymentTransactionRepository;
import com.monsterdam.admin.repository.search.PaymentTransactionSearchRepository;
import com.monsterdam.admin.service.PaymentTransactionService;
import com.monsterdam.admin.service.dto.PaymentTransactionDTO;
import com.monsterdam.admin.service.mapper.PaymentTransactionMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentTransactionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentTransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_PAYMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GenericStatus DEFAULT_PAYMENT_STATUS = GenericStatus.PENDING;
    private static final GenericStatus UPDATED_PAYMENT_STATUS = GenericStatus.COMPLETED;

    private static final String DEFAULT_PAYMENT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_CLOUD_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_CLOUD_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payment-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/payment-transactions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepositoryMock;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Mock
    private PaymentTransactionService paymentTransactionServiceMock;

    @Autowired
    private PaymentTransactionSearchRepository paymentTransactionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PaymentTransaction paymentTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTransaction createEntity(EntityManager em) {
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .amount(DEFAULT_AMOUNT)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .paymentStatus(DEFAULT_PAYMENT_STATUS)
            .paymentReference(DEFAULT_PAYMENT_REFERENCE)
            .cloudTransactionId(DEFAULT_CLOUD_TRANSACTION_ID);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        paymentTransaction.setViewer(userProfile);
        return paymentTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaymentTransaction createUpdatedEntity(EntityManager em) {
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        paymentTransaction.setViewer(userProfile);
        return paymentTransaction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PaymentTransaction.class).block();
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
        paymentTransactionSearchRepository.deleteAll().block();
        assertThat(paymentTransactionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        paymentTransaction = createEntity(em);
    }

    @Test
    void createPaymentTransaction() throws Exception {
        int databaseSizeBeforeCreate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(DEFAULT_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(DEFAULT_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(DEFAULT_CLOUD_TRANSACTION_ID);
    }

    @Test
    void createPaymentTransactionWithExistingId() throws Exception {
        // Create the PaymentTransaction with an existing ID
        paymentTransaction.setId(1L);
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        int databaseSizeBeforeCreate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        paymentTransaction.setAmount(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPaymentDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        paymentTransaction.setPaymentDate(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPaymentStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        paymentTransaction.setPaymentStatus(null);

        // Create the PaymentTransaction, which fails.
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPaymentTransactions() {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();

        // Get all the paymentTransactionList
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
            .value(hasItem(paymentTransaction.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].paymentDate")
            .value(hasItem(DEFAULT_PAYMENT_DATE.toString()))
            .jsonPath("$.[*].paymentStatus")
            .value(hasItem(DEFAULT_PAYMENT_STATUS.toString()))
            .jsonPath("$.[*].paymentReference")
            .value(hasItem(DEFAULT_PAYMENT_REFERENCE))
            .jsonPath("$.[*].cloudTransactionId")
            .value(hasItem(DEFAULT_CLOUD_TRANSACTION_ID));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentTransactionsWithEagerRelationshipsIsEnabled() {
        when(paymentTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(paymentTransactionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaymentTransactionsWithEagerRelationshipsIsNotEnabled() {
        when(paymentTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(paymentTransactionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPaymentTransaction() {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();

        // Get the paymentTransaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, paymentTransaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(paymentTransaction.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.paymentDate")
            .value(is(DEFAULT_PAYMENT_DATE.toString()))
            .jsonPath("$.paymentStatus")
            .value(is(DEFAULT_PAYMENT_STATUS.toString()))
            .jsonPath("$.paymentReference")
            .value(is(DEFAULT_PAYMENT_REFERENCE))
            .jsonPath("$.cloudTransactionId")
            .value(is(DEFAULT_CLOUD_TRANSACTION_ID));
    }

    @Test
    void getNonExistingPaymentTransaction() {
        // Get the paymentTransaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPaymentTransaction() throws Exception {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        paymentTransactionSearchRepository.save(paymentTransaction).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());

        // Update the paymentTransaction
        PaymentTransaction updatedPaymentTransaction = paymentTransactionRepository.findById(paymentTransaction.getId()).block();
        updatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID);
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(updatedPaymentTransaction);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PaymentTransaction> paymentTransactionSearchList = IterableUtils.toList(
                    paymentTransactionSearchRepository.findAll().collectList().block()
                );
                PaymentTransaction testPaymentTransactionSearch = paymentTransactionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPaymentTransactionSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testPaymentTransactionSearch.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
                assertThat(testPaymentTransactionSearch.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
                assertThat(testPaymentTransactionSearch.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
                assertThat(testPaymentTransactionSearch.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
            });
    }

    @Test
    void putNonExistingPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();

        // Update the paymentTransaction using partial update
        PaymentTransaction partialUpdatedPaymentTransaction = new PaymentTransaction();
        partialUpdatedPaymentTransaction.setId(paymentTransaction.getId());

        partialUpdatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
    }

    @Test
    void fullUpdatePaymentTransactionWithPatch() throws Exception {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();

        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();

        // Update the paymentTransaction using partial update
        PaymentTransaction partialUpdatedPaymentTransaction = new PaymentTransaction();
        partialUpdatedPaymentTransaction.setId(paymentTransaction.getId());

        partialUpdatedPaymentTransaction
            .amount(UPDATED_AMOUNT)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .paymentStatus(UPDATED_PAYMENT_STATUS)
            .paymentReference(UPDATED_PAYMENT_REFERENCE)
            .cloudTransactionId(UPDATED_CLOUD_TRANSACTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPaymentTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPaymentTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransaction testPaymentTransaction = paymentTransactionList.get(paymentTransactionList.size() - 1);
        assertThat(testPaymentTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPaymentTransaction.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testPaymentTransaction.getPaymentStatus()).isEqualTo(UPDATED_PAYMENT_STATUS);
        assertThat(testPaymentTransaction.getPaymentReference()).isEqualTo(UPDATED_PAYMENT_REFERENCE);
        assertThat(testPaymentTransaction.getCloudTransactionId()).isEqualTo(UPDATED_CLOUD_TRANSACTION_ID);
    }

    @Test
    void patchNonExistingPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, paymentTransactionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPaymentTransaction() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        paymentTransaction.setId(longCount.incrementAndGet());

        // Create the PaymentTransaction
        PaymentTransactionDTO paymentTransactionDTO = paymentTransactionMapper.toDto(paymentTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(paymentTransactionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PaymentTransaction in the database
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePaymentTransaction() {
        // Initialize the database
        paymentTransactionRepository.save(paymentTransaction).block();
        paymentTransactionRepository.save(paymentTransaction).block();
        paymentTransactionSearchRepository.save(paymentTransaction).block();

        int databaseSizeBeforeDelete = paymentTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the paymentTransaction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, paymentTransaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PaymentTransaction> paymentTransactionList = paymentTransactionRepository.findAll().collectList().block();
        assertThat(paymentTransactionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(paymentTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPaymentTransaction() {
        // Initialize the database
        paymentTransaction = paymentTransactionRepository.save(paymentTransaction).block();
        paymentTransactionSearchRepository.save(paymentTransaction).block();

        // Search the paymentTransaction
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + paymentTransaction.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(paymentTransaction.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].paymentDate")
            .value(hasItem(DEFAULT_PAYMENT_DATE.toString()))
            .jsonPath("$.[*].paymentStatus")
            .value(hasItem(DEFAULT_PAYMENT_STATUS.toString()))
            .jsonPath("$.[*].paymentReference")
            .value(hasItem(DEFAULT_PAYMENT_REFERENCE))
            .jsonPath("$.[*].cloudTransactionId")
            .value(hasItem(DEFAULT_CLOUD_TRANSACTION_ID));
    }
}
