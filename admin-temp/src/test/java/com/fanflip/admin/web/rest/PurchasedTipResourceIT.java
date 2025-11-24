package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.DirectMessage;
import com.monsterdam.admin.domain.PurchasedTip;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PurchasedTipRepository;
import com.monsterdam.admin.repository.search.PurchasedTipSearchRepository;
import com.monsterdam.admin.service.PurchasedTipService;
import com.monsterdam.admin.service.dto.PurchasedTipDTO;
import com.monsterdam.admin.service.mapper.PurchasedTipMapper;
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
 * Integration tests for the {@link PurchasedTipResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PurchasedTipResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

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

    private static final String ENTITY_API_URL = "/api/purchased-tips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/purchased-tips/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedTipRepository purchasedTipRepository;

    @Mock
    private PurchasedTipRepository purchasedTipRepositoryMock;

    @Autowired
    private PurchasedTipMapper purchasedTipMapper;

    @Mock
    private PurchasedTipService purchasedTipServiceMock;

    @Autowired
    private PurchasedTipSearchRepository purchasedTipSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PurchasedTip purchasedTip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedTip createEntity(EntityManager em) {
        PurchasedTip purchasedTip = new PurchasedTip()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createEntity(em)).block();
        purchasedTip.setCreatorEarning(creatorEarning);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createEntity(em)).block();
        purchasedTip.setMessage(directMessage);
        return purchasedTip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedTip createUpdatedEntity(EntityManager em) {
        PurchasedTip purchasedTip = new PurchasedTip()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createUpdatedEntity(em)).block();
        purchasedTip.setCreatorEarning(creatorEarning);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createUpdatedEntity(em)).block();
        purchasedTip.setMessage(directMessage);
        return purchasedTip;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PurchasedTip.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CreatorEarningResourceIT.deleteEntities(em);
        DirectMessageResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        purchasedTipSearchRepository.deleteAll().block();
        assertThat(purchasedTipSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        purchasedTip = createEntity(em);
    }

    @Test
    void createPurchasedTip() throws Exception {
        int databaseSizeBeforeCreate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPurchasedTipWithExistingId() throws Exception {
        // Create the PurchasedTip with an existing ID
        purchasedTip.setId(1L);
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        int databaseSizeBeforeCreate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedTip.setAmount(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedTip.setCreatedDate(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedTip.setIsDeleted(null);

        // Create the PurchasedTip, which fails.
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPurchasedTips() {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();

        // Get all the purchasedTipList
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
            .value(hasItem(purchasedTip.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
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
    void getAllPurchasedTipsWithEagerRelationshipsIsEnabled() {
        when(purchasedTipServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(purchasedTipServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedTipsWithEagerRelationshipsIsNotEnabled() {
        when(purchasedTipServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(purchasedTipRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPurchasedTip() {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();

        // Get the purchasedTip
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, purchasedTip.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(purchasedTip.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
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
    void getNonExistingPurchasedTip() {
        // Get the purchasedTip
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPurchasedTip() throws Exception {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        purchasedTipSearchRepository.save(purchasedTip).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());

        // Update the purchasedTip
        PurchasedTip updatedPurchasedTip = purchasedTipRepository.findById(purchasedTip.getId()).block();
        updatedPurchasedTip
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(updatedPurchasedTip);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedTipDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PurchasedTip> purchasedTipSearchList = IterableUtils.toList(
                    purchasedTipSearchRepository.findAll().collectList().block()
                );
                PurchasedTip testPurchasedTipSearch = purchasedTipSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPurchasedTipSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testPurchasedTipSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPurchasedTipSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPurchasedTipSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPurchasedTipSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPurchasedTipSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedTipDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePurchasedTipWithPatch() throws Exception {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();

        // Update the purchasedTip using partial update
        PurchasedTip partialUpdatedPurchasedTip = new PurchasedTip();
        partialUpdatedPurchasedTip.setId(purchasedTip.getId());

        partialUpdatedPurchasedTip
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedTip.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedTip))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdatePurchasedTipWithPatch() throws Exception {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();

        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();

        // Update the purchasedTip using partial update
        PurchasedTip partialUpdatedPurchasedTip = new PurchasedTip();
        partialUpdatedPurchasedTip.setId(purchasedTip.getId());

        partialUpdatedPurchasedTip
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedTip.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedTip))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        PurchasedTip testPurchasedTip = purchasedTipList.get(purchasedTipList.size() - 1);
        assertThat(testPurchasedTip.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPurchasedTip.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedTip.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedTip.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedTip.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedTip.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, purchasedTipDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPurchasedTip() throws Exception {
        int databaseSizeBeforeUpdate = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        purchasedTip.setId(longCount.incrementAndGet());

        // Create the PurchasedTip
        PurchasedTipDTO purchasedTipDTO = purchasedTipMapper.toDto(purchasedTip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedTipDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedTip in the database
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePurchasedTip() {
        // Initialize the database
        purchasedTipRepository.save(purchasedTip).block();
        purchasedTipRepository.save(purchasedTip).block();
        purchasedTipSearchRepository.save(purchasedTip).block();

        int databaseSizeBeforeDelete = purchasedTipRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the purchasedTip
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, purchasedTip.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PurchasedTip> purchasedTipList = purchasedTipRepository.findAll().collectList().block();
        assertThat(purchasedTipList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedTipSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPurchasedTip() {
        // Initialize the database
        purchasedTip = purchasedTipRepository.save(purchasedTip).block();
        purchasedTipSearchRepository.save(purchasedTip).block();

        // Search the purchasedTip
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + purchasedTip.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(purchasedTip.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
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
