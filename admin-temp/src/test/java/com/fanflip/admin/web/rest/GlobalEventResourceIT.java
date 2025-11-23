package com.fanflip.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fanflip.admin.IntegrationTest;
import com.fanflip.admin.domain.GlobalEvent;
import com.fanflip.admin.repository.EntityManager;
import com.fanflip.admin.repository.GlobalEventRepository;
import com.fanflip.admin.repository.search.GlobalEventSearchRepository;
import com.fanflip.admin.service.dto.GlobalEventDTO;
import com.fanflip.admin.service.mapper.GlobalEventMapper;
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
 * Integration tests for the {@link GlobalEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class GlobalEventResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final String ENTITY_API_URL = "/api/global-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/global-events/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GlobalEventRepository globalEventRepository;

    @Autowired
    private GlobalEventMapper globalEventMapper;

    @Autowired
    private GlobalEventSearchRepository globalEventSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private GlobalEvent globalEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalEvent createEntity(EntityManager em) {
        GlobalEvent globalEvent = new GlobalEvent()
            .eventName(DEFAULT_EVENT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return globalEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalEvent createUpdatedEntity(EntityManager em) {
        GlobalEvent globalEvent = new GlobalEvent()
            .eventName(UPDATED_EVENT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return globalEvent;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(GlobalEvent.class).block();
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
        globalEventSearchRepository.deleteAll().block();
        assertThat(globalEventSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        globalEvent = createEntity(em);
    }

    @Test
    void createGlobalEvent() throws Exception {
        int databaseSizeBeforeCreate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        GlobalEvent testGlobalEvent = globalEventList.get(globalEventList.size() - 1);
        assertThat(testGlobalEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testGlobalEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGlobalEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testGlobalEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testGlobalEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGlobalEvent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGlobalEvent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testGlobalEvent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testGlobalEvent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createGlobalEventWithExistingId() throws Exception {
        // Create the GlobalEvent with an existing ID
        globalEvent.setId(1L);
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        int databaseSizeBeforeCreate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        // set the field null
        globalEvent.setEventName(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        // set the field null
        globalEvent.setStartDate(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        // set the field null
        globalEvent.setCreatedDate(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        // set the field null
        globalEvent.setIsDeleted(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllGlobalEvents() {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();

        // Get all the globalEventList
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
            .value(hasItem(globalEvent.getId().intValue()))
            .jsonPath("$.[*].eventName")
            .value(hasItem(DEFAULT_EVENT_NAME))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
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
    void getGlobalEvent() {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();

        // Get the globalEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, globalEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(globalEvent.getId().intValue()))
            .jsonPath("$.eventName")
            .value(is(DEFAULT_EVENT_NAME))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
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
    void getNonExistingGlobalEvent() {
        // Get the globalEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingGlobalEvent() throws Exception {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        globalEventSearchRepository.save(globalEvent).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());

        // Update the globalEvent
        GlobalEvent updatedGlobalEvent = globalEventRepository.findById(globalEvent.getId()).block();
        updatedGlobalEvent
            .eventName(UPDATED_EVENT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(updatedGlobalEvent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, globalEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        GlobalEvent testGlobalEvent = globalEventList.get(globalEventList.size() - 1);
        assertThat(testGlobalEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testGlobalEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGlobalEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGlobalEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGlobalEvent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGlobalEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGlobalEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlobalEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlobalEvent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<GlobalEvent> globalEventSearchList = IterableUtils.toList(globalEventSearchRepository.findAll().collectList().block());
                GlobalEvent testGlobalEventSearch = globalEventSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testGlobalEventSearch.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
                assertThat(testGlobalEventSearch.getStartDate()).isEqualTo(UPDATED_START_DATE);
                assertThat(testGlobalEventSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testGlobalEventSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testGlobalEventSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testGlobalEventSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testGlobalEventSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testGlobalEventSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testGlobalEventSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, globalEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateGlobalEventWithPatch() throws Exception {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();

        // Update the globalEvent using partial update
        GlobalEvent partialUpdatedGlobalEvent = new GlobalEvent();
        partialUpdatedGlobalEvent.setId(globalEvent.getId());

        partialUpdatedGlobalEvent
            .eventName(UPDATED_EVENT_NAME)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGlobalEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        GlobalEvent testGlobalEvent = globalEventList.get(globalEventList.size() - 1);
        assertThat(testGlobalEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testGlobalEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testGlobalEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGlobalEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGlobalEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGlobalEvent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testGlobalEvent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testGlobalEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlobalEvent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateGlobalEventWithPatch() throws Exception {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();

        // Update the globalEvent using partial update
        GlobalEvent partialUpdatedGlobalEvent = new GlobalEvent();
        partialUpdatedGlobalEvent.setId(globalEvent.getId());

        partialUpdatedGlobalEvent
            .eventName(UPDATED_EVENT_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedGlobalEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        GlobalEvent testGlobalEvent = globalEventList.get(globalEventList.size() - 1);
        assertThat(testGlobalEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testGlobalEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGlobalEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testGlobalEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGlobalEvent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGlobalEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGlobalEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlobalEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlobalEvent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, globalEventDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteGlobalEvent() {
        // Initialize the database
        globalEventRepository.save(globalEvent).block();
        globalEventRepository.save(globalEvent).block();
        globalEventSearchRepository.save(globalEvent).block();

        int databaseSizeBeforeDelete = globalEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the globalEvent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, globalEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<GlobalEvent> globalEventList = globalEventRepository.findAll().collectList().block();
        assertThat(globalEventList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(globalEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchGlobalEvent() {
        // Initialize the database
        globalEvent = globalEventRepository.save(globalEvent).block();
        globalEventSearchRepository.save(globalEvent).block();

        // Search the globalEvent
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + globalEvent.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(globalEvent.getId().intValue()))
            .jsonPath("$.[*].eventName")
            .value(hasItem(DEFAULT_EVENT_NAME))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
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
