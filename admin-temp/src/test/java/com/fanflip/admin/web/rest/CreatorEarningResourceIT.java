package com.monsterdam.admin.web.rest;

import static com.monsterdam.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.CreatorEarningRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.CreatorEarningSearchRepository;
import com.monsterdam.admin.service.dto.CreatorEarningDTO;
import com.monsterdam.admin.service.mapper.CreatorEarningMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CreatorEarningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CreatorEarningResourceIT {

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

    private static final String ENTITY_API_URL = "/api/creator-earnings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/creator-earnings/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreatorEarningRepository creatorEarningRepository;

    @Autowired
    private CreatorEarningMapper creatorEarningMapper;

    @Autowired
    private CreatorEarningSearchRepository creatorEarningSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CreatorEarning creatorEarning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatorEarning createEntity(EntityManager em) {
        CreatorEarning creatorEarning = new CreatorEarning()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        creatorEarning.setCreator(userProfile);
        return creatorEarning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatorEarning createUpdatedEntity(EntityManager em) {
        CreatorEarning creatorEarning = new CreatorEarning()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        creatorEarning.setCreator(userProfile);
        return creatorEarning;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CreatorEarning.class).block();
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
        creatorEarningSearchRepository.deleteAll().block();
        assertThat(creatorEarningSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        creatorEarning = createEntity(em);
    }

    @Test
    void createCreatorEarning() throws Exception {
        int databaseSizeBeforeCreate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createCreatorEarningWithExistingId() throws Exception {
        // Create the CreatorEarning with an existing ID
        creatorEarning.setId(1L);
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        int databaseSizeBeforeCreate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        // set the field null
        creatorEarning.setAmount(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        // set the field null
        creatorEarning.setCreatedDate(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        // set the field null
        creatorEarning.setIsDeleted(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCreatorEarnings() {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();

        // Get all the creatorEarningList
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
            .value(hasItem(creatorEarning.getId().intValue()))
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

    @Test
    void getCreatorEarning() {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();

        // Get the creatorEarning
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, creatorEarning.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(creatorEarning.getId().intValue()))
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
    void getNonExistingCreatorEarning() {
        // Get the creatorEarning
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCreatorEarning() throws Exception {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        creatorEarningSearchRepository.save(creatorEarning).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());

        // Update the creatorEarning
        CreatorEarning updatedCreatorEarning = creatorEarningRepository.findById(creatorEarning.getId()).block();
        updatedCreatorEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(updatedCreatorEarning);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, creatorEarningDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CreatorEarning> creatorEarningSearchList = IterableUtils.toList(
                    creatorEarningSearchRepository.findAll().collectList().block()
                );
                CreatorEarning testCreatorEarningSearch = creatorEarningSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testCreatorEarningSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testCreatorEarningSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testCreatorEarningSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testCreatorEarningSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testCreatorEarningSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testCreatorEarningSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, creatorEarningDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCreatorEarningWithPatch() throws Exception {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();

        // Update the creatorEarning using partial update
        CreatorEarning partialUpdatedCreatorEarning = new CreatorEarning();
        partialUpdatedCreatorEarning.setId(creatorEarning.getId());

        partialUpdatedCreatorEarning
            .amount(UPDATED_AMOUNT)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCreatorEarning.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCreatorEarning))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateCreatorEarningWithPatch() throws Exception {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();

        // Update the creatorEarning using partial update
        CreatorEarning partialUpdatedCreatorEarning = new CreatorEarning();
        partialUpdatedCreatorEarning.setId(creatorEarning.getId());

        partialUpdatedCreatorEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCreatorEarning.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCreatorEarning))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, creatorEarningDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCreatorEarning() {
        // Initialize the database
        creatorEarningRepository.save(creatorEarning).block();
        creatorEarningRepository.save(creatorEarning).block();
        creatorEarningSearchRepository.save(creatorEarning).block();

        int databaseSizeBeforeDelete = creatorEarningRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the creatorEarning
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, creatorEarning.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll().collectList().block();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(creatorEarningSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCreatorEarning() {
        // Initialize the database
        creatorEarning = creatorEarningRepository.save(creatorEarning).block();
        creatorEarningSearchRepository.save(creatorEarning).block();

        // Search the creatorEarning
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + creatorEarning.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(creatorEarning.getId().intValue()))
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
