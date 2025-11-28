package com.monsterdam.profile.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.profile.IntegrationTest;
import com.monsterdam.profile.domain.SpecialAward;
import com.monsterdam.profile.repository.SpecialAwardRepository;
import com.monsterdam.profile.service.dto.SpecialAwardDTO;
import com.monsterdam.profile.service.mapper.SpecialAwardMapper;
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
 * Integration tests for the {@link SpecialAwardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialAwardResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ALT_SPECIAL_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ALT_SPECIAL_TITLE = "BBBBBBBBBB";

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

    private static final Long DEFAULT_SPECIAL_TITLE_ID = 1L;
    private static final Long UPDATED_SPECIAL_TITLE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/special-awards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialAwardRepository specialAwardRepository;

    @Autowired
    private SpecialAwardMapper specialAwardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialAwardMockMvc;

    private SpecialAward specialAward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createEntity(EntityManager em) {
        SpecialAward specialAward = new SpecialAward()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .reason(DEFAULT_REASON)
            .altSpecialTitle(DEFAULT_ALT_SPECIAL_TITLE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .specialTitleId(DEFAULT_SPECIAL_TITLE_ID);
        return specialAward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialAward createUpdatedEntity(EntityManager em) {
        SpecialAward specialAward = new SpecialAward()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
        return specialAward;
    }

    @BeforeEach
    public void initTest() {
        specialAward = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialAward() throws Exception {
        int databaseSizeBeforeCreate = specialAwardRepository.findAll().size();
        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);
        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(DEFAULT_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(DEFAULT_SPECIAL_TITLE_ID);
    }

    @Test
    @Transactional
    void createSpecialAwardWithExistingId() throws Exception {
        // Create the SpecialAward with an existing ID
        specialAward.setId(1L);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        int databaseSizeBeforeCreate = specialAwardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().size();
        // set the field null
        specialAward.setStartDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().size();
        // set the field null
        specialAward.setEndDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().size();
        // set the field null
        specialAward.setCreatedDate(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().size();
        // set the field null
        specialAward.setIsDeleted(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpecialTitleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialAwardRepository.findAll().size();
        // set the field null
        specialAward.setSpecialTitleId(null);

        // Create the SpecialAward, which fails.
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        restSpecialAwardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialAwards() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        // Get all the specialAwardList
        restSpecialAwardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialAward.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].altSpecialTitle").value(hasItem(DEFAULT_ALT_SPECIAL_TITLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].specialTitleId").value(hasItem(DEFAULT_SPECIAL_TITLE_ID.intValue())));
    }

    @Test
    @Transactional
    void getSpecialAward() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        // Get the specialAward
        restSpecialAwardMockMvc
            .perform(get(ENTITY_API_URL_ID, specialAward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialAward.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.altSpecialTitle").value(DEFAULT_ALT_SPECIAL_TITLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.specialTitleId").value(DEFAULT_SPECIAL_TITLE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialAward() throws Exception {
        // Get the specialAward
        restSpecialAwardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialAward() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();

        // Update the specialAward
        SpecialAward updatedSpecialAward = specialAwardRepository.findById(specialAward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialAward are not directly saved in db
        em.detach(updatedSpecialAward);
        updatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(updatedSpecialAward);

        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
    }

    @Test
    @Transactional
    void putNonExistingSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);

        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialAward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
    }

    @Test
    @Transactional
    void fullUpdateSpecialAwardWithPatch() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();

        // Update the specialAward using partial update
        SpecialAward partialUpdatedSpecialAward = new SpecialAward();
        partialUpdatedSpecialAward.setId(specialAward.getId());

        partialUpdatedSpecialAward
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .reason(UPDATED_REASON)
            .altSpecialTitle(UPDATED_ALT_SPECIAL_TITLE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .specialTitleId(UPDATED_SPECIAL_TITLE_ID);

        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialAward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialAward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
        SpecialAward testSpecialAward = specialAwardList.get(specialAwardList.size() - 1);
        assertThat(testSpecialAward.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testSpecialAward.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testSpecialAward.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testSpecialAward.getAltSpecialTitle()).isEqualTo(UPDATED_ALT_SPECIAL_TITLE);
        assertThat(testSpecialAward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialAward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialAward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialAward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialAward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialAward.getSpecialTitleId()).isEqualTo(UPDATED_SPECIAL_TITLE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialAwardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialAward() throws Exception {
        int databaseSizeBeforeUpdate = specialAwardRepository.findAll().size();
        specialAward.setId(longCount.incrementAndGet());

        // Create the SpecialAward
        SpecialAwardDTO specialAwardDTO = specialAwardMapper.toDto(specialAward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialAwardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialAwardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialAward in the database
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialAward() throws Exception {
        // Initialize the database
        specialAwardRepository.saveAndFlush(specialAward);

        int databaseSizeBeforeDelete = specialAwardRepository.findAll().size();

        // Delete the specialAward
        restSpecialAwardMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialAward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialAward> specialAwardList = specialAwardRepository.findAll();
        assertThat(specialAwardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
