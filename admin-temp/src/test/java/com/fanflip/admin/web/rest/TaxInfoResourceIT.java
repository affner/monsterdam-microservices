package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.domain.TaxInfo;
import com.monsterdam.admin.domain.enumeration.TaxType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.TaxInfoRepository;
import com.monsterdam.admin.repository.search.TaxInfoSearchRepository;
import com.monsterdam.admin.service.TaxInfoService;
import com.monsterdam.admin.service.dto.TaxInfoDTO;
import com.monsterdam.admin.service.mapper.TaxInfoMapper;
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
 * Integration tests for the {@link TaxInfoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class TaxInfoResourceIT {

    private static final Float DEFAULT_RATE_PERCENTAGE = 1F;
    private static final Float UPDATED_RATE_PERCENTAGE = 2F;

    private static final TaxType DEFAULT_TAX_TYPE = TaxType.VAT;
    private static final TaxType UPDATED_TAX_TYPE = TaxType.WITHHOLDING;

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

    private static final String ENTITY_API_URL = "/api/tax-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tax-infos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TaxInfoRepository taxInfoRepository;

    @Mock
    private TaxInfoRepository taxInfoRepositoryMock;

    @Autowired
    private TaxInfoMapper taxInfoMapper;

    @Mock
    private TaxInfoService taxInfoServiceMock;

    @Autowired
    private TaxInfoSearchRepository taxInfoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private TaxInfo taxInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInfo createEntity(EntityManager em) {
        TaxInfo taxInfo = new TaxInfo()
            .ratePercentage(DEFAULT_RATE_PERCENTAGE)
            .taxType(DEFAULT_TAX_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        Country country;
        country = em.insert(CountryResourceIT.createEntity(em)).block();
        taxInfo.setCountry(country);
        return taxInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaxInfo createUpdatedEntity(EntityManager em) {
        TaxInfo taxInfo = new TaxInfo()
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        Country country;
        country = em.insert(CountryResourceIT.createUpdatedEntity(em)).block();
        taxInfo.setCountry(country);
        return taxInfo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(TaxInfo.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CountryResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        taxInfoSearchRepository.deleteAll().block();
        assertThat(taxInfoSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        taxInfo = createEntity(em);
    }

    @Test
    void createTaxInfo() throws Exception {
        int databaseSizeBeforeCreate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(DEFAULT_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createTaxInfoWithExistingId() throws Exception {
        // Create the TaxInfo with an existing ID
        taxInfo.setId(1L);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        int databaseSizeBeforeCreate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTaxTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        // set the field null
        taxInfo.setTaxType(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        // set the field null
        taxInfo.setCreatedDate(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        // set the field null
        taxInfo.setIsDeleted(null);

        // Create the TaxInfo, which fails.
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllTaxInfos() {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();

        // Get all the taxInfoList
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
            .value(hasItem(taxInfo.getId().intValue()))
            .jsonPath("$.[*].ratePercentage")
            .value(hasItem(DEFAULT_RATE_PERCENTAGE.doubleValue()))
            .jsonPath("$.[*].taxType")
            .value(hasItem(DEFAULT_TAX_TYPE.toString()))
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

    @SuppressWarnings({ "unchecked" })
    void getAllTaxInfosWithEagerRelationshipsIsEnabled() {
        when(taxInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(taxInfoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTaxInfosWithEagerRelationshipsIsNotEnabled() {
        when(taxInfoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(taxInfoRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getTaxInfo() {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();

        // Get the taxInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, taxInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(taxInfo.getId().intValue()))
            .jsonPath("$.ratePercentage")
            .value(is(DEFAULT_RATE_PERCENTAGE.doubleValue()))
            .jsonPath("$.taxType")
            .value(is(DEFAULT_TAX_TYPE.toString()))
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
    void getNonExistingTaxInfo() {
        // Get the taxInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingTaxInfo() throws Exception {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        taxInfoSearchRepository.save(taxInfo).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());

        // Update the taxInfo
        TaxInfo updatedTaxInfo = taxInfoRepository.findById(taxInfo.getId()).block();
        updatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(updatedTaxInfo);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxInfoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TaxInfo> taxInfoSearchList = IterableUtils.toList(taxInfoSearchRepository.findAll().collectList().block());
                TaxInfo testTaxInfoSearch = taxInfoSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testTaxInfoSearch.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
                assertThat(testTaxInfoSearch.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
                assertThat(testTaxInfoSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testTaxInfoSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testTaxInfoSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testTaxInfoSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testTaxInfoSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, taxInfoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateTaxInfoWithPatch() throws Exception {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();

        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();

        // Update the taxInfo using partial update
        TaxInfo partialUpdatedTaxInfo = new TaxInfo();
        partialUpdatedTaxInfo.setId(taxInfo.getId());

        partialUpdatedTaxInfo
            .ratePercentage(UPDATED_RATE_PERCENTAGE)
            .taxType(UPDATED_TAX_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedTaxInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedTaxInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        TaxInfo testTaxInfo = taxInfoList.get(taxInfoList.size() - 1);
        assertThat(testTaxInfo.getRatePercentage()).isEqualTo(UPDATED_RATE_PERCENTAGE);
        assertThat(testTaxInfo.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testTaxInfo.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTaxInfo.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testTaxInfo.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTaxInfo.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTaxInfo.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, taxInfoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamTaxInfo() throws Exception {
        int databaseSizeBeforeUpdate = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        taxInfo.setId(longCount.incrementAndGet());

        // Create the TaxInfo
        TaxInfoDTO taxInfoDTO = taxInfoMapper.toDto(taxInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(taxInfoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the TaxInfo in the database
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteTaxInfo() {
        // Initialize the database
        taxInfoRepository.save(taxInfo).block();
        taxInfoRepository.save(taxInfo).block();
        taxInfoSearchRepository.save(taxInfo).block();

        int databaseSizeBeforeDelete = taxInfoRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the taxInfo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, taxInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<TaxInfo> taxInfoList = taxInfoRepository.findAll().collectList().block();
        assertThat(taxInfoList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(taxInfoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchTaxInfo() {
        // Initialize the database
        taxInfo = taxInfoRepository.save(taxInfo).block();
        taxInfoSearchRepository.save(taxInfo).block();

        // Search the taxInfo
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + taxInfo.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(taxInfo.getId().intValue()))
            .jsonPath("$.[*].ratePercentage")
            .value(hasItem(DEFAULT_RATE_PERCENTAGE.doubleValue()))
            .jsonPath("$.[*].taxType")
            .value(hasItem(DEFAULT_TAX_TYPE.toString()))
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
