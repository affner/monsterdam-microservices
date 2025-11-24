package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.Feedback;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.enumeration.FeedbackType;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.FeedbackRepository;
import com.monsterdam.admin.repository.search.FeedbackSearchRepository;
import com.monsterdam.admin.service.dto.FeedbackDTO;
import com.monsterdam.admin.service.mapper.FeedbackMapper;
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
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeedbackResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_FEEDBACK_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FEEDBACK_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_FEEDBACK_RATING = 1;
    private static final Integer UPDATED_FEEDBACK_RATING = 2;

    private static final FeedbackType DEFAULT_FEEDBACK_TYPE = FeedbackType.ERROR;
    private static final FeedbackType UPDATED_FEEDBACK_TYPE = FeedbackType.SUGGESTION;

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

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/feedbacks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FeedbackSearchRepository feedbackSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Feedback feedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .content(DEFAULT_CONTENT)
            .feedbackDate(DEFAULT_FEEDBACK_DATE)
            .feedbackRating(DEFAULT_FEEDBACK_RATING)
            .feedbackType(DEFAULT_FEEDBACK_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        feedback.setCreator(userProfile);
        return feedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        feedback.setCreator(userProfile);
        return feedback;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Feedback.class).block();
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
        feedbackSearchRepository.deleteAll().block();
        assertThat(feedbackSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        feedback = createEntity(em);
    }

    @Test
    void createFeedback() throws Exception {
        int databaseSizeBeforeCreate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(DEFAULT_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(DEFAULT_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(DEFAULT_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        int databaseSizeBeforeCreate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        // set the field null
        feedback.setContent(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFeedbackDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        // set the field null
        feedback.setFeedbackDate(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        // set the field null
        feedback.setCreatedDate(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        // set the field null
        feedback.setIsDeleted(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFeedbacks() {
        // Initialize the database
        feedbackRepository.save(feedback).block();

        // Get all the feedbackList
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
            .value(hasItem(feedback.getId().intValue()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].feedbackDate")
            .value(hasItem(DEFAULT_FEEDBACK_DATE.toString()))
            .jsonPath("$.[*].feedbackRating")
            .value(hasItem(DEFAULT_FEEDBACK_RATING))
            .jsonPath("$.[*].feedbackType")
            .value(hasItem(DEFAULT_FEEDBACK_TYPE.toString()))
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
    void getFeedback() {
        // Initialize the database
        feedbackRepository.save(feedback).block();

        // Get the feedback
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feedback.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feedback.getId().intValue()))
            .jsonPath("$.content")
            .value(is(DEFAULT_CONTENT))
            .jsonPath("$.feedbackDate")
            .value(is(DEFAULT_FEEDBACK_DATE.toString()))
            .jsonPath("$.feedbackRating")
            .value(is(DEFAULT_FEEDBACK_RATING))
            .jsonPath("$.feedbackType")
            .value(is(DEFAULT_FEEDBACK_TYPE.toString()))
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
    void getNonExistingFeedback() {
        // Get the feedback
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeedback() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback).block();

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        feedbackSearchRepository.save(feedback).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).block();
        updatedFeedback
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(updatedFeedback);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Feedback> feedbackSearchList = IterableUtils.toList(feedbackSearchRepository.findAll().collectList().block());
                Feedback testFeedbackSearch = feedbackSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testFeedbackSearch.getContent()).isEqualTo(UPDATED_CONTENT);
                assertThat(testFeedbackSearch.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
                assertThat(testFeedbackSearch.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
                assertThat(testFeedbackSearch.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
                assertThat(testFeedbackSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testFeedbackSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testFeedbackSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testFeedbackSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testFeedbackSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback).block();

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(DEFAULT_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        feedbackRepository.save(feedback).block();

        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .content(UPDATED_CONTENT)
            .feedbackDate(UPDATED_FEEDBACK_DATE)
            .feedbackRating(UPDATED_FEEDBACK_RATING)
            .feedbackType(UPDATED_FEEDBACK_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedback))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        Feedback testFeedback = feedbackList.get(feedbackList.size() - 1);
        assertThat(testFeedback.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFeedback.getFeedbackDate()).isEqualTo(UPDATED_FEEDBACK_DATE);
        assertThat(testFeedback.getFeedbackRating()).isEqualTo(UPDATED_FEEDBACK_RATING);
        assertThat(testFeedback.getFeedbackType()).isEqualTo(UPDATED_FEEDBACK_TYPE);
        assertThat(testFeedback.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFeedback.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testFeedback.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testFeedback.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testFeedback.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feedbackDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFeedback() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Feedback in the database
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFeedback() {
        // Initialize the database
        feedbackRepository.save(feedback).block();
        feedbackRepository.save(feedback).block();
        feedbackSearchRepository.save(feedback).block();

        int databaseSizeBeforeDelete = feedbackRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the feedback
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feedback.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Feedback> feedbackList = feedbackRepository.findAll().collectList().block();
        assertThat(feedbackList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(feedbackSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFeedback() {
        // Initialize the database
        feedback = feedbackRepository.save(feedback).block();
        feedbackSearchRepository.save(feedback).block();

        // Search the feedback
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + feedback.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(feedback.getId().intValue()))
            .jsonPath("$.[*].content")
            .value(hasItem(DEFAULT_CONTENT))
            .jsonPath("$.[*].feedbackDate")
            .value(hasItem(DEFAULT_FEEDBACK_DATE.toString()))
            .jsonPath("$.[*].feedbackRating")
            .value(hasItem(DEFAULT_FEEDBACK_RATING))
            .jsonPath("$.[*].feedbackType")
            .value(hasItem(DEFAULT_FEEDBACK_TYPE.toString()))
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
