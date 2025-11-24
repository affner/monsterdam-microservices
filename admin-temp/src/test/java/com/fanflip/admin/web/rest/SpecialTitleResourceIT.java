package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.SpecialTitle;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.SpecialTitleRepository;
import com.monsterdam.admin.repository.search.SpecialTitleSearchRepository;
import com.monsterdam.admin.service.dto.SpecialTitleDTO;
import com.monsterdam.admin.service.mapper.SpecialTitleMapper;
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
 * Integration tests for the {@link SpecialTitleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SpecialTitleResourceIT {

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

    private static final String ENTITY_API_URL = "/api/special-titles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/special-titles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialTitleRepository specialTitleRepository;

    @Autowired
    private SpecialTitleMapper specialTitleMapper;

    @Autowired
    private SpecialTitleSearchRepository specialTitleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private SpecialTitle specialTitle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialTitle createEntity(EntityManager em) {
        SpecialTitle specialTitle = new SpecialTitle()
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return specialTitle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialTitle createUpdatedEntity(EntityManager em) {
        SpecialTitle specialTitle = new SpecialTitle()
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return specialTitle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(SpecialTitle.class).block();
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
        specialTitleSearchRepository.deleteAll().block();
        assertThat(specialTitleSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        specialTitle = createEntity(em);
    }

    @Test
    void createSpecialTitle() throws Exception {
        int databaseSizeBeforeCreate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createSpecialTitleWithExistingId() throws Exception {
        // Create the SpecialTitle with an existing ID
        specialTitle.setId(1L);
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        int databaseSizeBeforeCreate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        // set the field null
        specialTitle.setCreatedDate(null);

        // Create the SpecialTitle, which fails.
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        // set the field null
        specialTitle.setIsDeleted(null);

        // Create the SpecialTitle, which fails.
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllSpecialTitles() {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();

        // Get all the specialTitleList
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
            .value(hasItem(specialTitle.getId().intValue()))
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
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getSpecialTitle() {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();

        // Get the specialTitle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, specialTitle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(specialTitle.getId().intValue()))
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
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingSpecialTitle() {
        // Get the specialTitle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSpecialTitle() throws Exception {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        specialTitleSearchRepository.save(specialTitle).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());

        // Update the specialTitle
        SpecialTitle updatedSpecialTitle = specialTitleRepository.findById(specialTitle.getId()).block();
        updatedSpecialTitle
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(updatedSpecialTitle);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialTitleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SpecialTitle> specialTitleSearchList = IterableUtils.toList(
                    specialTitleSearchRepository.findAll().collectList().block()
                );
                SpecialTitle testSpecialTitleSearch = specialTitleSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testSpecialTitleSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testSpecialTitleSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testSpecialTitleSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testSpecialTitleSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testSpecialTitleSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testSpecialTitleSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, specialTitleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateSpecialTitleWithPatch() throws Exception {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();

        // Update the specialTitle using partial update
        SpecialTitle partialUpdatedSpecialTitle = new SpecialTitle();
        partialUpdatedSpecialTitle.setId(specialTitle.getId());

        partialUpdatedSpecialTitle
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialTitle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialTitle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateSpecialTitleWithPatch() throws Exception {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();

        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();

        // Update the specialTitle using partial update
        SpecialTitle partialUpdatedSpecialTitle = new SpecialTitle();
        partialUpdatedSpecialTitle.setId(specialTitle.getId());

        partialUpdatedSpecialTitle
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSpecialTitle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialTitle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        SpecialTitle testSpecialTitle = specialTitleList.get(specialTitleList.size() - 1);
        assertThat(testSpecialTitle.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialTitle.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialTitle.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialTitle.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialTitle.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialTitle.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, specialTitleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamSpecialTitle() throws Exception {
        int databaseSizeBeforeUpdate = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        specialTitle.setId(longCount.incrementAndGet());

        // Create the SpecialTitle
        SpecialTitleDTO specialTitleDTO = specialTitleMapper.toDto(specialTitle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(specialTitleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the SpecialTitle in the database
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteSpecialTitle() {
        // Initialize the database
        specialTitleRepository.save(specialTitle).block();
        specialTitleRepository.save(specialTitle).block();
        specialTitleSearchRepository.save(specialTitle).block();

        int databaseSizeBeforeDelete = specialTitleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the specialTitle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, specialTitle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<SpecialTitle> specialTitleList = specialTitleRepository.findAll().collectList().block();
        assertThat(specialTitleList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(specialTitleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchSpecialTitle() {
        // Initialize the database
        specialTitle = specialTitleRepository.save(specialTitle).block();
        specialTitleSearchRepository.save(specialTitle).block();

        // Search the specialTitle
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + specialTitle.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(specialTitle.getId().intValue()))
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
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
