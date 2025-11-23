package com.fanflip.finance.web.rest;

import static com.fanflip.finance.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fanflip.finance.IntegrationTest;
import com.fanflip.finance.domain.CreatorEarning;
import com.fanflip.finance.repository.CreatorEarningRepository;
import com.fanflip.finance.service.dto.CreatorEarningDTO;
import com.fanflip.finance.service.mapper.CreatorEarningMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
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
 * Integration tests for the {@link CreatorEarningResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CreatorEarningResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

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

    private static final String ENTITY_API_URL = "/api/creator-earnings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreatorEarningRepository creatorEarningRepository;

    @Autowired
    private CreatorEarningMapper creatorEarningMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreatorEarningMockMvc;

    private CreatorEarning creatorEarning;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatorEarning createEntity(EntityManager em) {
        CreatorEarning creatorEarning = new CreatorEarning()
            .amount(DEFAULT_AMOUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .creatorId(DEFAULT_CREATOR_ID);
        return creatorEarning;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreatorEarning createUpdatedEntity(EntityManager em) {
        CreatorEarning creatorEarning = new CreatorEarning()
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        return creatorEarning;
    }

    @BeforeEach
    public void initTest() {
        creatorEarning = createEntity(em);
    }

    @Test
    @Transactional
    void createCreatorEarning() throws Exception {
        int databaseSizeBeforeCreate = creatorEarningRepository.findAll().size();
        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);
        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeCreate + 1);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testCreatorEarning.getCreatorId()).isEqualTo(DEFAULT_CREATOR_ID);
    }

    @Test
    @Transactional
    void createCreatorEarningWithExistingId() throws Exception {
        // Create the CreatorEarning with an existing ID
        creatorEarning.setId(1L);
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        int databaseSizeBeforeCreate = creatorEarningRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().size();
        // set the field null
        creatorEarning.setAmount(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().size();
        // set the field null
        creatorEarning.setCreatedDate(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().size();
        // set the field null
        creatorEarning.setIsDeleted(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatorIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = creatorEarningRepository.findAll().size();
        // set the field null
        creatorEarning.setCreatorId(null);

        // Create the CreatorEarning, which fails.
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        restCreatorEarningMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCreatorEarnings() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        // Get all the creatorEarningList
        restCreatorEarningMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creatorEarning.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].creatorId").value(hasItem(DEFAULT_CREATOR_ID.intValue())));
    }

    @Test
    @Transactional
    void getCreatorEarning() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        // Get the creatorEarning
        restCreatorEarningMockMvc
            .perform(get(ENTITY_API_URL_ID, creatorEarning.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creatorEarning.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.creatorId").value(DEFAULT_CREATOR_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCreatorEarning() throws Exception {
        // Get the creatorEarning
        restCreatorEarningMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCreatorEarning() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();

        // Update the creatorEarning
        CreatorEarning updatedCreatorEarning = creatorEarningRepository.findById(creatorEarning.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCreatorEarning are not directly saved in db
        em.detach(updatedCreatorEarning);
        updatedCreatorEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(updatedCreatorEarning);

        restCreatorEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creatorEarningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isOk());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCreatorEarning.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void putNonExistingCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creatorEarningDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreatorEarningWithPatch() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();

        // Update the creatorEarning using partial update
        CreatorEarning partialUpdatedCreatorEarning = new CreatorEarning();
        partialUpdatedCreatorEarning.setId(creatorEarning.getId());

        partialUpdatedCreatorEarning
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restCreatorEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreatorEarning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreatorEarning))
            )
            .andExpect(status().isOk());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCreatorEarning.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void fullUpdateCreatorEarningWithPatch() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();

        // Update the creatorEarning using partial update
        CreatorEarning partialUpdatedCreatorEarning = new CreatorEarning();
        partialUpdatedCreatorEarning.setId(creatorEarning.getId());

        partialUpdatedCreatorEarning
            .amount(UPDATED_AMOUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .creatorId(UPDATED_CREATOR_ID);

        restCreatorEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreatorEarning.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreatorEarning))
            )
            .andExpect(status().isOk());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
        CreatorEarning testCreatorEarning = creatorEarningList.get(creatorEarningList.size() - 1);
        assertThat(testCreatorEarning.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testCreatorEarning.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCreatorEarning.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCreatorEarning.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCreatorEarning.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCreatorEarning.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testCreatorEarning.getCreatorId()).isEqualTo(UPDATED_CREATOR_ID);
    }

    @Test
    @Transactional
    void patchNonExistingCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creatorEarningDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreatorEarning() throws Exception {
        int databaseSizeBeforeUpdate = creatorEarningRepository.findAll().size();
        creatorEarning.setId(longCount.incrementAndGet());

        // Create the CreatorEarning
        CreatorEarningDTO creatorEarningDTO = creatorEarningMapper.toDto(creatorEarning);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreatorEarningMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creatorEarningDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreatorEarning in the database
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreatorEarning() throws Exception {
        // Initialize the database
        creatorEarningRepository.saveAndFlush(creatorEarning);

        int databaseSizeBeforeDelete = creatorEarningRepository.findAll().size();

        // Delete the creatorEarning
        restCreatorEarningMockMvc
            .perform(delete(ENTITY_API_URL_ID, creatorEarning.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreatorEarning> creatorEarningList = creatorEarningRepository.findAll();
        assertThat(creatorEarningList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
