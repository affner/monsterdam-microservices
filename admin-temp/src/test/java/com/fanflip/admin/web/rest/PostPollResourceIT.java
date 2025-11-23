package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.PostFeed;
import com.fanflip.admin.domain.PostPoll;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.PostPollRepository;
import com.fanflip.admin.repository.search.PostPollSearchRepository;
import com.fanflip.admin.service.dto.PostPollDTO;
import com.fanflip.admin.service.mapper.PostPollMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PostPollResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PostPollResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_MULTI_CHOICE = false;
    private static final Boolean UPDATED_IS_MULTI_CHOICE = true;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Duration DEFAULT_POST_POLL_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_POST_POLL_DURATION = Duration.ofHours(12);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/post-polls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/post-polls/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PostPollRepository postPollRepository;

    @Autowired
    private PostPollMapper postPollMapper;

    @Autowired
    private PostPollSearchRepository postPollSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PostPoll postPoll;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createEntity(EntityManager em) {
        PostPoll postPoll = new PostPoll()
            .question(DEFAULT_QUESTION)
            .isMultiChoice(DEFAULT_IS_MULTI_CHOICE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .endDate(DEFAULT_END_DATE)
            .postPollDuration(DEFAULT_POST_POLL_DURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createEntity(em)).block();
        postPoll.setPost(postFeed);
        return postPoll;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PostPoll createUpdatedEntity(EntityManager em) {
        PostPoll postPoll = new PostPoll()
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        PostFeed postFeed;
        postFeed = em.insert(PostFeedResourceIT.createUpdatedEntity(em)).block();
        postPoll.setPost(postFeed);
        return postPoll;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PostPoll.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        PostFeedResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        postPollSearchRepository.deleteAll().block();
        assertThat(postPollSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        postPoll = createEntity(em);
    }

    @Test
    void createPostPoll() throws Exception {
        int databaseSizeBeforeCreate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(DEFAULT_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(DEFAULT_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createPostPollWithExistingId() throws Exception {
        // Create the PostPoll with an existing ID
        postPoll.setId(1L);
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        int databaseSizeBeforeCreate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsMultiChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // set the field null
        postPoll.setIsMultiChoice(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // set the field null
        postPoll.setEndDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkPostPollDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // set the field null
        postPoll.setPostPollDuration(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // set the field null
        postPoll.setCreatedDate(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        // set the field null
        postPoll.setIsDeleted(null);

        // Create the PostPoll, which fails.
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllPostPolls() {
        // Initialize the database
        postPollRepository.save(postPoll).block();

        // Get all the postPollList
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
            .value(hasItem(postPoll.getId().intValue()))
            .jsonPath("$.[*].question")
            .value(hasItem(DEFAULT_QUESTION.toString()))
            .jsonPath("$.[*].isMultiChoice")
            .value(hasItem(DEFAULT_IS_MULTI_CHOICE.booleanValue()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].postPollDuration")
            .value(hasItem(DEFAULT_POST_POLL_DURATION.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getPostPoll() {
        // Initialize the database
        postPollRepository.save(postPoll).block();

        // Get the postPoll
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, postPoll.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(postPoll.getId().intValue()))
            .jsonPath("$.question")
            .value(is(DEFAULT_QUESTION.toString()))
            .jsonPath("$.isMultiChoice")
            .value(is(DEFAULT_IS_MULTI_CHOICE.booleanValue()))
            .jsonPath("$.lastModifiedDate")
            .value(is(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.postPollDuration")
            .value(is(DEFAULT_POST_POLL_DURATION.toString()))
            .jsonPath("$.createdDate")
            .value(is(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.createdBy")
            .value(is(DEFAULT_CREATED_BY))
            .jsonPath("$.lastModifiedBy")
            .value(is(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.isDeleted")
            .value(is(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    void getNonExistingPostPoll() {
        // Get the postPoll
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPostPoll() throws Exception {
        // Initialize the database
        postPollRepository.save(postPoll).block();

        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        postPollSearchRepository.save(postPoll).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());

        // Update the postPoll
        PostPoll updatedPostPoll = postPollRepository.findById(postPoll.getId()).block();
        updatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        PostPollDTO postPollDTO = postPollMapper.toDto(updatedPostPoll);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postPollDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<PostPoll> postPollSearchList = IterableUtils.toList(postPollSearchRepository.findAll().collectList().block());
                PostPoll testPostPollSearch = postPollSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testPostPollSearch.getQuestion()).isEqualTo(UPDATED_QUESTION);
                assertThat(testPostPollSearch.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
                assertThat(testPostPollSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testPostPollSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testPostPollSearch.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
                assertThat(testPostPollSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testPostPollSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testPostPollSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testPostPollSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, postPollDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        postPollRepository.save(postPoll).block();

        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .question(UPDATED_QUESTION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostPoll))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(DEFAULT_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdatePostPollWithPatch() throws Exception {
        // Initialize the database
        postPollRepository.save(postPoll).block();

        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();

        // Update the postPoll using partial update
        PostPoll partialUpdatedPostPoll = new PostPoll();
        partialUpdatedPostPoll.setId(postPoll.getId());

        partialUpdatedPostPoll
            .question(UPDATED_QUESTION)
            .isMultiChoice(UPDATED_IS_MULTI_CHOICE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .endDate(UPDATED_END_DATE)
            .postPollDuration(UPDATED_POST_POLL_DURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPostPoll.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPostPoll))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        PostPoll testPostPoll = postPollList.get(postPollList.size() - 1);
        assertThat(testPostPoll.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testPostPoll.getIsMultiChoice()).isEqualTo(UPDATED_IS_MULTI_CHOICE);
        assertThat(testPostPoll.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testPostPoll.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPostPoll.getPostPollDuration()).isEqualTo(UPDATED_POST_POLL_DURATION);
        assertThat(testPostPoll.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPostPoll.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPostPoll.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPostPoll.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, postPollDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamPostPoll() throws Exception {
        int databaseSizeBeforeUpdate = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        postPoll.setId(longCount.incrementAndGet());

        // Create the PostPoll
        PostPollDTO postPollDTO = postPollMapper.toDto(postPoll);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(postPollDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PostPoll in the database
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deletePostPoll() {
        // Initialize the database
        postPollRepository.save(postPoll).block();
        postPollRepository.save(postPoll).block();
        postPollSearchRepository.save(postPoll).block();

        int databaseSizeBeforeDelete = postPollRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the postPoll
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, postPoll.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PostPoll> postPollList = postPollRepository.findAll().collectList().block();
        assertThat(postPollList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(postPollSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchPostPoll() {
        // Initialize the database
        postPoll = postPollRepository.save(postPoll).block();
        postPollSearchRepository.save(postPoll).block();

        // Search the postPoll
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + postPoll.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(postPoll.getId().intValue()))
            .jsonPath("$.[*].question")
            .value(hasItem(DEFAULT_QUESTION.toString()))
            .jsonPath("$.[*].isMultiChoice")
            .value(hasItem(DEFAULT_IS_MULTI_CHOICE.booleanValue()))
            .jsonPath("$.[*].lastModifiedDate")
            .value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].postPollDuration")
            .value(hasItem(DEFAULT_POST_POLL_DURATION.toString()))
            .jsonPath("$.[*].createdDate")
            .value(hasItem(DEFAULT_CREATED_DATE.toString()))
            .jsonPath("$.[*].createdBy")
            .value(hasItem(DEFAULT_CREATED_BY))
            .jsonPath("$.[*].lastModifiedBy")
            .value(hasItem(DEFAULT_LAST_MODIFIED_BY))
            .jsonPath("$.[*].isDeleted")
            .value(hasItem(DEFAULT_IS_DELETED.booleanValue()));
    }
}
