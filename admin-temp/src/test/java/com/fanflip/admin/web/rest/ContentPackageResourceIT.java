package com.fanflip.admin.web.rest;

import static com.fanflip.admin.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.ContentPackage;
import com.fanflip.admin.domain.DirectMessage;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.repository.ContentPackageRepository;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.search.ContentPackageSearchRepository;
import com.fanflip.admin.service.ContentPackageService;
import com.fanflip.admin.service.dto.ContentPackageDTO;
import com.fanflip.admin.service.mapper.ContentPackageMapper;
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
 * Integration tests for the {@link ContentPackageResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ContentPackageResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_VIDEO_COUNT = 1;
    private static final Integer UPDATED_VIDEO_COUNT = 2;

    private static final Integer DEFAULT_IMAGE_COUNT = 1;
    private static final Integer UPDATED_IMAGE_COUNT = 2;

    private static final Boolean DEFAULT_IS_PAID_CONTENT = false;
    private static final Boolean UPDATED_IS_PAID_CONTENT = true;

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

    private static final String ENTITY_API_URL = "/api/content-packages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/content-packages/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContentPackageRepository contentPackageRepository;

    @Mock
    private ContentPackageRepository contentPackageRepositoryMock;

    @Autowired
    private ContentPackageMapper contentPackageMapper;

    @Mock
    private ContentPackageService contentPackageServiceMock;

    @Autowired
    private ContentPackageSearchRepository contentPackageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ContentPackage contentPackage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPackage createEntity(EntityManager em) {
        ContentPackage contentPackage = new ContentPackage()
            .amount(DEFAULT_AMOUNT)
            .videoCount(DEFAULT_VIDEO_COUNT)
            .imageCount(DEFAULT_IMAGE_COUNT)
            .isPaidContent(DEFAULT_IS_PAID_CONTENT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createEntity(em)).block();
        contentPackage.setMessage(directMessage);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createEntity(em)).block();
        contentPackage.setPost(postFeed);
        return contentPackage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContentPackage createUpdatedEntity(EntityManager em) {
        ContentPackage contentPackage = new ContentPackage()
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        DirectMessage directMessage;
        directMessage = em.insert(DirectMessageResourceIT.createUpdatedEntity(em)).block();
        contentPackage.setMessage(directMessage);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createUpdatedEntity(em)).block();
        contentPackage.setPost(postFeed);
        return contentPackage;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_content_package__users_tagged").block();
            em.deleteAll(ContentPackage.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        DirectMessageResourceIT.deleteEntities(em);
        PostFeedResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        contentPackageSearchRepository.deleteAll().block();
        assertThat(contentPackageSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        contentPackage = createEntity(em);
    }

    @Test
    void createContentPackage() throws Exception {
        int databaseSizeBeforeCreate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(DEFAULT_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(DEFAULT_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(DEFAULT_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createContentPackageWithExistingId() throws Exception {
        // Create the ContentPackage with an existing ID
        contentPackage.setId(1L);
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        int databaseSizeBeforeCreate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsPaidContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        // set the field null
        contentPackage.setIsPaidContent(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        // set the field null
        contentPackage.setCreatedDate(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        // set the field null
        contentPackage.setIsDeleted(null);

        // Create the ContentPackage, which fails.
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllContentPackages() {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();

        // Get all the contentPackageList
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
            .value(hasItem(contentPackage.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].videoCount")
            .value(hasItem(DEFAULT_VIDEO_COUNT))
            .jsonPath("$.[*].imageCount")
            .value(hasItem(DEFAULT_IMAGE_COUNT))
            .jsonPath("$.[*].isPaidContent")
            .value(hasItem(DEFAULT_IS_PAID_CONTENT.booleanValue()))
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
    void getAllContentPackagesWithEagerRelationshipsIsEnabled() {
        when(contentPackageServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(contentPackageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContentPackagesWithEagerRelationshipsIsNotEnabled() {
        when(contentPackageServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(contentPackageRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getContentPackage() {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();

        // Get the contentPackage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, contentPackage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(contentPackage.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.videoCount")
            .value(is(DEFAULT_VIDEO_COUNT))
            .jsonPath("$.imageCount")
            .value(is(DEFAULT_IMAGE_COUNT))
            .jsonPath("$.isPaidContent")
            .value(is(DEFAULT_IS_PAID_CONTENT.booleanValue()))
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
    void getNonExistingContentPackage() {
        // Get the contentPackage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingContentPackage() throws Exception {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        contentPackageSearchRepository.save(contentPackage).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());

        // Update the contentPackage
        ContentPackage updatedContentPackage = contentPackageRepository.findById(contentPackage.getId()).block();
        updatedContentPackage
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(updatedContentPackage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contentPackageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ContentPackage> contentPackageSearchList = IterableUtils.toList(
                    contentPackageSearchRepository.findAll().collectList().block()
                );
                ContentPackage testContentPackageSearch = contentPackageSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testContentPackageSearch.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
                assertThat(testContentPackageSearch.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
                assertThat(testContentPackageSearch.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
                assertThat(testContentPackageSearch.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
                assertThat(testContentPackageSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testContentPackageSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testContentPackageSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testContentPackageSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testContentPackageSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contentPackageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateContentPackageWithPatch() throws Exception {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();

        // Update the contentPackage using partial update
        ContentPackage partialUpdatedContentPackage = new ContentPackage();
        partialUpdatedContentPackage.setId(contentPackage.getId());

        partialUpdatedContentPackage
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContentPackage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPackage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateContentPackageWithPatch() throws Exception {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();

        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();

        // Update the contentPackage using partial update
        ContentPackage partialUpdatedContentPackage = new ContentPackage();
        partialUpdatedContentPackage.setId(contentPackage.getId());

        partialUpdatedContentPackage
            .amount(UPDATED_AMOUNT)
            .videoCount(UPDATED_VIDEO_COUNT)
            .imageCount(UPDATED_IMAGE_COUNT)
            .isPaidContent(UPDATED_IS_PAID_CONTENT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContentPackage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContentPackage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        ContentPackage testContentPackage = contentPackageList.get(contentPackageList.size() - 1);
        assertThat(testContentPackage.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testContentPackage.getVideoCount()).isEqualTo(UPDATED_VIDEO_COUNT);
        assertThat(testContentPackage.getImageCount()).isEqualTo(UPDATED_IMAGE_COUNT);
        assertThat(testContentPackage.getIsPaidContent()).isEqualTo(UPDATED_IS_PAID_CONTENT);
        assertThat(testContentPackage.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testContentPackage.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testContentPackage.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testContentPackage.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testContentPackage.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, contentPackageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamContentPackage() throws Exception {
        int databaseSizeBeforeUpdate = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        contentPackage.setId(longCount.incrementAndGet());

        // Create the ContentPackage
        ContentPackageDTO contentPackageDTO = contentPackageMapper.toDto(contentPackage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contentPackageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContentPackage in the database
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteContentPackage() {
        // Initialize the database
        contentPackageRepository.save(contentPackage).block();
        contentPackageRepository.save(contentPackage).block();
        contentPackageSearchRepository.save(contentPackage).block();

        int databaseSizeBeforeDelete = contentPackageRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contentPackage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, contentPackage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ContentPackage> contentPackageList = contentPackageRepository.findAll().collectList().block();
        assertThat(contentPackageList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contentPackageSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchContentPackage() {
        // Initialize the database
        contentPackage = contentPackageRepository.save(contentPackage).block();
        contentPackageSearchRepository.save(contentPackage).block();

        // Search the contentPackage
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + contentPackage.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(contentPackage.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(sameNumber(DEFAULT_AMOUNT)))
            .jsonPath("$.[*].videoCount")
            .value(hasItem(DEFAULT_VIDEO_COUNT))
            .jsonPath("$.[*].imageCount")
            .value(hasItem(DEFAULT_IMAGE_COUNT))
            .jsonPath("$.[*].isPaidContent")
            .value(hasItem(DEFAULT_IS_PAID_CONTENT.booleanValue()))
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
