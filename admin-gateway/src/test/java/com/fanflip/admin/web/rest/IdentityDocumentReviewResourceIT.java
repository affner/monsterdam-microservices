package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.AssistanceTicket;
import com.fanflip.admin.domain.IdentityDocumentReview;
import com.fanflip.admin.domain.enumeration.DocumentStatus;
import com.fanflip.admin.domain.enumeration.ReviewStatus;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.IdentityDocumentReviewRepository;
import com.fanflip.admin.repository.search.IdentityDocumentReviewSearchRepository;
import com.fanflip.admin.service.dto.IdentityDocumentReviewDTO;
import com.fanflip.admin.service.mapper.IdentityDocumentReviewMapper;
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
 * Integration tests for the {@link IdentityDocumentReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IdentityDocumentReviewResourceIT {

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.PENDING;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.APPROVED;

    private static final Instant DEFAULT_RESOLUTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLUTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ReviewStatus DEFAULT_REVIEW_STATUS = ReviewStatus.REVIEWING;
    private static final ReviewStatus UPDATED_REVIEW_STATUS = ReviewStatus.APPROVED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/identity-document-reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/identity-document-reviews/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IdentityDocumentReviewRepository identityDocumentReviewRepository;

    @Autowired
    private IdentityDocumentReviewMapper identityDocumentReviewMapper;

    @Autowired
    private IdentityDocumentReviewSearchRepository identityDocumentReviewSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IdentityDocumentReview identityDocumentReview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocumentReview createEntity(EntityManager em) {
        IdentityDocumentReview identityDocumentReview = new IdentityDocumentReview()
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .resolutionDate(DEFAULT_RESOLUTION_DATE)
            .reviewStatus(DEFAULT_REVIEW_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createEntity(em)).block();
        identityDocumentReview.setTicket(assistanceTicket);
        return identityDocumentReview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocumentReview createUpdatedEntity(EntityManager em) {
        IdentityDocumentReview identityDocumentReview = new IdentityDocumentReview()
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        // Add required entity
        AssistanceTicket assistanceTicket;
        assistanceTicket = em.insert(AssistanceTicketResourceIT.createUpdatedEntity(em)).block();
        identityDocumentReview.setTicket(assistanceTicket);
        return identityDocumentReview;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IdentityDocumentReview.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        AssistanceTicketResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        identityDocumentReviewSearchRepository.deleteAll().block();
        assertThat(identityDocumentReviewSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        identityDocumentReview = createEntity(em);
    }

    @Test
    void createIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeCreate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        IdentityDocumentReview testIdentityDocumentReview = identityDocumentReviewList.get(identityDocumentReviewList.size() - 1);
        assertThat(testIdentityDocumentReview.getDocumentStatus()).isEqualTo(DEFAULT_DOCUMENT_STATUS);
        assertThat(testIdentityDocumentReview.getResolutionDate()).isEqualTo(DEFAULT_RESOLUTION_DATE);
        assertThat(testIdentityDocumentReview.getReviewStatus()).isEqualTo(DEFAULT_REVIEW_STATUS);
        assertThat(testIdentityDocumentReview.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIdentityDocumentReview.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocumentReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIdentityDocumentReview.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void createIdentityDocumentReviewWithExistingId() throws Exception {
        // Create the IdentityDocumentReview with an existing ID
        identityDocumentReview.setId(1L);
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        int databaseSizeBeforeCreate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        // set the field null
        identityDocumentReview.setCreatedDate(null);

        // Create the IdentityDocumentReview, which fails.
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllIdentityDocumentReviewsAsStream() {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        List<IdentityDocumentReview> identityDocumentReviewList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(IdentityDocumentReviewDTO.class)
            .getResponseBody()
            .map(identityDocumentReviewMapper::toEntity)
            .filter(identityDocumentReview::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(identityDocumentReviewList).isNotNull();
        assertThat(identityDocumentReviewList).hasSize(1);
        IdentityDocumentReview testIdentityDocumentReview = identityDocumentReviewList.get(0);
        assertThat(testIdentityDocumentReview.getDocumentStatus()).isEqualTo(DEFAULT_DOCUMENT_STATUS);
        assertThat(testIdentityDocumentReview.getResolutionDate()).isEqualTo(DEFAULT_RESOLUTION_DATE);
        assertThat(testIdentityDocumentReview.getReviewStatus()).isEqualTo(DEFAULT_REVIEW_STATUS);
        assertThat(testIdentityDocumentReview.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIdentityDocumentReview.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocumentReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIdentityDocumentReview.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void getAllIdentityDocumentReviews() {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        // Get all the identityDocumentReviewList
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
            .value(hasItem(identityDocumentReview.getId().intValue()))
            .jsonPath("$.[*].documentStatus")
            .value(hasItem(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.[*].resolutionDate")
            .value(hasItem(DEFAULT_RESOLUTION_DATE.toString()))
            .jsonPath("$.[*].reviewStatus")
            .value(hasItem(DEFAULT_REVIEW_STATUS.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getIdentityDocumentReview() {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        // Get the identityDocumentReview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, identityDocumentReview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(identityDocumentReview.getId().intValue()))
            .jsonPath("$.documentStatus")
            .value(is(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.resolutionDate")
            .value(is(DEFAULT_RESOLUTION_DATE.toString()))
            .jsonPath("$.reviewStatus")
            .value(is(DEFAULT_REVIEW_STATUS.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY));
    }

    @Test
    void getNonExistingIdentityDocumentReview() {
        // Get the identityDocumentReview
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIdentityDocumentReview() throws Exception {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        identityDocumentReviewSearchRepository.save(identityDocumentReview).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());

        // Update the identityDocumentReview
        IdentityDocumentReview updatedIdentityDocumentReview = identityDocumentReviewRepository
            .findById(identityDocumentReview.getId())
            .block();
        updatedIdentityDocumentReview
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(updatedIdentityDocumentReview);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocumentReview testIdentityDocumentReview = identityDocumentReviewList.get(identityDocumentReviewList.size() - 1);
        assertThat(testIdentityDocumentReview.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
        assertThat(testIdentityDocumentReview.getResolutionDate()).isEqualTo(UPDATED_RESOLUTION_DATE);
        assertThat(testIdentityDocumentReview.getReviewStatus()).isEqualTo(UPDATED_REVIEW_STATUS);
        assertThat(testIdentityDocumentReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocumentReview.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocumentReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIdentityDocumentReview.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<IdentityDocumentReview> identityDocumentReviewSearchList = IterableUtils.toList(
                    identityDocumentReviewSearchRepository.findAll().collectList().block()
                );
                IdentityDocumentReview testIdentityDocumentReviewSearch = identityDocumentReviewSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testIdentityDocumentReviewSearch.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
                assertThat(testIdentityDocumentReviewSearch.getResolutionDate()).isEqualTo(UPDATED_RESOLUTION_DATE);
                assertThat(testIdentityDocumentReviewSearch.getReviewStatus()).isEqualTo(UPDATED_REVIEW_STATUS);
                assertThat(testIdentityDocumentReviewSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testIdentityDocumentReviewSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testIdentityDocumentReviewSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testIdentityDocumentReviewSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
            });
    }

    @Test
    void putNonExistingIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateIdentityDocumentReviewWithPatch() throws Exception {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();

        // Update the identityDocumentReview using partial update
        IdentityDocumentReview partialUpdatedIdentityDocumentReview = new IdentityDocumentReview();
        partialUpdatedIdentityDocumentReview.setId(identityDocumentReview.getId());

        partialUpdatedIdentityDocumentReview
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentityDocumentReview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentityDocumentReview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocumentReview testIdentityDocumentReview = identityDocumentReviewList.get(identityDocumentReviewList.size() - 1);
        assertThat(testIdentityDocumentReview.getDocumentStatus()).isEqualTo(DEFAULT_DOCUMENT_STATUS);
        assertThat(testIdentityDocumentReview.getResolutionDate()).isEqualTo(UPDATED_RESOLUTION_DATE);
        assertThat(testIdentityDocumentReview.getReviewStatus()).isEqualTo(DEFAULT_REVIEW_STATUS);
        assertThat(testIdentityDocumentReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocumentReview.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocumentReview.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIdentityDocumentReview.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void fullUpdateIdentityDocumentReviewWithPatch() throws Exception {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();

        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();

        // Update the identityDocumentReview using partial update
        IdentityDocumentReview partialUpdatedIdentityDocumentReview = new IdentityDocumentReview();
        partialUpdatedIdentityDocumentReview.setId(identityDocumentReview.getId());

        partialUpdatedIdentityDocumentReview
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .resolutionDate(UPDATED_RESOLUTION_DATE)
            .reviewStatus(UPDATED_REVIEW_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentityDocumentReview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentityDocumentReview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocumentReview testIdentityDocumentReview = identityDocumentReviewList.get(identityDocumentReviewList.size() - 1);
        assertThat(testIdentityDocumentReview.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
        assertThat(testIdentityDocumentReview.getResolutionDate()).isEqualTo(UPDATED_RESOLUTION_DATE);
        assertThat(testIdentityDocumentReview.getReviewStatus()).isEqualTo(UPDATED_REVIEW_STATUS);
        assertThat(testIdentityDocumentReview.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocumentReview.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocumentReview.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIdentityDocumentReview.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void patchNonExistingIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, identityDocumentReviewDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamIdentityDocumentReview() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        identityDocumentReview.setId(longCount.incrementAndGet());

        // Create the IdentityDocumentReview
        IdentityDocumentReviewDTO identityDocumentReviewDTO = identityDocumentReviewMapper.toDto(identityDocumentReview);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentReviewDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentityDocumentReview in the database
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteIdentityDocumentReview() {
        // Initialize the database
        identityDocumentReviewRepository.save(identityDocumentReview).block();
        identityDocumentReviewRepository.save(identityDocumentReview).block();
        identityDocumentReviewSearchRepository.save(identityDocumentReview).block();

        int databaseSizeBeforeDelete = identityDocumentReviewRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the identityDocumentReview
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, identityDocumentReview.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IdentityDocumentReview> identityDocumentReviewList = identityDocumentReviewRepository.findAll().collectList().block();
        assertThat(identityDocumentReviewList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentReviewSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchIdentityDocumentReview() {
        // Initialize the database
        identityDocumentReview = identityDocumentReviewRepository.save(identityDocumentReview).block();
        identityDocumentReviewSearchRepository.save(identityDocumentReview).block();

        // Search the identityDocumentReview
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + identityDocumentReview.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(identityDocumentReview.getId().intValue()))
            .jsonPath("$.[*].documentStatus")
            .value(hasItem(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.[*].resolutionDate")
            .value(hasItem(DEFAULT_RESOLUTION_DATE.toString()))
            .jsonPath("$.[*].reviewStatus")
            .value(hasItem(DEFAULT_REVIEW_STATUS.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY));
    }
}
