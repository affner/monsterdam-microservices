package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.DocumentReviewObservation;
import com.monsterdam.admin.repository.DocumentReviewObservationRepository;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.search.DocumentReviewObservationSearchRepository;
import com.monsterdam.admin.service.dto.DocumentReviewObservationDTO;
import com.monsterdam.admin.service.mapper.DocumentReviewObservationMapper;
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
 * Integration tests for the {@link DocumentReviewObservationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DocumentReviewObservationResourceIT {

    private static final Instant DEFAULT_COMMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-review-observations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-review-observations/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentReviewObservationRepository documentReviewObservationRepository;

    @Autowired
    private DocumentReviewObservationMapper documentReviewObservationMapper;

    @Autowired
    private DocumentReviewObservationSearchRepository documentReviewObservationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DocumentReviewObservation documentReviewObservation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReviewObservation createEntity(EntityManager em) {
        DocumentReviewObservation documentReviewObservation = new DocumentReviewObservation()
            .commentDate(DEFAULT_COMMENT_DATE)
            .comment(DEFAULT_COMMENT);
        return documentReviewObservation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentReviewObservation createUpdatedEntity(EntityManager em) {
        DocumentReviewObservation documentReviewObservation = new DocumentReviewObservation()
            .commentDate(UPDATED_COMMENT_DATE)
            .comment(UPDATED_COMMENT);
        return documentReviewObservation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DocumentReviewObservation.class).block();
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
        documentReviewObservationSearchRepository.deleteAll().block();
        assertThat(documentReviewObservationSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        documentReviewObservation = createEntity(em);
    }

    @Test
    void createDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeCreate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(
                    documentReviewObservationSearchRepository.findAll().collectList().block()
                );
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        DocumentReviewObservation testDocumentReviewObservation = documentReviewObservationList.get(
            documentReviewObservationList.size() - 1
        );
        assertThat(testDocumentReviewObservation.getCommentDate()).isEqualTo(DEFAULT_COMMENT_DATE);
        assertThat(testDocumentReviewObservation.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    void createDocumentReviewObservationWithExistingId() throws Exception {
        // Create the DocumentReviewObservation with an existing ID
        documentReviewObservation.setId(1L);
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        int databaseSizeBeforeCreate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        // set the field null
        documentReviewObservation.setComment(null);

        // Create the DocumentReviewObservation, which fails.
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllDocumentReviewObservationsAsStream() {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        List<DocumentReviewObservation> documentReviewObservationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DocumentReviewObservationDTO.class)
            .getResponseBody()
            .map(documentReviewObservationMapper::toEntity)
            .filter(documentReviewObservation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(documentReviewObservationList).isNotNull();
        assertThat(documentReviewObservationList).hasSize(1);
        DocumentReviewObservation testDocumentReviewObservation = documentReviewObservationList.get(0);
        assertThat(testDocumentReviewObservation.getCommentDate()).isEqualTo(DEFAULT_COMMENT_DATE);
        assertThat(testDocumentReviewObservation.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    void getAllDocumentReviewObservations() {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        // Get all the documentReviewObservationList
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
            .value(hasItem(documentReviewObservation.getId().intValue()))
            .jsonPath("$.[*].commentDate")
            .value(hasItem(DEFAULT_COMMENT_DATE.toString()))
            .jsonPath("$.[*].comment")
            .value(hasItem(DEFAULT_COMMENT));
    }

    @Test
    void getDocumentReviewObservation() {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        // Get the documentReviewObservation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, documentReviewObservation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(documentReviewObservation.getId().intValue()))
            .jsonPath("$.commentDate")
            .value(is(DEFAULT_COMMENT_DATE.toString()))
            .jsonPath("$.comment")
            .value(is(DEFAULT_COMMENT));
    }

    @Test
    void getNonExistingDocumentReviewObservation() {
        // Get the documentReviewObservation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDocumentReviewObservation() throws Exception {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        documentReviewObservationSearchRepository.save(documentReviewObservation).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());

        // Update the documentReviewObservation
        DocumentReviewObservation updatedDocumentReviewObservation = documentReviewObservationRepository
            .findById(documentReviewObservation.getId())
            .block();
        updatedDocumentReviewObservation.commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(updatedDocumentReviewObservation);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        DocumentReviewObservation testDocumentReviewObservation = documentReviewObservationList.get(
            documentReviewObservationList.size() - 1
        );
        assertThat(testDocumentReviewObservation.getCommentDate()).isEqualTo(UPDATED_COMMENT_DATE);
        assertThat(testDocumentReviewObservation.getComment()).isEqualTo(UPDATED_COMMENT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(
                    documentReviewObservationSearchRepository.findAll().collectList().block()
                );
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentReviewObservation> documentReviewObservationSearchList = IterableUtils.toList(
                    documentReviewObservationSearchRepository.findAll().collectList().block()
                );
                DocumentReviewObservation testDocumentReviewObservationSearch = documentReviewObservationSearchList.get(
                    searchDatabaseSizeAfter - 1
                );
                assertThat(testDocumentReviewObservationSearch.getCommentDate()).isEqualTo(UPDATED_COMMENT_DATE);
                assertThat(testDocumentReviewObservationSearch.getComment()).isEqualTo(UPDATED_COMMENT);
            });
    }

    @Test
    void putNonExistingDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateDocumentReviewObservationWithPatch() throws Exception {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();

        // Update the documentReviewObservation using partial update
        DocumentReviewObservation partialUpdatedDocumentReviewObservation = new DocumentReviewObservation();
        partialUpdatedDocumentReviewObservation.setId(documentReviewObservation.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocumentReviewObservation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentReviewObservation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        DocumentReviewObservation testDocumentReviewObservation = documentReviewObservationList.get(
            documentReviewObservationList.size() - 1
        );
        assertThat(testDocumentReviewObservation.getCommentDate()).isEqualTo(DEFAULT_COMMENT_DATE);
        assertThat(testDocumentReviewObservation.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    void fullUpdateDocumentReviewObservationWithPatch() throws Exception {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();

        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();

        // Update the documentReviewObservation using partial update
        DocumentReviewObservation partialUpdatedDocumentReviewObservation = new DocumentReviewObservation();
        partialUpdatedDocumentReviewObservation.setId(documentReviewObservation.getId());

        partialUpdatedDocumentReviewObservation.commentDate(UPDATED_COMMENT_DATE).comment(UPDATED_COMMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDocumentReviewObservation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentReviewObservation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        DocumentReviewObservation testDocumentReviewObservation = documentReviewObservationList.get(
            documentReviewObservationList.size() - 1
        );
        assertThat(testDocumentReviewObservation.getCommentDate()).isEqualTo(UPDATED_COMMENT_DATE);
        assertThat(testDocumentReviewObservation.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    void patchNonExistingDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, documentReviewObservationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamDocumentReviewObservation() throws Exception {
        int databaseSizeBeforeUpdate = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        documentReviewObservation.setId(longCount.incrementAndGet());

        // Create the DocumentReviewObservation
        DocumentReviewObservationDTO documentReviewObservationDTO = documentReviewObservationMapper.toDto(documentReviewObservation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(documentReviewObservationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DocumentReviewObservation in the database
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteDocumentReviewObservation() {
        // Initialize the database
        documentReviewObservationRepository.save(documentReviewObservation).block();
        documentReviewObservationRepository.save(documentReviewObservation).block();
        documentReviewObservationSearchRepository.save(documentReviewObservation).block();

        int databaseSizeBeforeDelete = documentReviewObservationRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentReviewObservation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, documentReviewObservation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DocumentReviewObservation> documentReviewObservationList = documentReviewObservationRepository.findAll().collectList().block();
        assertThat(documentReviewObservationList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentReviewObservationSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchDocumentReviewObservation() {
        // Initialize the database
        documentReviewObservation = documentReviewObservationRepository.save(documentReviewObservation).block();
        documentReviewObservationSearchRepository.save(documentReviewObservation).block();

        // Search the documentReviewObservation
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + documentReviewObservation.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(documentReviewObservation.getId().intValue()))
            .jsonPath("$.[*].commentDate")
            .value(hasItem(DEFAULT_COMMENT_DATE.toString()))
            .jsonPath("$.[*].comment")
            .value(hasItem(DEFAULT_COMMENT));
    }
}
