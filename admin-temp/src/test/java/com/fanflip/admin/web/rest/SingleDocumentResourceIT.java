package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.SingleDocument;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.SingleDocumentRepository;
import com.monsterdam.admin.repository.search.SingleDocumentSearchRepository;
import com.monsterdam.admin.service.dto.SingleDocumentDTO;
import com.monsterdam.admin.service.mapper.SingleDocumentMapper;
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
 * Integration tests for the {@link SingleDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SingleDocumentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DOCUMENT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOCUMENT_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOCUMENT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOCUMENT_FILE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DOCUMENT_FILE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_FILE_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/single-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/single-documents/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SingleDocumentRepository singleDocumentRepository;

    @Autowired
    private SingleDocumentMapper singleDocumentMapper;

    @Autowired
    private SingleDocumentSearchRepository singleDocumentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SingleDocument singleDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleDocument createEntity(EntityManager em) {
        SingleDocument singleDocument = new SingleDocument()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .documentFile(DEFAULT_DOCUMENT_FILE)
            .documentFileContentType(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE)
            .documentFileS3Key(DEFAULT_DOCUMENT_FILE_S_3_KEY)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        singleDocument.setUser(userProfile);
        return singleDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SingleDocument createUpdatedEntity(EntityManager em) {
        SingleDocument singleDocument = new SingleDocument()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .documentFileS3Key(UPDATED_DOCUMENT_FILE_S_3_KEY)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        singleDocument.setUser(userProfile);
        return singleDocument;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SingleDocument.class).block();
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
        singleDocumentSearchRepository.deleteAll().block();
        assertThat(singleDocumentSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        singleDocument = createEntity(em);
    }

    @Test
    void createSingleDocument() throws Exception {
        int databaseSizeBeforeCreate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSingleDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSingleDocument.getDocumentFile()).isEqualTo(DEFAULT_DOCUMENT_FILE);
        assertThat(testSingleDocument.getDocumentFileContentType()).isEqualTo(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testSingleDocument.getDocumentFileS3Key()).isEqualTo(DEFAULT_DOCUMENT_FILE_S_3_KEY);
        assertThat(testSingleDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSingleDocumentWithExistingId() throws Exception {
        // Create the SingleDocument with an existing ID
        singleDocument.setId(1L);
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        int databaseSizeBeforeCreate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        singleDocument.setTitle(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDocumentFileS3KeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        singleDocument.setDocumentFileS3Key(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        singleDocument.setCreatedDate(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        // set the field null
        singleDocument.setIsDeleted(null);

        // Create the SingleDocument, which fails.
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSingleDocuments() {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();

        // Get all the singleDocumentList
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
            .value(hasItem(singleDocument.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].documentFileContentType")
            .value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].documentFile")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE)))
            .jsonPath("$.[*].documentFileS3Key")
            .value(hasItem(DEFAULT_DOCUMENT_FILE_S_3_KEY))
            .jsonPath("$.[*].documentType")
            .value(hasItem(DEFAULT_DOCUMENT_TYPE))
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
    void getSingleDocument() {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();

        // Get the singleDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, singleDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(singleDocument.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.documentFileContentType")
            .value(is(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE))
            .jsonPath("$.documentFile")
            .value(is(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE)))
            .jsonPath("$.documentFileS3Key")
            .value(is(DEFAULT_DOCUMENT_FILE_S_3_KEY))
            .jsonPath("$.documentType")
            .value(is(DEFAULT_DOCUMENT_TYPE))
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
    void getNonExistingSingleDocument() {
        // Get the singleDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSingleDocument() throws Exception {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        singleDocumentSearchRepository.save(singleDocument).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());

        // Update the singleDocument
        SingleDocument updatedSingleDocument = singleDocumentRepository.findById(singleDocument.getId()).block();
        updatedSingleDocument
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .documentFileS3Key(UPDATED_DOCUMENT_FILE_S_3_KEY)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(updatedSingleDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSingleDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleDocument.getDocumentFile()).isEqualTo(UPDATED_DOCUMENT_FILE);
        assertThat(testSingleDocument.getDocumentFileContentType()).isEqualTo(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testSingleDocument.getDocumentFileS3Key()).isEqualTo(UPDATED_DOCUMENT_FILE_S_3_KEY);
        assertThat(testSingleDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SingleDocument> singleDocumentSearchList = IterableUtils.toList(
                    singleDocumentSearchRepository.findAll().collectList().block()
                );
                SingleDocument testSingleDocumentSearch = singleDocumentSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSingleDocumentSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testSingleDocumentSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSingleDocumentSearch.getDocumentFile()).isEqualTo(UPDATED_DOCUMENT_FILE);
                assertThat(testSingleDocumentSearch.getDocumentFileContentType()).isEqualTo(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
                assertThat(testSingleDocumentSearch.getDocumentFileS3Key()).isEqualTo(UPDATED_DOCUMENT_FILE_S_3_KEY);
                assertThat(testSingleDocumentSearch.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
                assertThat(testSingleDocumentSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSingleDocumentSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSingleDocumentSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSingleDocumentSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSingleDocumentSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, singleDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSingleDocumentWithPatch() throws Exception {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();

        // Update the singleDocument using partial update
        SingleDocument partialUpdatedSingleDocument = new SingleDocument();
        partialUpdatedSingleDocument.setId(singleDocument.getId());

        partialUpdatedSingleDocument
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentFileS3Key(UPDATED_DOCUMENT_FILE_S_3_KEY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSingleDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleDocument.getDocumentFile()).isEqualTo(DEFAULT_DOCUMENT_FILE);
        assertThat(testSingleDocument.getDocumentFileContentType()).isEqualTo(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testSingleDocument.getDocumentFileS3Key()).isEqualTo(UPDATED_DOCUMENT_FILE_S_3_KEY);
        assertThat(testSingleDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateSingleDocumentWithPatch() throws Exception {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();

        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();

        // Update the singleDocument using partial update
        SingleDocument partialUpdatedSingleDocument = new SingleDocument();
        partialUpdatedSingleDocument.setId(singleDocument.getId());

        partialUpdatedSingleDocument
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .documentFile(UPDATED_DOCUMENT_FILE)
            .documentFileContentType(UPDATED_DOCUMENT_FILE_CONTENT_TYPE)
            .documentFileS3Key(UPDATED_DOCUMENT_FILE_S_3_KEY)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSingleDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSingleDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        SingleDocument testSingleDocument = singleDocumentList.get(singleDocumentList.size() - 1);
        assertThat(testSingleDocument.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSingleDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSingleDocument.getDocumentFile()).isEqualTo(UPDATED_DOCUMENT_FILE);
        assertThat(testSingleDocument.getDocumentFileContentType()).isEqualTo(UPDATED_DOCUMENT_FILE_CONTENT_TYPE);
        assertThat(testSingleDocument.getDocumentFileS3Key()).isEqualTo(UPDATED_DOCUMENT_FILE_S_3_KEY);
        assertThat(testSingleDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testSingleDocument.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSingleDocument.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSingleDocument.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSingleDocument.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSingleDocument.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, singleDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSingleDocument() throws Exception {
        int databaseSizeBeforeUpdate = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        singleDocument.setId(longCount.incrementAndGet());

        // Create the SingleDocument
        SingleDocumentDTO singleDocumentDTO = singleDocumentMapper.toDto(singleDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(singleDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SingleDocument in the database
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSingleDocument() {
        // Initialize the database
        singleDocumentRepository.save(singleDocument).block();
        singleDocumentRepository.save(singleDocument).block();
        singleDocumentSearchRepository.save(singleDocument).block();

        int databaseSizeBeforeDelete = singleDocumentRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the singleDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, singleDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SingleDocument> singleDocumentList = singleDocumentRepository.findAll().collectList().block();
        assertThat(singleDocumentList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(singleDocumentSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSingleDocument() {
        // Initialize the database
        singleDocument = singleDocumentRepository.save(singleDocument).block();
        singleDocumentSearchRepository.save(singleDocument).block();

        // Search the singleDocument
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + singleDocument.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(singleDocument.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].documentFileContentType")
            .value(hasItem(DEFAULT_DOCUMENT_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].documentFile")
            .value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOCUMENT_FILE)))
            .jsonPath("$.[*].documentFileS3Key")
            .value(hasItem(DEFAULT_DOCUMENT_FILE_S_3_KEY))
            .jsonPath("$.[*].documentType")
            .value(hasItem(DEFAULT_DOCUMENT_TYPE))
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
