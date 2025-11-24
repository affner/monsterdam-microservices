package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.ContentPackage;
import com.monsterdam.admin.domain.CreatorEarning;
import com.monsterdam.admin.domain.PurchasedContent;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.PurchasedContentRepository;
import com.monsterdam.admin.repository.search.PurchasedContentSearchRepository;
import com.monsterdam.admin.service.PurchasedContentService;
import com.monsterdam.admin.service.dto.PurchasedContentDTO;
import com.monsterdam.admin.service.mapper.PurchasedContentMapper;
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
 * Integration tests for the {@link PurchasedContentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PurchasedContentResourceIT {

    private static final Float DEFAULT_RATING = 1F;
    private static final Float UPDATED_RATING = 2F;

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

    private static final String ENTITY_API_URL = "/api/purchased-contents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/purchased-contents/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PurchasedContentRepository purchasedContentRepository;

    @Mock
    private PurchasedContentRepository purchasedContentRepositoryMock;

    @Autowired
    private PurchasedContentMapper purchasedContentMapper;

    @Mock
    private PurchasedContentService purchasedContentServiceMock;

    @Autowired
    private PurchasedContentSearchRepository purchasedContentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PurchasedContent purchasedContent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createEntity(EntityManager em) {
        PurchasedContent purchasedContent = new PurchasedContent()
            .rating(DEFAULT_RATING)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createEntity(em)).block();
        purchasedContent.setCreatorEarning(creatorEarning);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        purchasedContent.setViewer(userProfile);
        // Add required entity
        ContentPackage contentPackage;
        contentPackage = em.insert(ContentPackageResourceIT.createEntity(em)).block();
        purchasedContent.setPurchasedContentPackage(contentPackage);
        return purchasedContent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchasedContent createUpdatedEntity(EntityManager em) {
        PurchasedContent purchasedContent = new PurchasedContent()
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        CreatorEarning creatorEarning;
        creatorEarning = em.insert(CreatorEarningResourceIT.createUpdatedEntity(em)).block();
        purchasedContent.setCreatorEarning(creatorEarning);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        purchasedContent.setViewer(userProfile);
        // Add required entity
        ContentPackage contentPackage;
        contentPackage = em.insert(ContentPackageResourceIT.createUpdatedEntity(em)).block();
        purchasedContent.setPurchasedContentPackage(contentPackage);
        return purchasedContent;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PurchasedContent.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CreatorEarningResourceIT.deleteEntities(em);
        UserProfileResourceIT.deleteEntities(em);
        ContentPackageResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        purchasedContentSearchRepository.deleteAll().block();
        assertThat(purchasedContentSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        purchasedContent = createEntity(em);
    }

    @Test
    void createPurchasedContent() throws Exception {
        int databaseSizeBeforeCreate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPurchasedContentWithExistingId() throws Exception {
        // Create the PurchasedContent with an existing ID
        purchasedContent.setId(1L);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        int databaseSizeBeforeCreate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedContent.setCreatedDate(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        // set the field null
        purchasedContent.setIsDeleted(null);

        // Create the PurchasedContent, which fails.
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPurchasedContents() {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();

        // Get all the purchasedContentList
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
            .value(hasItem(purchasedContent.getId().intValue()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING.doubleValue()))
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
    void getAllPurchasedContentsWithEagerRelationshipsIsEnabled() {
        when(purchasedContentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(purchasedContentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPurchasedContentsWithEagerRelationshipsIsNotEnabled() {
        when(purchasedContentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(purchasedContentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPurchasedContent() {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();

        // Get the purchasedContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, purchasedContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(purchasedContent.getId().intValue()))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING.doubleValue()))
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
    void getNonExistingPurchasedContent() {
        // Get the purchasedContent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPurchasedContent() throws Exception {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        purchasedContentSearchRepository.save(purchasedContent).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());

        // Update the purchasedContent
        PurchasedContent updatedPurchasedContent = purchasedContentRepository.findById(purchasedContent.getId()).block();
        updatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(updatedPurchasedContent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PurchasedContent> purchasedContentSearchList = IterableUtils.toList(
                    purchasedContentSearchRepository.findAll().collectList().block()
                );
                PurchasedContent testPurchasedContentSearch = purchasedContentSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPurchasedContentSearch.getRating()).isEqualTo(UPDATED_RATING);
                assertThat(testPurchasedContentSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPurchasedContentSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPurchasedContentSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPurchasedContentSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPurchasedContentSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, purchasedContentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent.createdDate(UPDATED_CREATED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdatePurchasedContentWithPatch() throws Exception {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();

        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();

        // Update the purchasedContent using partial update
        PurchasedContent partialUpdatedPurchasedContent = new PurchasedContent();
        partialUpdatedPurchasedContent.setId(purchasedContent.getId());

        partialUpdatedPurchasedContent
            .rating(UPDATED_RATING)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPurchasedContent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPurchasedContent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        PurchasedContent testPurchasedContent = purchasedContentList.get(purchasedContentList.size() - 1);
        assertThat(testPurchasedContent.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testPurchasedContent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPurchasedContent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPurchasedContent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPurchasedContent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPurchasedContent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, purchasedContentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPurchasedContent() throws Exception {
        int databaseSizeBeforeUpdate = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        purchasedContent.setId(longCount.incrementAndGet());

        // Create the PurchasedContent
        PurchasedContentDTO purchasedContentDTO = purchasedContentMapper.toDto(purchasedContent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(purchasedContentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PurchasedContent in the database
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePurchasedContent() {
        // Initialize the database
        purchasedContentRepository.save(purchasedContent).block();
        purchasedContentRepository.save(purchasedContent).block();
        purchasedContentSearchRepository.save(purchasedContent).block();

        int databaseSizeBeforeDelete = purchasedContentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the purchasedContent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, purchasedContent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PurchasedContent> purchasedContentList = purchasedContentRepository.findAll().collectList().block();
        assertThat(purchasedContentList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(purchasedContentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPurchasedContent() {
        // Initialize the database
        purchasedContent = purchasedContentRepository.save(purchasedContent).block();
        purchasedContentSearchRepository.save(purchasedContent).block();

        // Search the purchasedContent
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + purchasedContent.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(purchasedContent.getId().intValue()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING.doubleValue()))
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
