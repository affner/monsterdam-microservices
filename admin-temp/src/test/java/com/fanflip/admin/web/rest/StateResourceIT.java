package com.monsterdam.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.monsterdam.admin.IntegrationTest;
import com.monsterdam.admin.domain.Country;
import com.monsterdam.admin.domain.State;
import com.monsterdam.admin.repository.EntityManager;
import com.monsterdam.admin.repository.StateRepository;
import com.monsterdam.admin.repository.search.StateSearchRepository;
import com.monsterdam.admin.service.StateService;
import com.monsterdam.admin.service.dto.StateDTO;
import com.monsterdam.admin.service.mapper.StateMapper;
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
 * Integration tests for the {@link StateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class StateResourceIT {

    private static final String DEFAULT_STATE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISO_CODE = "AAA";
    private static final String UPDATED_ISO_CODE = "BBB";

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

    private static final String ENTITY_API_URL = "/api/states";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/states/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StateRepository stateRepository;

    @Mock
    private StateRepository stateRepositoryMock;

    @Autowired
    private StateMapper stateMapper;

    @Mock
    private StateService stateServiceMock;

    @Autowired
    private StateSearchRepository stateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private State state;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static State createEntity(EntityManager em) {
        State state = new State()
            .stateName(DEFAULT_STATE_NAME)
            .isoCode(DEFAULT_ISO_CODE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        // Add required entity
        Country country;
        country = em.insert(CountryResourceIT.createEntity(em)).block();
        state.setCountry(country);
        return state;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static State createUpdatedEntity(EntityManager em) {
        State state = new State()
            .stateName(UPDATED_STATE_NAME)
            .isoCode(UPDATED_ISO_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        // Add required entity
        Country country;
        country = em.insert(CountryResourceIT.createUpdatedEntity(em)).block();
        state.setCountry(country);
        return state;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(State.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CountryResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        stateSearchRepository.deleteAll().block();
        assertThat(stateSearchRepository.count().block()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        state = createEntity(em);
    }

    @Test
    void createState() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(DEFAULT_STATE_NAME);
        assertThat(testState.getIsoCode()).isEqualTo(DEFAULT_ISO_CODE);
        assertThat(testState.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testState.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testState.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testState.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testState.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    void createStateWithExistingId() throws Exception {
        // Create the State with an existing ID
        state.setId(1L);
        StateDTO stateDTO = stateMapper.toDto(state);

        int databaseSizeBeforeCreate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkStateNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        // set the field null
        state.setStateName(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        // set the field null
        state.setIsoCode(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        // set the field null
        state.setCreatedDate(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        // set the field null
        state.setIsDeleted(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllStates() {
        // Initialize the database
        stateRepository.save(state).block();

        // Get all the stateList
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
            .value(hasItem(state.getId().intValue()))
            .jsonPath("$.[*].stateName")
            .value(hasItem(DEFAULT_STATE_NAME))
            .jsonPath("$.[*].isoCode")
            .value(hasItem(DEFAULT_ISO_CODE))
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
    void getAllStatesWithEagerRelationshipsIsEnabled() {
        when(stateServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(stateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatesWithEagerRelationshipsIsNotEnabled() {
        when(stateServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(stateRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getState() {
        // Initialize the database
        stateRepository.save(state).block();

        // Get the state
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, state.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(state.getId().intValue()))
            .jsonPath("$.stateName")
            .value(is(DEFAULT_STATE_NAME))
            .jsonPath("$.isoCode")
            .value(is(DEFAULT_ISO_CODE))
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
    void getNonExistingState() {
        // Get the state
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingState() throws Exception {
        // Initialize the database
        stateRepository.save(state).block();

        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        stateSearchRepository.save(state).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());

        // Update the state
        State updatedState = stateRepository.findById(state.getId()).block();
        updatedState
            .stateName(UPDATED_STATE_NAME)
            .isoCode(UPDATED_ISO_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        StateDTO stateDTO = stateMapper.toDto(updatedState);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getIsoCode()).isEqualTo(UPDATED_ISO_CODE);
        assertThat(testState.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testState.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testState.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testState.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testState.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<State> stateSearchList = IterableUtils.toList(stateSearchRepository.findAll().collectList().block());
                State testStateSearch = stateSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testStateSearch.getStateName()).isEqualTo(UPDATED_STATE_NAME);
                assertThat(testStateSearch.getIsoCode()).isEqualTo(UPDATED_ISO_CODE);
                assertThat(testStateSearch.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
                assertThat(testStateSearch.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
                assertThat(testStateSearch.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
                assertThat(testStateSearch.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
                assertThat(testStateSearch.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
            });
    }

    @Test
    void putNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, stateDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.save(state).block();

        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();

        // Update the state using partial update
        State partialUpdatedState = new State();
        partialUpdatedState.setId(state.getId());

        partialUpdatedState.stateName(UPDATED_STATE_NAME).isoCode(UPDATED_ISO_CODE).isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getIsoCode()).isEqualTo(UPDATED_ISO_CODE);
        assertThat(testState.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testState.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testState.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testState.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testState.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void fullUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.save(state).block();

        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();

        // Update the state using partial update
        State partialUpdatedState = new State();
        partialUpdatedState.setId(state.getId());

        partialUpdatedState
            .stateName(UPDATED_STATE_NAME)
            .isoCode(UPDATED_ISO_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedState.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
        assertThat(testState.getIsoCode()).isEqualTo(UPDATED_ISO_CODE);
        assertThat(testState.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testState.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testState.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testState.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testState.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    void patchNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, stateDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(stateDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteState() {
        // Initialize the database
        stateRepository.save(state).block();
        stateRepository.save(state).block();
        stateSearchRepository.save(state).block();

        int databaseSizeBeforeDelete = stateRepository.findAll().collectList().block().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the state
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, state.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<State> stateList = stateRepository.findAll().collectList().block();
        assertThat(stateList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchState() {
        // Initialize the database
        state = stateRepository.save(state).block();
        stateSearchRepository.save(state).block();

        // Search the state
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + state.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(state.getId().intValue()))
            .jsonPath("$.[*].stateName")
            .value(hasItem(DEFAULT_STATE_NAME))
            .jsonPath("$.[*].isoCode")
            .value(hasItem(DEFAULT_ISO_CODE))
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
