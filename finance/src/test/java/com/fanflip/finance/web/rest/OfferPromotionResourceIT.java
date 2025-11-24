package com.monsterdam.finance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.monsterdam.finance.IntegrationTest;
import com.monsterdam.finance.domain.OfferPromotion;
import com.monsterdam.finance.domain.enumeration.OfferPromotionType;
import com.monsterdam.finance.repository.OfferPromotionRepository;
import com.monsterdam.finance.service.dto.OfferPromotionDTO;
import com.monsterdam.finance.service.mapper.OfferPromotionMapper;
import jakarta.persistence.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link OfferPromotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfferPromotionResourceIT {

    private static final Duration DEFAULT_FREE_DAYS_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_FREE_DAYS_DURATION = Duration.ofHours(12);

    private static final Float DEFAULT_DISCOUNT_PERCENTAGE = 0F;
    private static final Float UPDATED_DISCOUNT_PERCENTAGE = 1F;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_SUBSCRIPTIONS_LIMIT = 1;
    private static final Integer UPDATED_SUBSCRIPTIONS_LIMIT = 2;

    private static final String DEFAULT_LINK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LINK_CODE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_FINISHED = false;
    private static final Boolean UPDATED_IS_FINISHED = true;

    private static final OfferPromotionType DEFAULT_PROMOTION_TYPE = OfferPromotionType.SPECIAL;
    private static final OfferPromotionType UPDATED_PROMOTION_TYPE = OfferPromotionType.TRIAL_LINK;

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

    private static final String ENTITY_API_URL = "/api/offer-promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfferPromotionRepository offerPromotionRepository;

    @Autowired
    private OfferPromotionMapper offerPromotionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfferPromotionMockMvc;

    private OfferPromotion offerPromotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferPromotion createEntity(EntityManager em) {
        OfferPromotion offerPromotion = new OfferPromotion()
            .freeDaysDuration(DEFAULT_FREE_DAYS_DURATION)
            .discountPercentage(DEFAULT_DISCOUNT_PERCENTAGE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .subscriptionsLimit(DEFAULT_SUBSCRIPTIONS_LIMIT)
            .linkCode(DEFAULT_LINK_CODE)
            .isFinished(DEFAULT_IS_FINISHED)
            .promotionType(DEFAULT_PROMOTION_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .isDeleted(DEFAULT_IS_DELETED);
        return offerPromotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OfferPromotion createUpdatedEntity(EntityManager em) {
        OfferPromotion offerPromotion = new OfferPromotion()
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        return offerPromotion;
    }

    @BeforeEach
    public void initTest() {
        offerPromotion = createEntity(em);
    }

    @Test
    @Transactional
    void createOfferPromotion() throws Exception {
        int databaseSizeBeforeCreate = offerPromotionRepository.findAll().size();
        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);
        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeCreate + 1);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(DEFAULT_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(DEFAULT_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(DEFAULT_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(DEFAULT_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(DEFAULT_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(DEFAULT_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
    }

    @Test
    @Transactional
    void createOfferPromotionWithExistingId() throws Exception {
        // Create the OfferPromotion with an existing ID
        offerPromotion.setId(1L);
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        int databaseSizeBeforeCreate = offerPromotionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setStartDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setEndDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLinkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setLinkCode(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsFinishedIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setIsFinished(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPromotionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setPromotionType(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setCreatedDate(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = offerPromotionRepository.findAll().size();
        // set the field null
        offerPromotion.setIsDeleted(null);

        // Create the OfferPromotion, which fails.
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        restOfferPromotionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOfferPromotions() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        // Get all the offerPromotionList
        restOfferPromotionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offerPromotion.getId().intValue())))
            .andExpect(jsonPath("$.[*].freeDaysDuration").value(hasItem(DEFAULT_FREE_DAYS_DURATION.toString())))
            .andExpect(jsonPath("$.[*].discountPercentage").value(hasItem(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].subscriptionsLimit").value(hasItem(DEFAULT_SUBSCRIPTIONS_LIMIT)))
            .andExpect(jsonPath("$.[*].linkCode").value(hasItem(DEFAULT_LINK_CODE)))
            .andExpect(jsonPath("$.[*].isFinished").value(hasItem(DEFAULT_IS_FINISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].promotionType").value(hasItem(DEFAULT_PROMOTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getOfferPromotion() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        // Get the offerPromotion
        restOfferPromotionMockMvc
            .perform(get(ENTITY_API_URL_ID, offerPromotion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offerPromotion.getId().intValue()))
            .andExpect(jsonPath("$.freeDaysDuration").value(DEFAULT_FREE_DAYS_DURATION.toString()))
            .andExpect(jsonPath("$.discountPercentage").value(DEFAULT_DISCOUNT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.subscriptionsLimit").value(DEFAULT_SUBSCRIPTIONS_LIMIT))
            .andExpect(jsonPath("$.linkCode").value(DEFAULT_LINK_CODE))
            .andExpect(jsonPath("$.isFinished").value(DEFAULT_IS_FINISHED.booleanValue()))
            .andExpect(jsonPath("$.promotionType").value(DEFAULT_PROMOTION_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingOfferPromotion() throws Exception {
        // Get the offerPromotion
        restOfferPromotionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOfferPromotion() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();

        // Update the offerPromotion
        OfferPromotion updatedOfferPromotion = offerPromotionRepository.findById(offerPromotion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOfferPromotion are not directly saved in db
        em.detach(updatedOfferPromotion);
        updatedOfferPromotion
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(updatedOfferPromotion);

        restOfferPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerPromotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isOk());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(UPDATED_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(UPDATED_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void putNonExistingOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offerPromotionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfferPromotionWithPatch() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();

        // Update the offerPromotion using partial update
        OfferPromotion partialUpdatedOfferPromotion = new OfferPromotion();
        partialUpdatedOfferPromotion.setId(offerPromotion.getId());

        partialUpdatedOfferPromotion
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .isDeleted(UPDATED_IS_DELETED);

        restOfferPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferPromotion))
            )
            .andExpect(status().isOk());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(DEFAULT_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(DEFAULT_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void fullUpdateOfferPromotionWithPatch() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();

        // Update the offerPromotion using partial update
        OfferPromotion partialUpdatedOfferPromotion = new OfferPromotion();
        partialUpdatedOfferPromotion.setId(offerPromotion.getId());

        partialUpdatedOfferPromotion
            .freeDaysDuration(UPDATED_FREE_DAYS_DURATION)
            .discountPercentage(UPDATED_DISCOUNT_PERCENTAGE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .subscriptionsLimit(UPDATED_SUBSCRIPTIONS_LIMIT)
            .linkCode(UPDATED_LINK_CODE)
            .isFinished(UPDATED_IS_FINISHED)
            .promotionType(UPDATED_PROMOTION_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .isDeleted(UPDATED_IS_DELETED);

        restOfferPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfferPromotion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfferPromotion))
            )
            .andExpect(status().isOk());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
        OfferPromotion testOfferPromotion = offerPromotionList.get(offerPromotionList.size() - 1);
        assertThat(testOfferPromotion.getFreeDaysDuration()).isEqualTo(UPDATED_FREE_DAYS_DURATION);
        assertThat(testOfferPromotion.getDiscountPercentage()).isEqualTo(UPDATED_DISCOUNT_PERCENTAGE);
        assertThat(testOfferPromotion.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testOfferPromotion.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testOfferPromotion.getSubscriptionsLimit()).isEqualTo(UPDATED_SUBSCRIPTIONS_LIMIT);
        assertThat(testOfferPromotion.getLinkCode()).isEqualTo(UPDATED_LINK_CODE);
        assertThat(testOfferPromotion.getIsFinished()).isEqualTo(UPDATED_IS_FINISHED);
        assertThat(testOfferPromotion.getPromotionType()).isEqualTo(UPDATED_PROMOTION_TYPE);
        assertThat(testOfferPromotion.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testOfferPromotion.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testOfferPromotion.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOfferPromotion.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testOfferPromotion.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void patchNonExistingOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offerPromotionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOfferPromotion() throws Exception {
        int databaseSizeBeforeUpdate = offerPromotionRepository.findAll().size();
        offerPromotion.setId(longCount.incrementAndGet());

        // Create the OfferPromotion
        OfferPromotionDTO offerPromotionDTO = offerPromotionMapper.toDto(offerPromotion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfferPromotionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offerPromotionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OfferPromotion in the database
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOfferPromotion() throws Exception {
        // Initialize the database
        offerPromotionRepository.saveAndFlush(offerPromotion);

        int databaseSizeBeforeDelete = offerPromotionRepository.findAll().size();

        // Delete the offerPromotion
        restOfferPromotionMockMvc
            .perform(delete(ENTITY_API_URL_ID, offerPromotion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OfferPromotion> offerPromotionList = offerPromotionRepository.findAll();
        assertThat(offerPromotionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
