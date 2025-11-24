package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.UserEvent;
import com.monsterdam.admin.domain.UserProfile;
import com.monsterdam.admin.domain.enumeration.UserEventStatus;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.UserEventRepository;
import com.monsterdam.admin.repository.search.UserEventSearchRepository;
import com.monsterdam.admin.service.dto.UserEventDTO;
import com.monsterdam.admin.service.mapper.UserEventMapper;
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
 * Integration tests for the {@link UserEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class UserEventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final UserEventStatus DEFAULT_CREATOR_EVENT_STATUS = UserEventStatus.CANCELED;
    private static final UserEventStatus UPDATED_CREATOR_EVENT_STATUS = UserEventStatus.ACTIVE;

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

    private static final String ENTITY_API_URL = "/api/user-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/user-events/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserEventMapper userEventMapper;

    @Autowired
    private UserEventSearchRepository userEventSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private UserEvent userEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEvent createEntity(EntityManager em) {
        UserEvent userEvent = new UserEvent()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .creatorEventStatus(DEFAULT_CREATOR_EVENT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createEntity(em)).block();
        userEvent.setCreator(userProfile);
        return userEvent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEvent createUpdatedEntity(EntityManager em) {
        UserEvent userEvent = new UserEvent()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        UserProfile userProfile;
        userProfile = em.insert(UserProfileResourceIT.createUpdatedEntity(em)).block();
        userEvent.setCreator(userProfile);
        return userEvent;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(UserEvent.class).block();
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
        userEventSearchRepository.deleteAll().block();
        assertThat(userEventSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        userEvent = createEntity(em);
    }

    @Test
    void createUserEvent() throws Exception {
        int databaseSizeBeforeCreate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UserEvent testUserEvent = userEventList.get(userEventList.size() - 1);
        assertThat(testUserEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testUserEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testUserEvent.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testUserEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testUserEvent.getCreatorEventStatus()).isEqualTo(DEFAULT_CREATOR_EVENT_STATUS);
        assertThat(testUserEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserEvent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserEvent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserEvent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserEvent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createUserEventWithExistingId() throws Exception {
        // Create the UserEvent with an existing ID
        userEvent.setId(1L);
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        int databaseSizeBeforeCreate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // set the field null
        userEvent.setTitle(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // set the field null
        userEvent.setStartDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // set the field null
        userEvent.setEndDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // set the field null
        userEvent.setCreatedDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        // set the field null
        userEvent.setIsDeleted(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllUserEvents() {
        // Initialize the database
        userEventRepository.save(userEvent).block();

        // Get all the userEventList
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
            .value(hasItem(userEvent.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].creatorEventStatus")
            .value(hasItem(DEFAULT_CREATOR_EVENT_STATUS.toString()))
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
    void getUserEvent() {
        // Initialize the database
        userEventRepository.save(userEvent).block();

        // Get the userEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, userEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(userEvent.getId().intValue()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.startDate")
            .value(is(DEFAULT_START_DATE.toString()))
            .jsonPath("$.endDate")
            .value(is(DEFAULT_END_DATE.toString()))
            .jsonPath("$.creatorEventStatus")
            .value(is(DEFAULT_CREATOR_EVENT_STATUS.toString()))
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
    void getNonExistingUserEvent() {
        // Get the userEvent
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingUserEvent() throws Exception {
        // Initialize the database
        userEventRepository.save(userEvent).block();

        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        userEventSearchRepository.save(userEvent).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());

        // Update the userEvent
        UserEvent updatedUserEvent = userEventRepository.findById(userEvent.getId()).block();
        updatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        UserEventDTO userEventDTO = userEventMapper.toDto(updatedUserEvent);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        UserEvent testUserEvent = userEventList.get(userEventList.size() - 1);
        assertThat(testUserEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testUserEvent.getCreatorEventStatus()).isEqualTo(UPDATED_CREATOR_EVENT_STATUS);
        assertThat(testUserEvent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserEvent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UserEvent> userEventSearchList = IterableUtils.toList(userEventSearchRepository.findAll().collectList().block());
                UserEvent testUserEventSearch = userEventSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUserEventSearch.getTitle()).isEqualTo(UPDATED_TITLE);
                assertThat(testUserEventSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
                assertThat(testUserEventSearch.getStartDate()).isEqualTo(UPDATED_START_DATE);
                assertThat(testUserEventSearch.getEndDate()).isEqualTo(UPDATED_END_DATE);
                assertThat(testUserEventSearch.getCreatorEventStatus()).isEqualTo(UPDATED_CREATOR_EVENT_STATUS);
                assertThat(testUserEventSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testUserEventSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testUserEventSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testUserEventSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testUserEventSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, userEventDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        userEventRepository.save(userEvent).block();

        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();

        // Update the userEvent using partial update
        UserEvent partialUpdatedUserEvent = new UserEvent();
        partialUpdatedUserEvent.setId(userEvent.getId());

        partialUpdatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdBy(UPDATED_CREATED_BY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        UserEvent testUserEvent = userEventList.get(userEventList.size() - 1);
        assertThat(testUserEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testUserEvent.getCreatorEventStatus()).isEqualTo(UPDATED_CREATOR_EVENT_STATUS);
        assertThat(testUserEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserEvent.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testUserEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserEvent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserEvent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void fullUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        userEventRepository.save(userEvent).block();

        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();

        // Update the userEvent using partial update
        UserEvent partialUpdatedUserEvent = new UserEvent();
        partialUpdatedUserEvent.setId(userEvent.getId());

        partialUpdatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .creatorEventStatus(UPDATED_CREATOR_EVENT_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEvent))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        UserEvent testUserEvent = userEventList.get(userEventList.size() - 1);
        assertThat(testUserEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testUserEvent.getCreatorEventStatus()).isEqualTo(UPDATED_CREATOR_EVENT_STATUS);
        assertThat(testUserEvent.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testUserEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testUserEvent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, userEventDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(userEventDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteUserEvent() {
        // Initialize the database
        userEventRepository.save(userEvent).block();
        userEventRepository.save(userEvent).block();
        userEventSearchRepository.save(userEvent).block();

        int databaseSizeBeforeDelete = userEventRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the userEvent
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, userEvent.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<UserEvent> userEventList = userEventRepository.findAll().collectList().block();
        assertThat(userEventList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(userEventSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchUserEvent() {
        // Initialize the database
        userEvent = userEventRepository.save(userEvent).block();
        userEventSearchRepository.save(userEvent).block();

        // Search the userEvent
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + userEvent.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(userEvent.getId().intValue()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION.toString()))
            .jsonPath("$.[*].startDate")
            .value(hasItem(DEFAULT_START_DATE.toString()))
            .jsonPath("$.[*].endDate")
            .value(hasItem(DEFAULT_END_DATE.toString()))
            .jsonPath("$.[*].creatorEventStatus")
            .value(hasItem(DEFAULT_CREATOR_EVENT_STATUS.toString()))
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
