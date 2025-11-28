package com.monsterdam.catalogs.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.catalogs.IntegrationTest;
import com.monsterdam.catalogs.domain.GlobalEvent;
import com.monsterdam.catalogs.repository.GlobalEventRepository;
import com.monsterdam.catalogs.service.dto.GlobalEventDTO;
import com.monsterdam.catalogs.service.mapper.GlobalEventMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GlobalEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GlobalEventRepository globalEventRepository;

    @Autowired
    private GlobalEventMapper globalEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlobalEventMockMvc;

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

    @BeforeEach
    public void initTest() {
        globalEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createGlobalEvent() throws Exception {
        int databaseSizeBeforeCreate = globalEventRepository.findAll().size();
        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);
        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeCreate + 1);
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
    @Transactional
    void createGlobalEventWithExistingId() throws Exception {
        // Create the GlobalEvent with an existing ID
        globalEvent.setId(1L);
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        int databaseSizeBeforeCreate = globalEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().size();
        // set the field null
        globalEvent.setEventName(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().size();
        // set the field null
        globalEvent.setStartDate(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().size();
        // set the field null
        globalEvent.setCreatedDate(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalEventRepository.findAll().size();
        // set the field null
        globalEvent.setIsDeleted(null);

        // Create the GlobalEvent, which fails.
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        restGlobalEventMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGlobalEvents() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        // Get all the globalEventList
        restGlobalEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getGlobalEvent() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        // Get the globalEvent
        restGlobalEventMockMvc
            .perform(get(ENTITY_API_URL_ID, globalEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(globalEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGlobalEvent() throws Exception {
        // Get the globalEvent
        restGlobalEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGlobalEvent() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();

        // Update the globalEvent
        GlobalEvent updatedGlobalEvent = globalEventRepository.findById(globalEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGlobalEvent are not directly saved in db
        em.detach(updatedGlobalEvent);
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

        restGlobalEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
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
    @Transactional
    void putNonExistingGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGlobalEventWithPatch() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();

        // Update the globalEvent using partial update
        GlobalEvent partialUpdatedGlobalEvent = new GlobalEvent();
        partialUpdatedGlobalEvent.setId(globalEvent.getId());

        partialUpdatedGlobalEvent
            .eventName(UPDATED_EVENT_NAME)
            .startDate(UPDATED_START_DATE)
            .description(UPDATED_DESCRIPTION)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restGlobalEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalEvent))
            )
            .andExpect(status().isOk());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
        GlobalEvent testGlobalEvent = globalEventList.get(globalEventList.size() - 1);
        assertThat(testGlobalEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testGlobalEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testGlobalEvent.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testGlobalEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testGlobalEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGlobalEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testGlobalEvent.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testGlobalEvent.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testGlobalEvent.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateGlobalEventWithPatch() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();

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

        restGlobalEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalEvent))
            )
            .andExpect(status().isOk());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
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
    @Transactional
    void patchNonExistingGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, globalEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlobalEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalEventRepository.findAll().size();
        globalEvent.setId(longCount.incrementAndGet());

        // Create the GlobalEvent
        GlobalEventDTO globalEventDTO = globalEventMapper.toDto(globalEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(globalEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalEvent in the database
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGlobalEvent() throws Exception {
        // Initialize the database
        globalEventRepository.saveAndFlush(globalEvent);

        int databaseSizeBeforeDelete = globalEventRepository.findAll().size();

        // Delete the globalEvent
        restGlobalEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, globalEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlobalEvent> globalEventList = globalEventRepository.findAll();
        assertThat(globalEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
