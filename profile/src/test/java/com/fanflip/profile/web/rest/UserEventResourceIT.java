package com.fanflip.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.profile.IntegrationTest;
import com.fanflip.profile.domain.UserEvent;
import com.fanflip.profile.domain.enumeration.UserEventStatus;
import com.fanflip.profile.repository.UserEventRepository;
import com.fanflip.profile.service.dto.UserEventDTO;
import com.fanflip.profile.service.mapper.UserEventMapper;
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
 * Integration tests for the {@link UserEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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

    private static final Long DEFAULT_CREATOR_ID = 1L;
    private static final Long UPDATED_CREATOR_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private UserEventMapper userEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserEventMockMvc;

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
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorId(DEFAULT_CREATOR_ID);
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
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        return userEvent;
    }

    @BeforeEach
    public void initTest() {
        userEvent = createEntity(em);
    }

    @Test
    @Transactional
    void createUserEvent() throws Exception {
        int databaseSizeBeforeCreate = userEventRepository.findAll().size();
        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);
        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isCreated());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeCreate + 1);
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
        assertThat(testUserEvent.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createUserEventWithExistingId() throws Exception {
        // Create the UserEvent with an existing ID
        userEvent.setId(1L);
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        int databaseSizeBeforeCreate = userEventRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setTitle(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setStartDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setEndDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setCreatedDate(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setIsDeleted(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userEventRepository.findAll().size();
        // set the field null
        userEvent.setCreatorId(null);

        // Create the UserEvent, which fails.
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        restUserEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isBadRequest());

        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserEvents() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        // Get all the userEventList
        restUserEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].creatorEventStatus").value(hasItem(DEFAULT_CREATOR_EVENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getUserEvent() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        // Get the userEvent
        restUserEventMockMvc
            .perform(get(ENTITY_API_URL_ID, userEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userEvent.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.creatorEventStatus").value(DEFAULT_CREATOR_EVENT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserEvent() throws Exception {
        // Get the userEvent
        restUserEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserEvent() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();

        // Update the userEvent
        UserEvent updatedUserEvent = userEventRepository.findById(userEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserEvent are not directly saved in db
        em.detach(updatedUserEvent);
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
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        UserEventDTO userEventDTO = userEventMapper.toDto(updatedUserEvent);

        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
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
        assertThat(testUserEvent.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();

        // Update the userEvent using partial update
        UserEvent partialUpdatedUserEvent = new UserEvent();
        partialUpdatedUserEvent.setId(userEvent.getId());

        partialUpdatedUserEvent
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEvent))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
        UserEvent testUserEvent = userEventList.get(userEventList.size() - 1);
        assertThat(testUserEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testUserEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testUserEvent.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testUserEvent.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testUserEvent.getCreatorEventStatus()).isEqualTo(DEFAULT_CREATOR_EVENT_STATUS);
        assertThat(testUserEvent.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testUserEvent.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testUserEvent.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserEvent.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testUserEvent.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testUserEvent.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserEventWithPatch() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();

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
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserEvent))
            )
            .andExpect(status().isOk());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
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
        assertThat(testUserEvent.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserEvent() throws Exception {
        int databaseSizeBeforeUpdate = userEventRepository.findAll().size();
        userEvent.setId(longCount.incrementAndGet());

        // Create the UserEvent
        UserEventDTO userEventDTO = userEventMapper.toDto(userEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userEventDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEvent in the database
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserEvent() throws Exception {
        // Initialize the database
        userEventRepository.saveAndFlush(userEvent);

        int databaseSizeBeforeDelete = userEventRepository.findAll().size();

        // Delete the userEvent
        restUserEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, userEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserEvent> userEventList = userEventRepository.findAll();
        assertThat(userEventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
