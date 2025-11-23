package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.Currency;
import com.fanflip.admin.repository.CurrencyRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.CurrencySearchRepository;
import com.fanflip.admin.service.dto.CurrencyDTO;
import com.fanflip.admin.service.mapper.CurrencyMapper;
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
 * Integration tests for the {@link CurrencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CurrencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYMBOL = "AAAAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAA";
    private static final String UPDATED_CODE = "BBB";

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

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/currencies/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private CurrencyMapper currencyMapper;

    @Autowired
    private CurrencySearchRepository currencySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Currency currency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createEntity(EntityManager em) {
        Currency currency = new Currency()
            .name(DEFAULT_NAME)
            .symbol(DEFAULT_SYMBOL)
            .code(DEFAULT_CODE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return currency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currency createUpdatedEntity(EntityManager em) {
        Currency currency = new Currency()
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .code(UPDATED_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return currency;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Currency.class).block();
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
        currencySearchRepository.deleteAll().block();
        assertThat(currencySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        currency = createEntity(em);
    }

    @Test
    void createCurrency() throws Exception {
        int databaseSizeBeforeCreate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCurrency.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCurrency.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCurrency.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCurrency.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCurrency.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createCurrencyWithExistingId() throws Exception {
        // Create the Currency with an existing ID
        currency.setId(1L);
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        int databaseSizeBeforeCreate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // set the field null
        currency.setName(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // set the field null
        currency.setSymbol(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // set the field null
        currency.setCode(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // set the field null
        currency.setCreatedDate(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        // set the field null
        currency.setIsDeleted(null);

        // Create the Currency, which fails.
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCurrencies() {
        // Initialize the database
        currencyRepository.save(currency).block();

        // Get all the currencyList
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
            .value(hasItem(currency.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
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
    void getCurrency() {
        // Initialize the database
        currencyRepository.save(currency).block();

        // Get the currency
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, currency.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(currency.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.symbol")
            .value(is(DEFAULT_SYMBOL))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
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
    void getNonExistingCurrency() {
        // Get the currency
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCurrency() throws Exception {
        // Initialize the database
        currencyRepository.save(currency).block();

        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        currencySearchRepository.save(currency).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());

        // Update the currency
        Currency updatedCurrency = currencyRepository.findById(currency.getId()).block();
        updatedCurrency
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .code(UPDATED_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        CurrencyDTO currencyDTO = currencyMapper.toDto(updatedCurrency);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, currencyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCurrency.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCurrency.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCurrency.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCurrency.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Currency> currencySearchList = IterableUtils.toList(currencySearchRepository.findAll().collectList().block());
                Currency testCurrencySearch = currencySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCurrencySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testCurrencySearch.getSymbol()).isEqualTo(UPDATED_SYMBOL);
                assertThat(testCurrencySearch.getCode()).isEqualTo(UPDATED_CODE);
                assertThat(testCurrencySearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testCurrencySearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testCurrencySearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testCurrencySearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testCurrencySearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, currencyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.save(currency).block();

        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency.name(UPDATED_NAME).createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testCurrency.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCurrency.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCurrency.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCurrency.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCurrency.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCurrency.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateCurrencyWithPatch() throws Exception {
        // Initialize the database
        currencyRepository.save(currency).block();

        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();

        // Update the currency using partial update
        Currency partialUpdatedCurrency = new Currency();
        partialUpdatedCurrency.setId(currency.getId());

        partialUpdatedCurrency
            .name(UPDATED_NAME)
            .symbol(UPDATED_SYMBOL)
            .code(UPDATED_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCurrency.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrency))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        Currency testCurrency = currencyList.get(currencyList.size() - 1);
        assertThat(testCurrency.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCurrency.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testCurrency.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCurrency.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCurrency.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCurrency.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCurrency.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCurrency.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, currencyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCurrency() throws Exception {
        int databaseSizeBeforeUpdate = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        currency.setId(longCount.incrementAndGet());

        // Create the Currency
        CurrencyDTO currencyDTO = currencyMapper.toDto(currency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(currencyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Currency in the database
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCurrency() {
        // Initialize the database
        currencyRepository.save(currency).block();
        currencyRepository.save(currency).block();
        currencySearchRepository.save(currency).block();

        int databaseSizeBeforeDelete = currencyRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the currency
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, currency.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Currency> currencyList = currencyRepository.findAll().collectList().block();
        assertThat(currencyList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(currencySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCurrency() {
        // Initialize the database
        currency = currencyRepository.save(currency).block();
        currencySearchRepository.save(currency).block();

        // Search the currency
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + currency.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(currency.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].symbol")
            .value(hasItem(DEFAULT_SYMBOL))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
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
