package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.WalletTransaction;
import com.monsterdam.admin.domain.enumeration.WalletTransactionType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.WalletTransactionRepository;
import com.monsterdam.admin.repository.search.WalletTransactionSearchRepository;
import com.monsterdam.admin.service.WalletTransactionService;
import com.monsterdam.admin.service.dto.WalletTransactionDTO;
import com.monsterdam.admin.service.mapper.WalletTransactionMapper;
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
 * Integration tests for the {@link WalletTransactionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class WalletTransactionResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final WalletTransactionType DEFAULT_TRANSACTION_TYPE = WalletTransactionType.TOP_UP;
    private static final WalletTransactionType UPDATED_TRANSACTION_TYPE = WalletTransactionType.PURCHASE;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/wallet-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/wallet-transactions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepositoryMock;

    @Autowired
    private WalletTransactionMapper walletTransactionMapper;

    @Mock
    private WalletTransactionService walletTransactionServiceMock;

    @Autowired
    private WalletTransactionSearchRepository walletTransactionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private WalletTransaction walletTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .amount(DEFAULT_AMOUNT)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        walletTransaction.setViewer(userProfile);
        return walletTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createUpdatedEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        walletTransaction.setViewer(userProfile);
        return walletTransaction;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(WalletTransaction.class).block();
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
        walletTransactionSearchRepository.deleteAll().block();
        assertThat(walletTransactionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        walletTransaction = createEntity(em);
    }

    @Test
    void createWalletTransaction() throws Exception {
        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createWalletTransactionWithExistingId() throws Exception {
        // Create the WalletTransaction with an existing ID
        walletTransaction.setId(1L);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        walletTransaction.setAmount(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTransactionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        walletTransaction.setTransactionType(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        walletTransaction.setCreatedDate(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        // set the field null
        walletTransaction.setIsDeleted(null);

        // Create the WalletTransaction, which fails.
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllWalletTransactions() {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();

        // Get all the walletTransactionList
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
            .value(hasItem(walletTransaction.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].transactionType")
            .value(hasItem(DEFAULT_TRANSACTION_TYPE.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsEnabled() {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(walletTransactionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletTransactionsWithEagerRelationshipsIsNotEnabled() {
        when(walletTransactionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(walletTransactionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getWalletTransaction() {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();

        // Get the walletTransaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, walletTransaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(walletTransaction.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.transactionType")
            .value(is(DEFAULT_TRANSACTION_TYPE.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingWalletTransaction() {
        // Get the walletTransaction
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        walletTransactionSearchRepository.save(walletTransaction).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());

        // Update the walletTransaction
        WalletTransaction updatedWalletTransaction = walletTransactionRepository.findById(walletTransaction.getId()).block();
        updatedWalletTransaction
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(updatedWalletTransaction);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, walletTransactionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WalletTransaction> walletTransactionSearchList = IterableUtils.toList(
                    walletTransactionSearchRepository.findAll().collectList().block()
                );
                WalletTransaction testWalletTransactionSearch = walletTransactionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testWalletTransactionSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testWalletTransactionSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testWalletTransactionSearch.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
                assertThat(testWalletTransactionSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testWalletTransactionSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testWalletTransactionSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testWalletTransactionSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, walletTransactionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction.createdDate(UPDATED_CREATED_DATE).createdBy(UPDATED_CREATED_BY).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWalletTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();

        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction
            .amount(UPDATED_AMOUNT)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedWalletTransaction))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testWalletTransaction.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testWalletTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testWalletTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testWalletTransaction.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, walletTransactionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        walletTransaction.setId(longCount.incrementAndGet());

        // Create the WalletTransaction
        WalletTransactionDTO walletTransactionDTO = walletTransactionMapper.toDto(walletTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(walletTransactionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteWalletTransaction() {
        // Initialize the database
        walletTransactionRepository.save(walletTransaction).block();
        walletTransactionRepository.save(walletTransaction).block();
        walletTransactionSearchRepository.save(walletTransaction).block();

        int databaseSizeBeforeDelete = walletTransactionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the walletTransaction
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, walletTransaction.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll().collectList().block();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(walletTransactionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchWalletTransaction() {
        // Initialize the database
        walletTransaction = walletTransactionRepository.save(walletTransaction).block();
        walletTransactionSearchRepository.save(walletTransaction).block();

        // Search the walletTransaction
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + walletTransaction.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(walletTransaction.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].transactionType")
            .value(hasItem(DEFAULT_TRANSACTION_TYPE.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
