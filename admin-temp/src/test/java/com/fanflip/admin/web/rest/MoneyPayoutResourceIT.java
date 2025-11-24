package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.MoneyPayout;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.enumeration.PayoutStatus;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.MoneyPayoutRepository;
import com.monsterdam.admin.repository.search.MoneyPayoutSearchRepository;
import com.monsterdam.admin.service.MoneyPayoutService;
import com.monsterdam.admin.service.dto.MoneyPayoutDTO;
import com.monsterdam.admin.service.mapper.MoneyPayoutMapper;
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
 * Integration tests for the {@link MoneyPayoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MoneyPayoutResourceIT {

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

    private static final PayoutStatus DEFAULT_WITHDRAW_STATUS = PayoutStatus.PENDING;
    private static final PayoutStatus UPDATED_WITHDRAW_STATUS = PayoutStatus.PROCESSED;

    private static final String ENTITY_API_URL = "/api/money-payouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/money-payouts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MoneyPayoutRepository moneyPayoutRepository;

    @Mock
    private MoneyPayoutRepository moneyPayoutRepositoryMock;

    @Autowired
    private MoneyPayoutMapper moneyPayoutMapper;

    @Mock
    private MoneyPayoutService moneyPayoutServiceMock;

    @Autowired
    private MoneyPayoutSearchRepository moneyPayoutSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MoneyPayout moneyPayout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyPayout createEntity(EntityManager em) {
        MoneyPayout moneyPayout = new MoneyPayout()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .withdrawStatus(DEFAULT_WITHDRAW_STATUS);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createEntity(em)).block();
        moneyPayout.setCreatorEarning(creatorEarning);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        moneyPayout.setCreator(userProfile);
        return moneyPayout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoneyPayout createUpdatedEntity(EntityManager em) {
        MoneyPayout moneyPayout = new MoneyPayout()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createUpdatedEntity(em)).block();
        moneyPayout.setCreatorEarning(creatorEarning);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        moneyPayout.setCreator(userProfile);
        return moneyPayout;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MoneyPayout.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CreatorEarningResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        moneyPayoutSearchRepository.deleteAll().block();
        assertThat(moneyPayoutSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        moneyPayout = createEntity(em);
    }

    @Test
    void createMoneyPayout() throws Exception {
        int databaseSizeBeforeCreate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(DEFAULT_WITHDRAW_STATUS);
    }

    @Test
    void createMoneyPayoutWithExistingId() throws Exception {
        // Create the MoneyPayout with an existing ID
        moneyPayout.setId(1L);
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        int databaseSizeBeforeCreate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        // set the field null
        moneyPayout.setAmount(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        // set the field null
        moneyPayout.setCreatedDate(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        // set the field null
        moneyPayout.setIsDeleted(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkWithdrawStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        // set the field null
        moneyPayout.setWithdrawStatus(null);

        // Create the MoneyPayout, which fails.
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllMoneyPayouts() {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();

        // Get all the moneyPayoutList
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
            .value(hasItem(moneyPayout.getId().intValue()))
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
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.[*].withdrawStatus")
            .value(hasItem(DEFAULT_WITHDRAW_STATUS.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyPayoutsWithEagerRelationshipsIsEnabled() {
        when(moneyPayoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(moneyPayoutServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoneyPayoutsWithEagerRelationshipsIsNotEnabled() {
        when(moneyPayoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(moneyPayoutRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMoneyPayout() {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();

        // Get the moneyPayout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, moneyPayout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(moneyPayout.getId().intValue()))
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
            .value(is(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.withdrawStatus")
            .value(is(DEFAULT_WITHDRAW_STATUS.toString()));
    }

    @Test
    void getNonExistingMoneyPayout() {
        // Get the moneyPayout
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMoneyPayout() throws Exception {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        moneyPayoutSearchRepository.save(moneyPayout).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());

        // Update the moneyPayout
        MoneyPayout updatedMoneyPayout = moneyPayoutRepository.findById(moneyPayout.getId()).block();
        updatedMoneyPayout
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(updatedMoneyPayout);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MoneyPayout> moneyPayoutSearchList = IterableUtils.toList(moneyPayoutSearchRepository.findAll().collectList().block());
                MoneyPayout testMoneyPayoutSearch = moneyPayoutSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testMoneyPayoutSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testMoneyPayoutSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testMoneyPayoutSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testMoneyPayoutSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testMoneyPayoutSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testMoneyPayoutSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testMoneyPayoutSearch.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
            });
    }

    @Test
    void putNonExistingMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateMoneyPayoutWithPatch() throws Exception {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();

        // Update the moneyPayout using partial update
        MoneyPayout partialUpdatedMoneyPayout = new MoneyPayout();
        partialUpdatedMoneyPayout.setId(moneyPayout.getId());

        partialUpdatedMoneyPayout
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMoneyPayout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyPayout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
    }

    @Test
    void fullUpdateMoneyPayoutWithPatch() throws Exception {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();

        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();

        // Update the moneyPayout using partial update
        MoneyPayout partialUpdatedMoneyPayout = new MoneyPayout();
        partialUpdatedMoneyPayout.setId(moneyPayout.getId());

        partialUpdatedMoneyPayout
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .withdrawStatus(UPDATED_WITHDRAW_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMoneyPayout.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMoneyPayout))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        MoneyPayout testMoneyPayout = moneyPayoutList.get(moneyPayoutList.size() - 1);
        assertThat(testMoneyPayout.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testMoneyPayout.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testMoneyPayout.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testMoneyPayout.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testMoneyPayout.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testMoneyPayout.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testMoneyPayout.getWithdrawStatus()).isEqualTo(UPDATED_WITHDRAW_STATUS);
    }

    @Test
    void patchNonExistingMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, moneyPayoutDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamMoneyPayout() throws Exception {
        int databaseSizeBeforeUpdate = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        moneyPayout.setId(longCount.incrementAndGet());

        // Create the MoneyPayout
        MoneyPayoutDTO moneyPayoutDTO = moneyPayoutMapper.toDto(moneyPayout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(moneyPayoutDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MoneyPayout in the database
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteMoneyPayout() {
        // Initialize the database
        moneyPayoutRepository.save(moneyPayout).block();
        moneyPayoutRepository.save(moneyPayout).block();
        moneyPayoutSearchRepository.save(moneyPayout).block();

        int databaseSizeBeforeDelete = moneyPayoutRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the moneyPayout
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, moneyPayout.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MoneyPayout> moneyPayoutList = moneyPayoutRepository.findAll().collectList().block();
        assertThat(moneyPayoutList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(moneyPayoutSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchMoneyPayout() {
        // Initialize the database
        moneyPayout = moneyPayoutRepository.save(moneyPayout).block();
        moneyPayoutSearchRepository.save(moneyPayout).block();

        // Search the moneyPayout
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + moneyPayout.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(moneyPayout.getId().intValue()))
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
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()))
            .jsonPath("$.[*].withdrawStatus")
            .value(hasItem(DEFAULT_WITHDRAW_STATUS.toString()));
    }
}
