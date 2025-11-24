package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.FinancialStatement;
import com.monsterdam.admin.domain.enumeration.StatementType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.FinancialStatementRepository;
import com.monsterdam.admin.repository.search.FinancialStatementSearchRepository;
import com.monsterdam.admin.service.FinancialStatementService;
import com.monsterdam.admin.service.dto.FinancialStatementDTO;
import com.monsterdam.admin.service.mapper.FinancialStatementMapper;
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
 * Integration tests for the {@link FinancialStatementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FinancialStatementResourceIT {

    private static final StatementType DEFAULT_STATEMENT_TYPE = StatementType.BALANCE_SHEET;
    private static final StatementType UPDATED_STATEMENT_TYPE = StatementType.INCOME_STATEMENT;

    private static final LocalDate DEFAULT_PERIOD_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIOD_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIOD_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/financial-statements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/financial-statements/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FinancialStatementRepository financialStatementRepository;

    @Mock
    private FinancialStatementRepository financialStatementRepositoryMock;

    @Autowired
    private FinancialStatementMapper financialStatementMapper;

    @Mock
    private FinancialStatementService financialStatementServiceMock;

    @Autowired
    private FinancialStatementSearchRepository financialStatementSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FinancialStatement financialStatement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialStatement createEntity(EntityManager em) {
        FinancialStatement financialStatement = new FinancialStatement()
            .statementType(DEFAULT_STATEMENT_TYPE)
            .periodStartDate(DEFAULT_PERIOD_START_DATE)
            .periodEndDate(DEFAULT_PERIOD_END_DATE)
            .createdDate(DEFAULT_CREATED_DATE);
        return financialStatement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialStatement createUpdatedEntity(EntityManager em) {
        FinancialStatement financialStatement = new FinancialStatement()
            .statementType(UPDATED_STATEMENT_TYPE)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .createdDate(UPDATED_CREATED_DATE);
        return financialStatement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_financial_statement__accounting_records").block();
            em.deleteAll(FinancialStatement.class).block();
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
        financialStatementSearchRepository.deleteAll().block();
        assertThat(financialStatementSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        financialStatement = createEntity(em);
    }

    @Test
    void createFinancialStatement() throws Exception {
        int databaseSizeBeforeCreate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        FinancialStatement testFinancialStatement = financialStatementList.get(financialStatementList.size() - 1);
        assertThat(testFinancialStatement.getStatementType()).isEqualTo(DEFAULT_STATEMENT_TYPE);
        assertThat(testFinancialStatement.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testFinancialStatement.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testFinancialStatement.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    void createFinancialStatementWithExistingId() throws Exception {
        // Create the FinancialStatement with an existing ID
        financialStatement.setId(1L);
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        int databaseSizeBeforeCreate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStatementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        // set the field null
        financialStatement.setStatementType(null);

        // Create the FinancialStatement, which fails.
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPeriodStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        // set the field null
        financialStatement.setPeriodStartDate(null);

        // Create the FinancialStatement, which fails.
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPeriodEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        // set the field null
        financialStatement.setPeriodEndDate(null);

        // Create the FinancialStatement, which fails.
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        // set the field null
        financialStatement.setCreatedDate(null);

        // Create the FinancialStatement, which fails.
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFinancialStatementsAsStream() {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        List<FinancialStatement> financialStatementList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FinancialStatementDTO.class)
            .getResponseBody()
            .map(financialStatementMapper::toEntity)
            .filter(financialStatement::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(financialStatementList).isNotNull();
        assertThat(financialStatementList).hasSize(1);
        FinancialStatement testFinancialStatement = financialStatementList.get(0);
        assertThat(testFinancialStatement.getStatementType()).isEqualTo(DEFAULT_STATEMENT_TYPE);
        assertThat(testFinancialStatement.getPeriodStartDate()).isEqualTo(DEFAULT_PERIOD_START_DATE);
        assertThat(testFinancialStatement.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testFinancialStatement.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    void getAllFinancialStatements() {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        // Get all the financialStatementList
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
            .value(hasItem(financialStatement.getId().intValue()))
            .jsonPath("$.[*].statementType")
            .value(hasItem(DEFAULT_STATEMENT_TYPE.toString()))
            .jsonPath("$.[*].periodStartDate")
            .value(hasItem(DEFAULT_PERIOD_START_DATE.toString()))
            .jsonPath("$.[*].periodEndDate")
            .value(hasItem(DEFAULT_PERIOD_END_DATE.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialStatementsWithEagerRelationshipsIsEnabled() {
        when(financialStatementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(financialStatementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFinancialStatementsWithEagerRelationshipsIsNotEnabled() {
        when(financialStatementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(financialStatementRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFinancialStatement() {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        // Get the financialStatement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, financialStatement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(financialStatement.getId().intValue()))
            .jsonPath("$.statementType")
            .value(is(DEFAULT_STATEMENT_TYPE.toString()))
            .jsonPath("$.periodStartDate")
            .value(is(DEFAULT_PERIOD_START_DATE.toString()))
            .jsonPath("$.periodEndDate")
            .value(is(DEFAULT_PERIOD_END_DATE.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    void getNonExistingFinancialStatement() {
        // Get the financialStatement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFinancialStatement() throws Exception {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        financialStatementSearchRepository.save(financialStatement).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());

        // Update the financialStatement
        FinancialStatement updatedFinancialStatement = financialStatementRepository.findById(financialStatement.getId()).block();
        updatedFinancialStatement
            .statementType(UPDATED_STATEMENT_TYPE)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .createdDate(UPDATED_CREATED_DATE);
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(updatedFinancialStatement);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, financialStatementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        FinancialStatement testFinancialStatement = financialStatementList.get(financialStatementList.size() - 1);
        assertThat(testFinancialStatement.getStatementType()).isEqualTo(UPDATED_STATEMENT_TYPE);
        assertThat(testFinancialStatement.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testFinancialStatement.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testFinancialStatement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FinancialStatement> financialStatementSearchList = IterableUtils.toList(
                    financialStatementSearchRepository.findAll().collectList().block()
                );
                FinancialStatement testFinancialStatementSearch = financialStatementSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFinancialStatementSearch.getStatementType()).isEqualTo(UPDATED_STATEMENT_TYPE);
                assertThat(testFinancialStatementSearch.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
                assertThat(testFinancialStatementSearch.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
                assertThat(testFinancialStatementSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
            });
    }

    @Test
    void putNonExistingFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, financialStatementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFinancialStatementWithPatch() throws Exception {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();

        // Update the financialStatement using partial update
        FinancialStatement partialUpdatedFinancialStatement = new FinancialStatement();
        partialUpdatedFinancialStatement.setId(financialStatement.getId());

        partialUpdatedFinancialStatement.periodStartDate(UPDATED_PERIOD_START_DATE).createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFinancialStatement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialStatement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        FinancialStatement testFinancialStatement = financialStatementList.get(financialStatementList.size() - 1);
        assertThat(testFinancialStatement.getStatementType()).isEqualTo(DEFAULT_STATEMENT_TYPE);
        assertThat(testFinancialStatement.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testFinancialStatement.getPeriodEndDate()).isEqualTo(DEFAULT_PERIOD_END_DATE);
        assertThat(testFinancialStatement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    void fullUpdateFinancialStatementWithPatch() throws Exception {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();

        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();

        // Update the financialStatement using partial update
        FinancialStatement partialUpdatedFinancialStatement = new FinancialStatement();
        partialUpdatedFinancialStatement.setId(financialStatement.getId());

        partialUpdatedFinancialStatement
            .statementType(UPDATED_STATEMENT_TYPE)
            .periodStartDate(UPDATED_PERIOD_START_DATE)
            .periodEndDate(UPDATED_PERIOD_END_DATE)
            .createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFinancialStatement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFinancialStatement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        FinancialStatement testFinancialStatement = financialStatementList.get(financialStatementList.size() - 1);
        assertThat(testFinancialStatement.getStatementType()).isEqualTo(UPDATED_STATEMENT_TYPE);
        assertThat(testFinancialStatement.getPeriodStartDate()).isEqualTo(UPDATED_PERIOD_START_DATE);
        assertThat(testFinancialStatement.getPeriodEndDate()).isEqualTo(UPDATED_PERIOD_END_DATE);
        assertThat(testFinancialStatement.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    void patchNonExistingFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, financialStatementDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFinancialStatement() throws Exception {
        int databaseSizeBeforeUpdate = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        financialStatement.setId(longCount.incrementAndGet());

        // Create the FinancialStatement
        FinancialStatementDTO financialStatementDTO = financialStatementMapper.toDto(financialStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(financialStatementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FinancialStatement in the database
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFinancialStatement() {
        // Initialize the database
        financialStatementRepository.save(financialStatement).block();
        financialStatementRepository.save(financialStatement).block();
        financialStatementSearchRepository.save(financialStatement).block();

        int databaseSizeBeforeDelete = financialStatementRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the financialStatement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, financialStatement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FinancialStatement> financialStatementList = financialStatementRepository.findAll().collectList().block();
        assertThat(financialStatementList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(financialStatementSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFinancialStatement() {
        // Initialize the database
        financialStatement = financialStatementRepository.save(financialStatement).block();
        financialStatementSearchRepository.save(financialStatement).block();

        // Search the financialStatement
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + financialStatement.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(financialStatement.getId().intValue()))
            .jsonPath("$.[*].statementType")
            .value(hasItem(DEFAULT_STATEMENT_TYPE.toString()))
            .jsonPath("$.[*].periodStartDate")
            .value(hasItem(DEFAULT_PERIOD_START_DATE.toString()))
            .jsonPath("$.[*].periodEndDate")
            .value(hasItem(DEFAULT_PERIOD_END_DATE.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()));
    }
}
