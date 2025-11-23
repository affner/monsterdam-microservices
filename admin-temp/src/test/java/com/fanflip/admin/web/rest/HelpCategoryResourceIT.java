package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.HelpCategory;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.HelpCategoryRepository;
import com.fanflip.admin.repository.search.HelpCategorySearchRepository;
import com.fanflip.admin.service.dto.HelpCategoryDTO;
import com.fanflip.admin.service.mapper.HelpCategoryMapper;
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
 * Integration tests for the {@link HelpCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/help-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpCategoryRepository helpCategoryRepository;

    @Autowired
    private HelpCategoryMapper helpCategoryMapper;

    @Autowired
    private HelpCategorySearchRepository helpCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpCategory helpCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpCategory createEntity(EntityManager em) {
        HelpCategory helpCategory = new HelpCategory().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return helpCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpCategory createUpdatedEntity(EntityManager em) {
        HelpCategory helpCategory = new HelpCategory().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return helpCategory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HelpCategory.class).block();
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
        helpCategorySearchRepository.deleteAll().block();
        assertThat(helpCategorySearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        helpCategory = createEntity(em);
    }

    @Test
    void createHelpCategory() throws Exception {
        int databaseSizeBeforeCreate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createHelpCategoryWithExistingId() throws Exception {
        // Create the HelpCategory with an existing ID
        helpCategory.setId(1L);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        int databaseSizeBeforeCreate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        // set the field null
        helpCategory.setName(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        // set the field null
        helpCategory.setIsDeleted(null);

        // Create the HelpCategory, which fails.
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHelpCategories() {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();

        // Get all the helpCategoryList
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
            .value(hasItem(helpCategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getHelpCategory() {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();

        // Get the helpCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpCategory.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingHelpCategory() {
        // Get the helpCategory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpCategory() throws Exception {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        helpCategorySearchRepository.save(helpCategory).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());

        // Update the helpCategory
        HelpCategory updatedHelpCategory = helpCategoryRepository.findById(helpCategory.getId()).block();
        updatedHelpCategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(updatedHelpCategory);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HelpCategory> helpCategorySearchList = IterableUtils.toList(
                    helpCategorySearchRepository.findAll().collectList().block()
                );
                HelpCategory testHelpCategorySearch = helpCategorySearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHelpCategorySearch.getName()).isEqualTo(UPDATED_NAME);
                assertThat(testHelpCategorySearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpCategoryDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory.isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateHelpCategoryWithPatch() throws Exception {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();

        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();

        // Update the helpCategory using partial update
        HelpCategory partialUpdatedHelpCategory = new HelpCategory();
        partialUpdatedHelpCategory.setId(helpCategory.getId());

        partialUpdatedHelpCategory.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpCategory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpCategory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        HelpCategory testHelpCategory = helpCategoryList.get(helpCategoryList.size() - 1);
        assertThat(testHelpCategory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHelpCategory.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpCategoryDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHelpCategory() throws Exception {
        int databaseSizeBeforeUpdate = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        helpCategory.setId(longCount.incrementAndGet());

        // Create the HelpCategory
        HelpCategoryDTO helpCategoryDTO = helpCategoryMapper.toDto(helpCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpCategoryDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpCategory in the database
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHelpCategory() {
        // Initialize the database
        helpCategoryRepository.save(helpCategory).block();
        helpCategoryRepository.save(helpCategory).block();
        helpCategorySearchRepository.save(helpCategory).block();

        int databaseSizeBeforeDelete = helpCategoryRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the helpCategory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpCategory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpCategory> helpCategoryList = helpCategoryRepository.findAll().collectList().block();
        assertThat(helpCategoryList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpCategorySearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHelpCategory() {
        // Initialize the database
        helpCategory = helpCategoryRepository.save(helpCategory).block();
        helpCategorySearchRepository.save(helpCategory).block();

        // Search the helpCategory
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + helpCategory.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpCategory.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
