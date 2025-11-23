package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.HelpQuestion;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.HelpQuestionRepository;
import com.fanflip.admin.repository.search.HelpQuestionSearchRepository;
import com.fanflip.admin.service.HelpQuestionService;
import com.fanflip.admin.service.dto.HelpQuestionDTO;
import com.fanflip.admin.service.mapper.HelpQuestionMapper;
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
 * Integration tests for the {@link HelpQuestionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class HelpQuestionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/help-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/help-questions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HelpQuestionRepository helpQuestionRepository;

    @Mock
    private HelpQuestionRepository helpQuestionRepositoryMock;

    @Autowired
    private HelpQuestionMapper helpQuestionMapper;

    @Mock
    private HelpQuestionService helpQuestionServiceMock;

    @Autowired
    private HelpQuestionSearchRepository helpQuestionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private HelpQuestion helpQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpQuestion createEntity(EntityManager em) {
        HelpQuestion helpQuestion = new HelpQuestion().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).isDeleted(DEFAULT_IS_DELETED);
        return helpQuestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HelpQuestion createUpdatedEntity(EntityManager em) {
        HelpQuestion helpQuestion = new HelpQuestion().title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);
        return helpQuestion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_help_question__question").block();
            em.deleteAll(HelpQuestion.class).block();
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
        helpQuestionSearchRepository.deleteAll().block();
        assertThat(helpQuestionSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        helpQuestion = createEntity(em);
    }

    @Test
    void createHelpQuestion() throws Exception {
        int databaseSizeBeforeCreate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createHelpQuestionWithExistingId() throws Exception {
        // Create the HelpQuestion with an existing ID
        helpQuestion.setId(1L);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        int databaseSizeBeforeCreate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        // set the field null
        helpQuestion.setTitle(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        // set the field null
        helpQuestion.setIsDeleted(null);

        // Create the HelpQuestion, which fails.
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllHelpQuestions() {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();

        // Get all the helpQuestionList
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
            .value(hasItem(helpQuestion.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT.toString()))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHelpQuestionsWithEagerRelationshipsIsEnabled() {
        when(helpQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(helpQuestionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHelpQuestionsWithEagerRelationshipsIsNotEnabled() {
        when(helpQuestionServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(helpQuestionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getHelpQuestion() {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();

        // Get the helpQuestion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, helpQuestion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(helpQuestion.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT.toString()))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingHelpQuestion() {
        // Get the helpQuestion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingHelpQuestion() throws Exception {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        helpQuestionSearchRepository.save(helpQuestion).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());

        // Update the helpQuestion
        HelpQuestion updatedHelpQuestion = helpQuestionRepository.findById(helpQuestion.getId()).block();
        updatedHelpQuestion.title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(updatedHelpQuestion);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpQuestionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<HelpQuestion> helpQuestionSearchList = IterableUtils.toList(
                    helpQuestionSearchRepository.findAll().collectList().block()
                );
                HelpQuestion testHelpQuestionSearch = helpQuestionSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testHelpQuestionSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testHelpQuestionSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testHelpQuestionSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, helpQuestionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion.content(UPDATED_CONTENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateHelpQuestionWithPatch() throws Exception {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();

        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();

        // Update the helpQuestion using partial update
        HelpQuestion partialUpdatedHelpQuestion = new HelpQuestion();
        partialUpdatedHelpQuestion.setId(helpQuestion.getId());

        partialUpdatedHelpQuestion.title(UPDATED_TITLE).content(UPDATED_CONTENT).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedHelpQuestion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedHelpQuestion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        HelpQuestion testHelpQuestion = helpQuestionList.get(helpQuestionList.size() - 1);
        assertThat(testHelpQuestion.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testHelpQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHelpQuestion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, helpQuestionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamHelpQuestion() throws Exception {
        int databaseSizeBeforeUpdate = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        helpQuestion.setId(longCount.incrementAndGet());

        // Create the HelpQuestion
        HelpQuestionDTO helpQuestionDTO = helpQuestionMapper.toDto(helpQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(helpQuestionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the HelpQuestion in the database
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteHelpQuestion() {
        // Initialize the database
        helpQuestionRepository.save(helpQuestion).block();
        helpQuestionRepository.save(helpQuestion).block();
        helpQuestionSearchRepository.save(helpQuestion).block();

        int databaseSizeBeforeDelete = helpQuestionRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the helpQuestion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, helpQuestion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<HelpQuestion> helpQuestionList = helpQuestionRepository.findAll().collectList().block();
        assertThat(helpQuestionList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(helpQuestionSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchHelpQuestion() {
        // Initialize the database
        helpQuestion = helpQuestionRepository.save(helpQuestion).block();
        helpQuestionSearchRepository.save(helpQuestion).block();

        // Search the helpQuestion
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + helpQuestion.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(helpQuestion.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT.toString()))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
