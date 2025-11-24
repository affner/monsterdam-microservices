package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.TaxDeclaration;
import com.monsterdam.admin.domain.enumeration.TaxDeclarationStatus;
import com.monsterdam.admin.domain.enumeration.TaxDeclarationType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.TaxDeclarationRepository;
import com.monsterdam.admin.repository.search.TaxDeclarationSearchRepository;
import com.monsterdam.admin.service.TaxDeclarationService;
import com.monsterdam.admin.service.dto.TaxDeclarationDTO;
import com.monsterdam.admin.service.mapper.TaxDeclarationMapper;
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
 * Integration tests for the {@link TaxDeclarationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TaxDeclarationResourceIT {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final TaxDeclarationType DEFAULT_DECLARATION_TYPE = TaxDeclarationType.INCOME_TAX;
    private static final TaxDeclarationType UPDATED_DECLARATION_TYPE = TaxDeclarationType.VAT;

    private static final Instant DEFAULT_SUBMITTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TaxDeclarationStatus DEFAULT_STATUS = TaxDeclarationStatus.DRAFT;
    private static final TaxDeclarationStatus UPDATED_STATUS = TaxDeclarationStatus.SUBMITTED;

    private static final BigDecimal DEFAULT_TOTAL_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_INCOME = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_TAXABLE_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TAXABLE_INCOME = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_TAX_PAID = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TAX_PAID = new BigDecimal(2);

    private static final String DEFAULT_SUPPORTING_DOCUMENTS_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORTING_DOCUMENTS_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tax-declarations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tax-declarations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxDeclarationRepository taxDeclarationRepository;

    @Mock
    private TaxDeclarationRepository taxDeclarationRepositoryMock;

    @Autowired
    private TaxDeclarationMapper taxDeclarationMapper;

    @Mock
    private TaxDeclarationService taxDeclarationServiceMock;

    @Autowired
    private TaxDeclarationSearchRepository taxDeclarationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TaxDeclaration taxDeclaration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxDeclaration createEntity(EntityManager em) {
        TaxDeclaration taxDeclaration = new TaxDeclaration()
            .year(DEFAULT_YEAR)
            .declarationType(DEFAULT_DECLARATION_TYPE)
            .submittedDate(DEFAULT_SUBMITTED_DATE)
            .status(DEFAULT_STATUS)
            .totalIncome(DEFAULT_TOTAL_INCOME)
            .totalTaxableIncome(DEFAULT_TOTAL_TAXABLE_INCOME)
            .totalTaxPaid(DEFAULT_TOTAL_TAX_PAID)
            .supportingDocumentsKey(DEFAULT_SUPPORTING_DOCUMENTS_KEY);
        return taxDeclaration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxDeclaration createUpdatedEntity(EntityManager em) {
        TaxDeclaration taxDeclaration = new TaxDeclaration()
            .year(UPDATED_YEAR)
            .declarationType(UPDATED_DECLARATION_TYPE)
            .submittedDate(UPDATED_SUBMITTED_DATE)
            .status(UPDATED_STATUS)
            .totalIncome(UPDATED_TOTAL_INCOME)
            .totalTaxableIncome(UPDATED_TOTAL_TAXABLE_INCOME)
            .totalTaxPaid(UPDATED_TOTAL_TAX_PAID)
            .supportingDocumentsKey(UPDATED_SUPPORTING_DOCUMENTS_KEY);
        return taxDeclaration;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_tax_declaration__accounting_records").block();
            em.deleteAll(TaxDeclaration.class).block();
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
        taxDeclarationSearchRepository.deleteAll().block();
        assertThat(taxDeclarationSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        taxDeclaration = createEntity(em);
    }

    @Test
    void createTaxDeclaration() throws Exception {
        int databaseSizeBeforeCreate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TaxDeclaration testTaxDeclaration = taxDeclarationList.get(taxDeclarationList.size() - 1);
        assertThat(testTaxDeclaration.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testTaxDeclaration.getDeclarationType()).isEqualTo(DEFAULT_DECLARATION_TYPE);
        assertThat(testTaxDeclaration.getSubmittedDate()).isEqualTo(DEFAULT_SUBMITTED_DATE);
        assertThat(testTaxDeclaration.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTaxDeclaration.getTotalIncome()).isEqualByComparingTo(DEFAULT_TOTAL_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxableIncome()).isEqualByComparingTo(DEFAULT_TOTAL_TAXABLE_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxPaid()).isEqualByComparingTo(DEFAULT_TOTAL_TAX_PAID);
        assertThat(testTaxDeclaration.getSupportingDocumentsKey()).isEqualTo(DEFAULT_SUPPORTING_DOCUMENTS_KEY);
    }

    @Test
    void createTaxDeclarationWithExistingId() throws Exception {
        // Create the TaxDeclaration with an existing ID
        taxDeclaration.setId(1L);
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        int databaseSizeBeforeCreate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        // set the field null
        taxDeclaration.setYear(null);

        // Create the TaxDeclaration, which fails.
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDeclarationTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        // set the field null
        taxDeclaration.setDeclarationType(null);

        // Create the TaxDeclaration, which fails.
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        // set the field null
        taxDeclaration.setStatus(null);

        // Create the TaxDeclaration, which fails.
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllTaxDeclarations() {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();

        // Get all the taxDeclarationList
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
            .value(hasItem(taxDeclaration.getId().intValue()))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].declarationType")
            .value(hasItem(DEFAULT_DECLARATION_TYPE.toString()))
            .jsonPath("$.[*].submittedDate")
            .value(hasItem(DEFAULT_SUBMITTED_DATE.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].totalIncome")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_INCOME)))
            .jsonPath("$.[*].totalTaxableIncome")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_TAXABLE_INCOME)))
            .jsonPath("$.[*].totalTaxPaid")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_TAX_PAID)))
            .jsonPath("$.[*].supportingDocumentsKey")
            .value(hasItem(DEFAULT_SUPPORTING_DOCUMENTS_KEY));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxDeclarationsWithEagerRelationshipsIsEnabled() {
        when(taxDeclarationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(taxDeclarationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxDeclarationsWithEagerRelationshipsIsNotEnabled() {
        when(taxDeclarationServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(taxDeclarationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTaxDeclaration() {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();

        // Get the taxDeclaration
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, taxDeclaration.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(taxDeclaration.getId().intValue()))
            .jsonPath("$.year")
            .value(is(DEFAULT_YEAR))
            .jsonPath("$.declarationType")
            .value(is(DEFAULT_DECLARATION_TYPE.toString()))
            .jsonPath("$.submittedDate")
            .value(is(DEFAULT_SUBMITTED_DATE.toString()))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS.toString()))
            .jsonPath("$.totalIncome")
            .value(is(sameNumber(DEFAULT_TOTAL_INCOME)))
            .jsonPath("$.totalTaxableIncome")
            .value(is(sameNumber(DEFAULT_TOTAL_TAXABLE_INCOME)))
            .jsonPath("$.totalTaxPaid")
            .value(is(sameNumber(DEFAULT_TOTAL_TAX_PAID)))
            .jsonPath("$.supportingDocumentsKey")
            .value(is(DEFAULT_SUPPORTING_DOCUMENTS_KEY));
    }

    @Test
    void getNonExistingTaxDeclaration() {
        // Get the taxDeclaration
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTaxDeclaration() throws Exception {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();

        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        taxDeclarationSearchRepository.save(taxDeclaration).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());

        // Update the taxDeclaration
        TaxDeclaration updatedTaxDeclaration = taxDeclarationRepository.findById(taxDeclaration.getId()).block();
        updatedTaxDeclaration
            .year(UPDATED_YEAR)
            .declarationType(UPDATED_DECLARATION_TYPE)
            .submittedDate(UPDATED_SUBMITTED_DATE)
            .status(UPDATED_STATUS)
            .totalIncome(UPDATED_TOTAL_INCOME)
            .totalTaxableIncome(UPDATED_TOTAL_TAXABLE_INCOME)
            .totalTaxPaid(UPDATED_TOTAL_TAX_PAID)
            .supportingDocumentsKey(UPDATED_SUPPORTING_DOCUMENTS_KEY);
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(updatedTaxDeclaration);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxDeclarationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        TaxDeclaration testTaxDeclaration = taxDeclarationList.get(taxDeclarationList.size() - 1);
        assertThat(testTaxDeclaration.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testTaxDeclaration.getDeclarationType()).isEqualTo(UPDATED_DECLARATION_TYPE);
        assertThat(testTaxDeclaration.getSubmittedDate()).isEqualTo(UPDATED_SUBMITTED_DATE);
        assertThat(testTaxDeclaration.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTaxDeclaration.getTotalIncome()).isEqualByComparingTo(UPDATED_TOTAL_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxableIncome()).isEqualByComparingTo(UPDATED_TOTAL_TAXABLE_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxPaid()).isEqualByComparingTo(UPDATED_TOTAL_TAX_PAID);
        assertThat(testTaxDeclaration.getSupportingDocumentsKey()).isEqualTo(UPDATED_SUPPORTING_DOCUMENTS_KEY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TaxDeclaration> taxDeclarationSearchList = IterableUtils.toList(
                    taxDeclarationSearchRepository.findAll().collectList().block()
                );
                TaxDeclaration testTaxDeclarationSearch = taxDeclarationSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTaxDeclarationSearch.getYear()).isEqualTo(UPDATED_YEAR);
                assertThat(testTaxDeclarationSearch.getDeclarationType()).isEqualTo(UPDATED_DECLARATION_TYPE);
                assertThat(testTaxDeclarationSearch.getSubmittedDate()).isEqualTo(UPDATED_SUBMITTED_DATE);
                assertThat(testTaxDeclarationSearch.getStatus()).isEqualTo(UPDATED_STATUS);
                assertThat(testTaxDeclarationSearch.getTotalIncome()).isEqualByComparingTo(UPDATED_TOTAL_INCOME);
                assertThat(testTaxDeclarationSearch.getTotalTaxableIncome()).isEqualByComparingTo(UPDATED_TOTAL_TAXABLE_INCOME);
                assertThat(testTaxDeclarationSearch.getTotalTaxPaid()).isEqualByComparingTo(UPDATED_TOTAL_TAX_PAID);
                assertThat(testTaxDeclarationSearch.getSupportingDocumentsKey()).isEqualTo(UPDATED_SUPPORTING_DOCUMENTS_KEY);
            });
    }

    @Test
    void putNonExistingTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxDeclarationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateTaxDeclarationWithPatch() throws Exception {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();

        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();

        // Update the taxDeclaration using partial update
        TaxDeclaration partialUpdatedTaxDeclaration = new TaxDeclaration();
        partialUpdatedTaxDeclaration.setId(taxDeclaration.getId());

        partialUpdatedTaxDeclaration.totalIncome(UPDATED_TOTAL_INCOME).supportingDocumentsKey(UPDATED_SUPPORTING_DOCUMENTS_KEY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxDeclaration.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxDeclaration))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        TaxDeclaration testTaxDeclaration = taxDeclarationList.get(taxDeclarationList.size() - 1);
        assertThat(testTaxDeclaration.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testTaxDeclaration.getDeclarationType()).isEqualTo(DEFAULT_DECLARATION_TYPE);
        assertThat(testTaxDeclaration.getSubmittedDate()).isEqualTo(DEFAULT_SUBMITTED_DATE);
        assertThat(testTaxDeclaration.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTaxDeclaration.getTotalIncome()).isEqualByComparingTo(UPDATED_TOTAL_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxableIncome()).isEqualByComparingTo(DEFAULT_TOTAL_TAXABLE_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxPaid()).isEqualByComparingTo(DEFAULT_TOTAL_TAX_PAID);
        assertThat(testTaxDeclaration.getSupportingDocumentsKey()).isEqualTo(UPDATED_SUPPORTING_DOCUMENTS_KEY);
    }

    @Test
    void fullUpdateTaxDeclarationWithPatch() throws Exception {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();

        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();

        // Update the taxDeclaration using partial update
        TaxDeclaration partialUpdatedTaxDeclaration = new TaxDeclaration();
        partialUpdatedTaxDeclaration.setId(taxDeclaration.getId());

        partialUpdatedTaxDeclaration
            .year(UPDATED_YEAR)
            .declarationType(UPDATED_DECLARATION_TYPE)
            .submittedDate(UPDATED_SUBMITTED_DATE)
            .status(UPDATED_STATUS)
            .totalIncome(UPDATED_TOTAL_INCOME)
            .totalTaxableIncome(UPDATED_TOTAL_TAXABLE_INCOME)
            .totalTaxPaid(UPDATED_TOTAL_TAX_PAID)
            .supportingDocumentsKey(UPDATED_SUPPORTING_DOCUMENTS_KEY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxDeclaration.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxDeclaration))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        TaxDeclaration testTaxDeclaration = taxDeclarationList.get(taxDeclarationList.size() - 1);
        assertThat(testTaxDeclaration.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testTaxDeclaration.getDeclarationType()).isEqualTo(UPDATED_DECLARATION_TYPE);
        assertThat(testTaxDeclaration.getSubmittedDate()).isEqualTo(UPDATED_SUBMITTED_DATE);
        assertThat(testTaxDeclaration.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testTaxDeclaration.getTotalIncome()).isEqualByComparingTo(UPDATED_TOTAL_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxableIncome()).isEqualByComparingTo(UPDATED_TOTAL_TAXABLE_INCOME);
        assertThat(testTaxDeclaration.getTotalTaxPaid()).isEqualByComparingTo(UPDATED_TOTAL_TAX_PAID);
        assertThat(testTaxDeclaration.getSupportingDocumentsKey()).isEqualTo(UPDATED_SUPPORTING_DOCUMENTS_KEY);
    }

    @Test
    void patchNonExistingTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, taxDeclarationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamTaxDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        taxDeclaration.setId(longCount.incrementAndGet());

        // Create the TaxDeclaration
        TaxDeclarationDTO taxDeclarationDTO = taxDeclarationMapper.toDto(taxDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxDeclarationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxDeclaration in the database
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteTaxDeclaration() {
        // Initialize the database
        taxDeclarationRepository.save(taxDeclaration).block();
        taxDeclarationRepository.save(taxDeclaration).block();
        taxDeclarationSearchRepository.save(taxDeclaration).block();

        int databaseSizeBeforeDelete = taxDeclarationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the taxDeclaration
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, taxDeclaration.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TaxDeclaration> taxDeclarationList = taxDeclarationRepository.findAll().collectList().block();
        assertThat(taxDeclarationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxDeclarationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchTaxDeclaration() {
        // Initialize the database
        taxDeclaration = taxDeclarationRepository.save(taxDeclaration).block();
        taxDeclarationSearchRepository.save(taxDeclaration).block();

        // Search the taxDeclaration
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + taxDeclaration.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(taxDeclaration.getId().intValue()))
            .jsonPath("$.[*].year")
            .value(hasItem(DEFAULT_YEAR))
            .jsonPath("$.[*].declarationType")
            .value(hasItem(DEFAULT_DECLARATION_TYPE.toString()))
            .jsonPath("$.[*].submittedDate")
            .value(hasItem(DEFAULT_SUBMITTED_DATE.toString()))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS.toString()))
            .jsonPath("$.[*].totalIncome")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_INCOME)))
            .jsonPath("$.[*].totalTaxableIncome")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_TAXABLE_INCOME)))
            .jsonPath("$.[*].totalTaxPaid")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_TAX_PAID)))
            .jsonPath("$.[*].supportingDocumentsKey")
            .value(hasItem(DEFAULT_SUPPORTING_DOCUMENTS_KEY));
    }
}
