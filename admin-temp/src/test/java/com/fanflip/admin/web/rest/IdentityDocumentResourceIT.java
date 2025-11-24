package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.IdentityDocument;
import com.monsterdam.admin.domain.enumeration.DocumentStatus;
import com.monsterdam.admin.domain.enumeration.DocumentType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.IdentityDocumentRepository;
import com.monsterdam.admin.repository.search.IdentityDocumentSearchRepository;
import com.monsterdam.admin.service.dto.IdentityDocumentDTO;
import com.monsterdam.admin.service.mapper.IdentityDocumentMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
 * Integration tests for the {@link IdentityDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IdentityDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_DESCRIPTION = "BBBBBBBBBB";

    private static final DocumentStatus DEFAULT_DOCUMENT_STATUS = DocumentStatus.PENDING;
    private static final DocumentStatus UPDATED_DOCUMENT_STATUS = DocumentStatus.APPROVED;

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.ID_VERIFICATION;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.CONTRACT;

    private static final byte[] DEFAULT_FILE_DOCUMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_DOCUMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_DOCUMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_DOCUMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_DOCUMENT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FILE_DOCUMENT_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/identity-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/identity-documents/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IdentityDocumentRepository identityDocumentRepository;

    @Autowired
    private IdentityDocumentMapper identityDocumentMapper;

    @Autowired
    private IdentityDocumentSearchRepository identityDocumentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IdentityDocument identityDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createEntity(EntityManager em) {
        IdentityDocument identityDocument = new IdentityDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentDescription(DEFAULT_DOCUMENT_DESCRIPTION)
            .documentStatus(DEFAULT_DOCUMENT_STATUS)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .fileDocument(DEFAULT_FILE_DOCUMENT)
            .fileDocumentContentType(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(DEFAULT_FILE_DOCUMENT_S_3_KEY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return identityDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createUpdatedEntity(EntityManager em) {
        IdentityDocument identityDocument = new IdentityDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return identityDocument;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IdentityDocument.class).block();
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
        identityDocumentSearchRepository.deleteAll().block();
        assertThat(identityDocumentSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        identityDocument = createEntity(em);
    }

    @Test
    void createIdentityDocument() throws Exception {
        int databaseSizeBeforeCreate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        IdentityDocument testIdentityDocument = identityDocumentList.get(identityDocumentList.size() - 1);
        assertThat(testIdentityDocument.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testIdentityDocument.getDocumentDescription()).isEqualTo(DEFAULT_DOCUMENT_DESCRIPTION);
        assertThat(testIdentityDocument.getDocumentStatus()).isEqualTo(DEFAULT_DOCUMENT_STATUS);
        assertThat(testIdentityDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testIdentityDocument.getFileDocument()).isEqualTo(DEFAULT_FILE_DOCUMENT);
        assertThat(testIdentityDocument.getFileDocumentContentType()).isEqualTo(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE);
        assertThat(testIdentityDocument.getFileDocumentS3Key()).isEqualTo(DEFAULT_FILE_DOCUMENT_S_3_KEY);
        assertThat(testIdentityDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIdentityDocument.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIdentityDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void createIdentityDocumentWithExistingId() throws Exception {
        // Create the IdentityDocument with an existing ID
        identityDocument.setId(1L);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        int databaseSizeBeforeCreate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDocumentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        identityDocument.setDocumentName(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFileDocumentS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        identityDocument.setFileDocumentS3Key(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        identityDocument.setCreatedDate(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllIdentityDocuments() {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();

        // Get all the identityDocumentList
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
            .value(hasItem(identityDocument.getId().intValue()))
            .jsonPath("$.[*].documentName")
            .value(hasItem(DEFAULT_DOCUMENT_NAME))
            .jsonPath("$.[*].documentDescription")
            .value(hasItem(DEFAULT_DOCUMENT_DESCRIPTION))
            .jsonPath("$.[*].documentStatus")
            .value(hasItem(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.[*].documentType")
            .value(hasItem(DEFAULT_DOCUMENT_TYPE.toString()))
            .jsonPath("$.[*].fileDocumentContentType")
            .value(hasItem(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE))
            .jsonPath("$.[*].fileDocument")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_DOCUMENT)))
            .jsonPath("$.[*].fileDocumentS3Key")
            .value(hasItem(DEFAULT_FILE_DOCUMENT_S_3_KEY))
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
    void getIdentityDocument() {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();

        // Get the identityDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, identityDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(identityDocument.getId().intValue()))
            .jsonPath("$.documentName")
            .value(is(DEFAULT_DOCUMENT_NAME))
            .jsonPath("$.documentDescription")
            .value(is(DEFAULT_DOCUMENT_DESCRIPTION))
            .jsonPath("$.documentStatus")
            .value(is(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.documentType")
            .value(is(DEFAULT_DOCUMENT_TYPE.toString()))
            .jsonPath("$.fileDocumentContentType")
            .value(is(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE))
            .jsonPath("$.fileDocument")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_FILE_DOCUMENT)))
            .jsonPath("$.fileDocumentS3Key")
            .value(is(DEFAULT_FILE_DOCUMENT_S_3_KEY))
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
    void getNonExistingIdentityDocument() {
        // Get the identityDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIdentityDocument() throws Exception {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();

        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        identityDocumentSearchRepository.save(identityDocument).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());

        // Update the identityDocument
        IdentityDocument updatedIdentityDocument = identityDocumentRepository.findById(identityDocument.getId()).block();
        updatedIdentityDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(updatedIdentityDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, identityDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocument testIdentityDocument = identityDocumentList.get(identityDocumentList.size() - 1);
        assertThat(testIdentityDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testIdentityDocument.getDocumentDescription()).isEqualTo(UPDATED_DOCUMENT_DESCRIPTION);
        assertThat(testIdentityDocument.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
        assertThat(testIdentityDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testIdentityDocument.getFileDocument()).isEqualTo(UPDATED_FILE_DOCUMENT);
        assertThat(testIdentityDocument.getFileDocumentContentType()).isEqualTo(UPDATED_FILE_DOCUMENT_CONTENT_TYPE);
        assertThat(testIdentityDocument.getFileDocumentS3Key()).isEqualTo(UPDATED_FILE_DOCUMENT_S_3_KEY);
        assertThat(testIdentityDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIdentityDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<IdentityDocument> identityDocumentSearchList = IterableUtils.toList(
                    identityDocumentSearchRepository.findAll().collectList().block()
                );
                IdentityDocument testIdentityDocumentSearch = identityDocumentSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testIdentityDocumentSearch.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
                assertThat(testIdentityDocumentSearch.getDocumentDescription()).isEqualTo(UPDATED_DOCUMENT_DESCRIPTION);
                assertThat(testIdentityDocumentSearch.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
                assertThat(testIdentityDocumentSearch.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
                assertThat(testIdentityDocumentSearch.getFileDocument()).isEqualTo(UPDATED_FILE_DOCUMENT);
                assertThat(testIdentityDocumentSearch.getFileDocumentContentType()).isEqualTo(UPDATED_FILE_DOCUMENT_CONTENT_TYPE);
                assertThat(testIdentityDocumentSearch.getFileDocumentS3Key()).isEqualTo(UPDATED_FILE_DOCUMENT_S_3_KEY);
                assertThat(testIdentityDocumentSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testIdentityDocumentSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testIdentityDocumentSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testIdentityDocumentSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
            });
    }

    @Test
    void putNonExistingIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, identityDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();

        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentityDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocument testIdentityDocument = identityDocumentList.get(identityDocumentList.size() - 1);
        assertThat(testIdentityDocument.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testIdentityDocument.getDocumentDescription()).isEqualTo(UPDATED_DOCUMENT_DESCRIPTION);
        assertThat(testIdentityDocument.getDocumentStatus()).isEqualTo(DEFAULT_DOCUMENT_STATUS);
        assertThat(testIdentityDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testIdentityDocument.getFileDocument()).isEqualTo(DEFAULT_FILE_DOCUMENT);
        assertThat(testIdentityDocument.getFileDocumentContentType()).isEqualTo(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE);
        assertThat(testIdentityDocument.getFileDocumentS3Key()).isEqualTo(UPDATED_FILE_DOCUMENT_S_3_KEY);
        assertThat(testIdentityDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIdentityDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
    }

    @Test
    void fullUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();

        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentDescription(UPDATED_DOCUMENT_DESCRIPTION)
            .documentStatus(UPDATED_DOCUMENT_STATUS)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileDocument(UPDATED_FILE_DOCUMENT)
            .fileDocumentContentType(UPDATED_FILE_DOCUMENT_CONTENT_TYPE)
            .fileDocumentS3Key(UPDATED_FILE_DOCUMENT_S_3_KEY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIdentityDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        IdentityDocument testIdentityDocument = identityDocumentList.get(identityDocumentList.size() - 1);
        assertThat(testIdentityDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testIdentityDocument.getDocumentDescription()).isEqualTo(UPDATED_DOCUMENT_DESCRIPTION);
        assertThat(testIdentityDocument.getDocumentStatus()).isEqualTo(UPDATED_DOCUMENT_STATUS);
        assertThat(testIdentityDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testIdentityDocument.getFileDocument()).isEqualTo(UPDATED_FILE_DOCUMENT);
        assertThat(testIdentityDocument.getFileDocumentContentType()).isEqualTo(UPDATED_FILE_DOCUMENT_CONTENT_TYPE);
        assertThat(testIdentityDocument.getFileDocumentS3Key()).isEqualTo(UPDATED_FILE_DOCUMENT_S_3_KEY);
        assertThat(testIdentityDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIdentityDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIdentityDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIdentityDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    void patchNonExistingIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, identityDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamIdentityDocument() throws Exception {
        int databaseSizeBeforeUpdate = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(identityDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IdentityDocument in the database
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteIdentityDocument() {
        // Initialize the database
        identityDocumentRepository.save(identityDocument).block();
        identityDocumentRepository.save(identityDocument).block();
        identityDocumentSearchRepository.save(identityDocument).block();

        int databaseSizeBeforeDelete = identityDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the identityDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, identityDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IdentityDocument> identityDocumentList = identityDocumentRepository.findAll().collectList().block();
        assertThat(identityDocumentList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(identityDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchIdentityDocument() {
        // Initialize the database
        identityDocument = identityDocumentRepository.save(identityDocument).block();
        identityDocumentSearchRepository.save(identityDocument).block();

        // Search the identityDocument
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + identityDocument.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(identityDocument.getId().intValue()))
            .jsonPath("$.[*].documentName")
            .value(hasItem(DEFAULT_DOCUMENT_NAME))
            .jsonPath("$.[*].documentDescription")
            .value(hasItem(DEFAULT_DOCUMENT_DESCRIPTION))
            .jsonPath("$.[*].documentStatus")
            .value(hasItem(DEFAULT_DOCUMENT_STATUS.toString()))
            .jsonPath("$.[*].documentType")
            .value(hasItem(DEFAULT_DOCUMENT_TYPE.toString()))
            .jsonPath("$.[*].fileDocumentContentType")
            .value(hasItem(DEFAULT_FILE_DOCUMENT_CONTENT_TYPE))
            .jsonPath("$.[*].fileDocument")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_DOCUMENT)))
            .jsonPath("$.[*].fileDocumentS3Key")
            .value(hasItem(DEFAULT_FILE_DOCUMENT_S_3_KEY))
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
