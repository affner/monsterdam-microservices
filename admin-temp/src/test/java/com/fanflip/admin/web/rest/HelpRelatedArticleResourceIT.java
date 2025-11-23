package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.HelpRelatedArticle;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.HelpRelatedArticleRepository;
import com.fanflip.admin.repository.search.HelpRelatedArticleSearchRepository;
import com.fanflip.admin.service.dto.HelpRelatedArticleDTO;
import com.fanflip.admin.service.mapper.HelpRelatedArticleMapper;
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
 * Integration tests for the {@link HelpRelatedArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpRelatedArticleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/help-related-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/help-related-articles/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpRelatedArticleRepository helpRelatedArticleRepository;

    @Autowired
    private HelpRelatedArticleMapper helpRelatedArticleMapper;

    @Autowired
    private HelpRelatedArticleSearchRepository helpRelatedArticleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpRelatedArticle helpRelatedArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createEntity(EntityManager em) {
        HelpRelatedArticle helpRelatedArticle = new HelpRelatedArticle().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
        return helpRelatedArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpRelatedArticle createUpdatedEntity(EntityManager em) {
        HelpRelatedArticle helpRelatedArticle = new HelpRelatedArticle().title(UPDATED_TITLE).content(UPDATED_CONTENT);
        return helpRelatedArticle;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(HelpRelatedArticle.class).block();
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
        helpRelatedArticleSearchRepository.deleteAll().block();
        assertThat(helpRelatedArticleSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        helpRelatedArticle = createEntity(em);
    }

    @Test
    void createHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeCreate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    void createHelpRelatedArticleWithExistingId() throws Exception {
        // Create the HelpRelatedArticle with an existing ID
        helpRelatedArticle.setId(1L);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        int databaseSizeBeforeCreate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        // set the field null
        helpRelatedArticle.setTitle(null);

        // Create the HelpRelatedArticle, which fails.
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHelpRelatedArticles() {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();

        // Get all the helpRelatedArticleList
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
            .value(hasItem(helpRelatedArticle.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT.toString()));
    }

    @Test
    void getHelpRelatedArticle() {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();

        // Get the helpRelatedArticle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpRelatedArticle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpRelatedArticle.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT.toString()));
    }

    @Test
    void getNonExistingHelpRelatedArticle() {
        // Get the helpRelatedArticle
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpRelatedArticle() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        helpRelatedArticleSearchRepository.save(helpRelatedArticle).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());

        // Update the helpRelatedArticle
        HelpRelatedArticle updatedHelpRelatedArticle = helpRelatedArticleRepository.findById(helpRelatedArticle.getId()).block();
        updatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(updatedHelpRelatedArticle);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(UPDATED_CONTENT);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HelpRelatedArticle> helpRelatedArticleSearchList = IterableUtils.toList(
                    helpRelatedArticleSearchRepository.findAll().collectList().block()
                );
                HelpRelatedArticle testHelpRelatedArticleSearch = helpRelatedArticleSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHelpRelatedArticleSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testHelpRelatedArticleSearch.getContent()).isEqualTo(UPDATED_CONTENT);
            });
    }

    @Test
    void putNonExistingHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        partialUpdatedHelpRelatedArticle.content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpRelatedArticle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    void fullUpdateHelpRelatedArticleWithPatch() throws Exception {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();

        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();

        // Update the helpRelatedArticle using partial update
        HelpRelatedArticle partialUpdatedHelpRelatedArticle = new HelpRelatedArticle();
        partialUpdatedHelpRelatedArticle.setId(helpRelatedArticle.getId());

        partialUpdatedHelpRelatedArticle.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpRelatedArticle.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpRelatedArticle))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        HelpRelatedArticle testHelpRelatedArticle = helpRelatedArticleList.get(helpRelatedArticleList.size() - 1);
        assertThat(testHelpRelatedArticle.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpRelatedArticle.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    void patchNonExistingHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpRelatedArticleDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHelpRelatedArticle() throws Exception {
        int databaseSizeBeforeUpdate = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        helpRelatedArticle.setId(longCount.incrementAndGet());

        // Create the HelpRelatedArticle
        HelpRelatedArticleDTO helpRelatedArticleDTO = helpRelatedArticleMapper.toDto(helpRelatedArticle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpRelatedArticleDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpRelatedArticle in the database
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHelpRelatedArticle() {
        // Initialize the database
        helpRelatedArticleRepository.save(helpRelatedArticle).block();
        helpRelatedArticleRepository.save(helpRelatedArticle).block();
        helpRelatedArticleSearchRepository.save(helpRelatedArticle).block();

        int databaseSizeBeforeDelete = helpRelatedArticleRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the helpRelatedArticle
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpRelatedArticle.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpRelatedArticle> helpRelatedArticleList = helpRelatedArticleRepository.findAll().collectList().block();
        assertThat(helpRelatedArticleList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpRelatedArticleSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHelpRelatedArticle() {
        // Initialize the database
        helpRelatedArticle = helpRelatedArticleRepository.save(helpRelatedArticle).block();
        helpRelatedArticleSearchRepository.save(helpRelatedArticle).block();

        // Search the helpRelatedArticle
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + helpRelatedArticle.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpRelatedArticle.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT.toString()));
    }
}
