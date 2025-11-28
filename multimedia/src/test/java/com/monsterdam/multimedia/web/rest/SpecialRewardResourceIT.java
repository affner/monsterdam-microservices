package com.monsterdam.multimedia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.multimedia.IntegrationTest;
import com.monsterdam.multimedia.domain.ContentPackage;
import com.monsterdam.multimedia.domain.SpecialReward;
import com.monsterdam.multimedia.repository.SpecialRewardRepository;
import com.monsterdam.multimedia.service.dto.SpecialRewardDTO;
import com.monsterdam.multimedia.service.mapper.SpecialRewardMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SpecialRewardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialRewardResourceIT {

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

    private static final Long DEFAULT_VIEWER_ID = 1L;
    private static final Long UPDATED_VIEWER_ID = 2L;

    private static final Long DEFAULT_OFFER_PROMOTION_ID = 1L;
    private static final Long UPDATED_OFFER_PROMOTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/special-rewards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecialRewardRepository specialRewardRepository;

    @Autowired
    private SpecialRewardMapper specialRewardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialRewardMockMvc;

    private SpecialReward specialReward;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialReward createEntity(EntityManager em) {
        SpecialReward specialReward = new SpecialReward()
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED)
            .viewerId(DEFAULT_VIEWER_ID)
            .offerPromotionId(DEFAULT_OFFER_PROMOTION_ID);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createEntity(em);
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        specialReward.setContentPackage(contentPackage);
        return specialReward;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpecialReward createUpdatedEntity(EntityManager em) {
        SpecialReward specialReward = new SpecialReward()
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);
        // Add required entity
        ContentPackage contentPackage;
        if (TestUtil.findAll(em, ContentPackage.class).isEmpty()) {
            contentPackage = ContentPackageResourceIT.createUpdatedEntity(em);
            em.persist(contentPackage);
            em.flush();
        } else {
            contentPackage = TestUtil.findAll(em, ContentPackage.class).get(0);
        }
        specialReward.setContentPackage(contentPackage);
        return specialReward;
    }

    @BeforeEach
    public void initTest() {
        specialReward = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecialReward() throws Exception {
        int databaseSizeBeforeCreate = specialRewardRepository.findAll().size();
        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);
        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeCreate + 1);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(DEFAULT_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(DEFAULT_OFFER_PROMOTION_ID);
    }

    @Test
    @Transactional
    void createSpecialRewardWithExistingId() throws Exception {
        // Create the SpecialReward with an existing ID
        specialReward.setId(1L);
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        int databaseSizeBeforeCreate = specialRewardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().size();
        // set the field null
        specialReward.setDescription(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().size();
        // set the field null
        specialReward.setCreatedDate(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().size();
        // set the field null
        specialReward.setIsDeleted(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkViewerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().size();
        // set the field null
        specialReward.setViewerId(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOfferPromotionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialRewardRepository.findAll().size();
        // set the field null
        specialReward.setOfferPromotionId(null);

        // Create the SpecialReward, which fails.
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        restSpecialRewardMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialRewards() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        // Get all the specialRewardList
        restSpecialRewardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialReward.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].viewerId").value(hasItem(DEFAULT_VIEWER_ID.intValue())))
            .andExpect(jsonPath("$.[*].offerPromotionId").value(hasItem(DEFAULT_OFFER_PROMOTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getSpecialReward() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        // Get the specialReward
        restSpecialRewardMockMvc
            .perform(get(ENTITY_API_URL_ID, specialReward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialReward.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.viewerId").value(DEFAULT_VIEWER_ID.intValue()))
            .andExpect(jsonPath("$.offerPromotionId").value(DEFAULT_OFFER_PROMOTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecialReward() throws Exception {
        // Get the specialReward
        restSpecialRewardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialReward() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();

        // Update the specialReward
        SpecialReward updatedSpecialReward = specialRewardRepository.findById(specialReward.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialReward are not directly saved in db
        em.detach(updatedSpecialReward);
        updatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(updatedSpecialReward);

        restSpecialRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isOk());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialRewardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialRewardWithPatch() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();

        // Update the specialReward using partial update
        SpecialReward partialUpdatedSpecialReward = new SpecialReward();
        partialUpdatedSpecialReward.setId(specialReward.getId());

        partialUpdatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);

        restSpecialRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialReward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateSpecialRewardWithPatch() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();

        // Update the specialReward using partial update
        SpecialReward partialUpdatedSpecialReward = new SpecialReward();
        partialUpdatedSpecialReward.setId(specialReward.getId());

        partialUpdatedSpecialReward
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED)
            .viewerId(UPDATED_VIEWER_ID)
            .offerPromotionId(UPDATED_OFFER_PROMOTION_ID);

        restSpecialRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialReward.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecialReward))
            )
            .andExpect(status().isOk());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
        SpecialReward testSpecialReward = specialRewardList.get(specialRewardList.size() - 1);
        assertThat(testSpecialReward.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecialReward.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSpecialReward.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSpecialReward.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSpecialReward.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testSpecialReward.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testSpecialReward.getViewerId()).isEqualTo(UPDATED_VIEWER_ID);
        assertThat(testSpecialReward.getOfferPromotionId()).isEqualTo(UPDATED_OFFER_PROMOTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialRewardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialReward() throws Exception {
        int databaseSizeBeforeUpdate = specialRewardRepository.findAll().size();
        specialReward.setId(longCount.incrementAndGet());

        // Create the SpecialReward
        SpecialRewardDTO specialRewardDTO = specialRewardMapper.toDto(specialReward);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialRewardMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specialRewardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SpecialReward in the database
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialReward() throws Exception {
        // Initialize the database
        specialRewardRepository.saveAndFlush(specialReward);

        int databaseSizeBeforeDelete = specialRewardRepository.findAll().size();

        // Delete the specialReward
        restSpecialRewardMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialReward.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpecialReward> specialRewardList = specialRewardRepository.findAll();
        assertThat(specialRewardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
