package com.fanflip.admin.web.rest;

import static com.fanflip.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.AccountingRecord;
import com.fanflip.admin.domain.enumeration.AccountingType;
import com.fanflip.admin.repository.AccountingRecordRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.AccountingRecordSearchRepository;
import com.fanflip.admin.service.dto.AccountingRecordDTO;
import com.fanflip.admin.service.mapper.AccountingRecordMapper;
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
 * Integration tests for the {@link AccountingRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AccountingRecordResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEBIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEBIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CREDIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CREDIT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    private static final AccountingType DEFAULT_ACCOUNT_TYPE = AccountingType.ASSET;
    private static final AccountingType UPDATED_ACCOUNT_TYPE = AccountingType.LIABILITY;

    private static final Long DEFAULT_PAYMENT_ID = 1L;
    private static final Long UPDATED_PAYMENT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/accounting-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/accounting-records/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountingRecordRepository accountingRecordRepository;

    @Autowired
    private AccountingRecordMapper accountingRecordMapper;

    @Autowired
    private AccountingRecordSearchRepository accountingRecordSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private AccountingRecord accountingRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingRecord createEntity(EntityManager em) {
        AccountingRecord accountingRecord = new AccountingRecord()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .debit(DEFAULT_DEBIT)
            .credit(DEFAULT_CREDIT)
            .balance(DEFAULT_BALANCE)
            .accountType(DEFAULT_ACCOUNT_TYPE)
            .paymentId(DEFAULT_PAYMENT_ID);
        return accountingRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountingRecord createUpdatedEntity(EntityManager em) {
        AccountingRecord accountingRecord = new AccountingRecord()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .debit(UPDATED_DEBIT)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .paymentId(UPDATED_PAYMENT_ID);
        return accountingRecord;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(AccountingRecord.class).block();
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
        accountingRecordSearchRepository.deleteAll().block();
        assertThat(accountingRecordSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        accountingRecord = createEntity(em);
    }

    @Test
    void createAccountingRecord() throws Exception {
        int databaseSizeBeforeCreate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        AccountingRecord testAccountingRecord = accountingRecordList.get(accountingRecordList.size() - 1);
        assertThat(testAccountingRecord.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAccountingRecord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountingRecord.getDebit()).isEqualByComparingTo(DEFAULT_DEBIT);
        assertThat(testAccountingRecord.getCredit()).isEqualByComparingTo(DEFAULT_CREDIT);
        assertThat(testAccountingRecord.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAccountingRecord.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountingRecord.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
    }

    @Test
    void createAccountingRecordWithExistingId() throws Exception {
        // Create the AccountingRecord with an existing ID
        accountingRecord.setId(1L);
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        int databaseSizeBeforeCreate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        // set the field null
        accountingRecord.setDate(null);

        // Create the AccountingRecord, which fails.
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        // set the field null
        accountingRecord.setDescription(null);

        // Create the AccountingRecord, which fails.
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        // set the field null
        accountingRecord.setBalance(null);

        // Create the AccountingRecord, which fails.
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAccountTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        // set the field null
        accountingRecord.setAccountType(null);

        // Create the AccountingRecord, which fails.
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllAccountingRecordsAsStream() {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        List<AccountingRecord> accountingRecordList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AccountingRecordDTO.class)
            .getResponseBody()
            .map(accountingRecordMapper::toEntity)
            .filter(accountingRecord::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(accountingRecordList).isNotNull();
        assertThat(accountingRecordList).hasSize(1);
        AccountingRecord testAccountingRecord = accountingRecordList.get(0);
        assertThat(testAccountingRecord.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testAccountingRecord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountingRecord.getDebit()).isEqualByComparingTo(DEFAULT_DEBIT);
        assertThat(testAccountingRecord.getCredit()).isEqualByComparingTo(DEFAULT_CREDIT);
        assertThat(testAccountingRecord.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAccountingRecord.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountingRecord.getPaymentId()).isEqualTo(DEFAULT_PAYMENT_ID);
    }

    @Test
    void getAllAccountingRecords() {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        // Get all the accountingRecordList
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
            .value(hasItem(accountingRecord.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].debit")
            .value(hasItem(sameNumber(DEFAULT_DEBIT)))
            .jsonPath("$.[*].credit")
            .value(hasItem(sameNumber(DEFAULT_CREDIT)))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.[*].accountType")
            .value(hasItem(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.[*].paymentId")
            .value(hasItem(DEFAULT_PAYMENT_ID.intValue()));
    }

    @Test
    void getAccountingRecord() {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        // Get the accountingRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, accountingRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(accountingRecord.getId().intValue()))
            .jsonPath("$.date")
            .value(is(DEFAULT_DATE.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.debit")
            .value(is(sameNumber(DEFAULT_DEBIT)))
            .jsonPath("$.credit")
            .value(is(sameNumber(DEFAULT_CREDIT)))
            .jsonPath("$.balance")
            .value(is(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.accountType")
            .value(is(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.paymentId")
            .value(is(DEFAULT_PAYMENT_ID.intValue()));
    }

    @Test
    void getNonExistingAccountingRecord() {
        // Get the accountingRecord
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAccountingRecord() throws Exception {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        accountingRecordSearchRepository.save(accountingRecord).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());

        // Update the accountingRecord
        AccountingRecord updatedAccountingRecord = accountingRecordRepository.findById(accountingRecord.getId()).block();
        updatedAccountingRecord
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .debit(UPDATED_DEBIT)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .paymentId(UPDATED_PAYMENT_ID);
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(updatedAccountingRecord);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, accountingRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        AccountingRecord testAccountingRecord = accountingRecordList.get(accountingRecordList.size() - 1);
        assertThat(testAccountingRecord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccountingRecord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccountingRecord.getDebit()).isEqualByComparingTo(UPDATED_DEBIT);
        assertThat(testAccountingRecord.getCredit()).isEqualByComparingTo(UPDATED_CREDIT);
        assertThat(testAccountingRecord.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAccountingRecord.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountingRecord.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AccountingRecord> accountingRecordSearchList = IterableUtils.toList(
                    accountingRecordSearchRepository.findAll().collectList().block()
                );
                AccountingRecord testAccountingRecordSearch = accountingRecordSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testAccountingRecordSearch.getDate()).isEqualTo(UPDATED_DATE);
                assertThat(testAccountingRecordSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testAccountingRecordSearch.getDebit()).isEqualByComparingTo(UPDATED_DEBIT);
                assertThat(testAccountingRecordSearch.getCredit()).isEqualByComparingTo(UPDATED_CREDIT);
                assertThat(testAccountingRecordSearch.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
                assertThat(testAccountingRecordSearch.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
                assertThat(testAccountingRecordSearch.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
            });
    }

    @Test
    void putNonExistingAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, accountingRecordDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateAccountingRecordWithPatch() throws Exception {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();

        // Update the accountingRecord using partial update
        AccountingRecord partialUpdatedAccountingRecord = new AccountingRecord();
        partialUpdatedAccountingRecord.setId(accountingRecord.getId());

        partialUpdatedAccountingRecord.date(UPDATED_DATE).credit(UPDATED_CREDIT).paymentId(UPDATED_PAYMENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAccountingRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountingRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        AccountingRecord testAccountingRecord = accountingRecordList.get(accountingRecordList.size() - 1);
        assertThat(testAccountingRecord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccountingRecord.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountingRecord.getDebit()).isEqualByComparingTo(DEFAULT_DEBIT);
        assertThat(testAccountingRecord.getCredit()).isEqualByComparingTo(UPDATED_CREDIT);
        assertThat(testAccountingRecord.getBalance()).isEqualByComparingTo(DEFAULT_BALANCE);
        assertThat(testAccountingRecord.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
        assertThat(testAccountingRecord.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
    }

    @Test
    void fullUpdateAccountingRecordWithPatch() throws Exception {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();

        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();

        // Update the accountingRecord using partial update
        AccountingRecord partialUpdatedAccountingRecord = new AccountingRecord();
        partialUpdatedAccountingRecord.setId(accountingRecord.getId());

        partialUpdatedAccountingRecord
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .debit(UPDATED_DEBIT)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .accountType(UPDATED_ACCOUNT_TYPE)
            .paymentId(UPDATED_PAYMENT_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAccountingRecord.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountingRecord))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        AccountingRecord testAccountingRecord = accountingRecordList.get(accountingRecordList.size() - 1);
        assertThat(testAccountingRecord.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testAccountingRecord.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccountingRecord.getDebit()).isEqualByComparingTo(UPDATED_DEBIT);
        assertThat(testAccountingRecord.getCredit()).isEqualByComparingTo(UPDATED_CREDIT);
        assertThat(testAccountingRecord.getBalance()).isEqualByComparingTo(UPDATED_BALANCE);
        assertThat(testAccountingRecord.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
        assertThat(testAccountingRecord.getPaymentId()).isEqualTo(UPDATED_PAYMENT_ID);
    }

    @Test
    void patchNonExistingAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, accountingRecordDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamAccountingRecord() throws Exception {
        int databaseSizeBeforeUpdate = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        accountingRecord.setId(longCount.incrementAndGet());

        // Create the AccountingRecord
        AccountingRecordDTO accountingRecordDTO = accountingRecordMapper.toDto(accountingRecord);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(accountingRecordDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AccountingRecord in the database
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteAccountingRecord() {
        // Initialize the database
        accountingRecordRepository.save(accountingRecord).block();
        accountingRecordRepository.save(accountingRecord).block();
        accountingRecordSearchRepository.save(accountingRecord).block();

        int databaseSizeBeforeDelete = accountingRecordRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the accountingRecord
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, accountingRecord.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AccountingRecord> accountingRecordList = accountingRecordRepository.findAll().collectList().block();
        assertThat(accountingRecordList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(accountingRecordSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchAccountingRecord() {
        // Initialize the database
        accountingRecord = accountingRecordRepository.save(accountingRecord).block();
        accountingRecordSearchRepository.save(accountingRecord).block();

        // Search the accountingRecord
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + accountingRecord.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(accountingRecord.getId().intValue()))
            .jsonPath("$.[*].date")
            .value(hasItem(DEFAULT_DATE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].debit")
            .value(hasItem(sameNumber(DEFAULT_DEBIT)))
            .jsonPath("$.[*].credit")
            .value(hasItem(sameNumber(DEFAULT_CREDIT)))
            .jsonPath("$.[*].balance")
            .value(hasItem(sameNumber(DEFAULT_BALANCE)))
            .jsonPath("$.[*].accountType")
            .value(hasItem(DEFAULT_ACCOUNT_TYPE.toString()))
            .jsonPath("$.[*].paymentId")
            .value(hasItem(DEFAULT_PAYMENT_ID.intValue()));
    }
}
