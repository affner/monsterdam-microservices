package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.SpecialReward;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.SpecialRewardRepository;
import com.fanflip.admin.repository.search.SpecialRewardSearchRepository;
import com.fanflip.admin.service.dto.SpecialRewardDTO;
import com.fanflip.admin.service.mapper.SpecialRewardMapper;
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
 * Integration tests for the {@link SpecialRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SpecialRewardResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

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

    private static final Long DEFAULT_CONTENT_PACKAGE_ID = 1L;
    private static final Long UPDATED_CONTENT_PACKAGE_ID = 2L;

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_OFFER_PROMOTION_ID = 1L;
    private static final Long UPDATED_OFFER_PROMOTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/special-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/special-rewards/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialRewardRepository specialRewardRepository;

    @Autowired
    private SpecialRewardMapper specialRewardMapper;

    @Autowired
    private SpecialRewardSearchRepository specialRewardSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SpecialReward specialReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialReward createEntity(EntityManager em) {
        SpecialReward specialReward = new SpecialReward()
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .contentPackageId(DEFAULT_CONTENT_PACKAGE_ID)
            .viewerId(DEFAULT_VIEWER_ID)
            .offerPromotionId(DEFAULT_OFFER_PROMOTION_ID);
        return specialReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialReward createUpdatedEntity(EntityManager em) {
        SpecialReward specialReward = new SpecialReward()
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);
        return specialReward;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SpecialReward.class).block();
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
        specialRewardSearchRepository.deleteAll().block();
        assertThat(specialRewardSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        specialReward = createEntity(em);
    }

    @Test
    void createSpecialReward() throws Exception {
        int databaseSizeBeforeCreate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSpecialReward.getContentPackageId()).isEqualTo(DEFAULT_CONTENT_PACKAGE_ID);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(DEFAULT_OFFER_PROMOTION_ID);
    }

    @Test
    void createSpecialRewardWithExistingId() throws Exception {
        // Create the SpecialReward with an existing ID
        specialReward.setId(1L);
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        int databaseSizeBeforeCreate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setDescription(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setCreatedDate(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setIsDeleted(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentPackageIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setContentPackageId(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setViewerId(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkOfferPromotionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        // set the field null
        specialReward.setOfferPromotionId(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSpecialRewards() {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();

        // Get all the specialRewardList
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
            .value(hasItem(specialReward.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
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
            .jsonPath("$.[*].contentPackageId")
            .value(hasItem(DEFAULT_CONTENT_PACKAGE_ID.intValue()))
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].offerPromotionId")
            .value(hasItem(DEFAULT_OFFER_PROMOTION_ID.intValue()));
    }

    @Test
    void getSpecialReward() {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();

        // Get the specialReward
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, specialReward.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(specialReward.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
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
            .jsonPath("$.contentPackageId")
            .value(is(DEFAULT_CONTENT_PACKAGE_ID.intValue()))
            .jsonPath("$.viewerId")
            .value(is(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.offerPromotionId")
            .value(is(DEFAULT_OFFER_PROMOTION_ID.intValue()));
    }

    @Test
    void getNonExistingSpecialReward() {
        // Get the specialReward
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSpecialReward() throws Exception {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        specialRewardSearchRepository.save(specialReward).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());

        // Update the specialReward
        SpecialReward updatedSpecialReward = specialRewardRepository.findById(specialReward.getId()).block();
        updatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(updatedSpecialReward);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialRewardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SpecialReward> specialRewardSearchList = IterableUtils.toList(
                    specialRewardSearchRepository.findAll().collectList().block()
                );
                SpecialReward testSpecialRewardSearch = specialRewardSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSpecialRewardSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSpecialRewardSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSpecialRewardSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSpecialRewardSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSpecialRewardSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSpecialRewardSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
                assertThat(testSpecialRewardSearch.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
                assertThat(testSpecialRewardSearch.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
                assertThat(testSpecialRewardSearch.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
            });
    }

    @Test
    void putNonExistingSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialRewardDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSpecialRewardWithPatch() throws Exception {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();

        // Update the specialReward using partial update
        SpecialReward partialUpdatedSpecialReward = new SpecialReward();
        partialUpdatedSpecialReward.setId(specialReward.getId());

        partialUpdatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isDeleted(UPDATED_IS_DELETED)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialReward.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialReward))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
    }

    @Test
    void fullUpdateSpecialRewardWithPatch() throws Exception {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();

        // Update the specialReward using partial update
        SpecialReward partialUpdatedSpecialReward = new SpecialReward();
        partialUpdatedSpecialReward.setId(specialReward.getId());

        partialUpdatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .contentPackageId(UPDATED_CONTENT_PACKAGE_ID)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialReward.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialReward))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getContentPackageId()).isEqualTo(UPDATED_CONTENT_PACKAGE_ID);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
    }

    @Test
    void patchNonExistingSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, specialRewardDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSpecialReward() {
        // Initialize the database
        specialRewardRepository.save(specialReward).block();
        specialRewardRepository.save(specialReward).block();
        specialRewardSearchRepository.save(specialReward).block();

        int databaseSizeBeforeDelete = specialRewardRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the specialReward
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, specialReward.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll().collectList().block();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialRewardSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSpecialReward() {
        // Initialize the database
        specialReward = specialRewardRepository.save(specialReward).block();
        specialRewardSearchRepository.save(specialReward).block();

        // Search the specialReward
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + specialReward.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(specialReward.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
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
            .jsonPath("$.[*].contentPackageId")
            .value(hasItem(DEFAULT_CONTENT_PACKAGE_ID.intValue()))
            .jsonPath("$.[*].viewerId")
            .value(hasItem(DEFAULT_VIEWER_ID.intValue()))
            .jsonPath("$.[*].offerPromotionId")
            .value(hasItem(DEFAULT_OFFER_PROMOTION_ID.intValue()));
    }
}
