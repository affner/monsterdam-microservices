package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.HelpSubcategory;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.HelpSubcategoryRepository;
import com.fanflip.admin.repository.search.HelpSubcategorySearchRepository;
import com.fanflip.admin.service.dto.HelpSubcategoryDTO;
import com.fanflip.admin.service.mapper.HelpSubcategoryMapper;
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
 * Integration tests for the {@link HelpSubcategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpSubcategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-subcategories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/help-subcategories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpSubcategoryRepository helpSubcategoryRepository;

    @Autowired
    private HelpSubcategoryMapper helpSubcategoryMapper;

    @Autowired
    private HelpSubcategorySearchRepository helpSubcategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpSubcategory helpSubcategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createEntity(EntityManager em) {
        HelpSubcategory helpSubcategory = new HelpSubcategory().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return helpSubcategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpSubcategory createUpdatedEntity(EntityManager em) {
        HelpSubcategory helpSubcategory = new HelpSubcategory().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return helpSubcategory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HelpSubcategory.class).block();
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
        helpSubcategorySearchRepository.deleteAll().block();
        assertThat(helpSubcategorySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        helpSubcategory = createEntity(em);
    }

    @Test
    void createHelpSubcategory() throws Exception {
        int databaseSizeBeforeCreate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createHelpSubcategoryWithExistingId() throws Exception {
        // Create the HelpSubcategory with an existing ID
        helpSubcategory.setId(1L);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        int databaseSizeBeforeCreate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        // set the field null
        helpSubcategory.setName(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        // set the field null
        helpSubcategory.setIsDeleted(null);

        // Create the HelpSubcategory, which fails.
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHelpSubcategories() {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();

        // Get all the helpSubcategoryList
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
            .value(hasItem(helpSubcategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getHelpSubcategory() {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();

        // Get the helpSubcategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpSubcategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpSubcategory.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingHelpSubcategory() {
        // Get the helpSubcategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpSubcategory() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        helpSubcategorySearchRepository.save(helpSubcategory).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());

        // Update the helpSubcategory
        HelpSubcategory updatedHelpSubcategory = helpSubcategoryRepository.findById(helpSubcategory.getId()).block();
        updatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(updatedHelpSubcategory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HelpSubcategory> helpSubcategorySearchList = IterableUtils.toList(
                    helpSubcategorySearchRepository.findAll().collectList().block()
                );
                HelpSubcategory testHelpSubcategorySearch = helpSubcategorySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHelpSubcategorySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testHelpSubcategorySearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpSubcategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateHelpSubcategoryWithPatch() throws Exception {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();

        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();

        // Update the helpSubcategory using partial update
        HelpSubcategory partialUpdatedHelpSubcategory = new HelpSubcategory();
        partialUpdatedHelpSubcategory.setId(helpSubcategory.getId());

        partialUpdatedHelpSubcategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpSubcategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpSubcategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpSubcategory testHelpSubcategory = helpSubcategoryList.get(helpSubcategoryList.size() - 1);
        assertThat(testHelpSubcategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpSubcategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpSubcategoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHelpSubcategory() throws Exception {
        int databaseSizeBeforeUpdate = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        helpSubcategory.setId(longCount.incrementAndGet());

        // Create the HelpSubcategory
        HelpSubcategoryDTO helpSubcategoryDTO = helpSubcategoryMapper.toDto(helpSubcategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpSubcategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpSubcategory in the database
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHelpSubcategory() {
        // Initialize the database
        helpSubcategoryRepository.save(helpSubcategory).block();
        helpSubcategoryRepository.save(helpSubcategory).block();
        helpSubcategorySearchRepository.save(helpSubcategory).block();

        int databaseSizeBeforeDelete = helpSubcategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the helpSubcategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpSubcategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpSubcategory> helpSubcategoryList = helpSubcategoryRepository.findAll().collectList().block();
        assertThat(helpSubcategoryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpSubcategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHelpSubcategory() {
        // Initialize the database
        helpSubcategory = helpSubcategoryRepository.save(helpSubcategory).block();
        helpSubcategorySearchRepository.save(helpSubcategory).block();

        // Search the helpSubcategory
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + helpSubcategory.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpSubcategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
