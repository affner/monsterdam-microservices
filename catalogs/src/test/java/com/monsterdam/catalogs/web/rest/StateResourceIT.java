package com.monsterdam.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.catalogs.IntegrationTest;
import com.monsterdam.catalogs.domain.Country;
import com.monsterdam.catalogs.domain.State;
import com.monsterdam.catalogs.repository.StateRepository;
import com.monsterdam.catalogs.repository.search.StateSearchRepository;
import com.monsterdam.catalogs.service.StateService;
import com.monsterdam.catalogs.service.dto.StateDTO;
import com.monsterdam.catalogs.service.mapper.StateMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
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
    private MockMvc restStateMockMvc;

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
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
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
        if (TestUtil.findAll(em, Country.class).isEmpty()) {
            country = CountryResourceIT.createUpdatedEntity(em);
            em.persist(country);
            em.flush();
        } else {
            country = TestUtil.findAll(em, Country.class).get(0);
        }
        state.setCountry(country);
        return state;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        stateSearchRepository.deleteAll();
        assertThat(stateSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        state = createEntity(em);
    }

    @Test
    @Transactional
    void createState() throws Exception {
        int databaseSizeBeforeCreate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);
        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isCreated());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
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
    @Transactional
    void createStateWithExistingId() throws Exception {
        // Create the State with an existing ID
        state.setId(1L);
        StateDTO stateDTO = stateMapper.toDto(state);

        int databaseSizeBeforeCreate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStateNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        // set the field null
        state.setStateName(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsoCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        // set the field null
        state.setIsoCode(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        // set the field null
        state.setCreatedDate(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        // set the field null
        state.setIsDeleted(null);

        // Create the State, which fails.
        StateDTO stateDTO = stateMapper.toDto(state);

        restStateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isBadRequest());

        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeTest);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllStates() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get all the stateList
        restStateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)))
            .andExpect(jsonPath("$.[*].isoCode").value(hasItem(DEFAULT_ISO_CODE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(stateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(stateServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(stateServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(stateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        // Get the state
        restStateMockMvc
            .perform(get(ENTITY_API_URL_ID, state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(state.getId().intValue()))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME))
            .andExpect(jsonPath("$.isoCode").value(DEFAULT_ISO_CODE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingState() throws Exception {
        // Get the state
        restStateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        stateSearchRepository.save(state);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());

        // Update the state
        State updatedState = stateRepository.findById(state.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedState are not directly saved in db
        em.detach(updatedState);
        updatedState
            .stateName(UPDATED_STATE_NAME)
            .isoCode(UPDATED_ISO_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        StateDTO stateDTO = stateMapper.toDto(updatedState);

        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateDTO))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
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
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<State> stateSearchList = IterableUtils.toList(stateSearchRepository.findAll());
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
    @Transactional
    void putNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

        // Update the state using partial update
        State partialUpdatedState = new State();
        partialUpdatedState.setId(state.getId());

        partialUpdatedState
            .isoCode(UPDATED_ISO_CODE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        State testState = stateList.get(stateList.size() - 1);
        assertThat(testState.getStateName()).isEqualTo(DEFAULT_STATE_NAME);
        assertThat(testState.getIsoCode()).isEqualTo(UPDATED_ISO_CODE);
        assertThat(testState.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testState.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testState.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testState.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testState.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateStateWithPatch() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);

        int databaseSizeBeforeUpdate = stateRepository.findAll().size();

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

        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedState.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedState))
            )
            .andExpect(status().isOk());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
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
    @Transactional
    void patchNonExistingState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamState() throws Exception {
        int databaseSizeBeforeUpdate = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        state.setId(longCount.incrementAndGet());

        // Create the State
        StateDTO stateDTO = stateMapper.toDto(state);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the State in the database
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteState() throws Exception {
        // Initialize the database
        stateRepository.saveAndFlush(state);
        stateRepository.save(state);
        stateSearchRepository.save(state);

        int databaseSizeBeforeDelete = stateRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the state
        restStateMockMvc
            .perform(delete(ENTITY_API_URL_ID, state.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<State> stateList = stateRepository.findAll();
        assertThat(stateList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(stateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchState() throws Exception {
        // Initialize the database
        state = stateRepository.saveAndFlush(state);
        stateSearchRepository.save(state);

        // Search the state
        restStateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + state.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(state.getId().intValue())))
            .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME)))
            .andExpect(jsonPath("$.[*].isoCode").value(hasItem(DEFAULT_ISO_CODE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }
}
